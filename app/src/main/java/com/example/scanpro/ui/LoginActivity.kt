package com.example.scanpro.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.lifecycleScope
import com.example.scanpro.R
import com.example.scanpro.utils.Constants.CLIENT_ID
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var loginBtn: MaterialButton
    private val loginViewModel: LoginViewModel by viewModels()
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
            .setServerClientId(CLIENT_ID)
            .setNonce(null)
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

                val credential = result.credential as GoogleIdTokenCredential
                val idToken = credential.idToken

                if (idToken.isNotEmpty()) {
                    loginViewModel.onGoogleLoginSuccess(idToken)
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