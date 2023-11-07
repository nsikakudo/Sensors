package com.example.sensors

import android.content.Context
import android.hardware.Sensor
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class DeviceSensorAdapter(private val context: Context, private val sensors: List<Sensor>) :
    BaseAdapter() {

    override fun getCount(): Int = sensors.size

    override fun getItem(position: Int): Any = sensors[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var view = convertView
        val name: TextView

        if (view == null) {
            view = LayoutInflater.from(context)
                .inflate(android.R.layout.simple_list_item_1, parent, false)
            name = view.findViewById(android.R.id.text1)
            view.tag = name
        } else {
            name = view.tag as TextView
        }

        name.text = sensors[position].name
        return view
    }
}