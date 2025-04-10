package com.fcc.calculadora

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BasicNumbersViewModel: ViewModel() {
    private var doOperation: MutableLiveData<Boolean> = MutableLiveData()
    private var currentOperation: MutableLiveData<String> = MutableLiveData("0") //current information in the resultsText view
    private var numberLength: Int = 0 //Count the length of the current number to limit it
    private var floatNumber: Boolean = false //Boolean to know if the current number is a float

    fun getCurrentOperation(): MutableLiveData<String>{
        return currentOperation
    }

    fun setCurrentOperation(value: String){
        currentOperation.value = value
    }
    fun getDoOperation(): MutableLiveData<Boolean>{
        return doOperation
    }

    fun setDoOperation(operate: Boolean){
        doOperation.value = operate
    }

    fun addDigit(){
        numberLength += 1 //Increment size
    }

    fun getNumberLength(): Int{
        return numberLength
    }

    fun resetNumberLength(){
        numberLength = 0
    }

    fun setFloat(floatNumber: Boolean){
        this.floatNumber = floatNumber
    }

    fun isFloatNumber(): Boolean{
        return floatNumber
    }
}