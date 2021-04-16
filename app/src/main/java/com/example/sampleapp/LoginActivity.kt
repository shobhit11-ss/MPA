package com.example.sampleapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.AuthUI.IdpConfig
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    val RC_SIGN_IN=123
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val actioncodesetting = ActionCodeSettings.newBuilder()
            .setAndroidPackageName(applicationContext.packageName, true, null)
            .setHandleCodeInApp(true) // This must be set to true
            .setUrl("https://awaynotify123.page.link/Go1D")
            .build();
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().enableEmailLinkSignIn()
                .setActionCodeSettings(actioncodesetting).build(),
            AuthUI.IdpConfig.PhoneBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )
        if (AuthUI.canHandleIntent(intent)) {
            if (intent.extras == null) {
                return
            }
            val link = intent.data.toString()
            if (link != null) {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setEmailLink(link)
                        .setAvailableProviders(providers)
                        .build(),
                    RC_SIGN_IN
                )
            }
        }
            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .setTosAndPrivacyPolicyUrls(
                        "https://example.com/terms.html",
                        "https://example.com/privacy.html"
                    ).setLogo(R.drawable.ic_baseline_notifications_paused_24)
                    .setIsSmartLockEnabled(false).setTheme(R.style.Theme_Sampleapp_NoActionBar)
                    .build(),
                RC_SIGN_IN
            )
        }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                Log.i("TAG", "Successfully signed in")
                val user = FirebaseAuth.getInstance().currentUser
                Log.i("TAG", "User Name ${user.displayName}")
                val intent=Intent(this, MainActivity::class.java)
                startActivity(intent)


            } else {
                Log.e("TAG", "Error encountered ${response}")
            }
        }
    }


}