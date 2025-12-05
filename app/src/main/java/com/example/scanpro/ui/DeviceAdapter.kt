package com.example.scanpro.ui

import android.provider.Settings.Global.getString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.scanpro.R
import com.example.scanpro.domain.model.Device

class DeviceAdapter(  private val onClick: (Device) -> Unit) : RecyclerView.Adapter<DeviceAdapter.DeviceVH>() {

    private val list = mutableListOf<Device>()

    fun submitList(new: List<Device>) {
        list.clear()
        list.addAll(new)
        notifyDataSetChanged()
    }

   inner class DeviceVH(val view: View) : RecyclerView.ViewHolder(view) {
        val txtName = view.findViewById<TextView>(R.id.txt_name)
        val txtIp = view.findViewById<TextView>(R.id.txt_ip)
        val status = view.findViewById<TextView>(R.id.status)

       fun onBind(device: Device) {
           txtName.text = view.context.getString(R.string.device_found, device.name)
           txtIp.text = view.context.getString(R.string.device_ip, device.ipAddress)
           if (device.isOnline){
               status.text = view.context.getString(R.string.online)
               status.setTextColor(view.context.getColor(R.color.green))
           } else {
               status.text = view.context.getString(R.string.offline)
               status.setTextColor(view.context.getColor(R.color.red))
           }

           itemView.setOnClickListener {
               onClick(device)
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dns_item, parent, false)
        return DeviceVH(view)
    }

    override fun onBindViewHolder(holder: DeviceVH, position: Int) {
        val device = list[position]
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size
}
