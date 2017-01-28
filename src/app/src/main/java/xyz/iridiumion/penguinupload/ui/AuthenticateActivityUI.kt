package xyz.iridiumion.penguinupload.ui

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.InputType
import android.view.View
import android.view.View.GONE
import android.widget.EditText
import android.widget.TextView
import com.github.salomonbrys.kotson.fromJson
import com.google.gson.Gson
import org.jetbrains.anko.*
import xyz.iridiumion.penguinupload.R
import xyz.iridiumion.penguinupload.activity.AuthenticateActivity
import xyz.iridiumion.penguinupload.activity.MainActivity
import xyz.iridiumion.penguinupload.api.client.ClientConfiguration
import xyz.iridiumion.penguinupload.api.client.ClientHelper
import xyz.iridiumion.penguinupload.api.client.PenguinUploadClientAutomator
import xyz.iridiumion.penguinupload.api.models.LoginResponse
import kotlin.jvm.javaClass

class AuthenticateActivityUI(val activity: AuthenticateActivity) : AnkoComponent<AuthenticateActivity> {
    companion object {
        val LAYOUT_ID = View.generateViewId()
        val LOGIN_PERSPECTIVE_ID = View.generateViewId()
    }

    fun onSuccessfulLogin(response: String) {
        // successful login
        ClientHelper.whenLoggedIn(response)
        // start next activity
        val mainActivityStartIntent = Intent(activity, MainActivity::class.java)
        activity.finish()
        activity.startActivity(mainActivityStartIntent)
    }

    override fun createView(ui: AnkoContext<AuthenticateActivity>) = with(ui) {
        ui.owner.supportActionBar?.hide()

        verticalLayout(theme = R.style.AppTheme_Minimal) {
            id = LAYOUT_ID
            padding = dip(46)
            backgroundColor = ContextCompat.getColor(ctx, R.color.colorSnow)
            val loginPerspective = verticalLayout {
                id = LOGIN_PERSPECTIVE_ID
                imageView {
                    imageResource = R.drawable.penguinupload
                }.lparams {
                    weight = 0.7f
                    width = matchParent
                }
                textView("Server Address")
                val serverAddress = editText {
                    hint = "Server Address"
                    inputType = InputType.TYPE_CLASS_TEXT
                }
                textView("Username")
                val usernameBox = editText {
                    hint = "Username"
                    inputType = InputType.TYPE_CLASS_TEXT
                }
                textView("Password")
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
                            try {
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
                                    val userApiKey = loginResp.apikey
                                    // save login info
                                    ClientConfiguration.saveLoginDetails(ClientConfiguration(usernameBox.text.toString(), userApiKey, serverAddress.text.toString()))
                                    uiThread {
                                        loginProgress.hide()
                                        alert("This app isn't finished.", "Login success")
                                        {
                                            positiveButton("Dismiss") {}
                                        }
                                                .show()
                                    }
                                    onSuccessfulLogin(response)
                                }
                            } catch (e: Exception) {
                                uiThread {
                                    loginProgress.hide()
                                    alert("Connection error. Make sure you have an internet connection and that the server address is valid.", "Login failure")
                                    {
                                        positiveButton("Dismiss") {}
                                    }
                                            .show()
                                }
                            }
                        }
                    }
                }
            }
            if (ClientConfiguration.hasLoginDetails()) {
                // attempt login with existing info
                loginPerspective.visibility = GONE
                val loginDetails = ClientConfiguration.getLoginDetails()
                val client = PenguinUploadClientAutomator.getClient(loginDetails.server)
                val loginProgress = indeterminateProgressDialog("Connecting...", "Just a moment") {
                    setCancelable(false)
                }
                doAsync {
                    try {
                        val (status, response) = client.validateKey(loginDetails.apikey)
                        uiThread {
                            loginProgress.hide()
                        }
                        if (status) {
                            onSuccessfulLogin(response)
                        }
                    } catch (e: Exception) {
                        uiThread {
                            loginPerspective.visibility = GONE
                            loginProgress.hide()
                            alert("Connection error. Make sure you have an internet connection and that the server address is valid.", "Login failure")
                            {
                                positiveButton("Dismiss") {}
                            }
                                    .show()
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