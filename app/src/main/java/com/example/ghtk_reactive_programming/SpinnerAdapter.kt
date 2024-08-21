package com.example.ghtk_reactive_programming

import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class SpinnerAdapter(private val activity: Activity, val list: MutableList<String>) :
    ArrayAdapter<String>(activity, R.layout.layout_item_spiner) {


    override fun getCount(): Int {
        return list.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return initView(position, convertView, parent)
    }

    private fun initView(position: Int, convertView: View?, parent: ViewGroup): View {
        val context = activity.layoutInflater
        val rowView = context.inflate(R.layout.layout_item_spiner, parent, false)
        val item = rowView.findViewById<TextView>(R.id.tvSpinner)
        item.text = list[position]
        return rowView
    }

    fun setData(list : List<String>){
        this.list.clear()
        this.list.add("          ")
        this.list.addAll(list)
        notifyDataSetChanged()
    }


}