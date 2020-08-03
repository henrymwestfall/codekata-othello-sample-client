package app

import kotlin.system.exitProcess
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.google.gson.Gson
import kotlin.concurrent.thread

/**
 * DON'T EDIT THIS FILE (edit AI.kt instead)
 *
 * This file contains the Runner class, which manages the connection with the server
 */

class Runner(val ai: AI, val apiURL: String, val apiKey: String) {
    val api = API(apiURL, apiKey)
    val refreshRate: Long = 500

    fun waitForTurn() {
        while (!api.getMoveNeeded()) Thread.sleep(refreshRate, 0)
    }

    fun doMove() {
        val move = ai.doMove(Board(api.getBoard().board.toTileTypes()))
        val resp = api.doMove(move)
        if (resp.error != null) println(resp.error)
    }

    fun setName() {
        api.setName(ai.name)
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            if (args.size < 2) {
                println("Expected API url as first command line argument and API key(s) as rest.")
                exitProcess(1)
            }

            for (i in 1 until args.size) {
                thread {
                    val run = Runner(AI(), args[0], args[i])
                    println("Thread $i Starting. API URL: ${args[0]}, API KEY: ${args[i]}")
                    println("Thread $i Connecting to server...")
                    run.setName()
                    while (true) {
                        run.waitForTurn()
                        println("(Thread $i) Starting Turn.")
                        run.doMove()
                        println("(Thread $i)Ending Turn.")
                    }
                }
            }
        }
    }
}
