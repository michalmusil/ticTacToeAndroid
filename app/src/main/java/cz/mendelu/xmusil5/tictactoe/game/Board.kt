package cz.mendelu.xmusil5.tictactoe.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.min

class Board(
    val sizeX: Int,
    val sizeY: Int,
) {
    val numberOfTiles = sizeX * sizeY
    val tiles: MutableStateFlow<Array<PlayerMark?>> = MutableStateFlow(arrayOfNulls(numberOfTiles))

    fun setTile(index: Int, mark: PlayerMark?){
        val newTiles: Array<PlayerMark?> = arrayOfNulls(numberOfTiles)
        tiles.value.copyInto(newTiles)
        newTiles.set(index, mark)

        tiles.value = newTiles
    }

    fun getEncodedStateRepresentation(humanPlayer: Player): IntArray{
        val encodedState = mutableListOf<Int>()
        for (i in tiles.value.indices){
            val mark = tiles.value.get(i)
            when(mark){
                humanPlayer.mark -> {
                    encodedState.add(1)
                }
                null -> {
                    encodedState.add(0)
                }
                else -> {
                    encodedState.add(-1)
                }
            }
        }
        return encodedState.toIntArray()
    }

    fun getFreeTileIndexes(): IntArray{
        val freeIndexes = mutableListOf<Int>()
        for (i in tiles.value.indices){
            val tile = tiles.value.get(i)
            if (tile == null){
                freeIndexes.add(i)
            }
        }
        return freeIndexes.toIntArray()
    }

    fun checkRowsForVictoriousMark(numberOfMarksToWin: Int): PlayerMark?{
        for (i in 0 until sizeY){
            var markBeingCounted: PlayerMark? = null
            var numberOfMarksReached = 0

            for (j in 0 until sizeX){
                val currentIndex = (i * sizeX) + j
                val currentMark = tiles.value.get(currentIndex)
                when{
                    currentMark == null -> {
                        markBeingCounted = null
                        numberOfMarksReached = 0
                    }
                    currentMark == markBeingCounted -> {
                        numberOfMarksReached++
                    }
                    else -> {
                        markBeingCounted = currentMark
                        numberOfMarksReached = 1
                    }
                }
                if (numberOfMarksReached >= numberOfMarksToWin){
                    return currentMark
                }
            }
        }
        return null
    }

    fun checkColumnsForVictoriousMark(numberOfMarksToWin: Int): PlayerMark?{
        for (i in 0 until sizeX){
            var markBeingCounted: PlayerMark? = null
            var numberOfMarksReached = 0

            for (j in 0 until sizeY){
                val currentIndex = (j * sizeX) + i
                val currentMark = tiles.value.get(currentIndex)
                when{
                    currentMark == null -> {
                        markBeingCounted = null
                        numberOfMarksReached = 0
                    }
                    currentMark == markBeingCounted -> {
                        numberOfMarksReached++
                    }
                    else -> {
                        markBeingCounted = currentMark
                        numberOfMarksReached = 1
                    }
                }
                if (numberOfMarksReached >= numberOfMarksToWin){
                    return currentMark
                }
            }
        }
        return null
    }

    fun checkDiagonalsForVictoriousMark(numberOfMarksToWin: Int): PlayerMark?{
        // First checking diagonals of the top row going to the south-east next tile
        for(i in 0 until sizeX){
            var markBeingCounted: PlayerMark? = null
            var numberOfMarksReached = 0

            var possibleToContinue = true
            var j = 0

            while (possibleToContinue){
                // If j overreaches any of the sides => it is on illegal spot in the board
                val remainingSizeX = sizeX - i - j
                val remainingSizeY = sizeY - j

                val smallerRemainingSide = min(remainingSizeX, remainingSizeY)
                if (smallerRemainingSide == 0){
                    // one of the sides has been overreached, ending the while loop
                    possibleToContinue = false
                    break
                }

                val currentIndex = i + (j * (sizeX + 1))
                val currentMark = tiles.value.get(currentIndex)
                when{
                    currentMark == null -> {
                        markBeingCounted = null
                        numberOfMarksReached = 0
                    }
                    currentMark == markBeingCounted -> {
                        numberOfMarksReached++
                    }
                    else -> {
                        markBeingCounted = currentMark
                        numberOfMarksReached = 1
                    }
                }
                if (numberOfMarksReached >= numberOfMarksToWin){
                    return currentMark
                }

                j++
            }
        }



        // Second checking diagonals from the left column going to the south-east next tile
        // Starting from index 1 as the main diagonal has already been scanned by previous loop
        for(i in 1 until sizeY){
            var markBeingCounted: PlayerMark? = null
            var numberOfMarksReached = 0

            var possibleToContinue = true
            var j = 0

            while (possibleToContinue){
                // If j overreaches any of the sides => it is on illegal spot in the board
                val remainingSizeX = sizeX - j
                val remainingSizeY = sizeY -i - j

                val smallerRemainingSide = min(remainingSizeX, remainingSizeY)
                if (smallerRemainingSide == 0){
                    // one of the sides has been overreached, ending the while loop
                    possibleToContinue = false
                    break
                }

                val currentIndex = (i * sizeX) + (j * (sizeX + 1))
                val currentMark = tiles.value.get(currentIndex)
                when{
                    currentMark == null -> {
                        markBeingCounted = null
                        numberOfMarksReached = 0
                    }
                    currentMark == markBeingCounted -> {
                        numberOfMarksReached++
                    }
                    else -> {
                        markBeingCounted = currentMark
                        numberOfMarksReached = 1
                    }
                }
                if (numberOfMarksReached >= numberOfMarksToWin){
                    return currentMark
                }

                j++
            }
        }


        return null
    }
}