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

        supportActionBar?.title = name ?: "Device Details"

        findViewById<AppCompatTextView>(R.id.txt_name).text = name
        findViewById<AppCompatTextView>(R.id.txt_ip).text = ip
    }
}