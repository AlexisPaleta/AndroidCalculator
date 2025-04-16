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
        val totalLength = currentValue?.length
        val currentNumber = basicNumbersVM.getCurrentNumber()
        val lastElement = currentValue?.get(totalLength!! - 1).toString()
        val notPermittedSymbols = listOf("%","π","!")
        if (!isMaximumNumberLength() && currentValue != "0" && lastElement !in notPermittedSymbols && !currentNumber.endsWith("\uD835\uDC52")) { //Only add a zero if there is not a unique zero
            //and the limit of elements on screen is 10
            basicNumbersVM.addDigit()
            basicNumbersVM.setCurrentOperation(currentValue + '0')
            basicNumbersVM.addCharCurrentNumber('0')
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            basicNumbersVM.addCharEncapsulatedCurrentNumber('0')
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
        }
    }

    fun addNumber(number: Char): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"//Consider the currentOperation as "0", I don't call the
            //acButtonFunction() because it writes "0" on screen and that is not necessary, but either
            //options are ok
        }
        val totalLength = currentValue?.length
        val lastElement = currentValue?.get(totalLength!! - 1).toString()
        val notPermittedSymbols = listOf("%","π","!")
        val currentNumber = basicNumbersVM.getCurrentNumber()

        if(currentValue == "0"){ //If the operation is only a "0" then I'll replace it with the value of the pressed button
            basicNumbersVM.addDigit()
            basicNumbersVM.setCurrentNumber("+$number")//If the resultText is showing "0" and the user types a number it will always
            //be positive, if the user press "-" the operation will be "0-" that is the reason
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            basicNumbersVM.setEncapsulatedCurrentNumber("$number")
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return number.toString()
        }else if (!isMaximumNumberLength() && lastElement !in notPermittedSymbols && !currentNumber.endsWith("\uD835\uDC52")){// check if the current number is not too large and the just behind element
            //is not a percentage symbol
            basicNumbersVM.addDigit()
            displayMessage("Current numberLength is " + basicNumbersVM.getNumberLength())
            basicNumbersVM.addCharCurrentNumber(number)
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            basicNumbersVM.addCharEncapsulatedCurrentNumber(number)
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return currentValue + number
        }else{
            return currentValue + ""
        }
    }

    fun addOperator(operator: String): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"//Consider the currentOperation as "0", I don't call the
            //acButtonFunction() because it writes "0" on screen and that is not necessary, but either
            //options are ok
        }
        if (currentValue == null)
            return "ERROR"
        val lastElement = currentValue.get(currentValue.length - 1).toString()
        val notPermittedSymbols = listOf("E",".","(")
        if(lastElement in notPermittedSymbols || basicNumbersVM.getCurrentNumber() == "0"){ //do not permit to add an operator immediately after a left parenthesis or other operator
            println("here")
            return currentValue
        }
        val operators = listOf("+","-","÷","x")
        basicNumbersVM.resetNumberLength() //after an operator the next digits will be part of a new number
        basicNumbersVM.setFloat(false)//after an operator the current number is another, by default is not a float
        //so the length needs to be 0
        basicNumbersVM.setScientificNotation(false)
        //Check if the last element of the current operation is a sign, in that case it'll be replaced with the new operator
        if (basicNumbersVM.getCurrentNumber().length>1){ //Without this condition if the user operation is for example "50 +"
            //and then press the "-", when the "+" was pressed the previousButton was correct, a "+50" but with the pressed "-"
            //the previous would be "+" because when the "+" was pressed It was assigned as the current number. This is for only don't lose
            //the real previous number
            basicNumbersVM.setPreviousNumber(basicNumbersVM.getCurrentNumber())//Save the previous number
            if(basicNumbersVM.getOpenParenthesis() == 0 && basicNumbersVM.getReplacePreviousNumberForEncapsulated()){//If all the parenthesis were closed then I will consider the entire
                //encapsulated number as the previousNumber
                basicNumbersVM.setPreviousNumber(basicNumbersVM.getEncapsulatedCurrentNumber())
                basicNumbersVM.setReplacePreviousNumberForEncapsulated(false)//All the parenthesis were closed, until a left is pressed
                //the rightParenthesis function won't replace the previousNumber
                basicNumbersVM.setOnlyWritePercentage(false)//The percentage function can work with the previousNumber logic (if the conditions are the correct ones to permit it)
                println("OnlyWritePercentage = " + basicNumbersVM.getOnlyWritePercentage())
                println("Replacing previousNumber with : " + basicNumbersVM.getEncapsulatedCurrentNumber())
            }
            println("Saved previous number: " + basicNumbersVM.getPreviousNumber())
        }


        if(operator == "-"){
            basicNumbersVM.setCurrentNumber("-")//If a sign is added the current number "restarts" and it will
            //be a negative only if the minus operator is pressed, in other case it will be considered
            //as a positive number, the only way to transform this number into negative is using the changeSign Button
        }else{
            basicNumbersVM.setCurrentNumber("+")
        }
        println("Current number: ${basicNumbersVM.getCurrentNumber()}")
        if(lastElement in operators){
            println("replacing last condition")

            basicNumbersVM.replaceLastCharEncapsulatedCurrentNumber(operator)




            //if(basicNumbersVM.getOpenParenthesis() == 0){
               // basicNumbersVM.setEncapsulatedCurrentNumber("")//All the parenthesis are closed, a new encapsulated starts
            //}//TODO THIS CONDITION MADE THE CHANGE SIGN OPERATION FAIL WHEN THERE WAS A PREVIOUS OPERATOR AND THIS CONDITION WAS TRYINGG TO
            //TODO REPLACE IT AND THE FINAL OPERATOR WAS "-", THE CHANGE SIGN DO NOT FIND THE NUMBER. AN EXAMPLE IS "8+" -> "8x" -> "8-" -> "8-9" AND THEN CHANGE SIGN
            //val newEncapsulated = basicNumbersVM.getEncapsulatedCurrentNumber().dropLast(1) + operator
            //basicNumbersVM.setEncapsulatedCurrentNumber(newEncapsulated)
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return currentValue.dropLast(1) + operator //Drops last character and replaces with the new operator
        }else{
            basicNumbersVM.addStrEncapsulatedCurrentNumber(operator)
            if(basicNumbersVM.getOpenParenthesis() == 0 && !basicNumbersVM.getReplacePreviousNumberForEncapsulated() && operator in listOf("+","-")){
                println("All the parenthesis are closed, a new encapsulated starts")
                basicNumbersVM.setEncapsulatedCurrentNumber(operator)//All the parenthesis are closed, a new encapsulated starts
            }
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return currentValue + operator
        }
    }

    fun pointFunction(){
        val currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        val currentNumber = basicNumbersVM.getCurrentNumber()
        val totalLength = currentValue?.length
        val lastElement = currentValue?.get(totalLength!! - 1).toString()
        val notPermittedSymbols = listOf("\uD835\uDC52","%","π","!")
        if(!basicNumbersVM.isFloatNumber() && !currentNumber.contains('E') && (lastElement !in notPermittedSymbols)){ //Only add the "." when the number was not already a float
            basicNumbersVM.setFloat(true)
            if (currentValue == "0") {
                basicNumbersVM.addDigit()
                basicNumbersVM.setCurrentOperation("0.")
                basicNumbersVM.setCurrentNumber("+0.")
                basicNumbersVM.setEncapsulatedCurrentNumber("0.")
                println("Current number: ${basicNumbersVM.getCurrentNumber()}")
                println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            }else if (lastElement != ")"){ //I won't permit add a "." after a parenthesis
                if(basicNumbersVM.getNumberLength() == 0){//This case is when an operator was pressed
                    val sign = basicNumbersVM.getCurrentNumber() //Recover the sign (remember that it can be
                    // only a "+" or "-" because of the addOperator method)
                    basicNumbersVM.addDigit()
                    basicNumbersVM.setCurrentOperation(currentValue + "0.")
                    basicNumbersVM.setCurrentNumber(sign + "0.")
                    println("Current number: ${basicNumbersVM.getCurrentNumber()}")
                }else{
                    basicNumbersVM.setCurrentOperation(currentValue + ".")
                    basicNumbersVM.addCharCurrentNumber('.')
                    basicNumbersVM.addCharEncapsulatedCurrentNumber('.')
                    println("Current number: ${basicNumbersVM.getCurrentNumber()}")
                    println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
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
            println("searchNumber2: $searchNumber2")
            //val newOperation = basicNumbersVM.getCurrentOperation().value?.dropLast(currentNumberLength) + sign + changedNumber
            val newOperation = basicNumbersVM.getCurrentOperation().value?.replaceLast(searchNumber2, (sign + changedNumber)) + ""
            basicNumbersVM.setCurrentOperation(newOperation)
            val changedEncapsulated = basicNumbersVM.getEncapsulatedCurrentNumber().replaceLast(searchNumber2, (sign + changedNumber))
            println("Encapsulated Current number BEFORE: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            basicNumbersVM.setEncapsulatedCurrentNumber(changedEncapsulated)
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")

            if(basicNumbersVM.getEncapsulatedCurrentNumber() == "${searchNumber2.drop(1)}"){
                basicNumbersVM.setEncapsulatedCurrentNumber(sign + changedNumber)
            }
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
        }
    }

    private fun checkParenthesis(str: String, currentNumber: String):Boolean{//Function do check if the previousNumber is candidate to use it with the
        //percentage logic. The percentage button uses (when the most recent operator is "+" or "-") the previous number of the operator
        //to obtain a certain percentage of the previous number, but with the parenthesis as a part of the current number, a validation
        //is needed because the operation could be "1(+2", if the user press the percentageButton, the previousNumber after using the "+"
        //is "1(" so the operation would try to obtain 2% of the previousNumber in other words 2% of "1(" what is obviously a not valid
        //operation, so in this function I first check if all the parenthesis in the previousNumber are closed and there has to be almost
        //one digit between two parenthesis because "()" cause a NaN result. If the previous number is not valid the percentageButton
        //won't do anything
        var filledOutString = str
        println("OnlyWritePercentage: " + basicNumbersVM.getOnlyWritePercentage())
        if(currentNumber.count('(')>0 || basicNumbersVM.getOnlyWritePercentage()){//If the current number has almost one parenthesis, this happens only when the user
            //is typing something like "1 + 2(4" and then press the '%' Button, maybe the user wants to type 4%x20 so that's the reason
            //to have this condition. There isn't problem with "1 + 2(4 +" and then the '%' Button because the current number in that case
            //will be +4, so the percentage will be of the 4, the condition don't enter
            println("swap")
            basicNumbersVM.setPreviousNumber("_")//"Forcing" to the percentageProcess to take the behavior of only add the '%' symbol
            //and do not obtain the percentage of a previousNumber
            return true
        }

        var leftParenthesisCount = filledOutString.count('(')
        var rightParenthesisCount = filledOutString.count(')')
        var difference =  rightParenthesisCount - leftParenthesisCount

        //if (str.contains('(') && str.get(0)!='(' && difference == 0){
            //for ((index, character) in str.withIndex()){
                //if(character == '('){
                   // filledOutString = str.substring(index,str.length)
                   // println("Taking from the first left parenthesis: filledOutString = $filledOutString")

               // } TODO UNCOMMENT IF THE LEFT PARENTHESIS ALLOWS THE PREVIOUS NUMBER LOGIC, IN OTHER CASE KEEP COMMENTED
            //}
        //}

        leftParenthesisCount = filledOutString.count('(')
        rightParenthesisCount = filledOutString.count(')')

        if(rightParenthesisCount>leftParenthesisCount && filledOutString.startsWith('(')){
            difference =  rightParenthesisCount - leftParenthesisCount
            println("The previousNumber has more rightParenthesis than leftParenthesis, difference: $difference")
            filledOutString = filledOutString.dropLast(difference)
            basicNumbersVM.setPreviousNumber(filledOutString)
            println("New previousNumber: $filledOutString")
            return true
        }else if (rightParenthesisCount>leftParenthesisCount ){
            if (!filledOutString.startsWith('(')){
                filledOutString = '(' + filledOutString
            }
            println("checking again: " + correctInner(filledOutString, 0))
            filledOutString = correctInner(filledOutString, 0)
            leftParenthesisCount = filledOutString.count('(')
            rightParenthesisCount = filledOutString.count(')')
        }
        //an example of a number with the next behavior (using the isNeededInnerNumber) is "1 + (50(70 + 5" and the percentageButton is pressed, in that case currentNumber
        //is 5 and the previous is (50(70, the user has the intention to obtain the 5% of 70 so I have to "find it". By the hierarchy I know
        //that the number that is more and more inner the parenthesis is the number that the user wants to obtain the percentage
        var neededParenthesis = leftParenthesisCount - rightParenthesisCount //Check if there are any rightParenthesis left to add
        val isNeededInnerNumber = ((neededParenthesis > 0) && leftParenthesisCount>0)
        while (neededParenthesis > 0){
            filledOutString += ')'
            neededParenthesis -=1
        }
        println("filledOutString: $filledOutString")
        basicNumbersVM.setPreviousNumber(filledOutString)
        if (isNeededInnerNumber){
            innerNumber(filledOutString, leftParenthesisCount, rightParenthesisCount)
        }

        if(!filledOutString.contains("()") || (filledOutString.contains("(-") || filledOutString.contains("(+"))){
            return true //Check if the conditions are correct, maybe they need readjustment
        }else{
            return false //If false is returned that means that the currentOperation is not correct, it has to be restarted with AC
            //because
        }

    }

    fun correctInner(str: String, difference: Int): String{
        var check = str
        if(difference>1){
            check = str.dropLast(difference-1)
        }
        var position = 0
        var leftCount = difference

        for((index, character) in check.withIndex()){
            if(character == '(' ){
                if(leftCount == 0){
                    position = index
                }
                leftCount -= 1
            }
        }
        return check.substring(position,check.length)
    }

    private fun innerNumber(str: String, leftCount: Int, rightCount: Int){
        var check = str
        var consideredLeftParenthesis = leftCount - rightCount - 1
        var consideredRightParenthesis = leftCount - rightCount - 1

        if (rightCount == 0){//If none leftParenthesis were closed on the original operation then I have to take the first rightParenthesis
            //to now the inner number
            consideredRightParenthesis = 0
        }

        println("consideredLeftParenthesis :" + consideredLeftParenthesis)
        println("consideredRightParenthesis :" + consideredRightParenthesis)

        if((consideredLeftParenthesis == consideredRightParenthesis)){//This function need to be carefully tested
            check = correctInner(str, leftCount - rightCount)
            println("Check for innerNumber is needed")
            if(check != str){
                println("Correct inner ===: $check")
                consideredLeftParenthesis = 0
                consideredRightParenthesis = -1 //The string will take from the 0 index to the final
            }else{
                check = str
                println("Check complete, no needed changes")
            }
        }

        var indexInnerLeftParenthesis = 0
        var indexInnerRightParenthesis = check.length
        for((index, character) in check.withIndex()){
            println("index: $index - character: $character")
            if(character == '(' ){
                if(consideredLeftParenthesis == 0){
                    indexInnerLeftParenthesis = index + 1
                }
                consideredLeftParenthesis -= 1

            }else if(character == ')'){
                if(consideredRightParenthesis == 0){
                    indexInnerRightParenthesis = index
                }
                consideredRightParenthesis -=1
            }
        }

        var innerNumber = check.substring(indexInnerLeftParenthesis, indexInnerRightParenthesis)
        println("innerNumber: " + innerNumber)
        //The next check is because if the operation is like "(50 + (20(50) +6" and then the '%' Button is pressed the previousNumber
        //will be (20(50), the checkParenthesis function will add the remaining Parenthesis so the leftCount - rightCount - 1 will be zero
        //that is ok for the indexInnerLeftParenthesis index but for the indexInnerRightParenthesis will consider the first ')' parenthesis as
        //the limit of the number, so the substring will omit it and the innerNumber will be "20(50", the percentage function will take that as
        //the previous number and because of the parenthesis of "20(50" is not closed the percentage value will not also be closed. To ensure that
        //the collected innerNumber has all its parenthesis closed the next loop adds the necessary ones
        val recountLeft = innerNumber.count('(')
        val recountRight = innerNumber.count(')')
        var neededParenthesisRight = recountLeft - recountRight
        var neededParenthesisLeft = recountRight - recountLeft

        while (neededParenthesisRight > 0){
            innerNumber += ')'
            neededParenthesisRight -=1
        }

        while (neededParenthesisLeft > 0){
            innerNumber = '(' + innerNumber
            neededParenthesisLeft -=1
        }

        println("innerNumber revised: " + innerNumber)
        basicNumbersVM.setPreviousNumber(innerNumber)

    }

    fun percentageFunctionality(): String {
        val actualNumber = basicNumbersVM.getCurrentNumber()
        val currentOperation = basicNumbersVM.getCurrentOperation().value
        val currentNumberLength = actualNumber.length
        val totalLength = basicNumbersVM.getCurrentOperation().value?.length
        var previousNumber = basicNumbersVM.getPreviousNumber()
        println("PreviousNumber = $previousNumber")
        //Check if the current operation wants to obtain the percentage of an specific number
        //In other words, if the user enters 100-1% what the user is trying to say is
        //"100 - (1% * 100)"
        var sign: Char = actualNumber.get(0)
        val notPermittedSymbols = listOf('%','.','(')
        val oneOnly = totalLength!! > currentNumberLength//I need to know if the user is
        //trying to obtain the percentages of a unique number like +0.5% or 10%, if that
        //is the case that has to be in his own condition, and that is not the next line
        if((previousNumber == "(") || !checkParenthesis(previousNumber, actualNumber) || basicNumbersVM.isScientificNotation()){
            println("Not validated")
            return currentOperation + ""
        }
        previousNumber = basicNumbersVM.getPreviousNumber()//It is necessary to call this getter multiple times at the function because
        //it could change during the checkParenthesis process
        println("Processed number: $previousNumber")
        var currentOperator = if(oneOnly) (currentOperation?.get(totalLength!! - (currentNumberLength))) else 'N' //I want to know what is the most recent
        //operator because depending if is a "+" or "-" I do something but if is an "x" or "÷" other process is needed. The oneOnly condition is
        //necessary because if it is false the totalLength!! - (currentNumberLength) operation will throw an exception
        if (previousNumber == "_"){
            currentOperator = 'n'
        }
        println("Actual operator: $currentOperator")
        if((currentOperation != "0") && (previousNumber!= "0") && (oneOnly) && (currentNumberLength > 1) && (currentOperator in listOf('+','-')) ){//The condition
            //is true when the operation is like the followings examples: 100-2%, 0.5+2%. That is because
            //when the operator of the operation is a "+" or "-" the user wants to obtain that percentage of the
            //previous number
            val percentageNumber = "${sign}(${actualNumber.drop(1)}%x(${previousNumber}))" //I will considerate all the parenthesis operation
            //as a unique number, first I separate the sign to write it before the parenthesis and because I already used the sign
            //of the current number in that position I have to omit it inside the parenthesis
            basicNumbersVM.setCurrentNumber(percentageNumber)
            basicNumbersVM.resetNumberLength()//If a digit is entered after the ')' I will consider it as a new number, is like the 'x' operator
            basicNumbersVM.setEncapsulatedCurrentNumber(basicNumbersVM.getEncapsulatedCurrentNumber().dropLast(currentNumberLength))
            basicNumbersVM.addStrEncapsulatedCurrentNumber(percentageNumber)
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return currentOperation?.dropLast(currentNumberLength) + percentageNumber//I returned the new operation that has to be
            //written on screen, but firstly remove the currentNumber because I am rewriting it with the
            //percentageNumber variable, that's why I use the drop with the currentOperation
        }else if((currentOperation != "0")  && (currentNumberLength > 1) && (currentOperation?.get(totalLength-1) !in notPermittedSymbols) && !basicNumbersVM.isNaN()){
            //The currentNumberLength condition is to avoid add a "%" when there is only a "+" or "-"
            basicNumbersVM.setCurrentNumber(actualNumber + '%')//TODO !!IMPORTANT This has to be tested, if affects the changeSign button then remove this line
            basicNumbersVM.addCharEncapsulatedCurrentNumber('%')
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return currentOperation + '%'
        }

        return currentOperation + ""
    }

    fun leftParenthesisFunction(): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"//Consider the currentOperation as "0", I don't call the
            //acButtonFunction() because it writes "0" on screen and that is not necessary, but either
            //options are ok
        }
        if (currentValue == null)
            return "Error left parenthesis"

        if(basicNumbersVM.isScientificNotation()){//A leftParenthesis can't be after the 'E'
            return currentValue
        }
        basicNumbersVM.addOpenParenthesis()//add 1 to the openParenthesis
        basicNumbersVM.setOnlyWritePercentage(true)//after a parenthesis the percentage operation can use the previousNumber logic, but for the moment the only
        //way is outside of parenthesis or using the whole encapsulated number, for that reason the assigned value is 'true'
        basicNumbersVM.setReplacePreviousNumberForEncapsulated(true)
        if(currentValue == "0"){//If the current operation is empty, replace the 0 with the left parenthesis
            basicNumbersVM.setCurrentNumber("+(")
            basicNumbersVM.setEncapsulatedCurrentNumber("(")
            return "("
        }

        basicNumbersVM.resetNumberLength() //after an operator the next digits will be part of a new number
        basicNumbersVM.setFloat(false)//after an operator the current number is another, by default is not a float
        //so the length needs to be 0
        basicNumbersVM.addCharCurrentNumber('(')//The parenthesis will be part of the current number
        basicNumbersVM.addCharEncapsulatedCurrentNumber('(')
        println("Current number: ${basicNumbersVM.getCurrentNumber()}")
        println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
        return currentValue + "("

    }

    fun rightParenthesis(): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        val lastElement = currentValue?.get(currentValue.length - 1)
        val notPermittedSymbols = listOf('x','÷','-','+','(')
        if(currentValue == "0" || lastElement in notPermittedSymbols || basicNumbersVM.getOpenParenthesis() == 0){
            return currentValue + ""
        }
        if(basicNumbersVM.getOpenParenthesis() > 0){//Only execute when there are a correct value of parenthesis
            basicNumbersVM.addCharEncapsulatedCurrentNumber(')')
            basicNumbersVM.subOpenParenthesis()//1 parenthesis was closed
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            if(basicNumbersVM.getOpenParenthesis() == 0 ){//If all the parenthesis were closed then I will consider the entire
                //encapsulated number as the previousNumber
                //basicNumbersVM.setPreviousNumber(basicNumbersVM.getEncapsulatedCurrentNumber())
                //basicNumbersVM.setReplacePreviousNumberForEncapsulated(false)//All the parenthesis were closed, until a left is pressed
                //the rightParenthesis function won't replace the previousNumber
                println("All the parenthesis closed: " + basicNumbersVM.getEncapsulatedCurrentNumber())
            }
        }
        basicNumbersVM.addCharCurrentNumber(')')

        println("Inside rightParenthesisFunction CURRENT NUMBER: " + basicNumbersVM.getCurrentNumber())
        return currentValue + ")"
    }

    fun specificExponentFunction(exponent : String): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"//Consider the currentOperation as "0", I don't call the
            //acButtonFunction() because it writes "0" on screen and that is not necessary, but either
            //options are ok
        }
        if (currentValue == null)
            return "ERROR in specific exponent"
        val lastElement = currentValue.get(currentValue.length - 1).toString()
        println("Last element specific exponent: $lastElement")
        val notPermittedOperators = listOf("x","÷","+","-","^",".","(")
        if(lastElement in notPermittedOperators || currentValue == "0" || basicNumbersVM.isScientificNotation()){ //do not permit to add an '^' immediately after a left parenthesis or other operator
            return currentValue
        }

        if (!isMaximumNumberLength()){// check if the current number is not too large and the just behind element
            //is not a percentage symbol
            basicNumbersVM.addDigit()
            basicNumbersVM.setOnlyWritePercentage(true)
            displayMessage("In exponent, Current numberLength is " + basicNumbersVM.getNumberLength())
            basicNumbersVM.addStrCurrentNumber("^($exponent)")
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            basicNumbersVM.addStrEncapsulatedCurrentNumber("^($exponent)")
            println("Encapsulated Current number: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return currentValue + "^($exponent)"
        }
        return currentValue

    }

    fun customExponent(complement: String): String{//The complement is to use this operation with the customExponent logic and the custom root
        //when the customExponent logic is working the complement is an empty string "", but with the custom root is "1/" because the roots
        //are exponents but diving one, example: the square root of 4 is equal to 4^(1/2).
        //The complement will be added after the leftParenthesis
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"//Consider the currentOperation as "0", I don't call the
            //acButtonFunction() because it writes "0" on screen and that is not necessary, but either
            //options are ok
        }
        if (currentValue == null)
            return "ERROR in customExponent"

        val lastElement = currentValue.get(currentValue.length - 1).toString()
        println("Last element customExponent: $lastElement")
        val notPermittedOperators = listOf("x","÷","+","-","^",".","(")
        if(lastElement in notPermittedOperators || currentValue == "0" || basicNumbersVM.isScientificNotation()){ //do not permit to add an '^' immediately after a left parenthesis or other operator
            return currentValue
        }

        if (!isMaximumNumberLength()){// check if the current number is not too large and the just behind element
            //is not a percentage symbol
            basicNumbersVM.addDigit()
            basicNumbersVM.setOnlyWritePercentage(true)
            displayMessage("In customExponent before left (, Current numberLength is " + basicNumbersVM.getNumberLength())
            basicNumbersVM.setCurrentOperation(currentValue + '^')
            basicNumbersVM.addCharCurrentNumber('^')
            basicNumbersVM.addCharEncapsulatedCurrentNumber('^')
            val newString = leftParenthesisFunction() + complement //The leftParenthesisFunction will add the '(' char to the current, encapsulated
            //and return the currentOperation with the '(' added. The leftParenthesis function gets the currentOperation value that's the
            //reason why I modify the currentOperationValue to add '^' before calling leftParenthesis
            basicNumbersVM.addStrCurrentNumber(complement)
            basicNumbersVM.addStrEncapsulatedCurrentNumber(complement)
            println("Current number in customExponent: ${basicNumbersVM.getCurrentNumber()}")
            println("Encapsulated Current number in customExponent: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return newString
        }
        return currentValue
    }

    fun specialNumbers(number: String): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"
        }
        if (currentValue == null)
            return "ERROR in specialNumbers"

        if (currentValue == "0"){
            basicNumbersVM.addDigit()
            basicNumbersVM.setCurrentNumber("+$number")
            println("Current number in specialNumbers: ${basicNumbersVM.getCurrentNumber()}")
            basicNumbersVM.setEncapsulatedCurrentNumber(number)
            println("Encapsulated Current number in specialNumbers: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return number
        }

        val lastElement = currentValue.get(currentValue.length - 1).toString()
        println("Last element specialNumbers: $lastElement")
        val permittedOperators = listOf("x","÷","+","-","(",")")
        if(lastElement !in permittedOperators || basicNumbersVM.isScientificNotation()){ //Only permit to add the special number after an operator, or parenthesis
            return currentValue
        }

        //is not a percentage symbol
        basicNumbersVM.addDigit()
        displayMessage("Current numberLength is " + basicNumbersVM.getNumberLength())
        basicNumbersVM.addStrCurrentNumber(number)
        println("Current number in specialNumbers: ${basicNumbersVM.getCurrentNumber()}")
        basicNumbersVM.addStrEncapsulatedCurrentNumber(number)
        println("Encapsulated Current number in specialNumbers: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
        return currentValue + number
    }

    fun scientificNotation(): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        val currentNumber = basicNumbersVM.getCurrentNumber()
        val totalLength = currentValue?.length
        val lastElement = currentValue?.get(totalLength!! - 1).toString()
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"//Consider the currentOperation as "0", I don't call the
            //acButtonFunction() because it writes "0" on screen and that is not necessary, but either
            //options are ok
        }
        val notPermittedSymbols = listOf("x","÷","+","-","%","(",".","!","π")
        if (currentValue != "0" && !currentNumber.contains('E') && lastElement !in notPermittedSymbols && !basicNumbersVM.isScientificNotation() && !currentNumber.endsWith("\uD835\uDC52")){
            basicNumbersVM.setNumberLength(2)
            //basicNumbersVM.addCharCurrentNumber('E')
            //basicNumbersVM.addCharEncapsulatedCurrentNumber('E')
            basicNumbersVM.setCurrentNumber("+")
            basicNumbersVM.addCharEncapsulatedCurrentNumber('E')
            basicNumbersVM.setScientificNotation(true)
            return currentValue + 'E'
        }else{
            return currentValue + ""
        }
    }

    fun operationsWithOpenParenthesis(operation: String): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"//Consider the currentOperation as "0", I don't call the
            //acButtonFunction() because it writes "0" on screen and that is not necessary, but either
            //options are ok
        }
        if (currentValue == null)
            return "ERROR in operationsWithOpenParenthesis"

        if (currentValue == "0"){
            basicNumbersVM.addDigit()
            basicNumbersVM.setCurrentOperation(operation)
            basicNumbersVM.setCurrentNumber("+$operation")
            basicNumbersVM.setEncapsulatedCurrentNumber(operation)
            println("Current number in operationsWithOpenParenthesis: ${basicNumbersVM.getCurrentNumber()}")
            println("Encapsulated Current number in specialNumbers: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            val newString = leftParenthesisFunction()
            println("Current number in operationsWithOpenParenthesis: ${basicNumbersVM.getCurrentNumber()}")
            println("Encapsulated Current number in specialNumbers: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return newString
        }

        val lastElement = currentValue.get(currentValue.length - 1).toString()
        println("Last element customExponent: $lastElement")
        val permittedOperators = listOf("x","÷","+","-","(",")")
        if(lastElement !in permittedOperators || basicNumbersVM.isScientificNotation()){ //do not permit to add an '^' immediately after a left parenthesis or other operator
            return currentValue
        }

        if (!isMaximumNumberLength()){// check if the current number is not too large and the just behind element
            //is not a percentage symbol
            basicNumbersVM.addDigit()
            basicNumbersVM.setOnlyWritePercentage(true)
            displayMessage("In operationsWithOpenParenthesis before left (, Current numberLength is " + basicNumbersVM.getNumberLength())
            basicNumbersVM.setCurrentOperation(currentValue + operation)
            basicNumbersVM.addStrCurrentNumber(operation)
            basicNumbersVM.addStrEncapsulatedCurrentNumber(operation)
            val newString = leftParenthesisFunction()  //The leftParenthesisFunction will add the '(' char to the current, encapsulated
            //and return the currentOperation with the '(' added. The leftParenthesis function gets the currentOperation value that's the
            //reason why I modify the currentOperationValue to add '^' before calling leftParenthesis
            println("Current number in operationsWithOpenParenthesis: ${basicNumbersVM.getCurrentNumber()}")
            println("Encapsulated Current number in operationsWithOpenParenthesis: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return newString
        }
        return currentValue
    }

    fun factorial(): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            someResetActions()
            currentValue = "0"//Consider the currentOperation as "0", I don't call the
            //acButtonFunction() because it writes "0" on screen and that is not necessary, but either
            //options are ok
        }
        if (currentValue == null)
            return "ERROR in factorial"

        val notPermittedSymbols = listOf("x","÷","+","-","%","(",".","!","π")
        val currentNumber = basicNumbersVM.getCurrentNumber()
        val totalLength = currentValue?.length
        val lastElement = currentValue?.get(totalLength!! - 1).toString()
        if(currentValue != "0" && !currentNumber.contains('E') && lastElement !in notPermittedSymbols && !basicNumbersVM.isScientificNotation() && !currentNumber.endsWith("\uD835\uDC52")){
            //Do not permit to add numbers after a !
            basicNumbersVM.addCharCurrentNumber('!')
            basicNumbersVM.addCharEncapsulatedCurrentNumber('!')
            println("Current number in specialNumbers: ${basicNumbersVM.getCurrentNumber()}")
            println("Encapsulated Current number in specialNumbers: ${basicNumbersVM.getEncapsulatedCurrentNumber()}")
            return currentValue + '!'
        }
        return currentValue
    }

    private fun someResetActions(){
        basicNumbersVM.resetNumberLength()//Reset current number length
        basicNumbersVM.setFloat(false) //The current number is just a "0", it is not a float
        basicNumbersVM.setNaN(false)
        basicNumbersVM.setCurrentNumber("0")
        basicNumbersVM.setPreviousNumber("0")
        basicNumbersVM.setEncapsulatedCurrentNumber("")
        basicNumbersVM.setOpenParenthesis(0)
        basicNumbersVM.setOnlyWritePercentage(false)
        basicNumbersVM.setScientificNotation(false)
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

    fun String.count(character: Char): Int{
        var count = 0
        for(char in this){
            if(char == character){
                count += 1
            }
        }
        return count
    }


}