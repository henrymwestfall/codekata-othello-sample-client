package app

import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson

data class PostResponse(val error: String?)

data class BoardResponse(val error: String?, val boards: Array<Array<Array<Int>>>)

data class MoveNeededResponse(val error: String?, val needed: Boolean)

class API(val url: String, val key: String) {
    val gson = Gson()

    // GET /api/board
    fun getBoard(): BoardResponse {
        val (_, response, _) = "${url}/boards/$key".httpGet().responseString()

        if (response.statusCode != 200) System.err.println("Error fetching board")

        return gson.fromJson(response.body().asString("application/json"), BoardResponse::class.java)
    }

    // GET /api/move_needed
    fun getMoveNeeded(): Boolean {
        val (_, response, _) = "${url}/move_needed/$key".httpGet().responseString()

        if (response.statusCode != 200) System.err.println("Error checking for needed move")

        return gson.fromJson(response.body().asString("application/json"), MoveNeededResponse::class.java).needed
    }

    // POST /api/move
    fun doMove(move: Pair<Int, Int>): PostResponse {
        val x = move.first
        val y = move.second
        val (_, response, _) = "${url}/move/$key/$x/$y".httpPost().responseString()

        if (response.statusCode != 200) System.err.println("Error sending move")

        return gson.fromJson(response.body().asString("application/json"), PostResponse::class.java)
    }

    // POST /api/set_name
    fun setName(name: String): PostResponse {
        val (_, response, _) = "${url}/set_name/$key/$name".httpPost().responseString()

        if (response.statusCode != 200) System.err.println("Error setting name")

        return gson.fromJson(response.body().asString("application/json"), PostResponse::class.java)
    }
}