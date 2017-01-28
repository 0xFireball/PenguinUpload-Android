package xyz.iridiumion.penguinupload.api.client

import android.content.Context
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import xyz.iridiumion.penguinupload.api.models.UserInfo

class ClientHelper {
    companion object {
        fun whenLoggedIn(response: String) {
            // login was successful
            val gson = Gson()
            val userInfoResp = gson.fromJson<UserInfo>(response)
        }
    }
}