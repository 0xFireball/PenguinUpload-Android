package xyz.iridiumion.penguinupload.api.client

import xyz.iridiumion.penguinupload.PenguinUploadApplication

data class ClientConfiguration(val username: String, val apikey: String, val server: String) {
    companion object {

        fun hasLoginDetails(): Boolean {
            return PenguinUploadApplication.preferences!!.contains("apikey")
        }

        fun saveLoginDetails(config: ClientConfiguration) {
            val editor = PenguinUploadApplication.preferences!!.edit()
            editor.putString("username", config.username)
            editor.putString("apikey", config.apikey)
            editor.putString("server", config.server)
            editor.apply()
        }

        fun getLoginDetails(): ClientConfiguration {
            val un = PenguinUploadApplication.preferences!!.getString("username", "error")
            val key = PenguinUploadApplication.preferences!!.getString("apikey", "error")
            val srv = PenguinUploadApplication.preferences!!.getString("server", "error")
            return ClientConfiguration(un, key, srv)
        }
    }
}