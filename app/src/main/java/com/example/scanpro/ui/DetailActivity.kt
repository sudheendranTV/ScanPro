package com.example.scanpro.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import com.example.scanpro.R

class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail)

        val ip = intent.getStringExtra("ip")
        val name = intent.getStringExtra("name")
        val port = intent.getStringExtra("port") ?: "80"

        supportActionBar?.title = name ?: "Device Details"

        findViewById<AppCompatTextView>(R.id.txt_name).text = getString(R.string.device_full_info, name)
        findViewById<AppCompatTextView>(R.id.txt_ip).text = getString(R.string.device_ip, ip)
        findViewById<AppCompatTextView>(R.id.port).text = getString(R.string.device_port, port)
    }
}