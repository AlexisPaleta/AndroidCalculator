package com.fcc.calculadora

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BasicNumbersViewModel: ViewModel() {
    private var doOperation: MutableLiveData<Boolean> = MutableLiveData()
    private var currentOperation: MutableLiveData<String> = MutableLiveData("0") //current information in the resultsText view
    private var numberLength: Int = 0 //Count the length of the current number to limit it, this does not counts the "."
    private var floatNumber: Boolean = false //Boolean to know if the current number is a float
    private var isNaN: Boolean = false
    private var currentNumber: String = "0" //This is for the changeSignButton, I need to know
    //the number and its sign to change it if necessary
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

    fun setNumberLength(length: Int){
        this.numberLength = length
    }

    fun setFloat(floatNumber: Boolean){
        this.floatNumber = floatNumber
    }

    fun isFloatNumber(): Boolean{
        return floatNumber
    }

    fun isNaN(): Boolean{
        return this.isNaN
    }

    fun setNaN(nan: Boolean){
        isNaN = nan
    }

    fun getCurrentNumber(): String{
        return currentNumber
    }

    fun setCurrentNumber(current: String){
        currentNumber = current
    }

    fun addCharCurrentNumber(character: Char){
        currentNumber += character
    }
}