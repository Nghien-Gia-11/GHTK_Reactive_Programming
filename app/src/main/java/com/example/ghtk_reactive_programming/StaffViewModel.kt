package com.example.ghtk_reactive_programming

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class StaffViewModel : ViewModel() {
    private var _listStaff = MutableStateFlow(mutableListOf<Staff>())
    val listStaff : StateFlow<MutableList<Staff>> = _listStaff

    private var _staffSearch = MutableStateFlow(mutableListOf<Staff>())
    val staffSearch : StateFlow<MutableList<Staff>> = _staffSearch

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

    fun search(yearOfBirth : Int?, address : String?){
        var listStaff = mutableListOf<Staff>()
        listStaff = if (yearOfBirth == null && address == null ){
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

}