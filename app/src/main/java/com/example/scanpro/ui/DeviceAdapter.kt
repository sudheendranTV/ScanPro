package com.example.scanpro.ui

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

       fun onBind(item: Device) {
           txtName.text = item.name
           txtIp.text = item.ipAddress

           itemView.setOnClickListener {
               onClick(item)   // PASS CLICK UPWARD
           }
       }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceVH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.dns_item




                , parent, false)
        return DeviceVH(view)
    }

    override fun onBindViewHolder(holder: DeviceVH, position: Int) {
        val device = list[position]
        holder.txtName.text = device.name
        holder.txtIp.text = device.ipAddress
        holder.onBind(list[position])
    }

    override fun getItemCount() = list.size
}
