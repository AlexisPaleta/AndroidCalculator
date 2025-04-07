package com.fcc.calculadora

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BasicNumbersViewModel: ViewModel() {
    private var currentNumber: MutableLiveData<String> = MutableLiveData("0")

    fun getCurrent(): MutableLiveData<String>{
        return currentNumber
    }

    fun setCurrent(value: String){
        currentNumber.value = value
    }
}