package xyz.iridiumion.penguinupload.api.client

import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

class PenguinUploadClientAutomator constructor(val serverAddress: String) {
    init {

    }

    fun attemptLogin(username: String, password: String): Pair<Boolean, String> {
        val (request, response, result) = (serverAddress + "/login").httpPost(listOf(Pair("username", username), Pair("password", password))).responseString()
        // process response
        when (result) {
            is Result.Failure -> {
                return Pair(false, response.httpResponseMessage)
            }
            is Result.Success -> {
                return Pair(true, result.get())
            }
        }
    }

    companion object {
        // static stuff
        fun getClient(serverAddress: String): PenguinUploadClientAutomator {
            return PenguinUploadClientAutomator(serverAddress)
        }
    }
}