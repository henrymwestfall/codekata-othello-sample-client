package app

/**
 * DON'T EDIT THIS FILE (edit AI.kt instead)
 *
 * This file contains the Board class, which manages the client's representation of
 * the game state.
 */


fun toTileTypes(array: Array<Array<Int>>): Array<Array<TileType>> {
    // reverse indices
    return Array(array.size) { x -> Array(array[0].size) { y ->
        when (array[y][x]) {
            1 -> TileType.Us
            -1 -> TileType.Them
            else -> TileType.Empty
        }
    } }
}

enum class TileType(val type: Int) {
    Empty(0),
    Us(1),
    Them(-1)
}

enum class Direction(val dx: Int, val dy: Int) {
    /** cardinal directions **/
    North(0, -1),
    South(0, 1),
    East(-1, 0),
    West(1, 0),

    /** orthogonal directions **/
    NorthEast(-1, -1),
    NorthWest(1, -1),
    SouthEast(-1, 1),
    SouthWest(1, 1);

    fun from(point: Pair<Int, Int>): Pair<Int, Int> {
        val x = point.first + this.dx
        val y = point.second + this.dy
        return Pair(x, y)

    }
}

class Board(val contents: Array<Array<TileType>>) {
    // central positions, ordered Player1, Player2, Player1, Player2
    private val centerFourPositions = listOf(Pair(3, 3), Pair(4, 3), Pair(4, 4), Pair(3, 4))

    fun inbounds(value: Int): Boolean {
        return (value in 0 until 8)
    }

    fun getTileAt(pos: Pair<Int, Int>): TileType {
        return contents[pos.first][pos.second]
    }

    fun getFlippedTiles(player: TileType, x: Int, y: Int): List<Pair<Int, Int>> {
        val opponent = when (player) {
            TileType.Us -> TileType.Them
            TileType.Them -> TileType.Us
            else -> null
        }

        val flippedTiles = mutableListOf<Pair<Int, Int>>()

        if (opponent != null) {
            // find first friendly stone in each direction
            for (direction in Direction.values()) {
                var first = true
                var focus = Pair(x, y)
                var foundEnd = false
                val path = mutableListOf<Pair<Int, Int>>()

                while (getTileAt(focus) == opponent || first) {
                    first = false

                    focus = direction.from(focus)

                    if (!inbounds(focus.first) || !inbounds(focus.second)) break

                    if (getTileAt(focus) == player) {
                        foundEnd = true
                        break
                    }
                    else if (getTileAt(focus) == opponent) {
                        path.add(focus.copy())
                    }
                }

                if (foundEnd) flippedTiles.addAll(path)
            }
        }

        return flippedTiles.toList()
    }

    fun checkLegal(player: TileType, move: Pair<Int, Int>): Boolean {
        if (getTileAt(move) == TileType.Empty) {
            val flippedTiles = getFlippedTiles(player, move.first, move.second)
            return flippedTiles.isNotEmpty()
        } else {
            return false
        }
    }

    fun getLegalMoves(player: TileType): List<Pair<Int, Int>> {
        val moves = mutableListOf<Pair<Int, Int>>()
        contents.forEachIndexed { x, col ->
            col.forEachIndexed { y, _ ->
                val move = Pair(x, y)
                if (checkLegal(player, move)) {
                    moves.add(move)
                }
            }
        }

        return moves.toList()
    }

    fun flipTiles(tiles: List<Pair<Int, Int>>) {
        for (tile in tiles) {
            val x = tile.first
            val y = tile.second
            when (contents[x][y]) {
                TileType.Us -> contents[x][y] = TileType.Them
                TileType.Them -> contents[x][y] = TileType.Us
                TileType.Empty -> println("Warning: tried to flip empty tile.")
            }
        }
    }

    fun putMove(player: TileType, x: Int, y: Int): String? {
        // TODO: ensure incoming moves are legal
        var error: String? = null

        if (contents[x][y] == TileType.Empty) {
            flipTiles(getFlippedTiles(player, x, y))
            contents[x][y] = player
        }
        else error = "space is occupied"

        return error
    }
}
