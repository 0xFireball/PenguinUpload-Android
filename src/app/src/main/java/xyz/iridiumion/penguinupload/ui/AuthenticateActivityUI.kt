package xyz.iridiumion.penguinupload.ui

import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import org.jetbrains.anko.*
import xyz.iridiumion.penguinupload.AuthenticateActivity
import xyz.iridiumion.penguinupload.R
import xyz.iridiumion.penguinupload.api.client.PenguinUploadClientAutomator
import xyz.iridiumion.penguinupload.api.models.LoginResponse

class AuthenticateActivityUI : AnkoComponent<AuthenticateActivity> {
    companion object {
        val LAYOUT_ID = View.generateViewId()
        val LOGIN_PERSPECTIVE_ID = View.generateViewId()
        val SIGNUP_PERSPECTIVE_ID = View.generateViewId()
    }

    override fun createView(ui: AnkoContext<AuthenticateActivity>) = with(ui) {
        ui.owner.supportActionBar?.hide()
        verticalLayout(theme = R.style.AppTheme_Minimal) {
            id = LAYOUT_ID
            padding = dip(32)
            backgroundColor = ContextCompat.getColor(ctx, R.color.colorSnow)
            val loginPerspective = verticalLayout {
                id = LOGIN_PERSPECTIVE_ID

                val serverAddress = editText {
                    hint = "Server Address"
                }
                val usernameBox = editText {
                    hint = "Username"
                }
                val passwordBox = editText {
                    hint = "Password"
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                val connectButton = button("Connect") {
                    onClick {
                        val client = PenguinUploadClientAutomator.getClient(serverAddress.text.toString())
                        val loginProgress = indeterminateProgressDialog("Logging in...", "Just a moment") {
                            setCancelable(false)
                        }
                        doAsync {
                            val (status, response) = client.attemptLogin(usernameBox.text.toString(), passwordBox.text.toString())

                            if (!status) {
                                uiThread {
                                    loginProgress.hide()
                                    alert("Login failed. Please check your username and password.", "Login failure")
                                    {
                                        positiveButton("Dismiss") {}
                                    }
                                            .show()
                                }
                            } else {
                                // parse returned data
                                val gson = Gson()
                                val loginResp = gson.fromJson<LoginResponse>(response)
                                uiThread {
                                    loginProgress.hide()
                                    alert("This app isn't finished.", "Login success")
                                    {
                                        positiveButton("Dismiss") {}
                                    }
                                            .show()
                                }
                                val userApiKey = loginResp.apikey
                            }
                        }
                    }
                }
            }
        }.applyRecursively {
            view ->
            when (view) {
                is EditText -> {
                    view.textColor = ContextCompat.getColor(ctx, android.R.color.white)
                }
                is TextView -> {
                    view.textColor = ContextCompat.getColor(ctx, android.R.color.white)
                }
            }
        }
    }

}