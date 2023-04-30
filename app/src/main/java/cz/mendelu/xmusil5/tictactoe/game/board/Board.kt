package cz.mendelu.xmusil5.tictactoe.game.board

import cz.mendelu.xmusil5.tictactoe.game.player.Player
import cz.mendelu.xmusil5.tictactoe.game.player.PlayerMark
import kotlinx.coroutines.flow.MutableStateFlow

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

    fun getTileIndexesOfPlayerMark(playerMark: PlayerMark): IntArray{
        val playerMarksIndexes = mutableListOf<Int>()
        for (i in tiles.value.indices){
            val tile = tiles.value.get(i)
            if (tile == playerMark){
                playerMarksIndexes.add(i)
            }
        }
        return playerMarksIndexes.toIntArray()
    }

    fun checkRowsForVictoryPath(numberOfMarksToWin: Int): VictoryPath?{
        for (i in 0 until sizeY){
            var markBeingCounted: PlayerMark? = null
            var victoryAlreadyReached = false // Is here to ensure that the path contains all the marks (if there are more than required)
            val path = mutableListOf<Int>()

            for (j in 0 until sizeX){
                val currentIndex = (i * sizeX) + j
                val currentMark = tiles.value.get(currentIndex)
                when{
                    currentMark == null -> {
                        if (victoryAlreadyReached){
                            break
                        }
                        markBeingCounted = null
                        path.clear()
                    }
                    currentMark == markBeingCounted -> {
                        path.add(currentIndex)
                    }
                    else -> {
                        if (victoryAlreadyReached){
                            break
                        }
                        markBeingCounted = currentMark
                        path.clear()
                        path.add(currentIndex)
                    }
                }
                if (path.size >= numberOfMarksToWin){
                    victoryAlreadyReached = true
                }
            }
            if (victoryAlreadyReached){
                return VictoryPath(
                    playerMark = markBeingCounted!!,
                    lineOrientation = LineOrientation.HORIZONTAL,
                    pathIndexes = path.toIntArray()
                )
            }
        }
        return null
    }

    fun checkColumnsForVictoryPath(numberOfMarksToWin: Int): VictoryPath?{
        for (i in 0 until sizeX){
            var markBeingCounted: PlayerMark? = null
            var victoryAlreadyReached = false // Is here to ensure that the path contains all the marks (if there are more than required)
            val path = mutableListOf<Int>()

            for (j in 0 until sizeY){
                val currentIndex = (j * sizeX) + i
                val currentMark = tiles.value.get(currentIndex)
                when{
                    currentMark == null -> {
                        if (victoryAlreadyReached){
                            break
                        }
                        markBeingCounted = null
                        path.clear()
                    }
                    currentMark == markBeingCounted -> {
                        path.add(currentIndex)
                    }
                    else -> {
                        if (victoryAlreadyReached){
                            break
                        }
                        markBeingCounted = currentMark
                        path.clear()
                        path.add(currentIndex)
                    }
                }
                if (path.size >= numberOfMarksToWin){
                    victoryAlreadyReached = true
                }
            }
            if (victoryAlreadyReached){
                return VictoryPath(
                    playerMark = markBeingCounted!!,
                    lineOrientation = LineOrientation.VERTICAL,
                    pathIndexes = path.toIntArray()
                )
            }
        }
        return null
    }

    fun checkDiagonalsForVictoriousMark(numberOfMarksToWin: Int): VictoryPath?{
        // Checking for both player marks
        PlayerMark.values().forEach { mark ->
            val tilesOfMark = getTileIndexesOfPlayerMark(mark)
            var bestVictoryPath: VictoryPath? = null
            // For each player mark, checking recursively diagonals in all directions

            tilesOfMark.forEach { tileIndex ->
                DiagonalDirection.values().forEach { direction ->
                    val path = mutableListOf<Int>()

                    val maxPathLength = searchTilesDiagonally(
                        currentIndex = tileIndex,
                        desiredMark = mark,
                        direction = direction,
                        indexPath = path
                    )
                    if (maxPathLength >= numberOfMarksToWin){
                        val lineOrientation = if (direction == DiagonalDirection.TOP_LEFT || direction == DiagonalDirection.BOTTOM_RIGHT) {
                            LineOrientation.DIAGONAL_DOWN
                        } else {
                            LineOrientation.DIAGONAL_UP
                        }
                        when{
                            bestVictoryPath == null -> {
                                bestVictoryPath = VictoryPath(
                                    playerMark = mark,
                                    lineOrientation = lineOrientation,
                                    pathIndexes = path.toIntArray()
                                )
                            }
                            bestVictoryPath!!.pathIndexes.size < maxPathLength -> {
                                bestVictoryPath = VictoryPath(
                                    playerMark = mark,
                                    lineOrientation = lineOrientation,
                                    pathIndexes = path.toIntArray()
                                )
                            }
                        }
                    }
                }
            }
            bestVictoryPath?.let {
                return it
            }
        }
        return null
    }

    fun searchTilesDiagonally(
        currentIndex: Int,
        desiredMark: PlayerMark,
        direction: DiagonalDirection,
        indexPath: MutableList<Int>
    ): Int{
        val tile = tiles.value.get(currentIndex)
        if (tile != desiredMark){
            return 0
        }

        indexPath.add(currentIndex)
        val offsetFromLeft = (currentIndex % sizeX)+1
        val offsetFromRight = sizeX - (offsetFromLeft-1)
        val offsetFromTop = (currentIndex / sizeX)+1
        val offsetFromBottom = sizeY - (offsetFromTop-1)

        var nextIndex: Int? = null
        var canContinue = false

        when(direction){
            DiagonalDirection.TOP_LEFT -> {
                canContinue = offsetFromLeft > 1 && offsetFromTop > 1
                nextIndex = currentIndex-sizeX-1
            }
            DiagonalDirection.TOP_RIGHT -> {
                canContinue = offsetFromRight > 1 && offsetFromTop > 1
                nextIndex = currentIndex-sizeX+1
            }
            DiagonalDirection.BOTTOM_LEFT -> {
                canContinue = offsetFromLeft > 1 && offsetFromBottom > 1
                nextIndex = currentIndex+sizeX-1
            }
            DiagonalDirection.BOTTOM_RIGHT -> {
                canContinue = offsetFromRight > 1 && offsetFromBottom > 1
                nextIndex = currentIndex+sizeX+1
            }
        }

        if (canContinue){
            return 1 + searchTilesDiagonally(
                currentIndex = nextIndex,
                desiredMark = desiredMark,
                direction = direction,
                indexPath = indexPath
            )
        } else {
            return 1
        }
    }
}