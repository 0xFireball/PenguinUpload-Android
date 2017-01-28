package xyz.iridiumion.penguinupload.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import org.jetbrains.anko.setContentView
import xyz.iridiumion.penguinupload.ui.MainActivityUI

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI(this).setContentView(this)
    }
}
