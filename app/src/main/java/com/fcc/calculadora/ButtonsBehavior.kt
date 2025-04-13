package com.fcc.calculadora

import android.content.Context
import android.widget.Toast

class ButtonsBehavior(private val basicNumbersVM: BasicNumbersViewModel, private val context: Context, private var currentToast: Toast? = null) {


    fun acButtonFunction(){//The ac button does one more action than the someResetActions
        basicNumbersVM.setCurrentOperation("0") //Reset the value of the operation to "0"
        someResetActions()
    }

    fun zeroButtonFunction(){
        val currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if (!isMaximumNumberLength() && currentValue != "0") { //Only add a zero if there is not a unique zero
            //and the limit of elements on screen is 10
            basicNumbersVM.addDigit()
            basicNumbersVM.setCurrentOperation(currentValue + '0')
            basicNumbersVM.addCharCurrentNumber('0')
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
        }
    }

    fun addNumber(number: Char): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"
        }
        val totalLength = currentValue?.length
        val lastElement = currentValue?.get(totalLength!! - 1)

        if(currentValue == "0"){ //If the operation is only a "0" then I'll replace it with the value of the pressed button
            basicNumbersVM.addDigit()
            basicNumbersVM.setCurrentNumber("+$number")//If the resultText is showing "0" and the user types a number it will always
            //be positive, if the user press "-" the operation will be "0-" that is the reason
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            return number.toString()
        }else if (!isMaximumNumberLength() && lastElement != '%'){// check if the current number is not too large and the just behind element
            //is not a percentage symbol
            basicNumbersVM.addDigit()
            displayMessage("Current numberLength is " + basicNumbersVM.getNumberLength())
            basicNumbersVM.addCharCurrentNumber(number)
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            return currentValue + number
        }else{
            return currentValue + ""
        }
    }

    fun addOperator(operator: String): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            basicNumbersVM.resetNumberLength()
            basicNumbersVM.setFloat(false)
            currentValue = "0"
            basicNumbersVM.setNaN(false)
            basicNumbersVM.setCurrentNumber("0")
        }
        if (currentValue == null)
            return "ERROR"
        val lastElement = currentValue.get(currentValue.length - 1).toString()
        val operators = listOf("+","-","÷","x")
        basicNumbersVM.resetNumberLength() //after an operator the next digits will be part of a new number
        basicNumbersVM.setFloat(false)//after an operator the current number is another, by default is not a float
        //so the length needs to be 0
        //Check if the last element of the current operation is a sign, in that case it'll be replaced with the new operator
        basicNumbersVM.setPreviousNumber(basicNumbersVM.getCurrentNumber())//Save the previous number
        if(operator == "-"){
            basicNumbersVM.setCurrentNumber("-")//If a sign is added the current number "restarts" and it will
            //be a negative only if the minus operator is pressed, in other case it will be considered
            //as a positive number, the only way to transform this number into negative is using the changeSign Button
        }else{
            basicNumbersVM.setCurrentNumber("+")
        }
        println("Current number: ${basicNumbersVM.getCurrentNumber()}")
        if(lastElement in operators){
            return currentValue.dropLast(1) + operator //Drops last character and replaces with the new operator
        }else{
            return currentValue + operator
        }
    }

    fun pointFunction(){
        val currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        val currentNumber = basicNumbersVM.getCurrentNumber()
        val totalLength = currentValue?.length
        val lastElement = currentValue?.get(totalLength!! - 1)
        if(!basicNumbersVM.isFloatNumber() && !currentNumber.contains('E') && (lastElement != '%')){ //Only add the "." when the number was not already a float
            basicNumbersVM.setFloat(true)
            if (currentValue == "0") {
                basicNumbersVM.addDigit()
                basicNumbersVM.setCurrentOperation("0.")
                basicNumbersVM.setCurrentNumber("+0.")
                println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            }else{
                if(basicNumbersVM.getNumberLength() == 0){//This case is only activated when
                    //an operator was pressed
                    val sign = basicNumbersVM.getCurrentNumber() //Recover the sign (remember that it can be
                    // only a "+" or "-" because of the addOperator method)
                    basicNumbersVM.addDigit()
                    basicNumbersVM.setCurrentOperation(currentValue + "0.")
                    basicNumbersVM.setCurrentNumber(sign + "0.")
                    println("Current number: ${basicNumbersVM.getCurrentNumber()}")
                }else{
                    basicNumbersVM.setCurrentOperation(currentValue + ".")
                    basicNumbersVM.addCharCurrentNumber('.')
                    println("Current number: ${basicNumbersVM.getCurrentNumber()}")
                }

            }
        }
    }

    fun changeSignFunction(){
        val oldNumber = basicNumbersVM.getCurrentNumber()
        if(!basicNumbersVM.isNaN() && oldNumber != "0" && oldNumber.length > 1){//Only executes when
            //there is a number on screen, not a zero, not only a sign

            var changedNumber: String
            var sign: String//This is because if the new sign is a "+" it will not be displayed
            //at the result text, it will only appear if the sign is a "-"
            if(oldNumber.startsWith("+")){
                changedNumber = oldNumber.drop(1)//Remove the first digit, the sign
                basicNumbersVM.setCurrentNumber("-$changedNumber")
                sign = "-"
            }else{
                changedNumber = oldNumber.drop(1)//Remove the first digit, the sign
                basicNumbersVM.setCurrentNumber("+$changedNumber")
                sign =  ""
            }
            val currentNumberLength = changedNumber.length
            val totalLength = basicNumbersVM.getCurrentOperation().value?.length
            var isThereAMinus: Char?


            val searchNumber = if(oldNumber.startsWith("-")) oldNumber else oldNumber.drop(1)
            var searchNumber2: String = searchNumber
            if(totalLength!! > currentNumberLength ){
                isThereAMinus = basicNumbersVM.getCurrentOperation().value?.get(totalLength - currentNumberLength - 1)
                println("total $totalLength, curren $currentNumberLength ,isThereAMinus $isThereAMinus")
                if(isThereAMinus == '-' && oldNumber.startsWith("-")){
                    sign = "+"
                }else if (isThereAMinus == '+' && oldNumber.startsWith("+")){
                    sign = "-"
                    searchNumber2 = "+" + searchNumber2
                }
            }

            if(oldNumber.startsWith("-")){
                searchNumber2 = oldNumber
            }
            //val newOperation = basicNumbersVM.getCurrentOperation().value?.dropLast(currentNumberLength) + sign + changedNumber
            val newOperation = basicNumbersVM.getCurrentOperation().value?.replaceLast(searchNumber2, (sign + changedNumber)) + ""
            basicNumbersVM.setCurrentOperation(newOperation)
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
        }
    }

    fun percentageFunctionality(): String {
        val actualNumber = basicNumbersVM.getCurrentNumber()
        val currentOperation = basicNumbersVM.getCurrentOperation().value
        val currentNumberLength = actualNumber.length
        val totalLength = basicNumbersVM.getCurrentOperation().value?.length
        val previousNumber = basicNumbersVM.getPreviousNumber()
        //Check if the current operation wants to obtain the percentage of an specific number
        //In other words, if the user enters 100-1% what the user is trying to say is
        //"100 - (1% * 100)"
        var sign: Char = actualNumber.get(0)

        val oneOnly = totalLength!! > currentNumberLength//I need to know if the user is
        //trying to obtain the percentages of a unique number like +0.5% or 10%, if that
        //is the case that has to be in his own condition, and that is not the next line
        val currentOperator = if(oneOnly) (currentOperation?.get(totalLength!! - (currentNumberLength))) else 'N' //I want to know what is the most recent
        //operator because depending if is a "+" or "-" I do something but if is an "x" or "÷" other process is needed. The oneOnly condition is
        //necessary because if it is false the totalLength!! - (currentNumberLength) operation will throw an exception
        println("Actual operator: $currentOperator")
        if((currentOperation != "0") && (oneOnly) && (currentNumberLength > 1) && (currentOperator in listOf('+','-')) ){//The condition
            //is true when the operation is like the followings examples: 100-2%, 0.5+2%. That is because
            //when the operator of the operation is a "+" or "-" the user wants to obtain that percentage of the
            //previous number
            val percentageNumber = "${sign}(${actualNumber.drop(1)}%x${previousNumber})" //I will considerate all the parenthesis operation
            //as a unique number, first I separate the sign to write it before the parenthesis and because I already used the sign
            //of the current number in that position I have to omit it inside the parenthesis
            basicNumbersVM.setCurrentNumber(percentageNumber)
            basicNumbersVM.resetNumberLength()//I a digit is entered after the ')' I will consider it as a new number, is like the 'x' operator
            return currentOperation?.dropLast(currentNumberLength) + percentageNumber//I returned the new operation that has to be
            //written on screen, but firstly remove the currentNumber because I am rewriting it with the
            //percentageNumber variable, that's why I use the drop with the currentOperation
        }else if((currentOperation != "0") && (currentNumberLength > 1) && (currentOperation?.get(totalLength-1) != '%')){
            return currentOperation + '%'
        }

        return currentOperation + ""
    }

    private fun someResetActions(){
        basicNumbersVM.resetNumberLength()//Reset current number length because it is only a "0"
        basicNumbersVM.setFloat(false) //The current number is just a "0", it is not a float
        basicNumbersVM.setNaN(false)
        basicNumbersVM.setCurrentNumber("0")
        basicNumbersVM.setPreviousNumber("0")
        println("Current number: ${basicNumbersVM.getCurrentNumber()}")
    }

    private fun isMaximumNumberLength(): Boolean{ //The maximum digits that a number can have is 10

        if(basicNumbersVM.isFloatNumber() && basicNumbersVM.getNumberLength() > 13){//If the current number is float, then the maximum length increments, now is 14
            displayMessage("Maximum float length is 14 digits")
            return true
        }else if(basicNumbersVM.isFloatNumber()){
            return false
        }

        if(basicNumbersVM.getNumberLength() > 9){
            displayMessage("Maximum number length is 10 digits")
            return true
        }
        return false
    }

    private fun displayMessage(message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(context,message, Toast.LENGTH_SHORT)
        currentToast?.show()
    }



    fun String.replaceLast(oldValue: String, newValue: String): String { //Function used when the changeSignButton was pressed, the
        //function replaces an specific stringPattern for a new one. The function extends String class so the method executes "on" the
        //string that call the function. An example of works is: "HelloHiTestingFunction".replaceLast("lloHi","12345") -> "He12345TestingFunction"
        val lastIndex = lastIndexOf(oldValue)//First prove that te oldValue string is in the original string, if not then return error
        if (lastIndex == -1) {
            return "Error"
        }
        val prefix = substring(0, lastIndex)//"cut" the string, taking the beginning of the original string and the lastChar before the
        //oldValue pattern
        val suffix = substring(lastIndex + oldValue.length)//take the remaining string, the beginning is the next one char after the
        //oldValue pattern and then take all the remaining string, retaking the example with "HelloHiTestingFunction".replaceLast("loHi","1234")
        // the prefix is "He" and the suffix is "TestingFunction"
        return "$prefix$newValue$suffix"//Merging "He" "12345" "TestingFunction"
    }


}