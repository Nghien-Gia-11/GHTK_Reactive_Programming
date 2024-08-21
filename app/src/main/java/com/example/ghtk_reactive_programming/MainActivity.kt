package com.example.ghtk_reactive_programming

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ghtk_reactive_programming.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: StaffViewModel by viewModels()

    private lateinit var staffAdapter: StaffAdapter
    private lateinit var spinnerYearOfBirth: SpinnerAdapter
    private lateinit var spinnerAddress: SpinnerAdapter
    private var yearOfBirth : Int? = null
    private var address : String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initAdapter()

        initSpinner() // khởi tạo adapter cho spinner

        initData() // khởi tạo data đầu

        initDataForSpinner() // gán năm sinh và quê quán vào spinner để chọn (không trùng lặp)

        binding.btnSearch.setOnClickListener {
            viewModel.search(yearOfBirth, address)
            initDataSearch()
        }

    }

    private fun initDataForSpinner() {
        lifecycleScope.launch {
            viewModel.listStaff.collect{
                spinnerYearOfBirth.setData(it.map { staff -> staff.yearOfBirth.toString() }.distinct())
                spinnerAddress.setData(it.map { staff -> staff.address }.distinct())
            }
        }
    }

    private fun initDataSearch(){
        lifecycleScope.launch {
            viewModel.staffSearch.collect{
                staffAdapter.setData(it)
            }
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            viewModel.listStaff.collect{
                staffAdapter.setData(it)
            }
        }
    }

    private fun initSpinner() {
        spinnerYearOfBirth = SpinnerAdapter(this, mutableListOf())
        spinnerAddress = SpinnerAdapter(this, mutableListOf())

        binding.spAddress.apply {
            adapter = spinnerAddress
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    address = if (spinnerAddress.list[position].trim().isNotEmpty()){
                        spinnerAddress.list[position]
                    } else{
                        null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }

        binding.spYearOfBirth.apply {
            adapter = spinnerYearOfBirth
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    yearOfBirth = if (spinnerYearOfBirth.list[position].trim().isNotEmpty()){
                        spinnerYearOfBirth.list[position].toInt()
                    } else{
                        null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }


    }

    private fun initAdapter() {
        staffAdapter = StaffAdapter(mutableListOf())
        binding.rvStaff.apply {
            adapter = staffAdapter
            layoutManager = StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(
                DividerItemDecoration(
                    this@MainActivity,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun showToast(title : String){
        Toast.makeText(this@MainActivity, title, Toast.LENGTH_LONG).show()
    }

}