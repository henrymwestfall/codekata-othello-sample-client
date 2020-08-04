package app

class AI {
    var name = "Sample AI"

    fun doMove(board: Board): Pair<Int, Int> {
        /** Edit this method.
         * param board: the current game state
         * return: a Pair containing the (x, y) coordinates of the AI's move
         * **/

        // Sample AI (Move randomly and legally)
        val legalMoves = board.getLegalMoves(TileType.Us) // get legal moves for us
        val move = legalMoves.random()
        return move

        //return move // select one at random
    }
}