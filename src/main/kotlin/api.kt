package app

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson

data class PostResponse(val error: String?)

data class BoardResponse(val board: Array<Array<Int>>)

data class MoveNeededResponse(val needed: Boolean)

class API(val url: String, val key: String) {
    val gson = Gson()

    // GET /api/board
    fun getBoard(): BoardResponse {
        val (_, response, _) = "${url}/api/board".httpGet(
            listOf(Pair("key", key))
        ).responseString()

        if (response.statusCode != 200) System.err.println("Error fetching board")

        return gson.fromJson(response.body().asString("application/json"), BoardResponse::class.java)
    }

    // GET /api/move_needed
    fun getMoveNeeded(): Boolean {
        val (_, response, _) = "${url}/api/move_needed".httpGet(
            listOf(Pair("key", key))
        ).responseString()

        if (response.statusCode != 200) System.err.println("Error checking for needed move")

        return gson.fromJson(response.body().asString("application/json"), MoveNeededResponse::class.java).needed
    }

    // POST /api/move
    fun doMove(move: Pair<Int, Int>): PostResponse {
        val (_, response, _) = "${url}/api/move".httpPost(
            listOf(Pair("key", key), Pair("move", move))
        ).responseString()

        if (response.statusCode != 200) System.err.println("Error sending move")

        return gson.fromJson(response.body().asString("application/json"), PostResponse::class.java)
    }

    // POST /api/set_name
    fun setName(name: String): PostResponse {
        val (_, response, _) = "${url}/api/set_name".httpPost(
            listOf(Pair("key", key), Pair("newName", name))
        ).responseString()

        if (response.statusCode != 200) System.err.println("Error setting name")

        return gson.fromJson(response.body().asString("application/json"), PostResponse::class.java)
    }
}