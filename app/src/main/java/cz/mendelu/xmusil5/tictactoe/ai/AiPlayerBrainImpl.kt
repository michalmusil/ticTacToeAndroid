package cz.mendelu.xmusil5.tictactoe.ai

import android.content.Context
import android.util.Log
import com.google.firebase.ml.custom.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class AiPlayerBrainImpl(
    private val modelPath: String,
    private val context: Context
): IAiPlayerBrain {

    fun initializeInterpreter(): FirebaseModelInterpreter {
        val model = FirebaseCustomLocalModel.Builder()
            .setAssetFilePath(modelPath)
            .build()

        val options = FirebaseModelInterpreterOptions.Builder(model).build()
        val interpreter = FirebaseModelInterpreter.getInstance(options)

        return interpreter!!
    }

    fun getInputOutputOptions(): FirebaseModelInputOutputOptions{
        val inputOutputOptions = FirebaseModelInputOutputOptions.Builder()
            .setInputFormat(0, FirebaseModelDataType.INT32, intArrayOf(1, 9))
            .setOutputFormat(0, FirebaseModelDataType.FLOAT32, intArrayOf(1, 9))
            .build()
        return inputOutputOptions
    }

    private var interpreter: FirebaseModelInterpreter? = null

    override suspend fun makeDecision(boardState: IntArray): Int {
        if (interpreter == null){
            interpreter = initializeInterpreter()
        }
        val inputIntLength = 9
        val inputOutputOptions = getInputOutputOptions()

        val input = Array(1){ IntArray(inputIntLength) }
        for (i in 0 until inputIntLength){
            input[0][i] = boardState[i]
        }

        val finalizedInputs = FirebaseModelInputs.Builder()
            .add(input)
            .build()

        val nextMove = suspendCoroutine<Int> { continuation ->
            interpreter!!.run(finalizedInputs, inputOutputOptions)
                .addOnSuccessListener { result ->
                    val output = result.getOutput<Array<FloatArray>>(0)[0]
                    var bestIndex = 0
                    var bestValue = 0.0f
                    for (i in 0 until output.size) {
                        val probability = output.get(i)
                        if (probability > bestValue){
                            bestIndex = i
                            bestValue = probability
                        }
                    }
                    continuation.resume(bestIndex)
                }
                .addOnFailureListener { e ->
                    Log.e("AiBrain", "Something went wrong with predicting the next move by neural network")
                    throw e
                }
        }
        return nextMove
    }
}