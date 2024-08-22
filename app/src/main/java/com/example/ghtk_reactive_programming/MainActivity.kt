package com.example.ghtk_reactive_programming

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.ghtk_reactive_programming.databinding.ActivityMainBinding
import com.example.ghtk_reactive_programming.databinding.LayoutDialogAddStaffBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), OnClick {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bindingDiaLog: LayoutDialogAddStaffBinding
    private val viewModel: StaffViewModel by viewModels()

    private lateinit var diaLog: AlertDialog
    private lateinit var staffAdapter: StaffAdapter
    private lateinit var spinnerYearOfBirth: SpinnerAdapter
    private lateinit var spinnerAddress: SpinnerAdapter
    private var yearOfBirth: Int? = null
    private var address: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initAdapter()

        initSpinner() // khởi tạo adapter cho spinner

        initData() // khởi tạo data đầu

        initDataForSpinner() // gán năm sinh và quê quán vào spinner để chọn (không trùng lặp)

        binding.btnSearch.setOnClickListener {
            viewModel.searchBySpinner(yearOfBirth, address)
            initDataSearch()
        }

        onSearchTextChanged()

        binding.btnAdd.setOnClickListener {
            showDiaLog()
        }

    }

    private fun showDiaLog() {
        val build = AlertDialog.Builder(this)
        bindingDiaLog = LayoutDialogAddStaffBinding.inflate(LayoutInflater.from(this))
        build.setView(bindingDiaLog.root)

        bindingDiaLog.btnCancel.setOnClickListener {
            diaLog.dismiss()
        }

        bindingDiaLog.btnAdd.setOnClickListener {
            viewModel.addStaff(
                bindingDiaLog.edtName.text.toString(),
                bindingDiaLog.edtAddress.text.toString(),
                bindingDiaLog.edtYearOfBirth.text.toString()
            )
            diaLog.dismiss()
        }

        lifecycleScope.launch {
            viewModel.addStaffState.collect {
                showState(it)
            }
        }



        diaLog = build.create()
        diaLog.show()
    }

    private fun onSearchTextChanged() {
        binding.edtNameSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    delay(1000)
                    if (binding.edtNameSearch.text.toString().trim().isNotEmpty()) {
                        viewModel.updateStateNameSearch(binding.edtNameSearch.text.toString())
                    } else {
                        viewModel.updateStateNameSearch(null)
                    }
                    initDataSearch()
                }
            }

        })
    }

    private fun initDataForSpinner() {
        lifecycleScope.launch {
            viewModel.listStaff.observe(this@MainActivity) {
                spinnerYearOfBirth.setData(it.map { staff -> staff.yearOfBirth.toString() }
                    .distinct())
                spinnerAddress.setData(it.map { staff -> staff.address }.distinct())
            }
        }
    }

    private fun initDataSearch() {
        lifecycleScope.launch {
            viewModel.staffSearch.observe(this@MainActivity) {
                staffAdapter.setData(it)
            }
        }
    }

    private fun initData() {
        lifecycleScope.launch {
            viewModel.listStaff.observe(this@MainActivity)  {
                staffAdapter.setData(it)
            }
        }
    }

    private fun initSpinner() {
        spinnerYearOfBirth = SpinnerAdapter(this, mutableListOf())
        spinnerAddress = SpinnerAdapter(this, mutableListOf())

        binding.spAddress.apply {
            adapter = spinnerAddress
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    address = if (spinnerAddress.list[position].trim().isNotEmpty()) {
                        spinnerAddress.list[position]
                    } else {
                        null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }

        binding.spYearOfBirth.apply {
            adapter = spinnerYearOfBirth
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    yearOfBirth = if (spinnerYearOfBirth.list[position].trim().isNotEmpty()) {
                        spinnerYearOfBirth.list[position].toInt()
                    } else {
                        null
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
        }


    }

    private fun initAdapter() {
        staffAdapter = StaffAdapter(mutableListOf(), this)
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

    private fun showToast(title: String) {
        Toast.makeText(this@MainActivity, title, Toast.LENGTH_LONG).show()
    }

    override fun onClick(pos: Int) {
        viewModel.deleteStaff(pos)
        lifecycleScope.launch {
            viewModel.deleteStaffState.collect {
                showState(it)
            }
        }
    }

    private fun showState(state: String) {
        when (state) {
            StaffViewModel.START -> {
                showToast("Start")
            }

            StaffViewModel.LOADING -> {
                showToast("Loading")
            }

            StaffViewModel.FAILED -> {
                showToast("Vui lòng nhập đầy đủ thông tin !!")
            }

            else -> {
                showToast("Success")
            }
        }
    }
}