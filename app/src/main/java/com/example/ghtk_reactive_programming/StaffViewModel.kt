package com.example.ghtk_reactive_programming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StaffViewModel : ViewModel() {

    companion object {
        const val START = "Start"
        const val LOADING = "Loading"
        const val SUCCESS = "Success"
        const val FAILED = "Failed"
    }

    private var _listStaff = MutableStateFlow(mutableListOf<Staff>())
    val listStaff : StateFlow<MutableList<Staff>> get() = _listStaff

    private var _staffSearch = MutableStateFlow(mutableListOf<Staff>())
    val staffSearch : StateFlow<MutableList<Staff>> get() = _staffSearch

    private var _nameSearch = MutableStateFlow("")

    private var _deleteStaffState = MutableStateFlow(START)
    val deleteStaffState: StateFlow<String> get() = _deleteStaffState

    private var _addStaffState = MutableStateFlow(START)
    val addStaffState: StateFlow<String> get() = _addStaffState


    init {
        initData()
    }

    private fun initData() {
        val listStaff = mutableListOf<Staff>()

        listStaff.add(Staff("Tit0", "NB0", 2003))
        listStaff.add(Staff("Tit1", "NB1", 2002))
        listStaff.add(Staff("Tit2", "NB2", 2001))
        listStaff.add(Staff("Tit3", "NB3", 2006))
        listStaff.add(Staff("Tit4", "NB4", 2005))
        listStaff.add(Staff("Tit5", "NB5", 2003))
        listStaff.add(Staff("Tit6", "NB6", 2002))
        listStaff.add(Staff("Tit7", "NB7", 2003))
        listStaff.add(Staff("Tit8", "NB8", 2005))
        listStaff.add(Staff("Tit9", "NB9", 2007))
        listStaff.add(Staff("Tit10", "NB10", 2005))
        listStaff.add(Staff("Tit11", "NB11", 2003))
        listStaff.add(Staff("Tit12", "NB12", 2007))
        listStaff.add(Staff("Tit13", "NB13", 2005))

        _listStaff.value = listStaff
    }

    fun searchBySpinner(yearOfBirth: Int?, address: String?) {
        val listStaff = if (yearOfBirth == null && address == null) {
            _listStaff.value
        } else if (yearOfBirth == null){
            _listStaff.value.filter { staff -> staff.address == address }.toMutableList()
        } else if (address == null){
            _listStaff.value.filter { staff -> staff.yearOfBirth == yearOfBirth }.toMutableList()
        } else {
            _listStaff.value.filter { staff -> staff.yearOfBirth == yearOfBirth && staff.address == address}.toMutableList()
        }
        _staffSearch.value = listStaff
    }

    fun updateStateNameSearch(name: String?) {
        if (name != null) {
            _nameSearch.value = name
        } else {
            _nameSearch.value = ""
        }
        searchByEditText()
    }

    private fun searchByEditText() {
        val listStaff = if (_nameSearch.value.isNotEmpty()) {
            _listStaff.value.filter { staff -> staff.name == _nameSearch.value }.toMutableList()
        } else {
            _listStaff.value
        }
        _staffSearch.value = listStaff
    }

    fun deleteStaff(pos: Int) {
        _deleteStaffState.value = LOADING
        val listStaff = _listStaff.value
        listStaff.removeAt(pos)
        _listStaff.value = listStaff
        _deleteStaffState.value = SUCCESS
    }

    fun addStaff(name : String, address : String, yearOfBirth: String) {
        _addStaffState.value = LOADING
        val listStaff = _listStaff.value.toMutableList()
        if (name.isEmpty() || address.isEmpty() || yearOfBirth.isEmpty()){
            _addStaffState.value = FAILED
        } else {
            val staff = Staff(name, address, yearOfBirth.toInt())
            _addStaffState.value = SUCCESS
            Log.e("Size before", listStaff.size.toString())
            listStaff.add(staff)
            Log.e("Size after", listStaff.size.toString())
        }
        _listStaff.value = listStaff
    }

}