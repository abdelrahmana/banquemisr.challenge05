package com.banquemisr.currencyapp.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    private val _errorViewModel = MutableLiveData<String?>()
    val errorViewModel :LiveData<String?> = _errorViewModel
    private var _loadPreviousNavBottom = MutableLiveData<Int?>()
     var loadPreviousNavBottom : LiveData<Int?> =  _loadPreviousNavBottom

    private val _networkLoader = MutableLiveData<Int?>() // visibilty
    val networkLoader :LiveData<Int?> = _networkLoader

    fun setError(error : String?) {
       _errorViewModel.postValue(error)
    }
    fun setNetworkLoader(loader : Int?) {
        _networkLoader.postValue(loader)
    }

}