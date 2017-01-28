package xyz.iridiumion.penguinupload

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager

class PenguinUploadApplication : Application() {
    companion object {
        var preferences: SharedPreferences? = null
    }

    override fun onCreate() {
        super.onCreate()
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
    }
}