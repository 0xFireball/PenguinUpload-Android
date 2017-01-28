package xyz.iridiumion.penguinupload

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import org.jetbrains.anko.setContentView
import xyz.iridiumion.penguinupload.ui.AuthenticateActivityUI

class AuthenticateActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AuthenticateActivityUI().setContentView(this)
    }
}
