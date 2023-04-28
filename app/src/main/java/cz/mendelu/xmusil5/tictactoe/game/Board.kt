package cz.mendelu.xmusil5.tictactoe.game

class Board(
    val sizeX: Int,
    val sizeY: Int,
) {
    val numberOfTiles = sizeX * sizeY
    val tiles: Array<PlayerMark?> = arrayOfNulls(numberOfTiles)

    fun setTile(index: Int, mark: PlayerMark?){
        tiles.set(index, mark)
    }

    fun getEncodedStateRepresentation(humanPlayer: Player): IntArray{
        val encodedState = mutableListOf<Int>()
        for (i in tiles.indices){
            val mark = tiles.get(i)
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
        for (i in tiles.indices){
            val tile = tiles.get(i)
            if (tile == null){
                freeIndexes.add(i)
            }
        }
        return freeIndexes.toIntArray()
    }
}