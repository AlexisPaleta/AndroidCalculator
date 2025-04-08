package com.fcc.calculadora

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BasicNumbersViewModel: ViewModel() {
    private var doOperation: MutableLiveData<Boolean> = MutableLiveData(false)
    private var currentNumber: MutableLiveData<String> = MutableLiveData("0")

    fun getCurrent(): MutableLiveData<String>{
        return currentNumber
    }

    fun setCurrent(value: String){
        currentNumber.value = value
    }
    fun getDoOperation(): MutableLiveData<Boolean>{
        return doOperation
    }

    fun setDoOperation(operate: Boolean){
        doOperation.value = operate
    }
}