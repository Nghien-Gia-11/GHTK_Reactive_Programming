package com.example.ghtk_reactive_programming

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ghtk_reactive_programming.databinding.LayoutItemStaffBinding

class StaffAdapter(private var listStaff : MutableList<Staff>, private val onClick : OnClick) : RecyclerView.Adapter<StaffAdapter.ViewHolder>() {

    inner class ViewHolder(val binding : LayoutItemStaffBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
        val binding = LayoutItemStaffBinding.inflate(view, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = listStaff.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = listStaff[position].name
        holder.binding.tvAddress.text = listStaff[position].address
        holder.binding.tvYearOfBirth.text = listStaff[position].yearOfBirth.toString()
        holder.binding.btnDelete.setOnClickListener {
            onClick.onClick(position)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun setData(newData : MutableList<Staff>) {
        Log.e("Size1", listStaff.size.toString())
        listStaff.clear()
        listStaff.addAll(newData)
        Log.e("Size2", listStaff.size.toString())
        notifyDataSetChanged()
    }

}