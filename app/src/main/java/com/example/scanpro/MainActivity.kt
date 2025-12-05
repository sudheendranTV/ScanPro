package com.example.scanpro

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.scanpro.data.TokenStore
import com.example.scanpro.ui.HomeActivity
import com.example.scanpro.ui.LoginActivity
import com.example.scanpro.utils.NetworkUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var tokenStore: TokenStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        tokenStore = TokenStore(this)


        decideStartDestination()
    }

    private fun decideStartDestination() {
        lifecycleScope.launch {

            if (!NetworkUtils.isNetworkAvailable(this@MainActivity)) {
                tokenStore.clearAccessToken()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                Toast.makeText(this@MainActivity, "No internet connection", Toast.LENGTH_LONG).show()
                return@launch
            }

            val token = tokenStore.accessToken.first()

            if (token.isNullOrEmpty()) {
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                startActivity(Intent(this@MainActivity, HomeActivity::class.java))
                finish()
            }

            finish()
        }
    }
}