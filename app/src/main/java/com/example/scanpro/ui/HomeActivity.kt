package com.example.scanpro.ui

import MdnsDiscoveryManager
import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpro.R
import com.example.scanpro.data.AppDatabase
import com.example.scanpro.data.DeviceRepositoryImpl
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var adapter: DeviceAdapter
    private lateinit var scanBtn: AppCompatButton
    private lateinit var scanningProgress: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_home)

        val dao = AppDatabase.getInstance(this).deviceDao()
        val repo = DeviceRepositoryImpl(dao)
        val mdns = MdnsDiscoveryManager(this)
        viewModel = HomeViewModel(repo, mdns)

        scanBtn = findViewById(R.id.btnScnDevices)
        scanningProgress = findViewById(R.id.scanningProgress)
        val recyclerview = findViewById<RecyclerView>(R.id.rvDevices)

        adapter = DeviceAdapter { device ->
            val i = Intent(this, DetailActivity::class.java)
            i.putExtra("ip", device.ipAddress)
            i.putExtra("name", device.name)
            i.putExtra("port", device.port)
            startActivity(i)
        }

        recyclerview.apply {
            layoutManager = LinearLayoutManager(this@HomeActivity)
            val divider = DividerItemDecoration(this@HomeActivity, DividerItemDecoration.VERTICAL)
            recyclerview.addItemDecoration(divider)
            this.adapter = this@HomeActivity.adapter
        }

        scanBtn.setOnClickListener {
            scanningProgress.visibility = ProgressBar.VISIBLE
            viewModel.startDiscovery()
        }

        lifecycleScope.launch {
            viewModel.devices.collect {
                adapter.submitList(it)
                scanningProgress.visibility = ProgressBar.GONE
            }
        }
    }
}

private fun markAllOffline() {
    TODO("Not yet implemented")
}
