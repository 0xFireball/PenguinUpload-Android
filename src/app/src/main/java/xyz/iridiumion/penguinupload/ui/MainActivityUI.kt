package xyz.iridiumion.penguinupload.ui

import android.view.Gravity
import android.view.View
import org.jetbrains.anko.*
import xyz.iridiumion.penguinupload.R
import xyz.iridiumion.penguinupload.activity.MainActivity
import xyz.iridiumion.penguinupload.api.client.ClientConfiguration

class MainActivityUI(val activity: MainActivity) : AnkoComponent<MainActivity> {

    companion object {
        val LAYOUT_ID = View.generateViewId()
    }

    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        val loginInfo = ClientConfiguration.getLoginDetails()
        frameLayout(theme = R.style.AppTheme_Minimal) {
            id = LAYOUT_ID
            padding = dip(10)
            textView("Welcome, ${loginInfo.username}")
            textView("Logged in to ${loginInfo.server}")
                    .lparams {
                        gravity = Gravity.BOTTOM or Gravity.CENTER
                    }
        }
    }
}