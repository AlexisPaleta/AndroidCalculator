package com.fcc.calculadora

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BasicNumbersViewModel: ViewModel() {
    private var currentNumber: MutableLiveData<Float> = MutableLiveData()

    fun getCurrent(): MutableLiveData<Float>{
        return currentNumber
    }

    fun setCurrent(value: String){
        currentNumber.value = value.toFloat();
    }
}