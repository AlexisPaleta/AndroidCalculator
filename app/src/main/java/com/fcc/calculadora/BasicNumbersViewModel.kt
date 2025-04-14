package com.fcc.calculadora

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BasicNumbersViewModel: ViewModel() {
    private var doOperation: MutableLiveData<Boolean> = MutableLiveData()
    private var currentOperation: MutableLiveData<String> = MutableLiveData("0") //current information in the resultsText view
    private var previousOperation: MutableLiveData<String> = MutableLiveData("")//current information in the previousOperationText view
    private var numberLength: Int = 0 //Count the length of the current number to limit it, this does not counts the "."
    private var floatNumber: Boolean = false //Boolean to know if the current number is a float
    private var isNaN: Boolean = false
    private var currentNumber: String = "0" //This is for the changeSignButton, I need to know
    //the number and its sign to change it if necessary
    private var previousNumber: String = "0"//This is for the percentageButton, I need to know the previous number because
    //if the user types 100-2%, the user wants that the percentage value is the 2% of 100. This operation happens only when a
    //"-" or "+" is the previous operator. It is necessary to store the previous number at a different variable because when an
    //operator is pressed the current number is just the sign so I will not be able to know of what number I have to obtain
    //the percentage
    private var encapsulatedCurrentNumber = "0"//This variable is for the parenthesis logic and "x" and "÷" operators to a correct use on the percentage function.
    //If the user operation is "(5(9)) + 5" and then press the percentage button the operation will be (5(9)) + (5%x(5(9))), that is because the parenthesis don't reset
    //the currentNumber status. But when I press an operator ("+","-") the currentNumber and the previousNumber are updated (the previous until a digit is entered after the sign)
    //and that is OK if all the parenthesis are not closed or there are not parenthesis at all, but if all the parenthesis are correctly closed like (56(7 + 8 x 2))
    // or the number is something like "5x7÷9" then I want to consider that whole number as unique
    //
    private var sameNumber = 0// This variable is the complement of the encapsulatedCurrentNumber, when a left parenthesis is opened with his
    //button, this variable add 1 to its value, so all the numbers after that activation will be part of the encapsulatedCurrentNumber, the variable will increment
    //its value with each leftParenthesis Button press and it will be decreased when the rightParenthesisButton is pressed. The add operator function will check
    //the value and if a "x" or "÷" are added to the operation they will always be added to the encapsulatedCurrentNumber. The "+" and "-" need to check
    //the value of sameNumber and if it is zero they will reset the encapsulatedCurrentNumber and add the first element with his sign, if it is not zero then
    //the "+" and "-" will be part of the encapsulatedCurrentNumber. The percentageButton will check the length of encapsulatedCurrentNumber and if is more than
    //maybe 1 then the currentNumber will be replaced with encapsulatedCurrentNumber.
    fun getCurrentOperation(): MutableLiveData<String>{
        return currentOperation
    }

    fun setCurrentOperation(value: String){
        currentOperation.value = value
    }

    fun getPreviousOperation(): MutableLiveData<String>{
        return previousOperation
    }

    fun setPreviousOperation(value: String){
        previousOperation.value = value
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

    fun getPreviousNumber(): String{
        return previousNumber
    }

    fun setPreviousNumber(number: String){
        previousNumber = number
    }
}