package com.example.scanpro.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.scanpro.R
import com.example.scanpro.utils.Constants.CLIENT_ID
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBtn: MaterialButton
    //private val loginViewModel: LoginViewModel by viewModels()
    private val TAG = "LoginActivity"
    private val loginViewModel: LoginViewModel by viewModels {
        LoginViewModelFactory(this)
    }
    //private val credentialManager by lazy { CredentialManager.create(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        loginBtn = findViewById(R.id.btnLogin)

        loginBtn.setOnClickListener {

           startGoogleLogin()
        }

        lifecycleScope.launchWhenStarted {
            loginViewModel.loginSuccess.collect { if (it) navigateToHome() }
        }
    }

    private fun startGoogleLogin() {

        val credentialManager = CredentialManager.create(this)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(CLIENT_ID)
            .setNonce(nonce = null)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    context = this@LoginActivity,
                    request = request
                )

                val credential = result.credential as CustomCredential
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential = GoogleIdTokenCredential
                            .createFrom(credential.data)
                        if (googleIdTokenCredential.idToken.isNotEmpty()) {
                            loginViewModel.onGoogleLoginSuccess(googleIdTokenCredential.idToken)
                        }

                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    Log.e(TAG, "Unexpected type of credential")
                }


            } catch (e: Exception) {
                Toast.makeText(this@LoginActivity, "Login failed ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}