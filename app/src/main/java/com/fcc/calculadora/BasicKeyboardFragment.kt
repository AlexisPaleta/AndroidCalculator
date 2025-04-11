package com.fcc.calculadora

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.fcc.calculadora.databinding.FragmentBasicKeyboardBinding
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [BasicKeyboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BasicKeyboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentBasicKeyboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var basicNumbersVM: BasicNumbersViewModel
    private var currentToast: Toast? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        basicNumbersVM = ViewModelProvider(requireParentFragment()).get(BasicNumbersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentBasicKeyboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ACbutton.setOnClickListener {
            basicNumbersVM.setCurrentOperation("0") //Reset the value of the operation to "0"
            basicNumbersVM.resetNumberLength() //Reset current number length because it is only a "0"
            basicNumbersVM.setFloat(false) //The current number is just a "0", it is not a float
            basicNumbersVM.setNaN(false)
            basicNumbersVM.setCurrentNumber("0")
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
        }

        binding.zeroButton.setOnClickListener {
            val currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
            if (!isMaximumNumberLength() && currentValue != "0") { //Only add a zero if there is not a unique zero
                //and the limit of elements on screen is 10
                basicNumbersVM.addDigit()
                basicNumbersVM.setCurrentOperation(currentValue + '0')
                basicNumbersVM.addCharCurrentNumber('0')
                println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            }

        }

        binding.oneButton.setOnClickListener {
            val value = addNumber('1')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.twoButton.setOnClickListener {
            val value = addNumber('2')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.threeButton.setOnClickListener {
            val value = addNumber('3')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.fourButton.setOnClickListener {
            val value = addNumber('4')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.fiveButton.setOnClickListener {
            val value = addNumber('5')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.sixButton.setOnClickListener {
            val value = addNumber('6')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.sevenButton.setOnClickListener {
            val value = addNumber('7')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.eightButton.setOnClickListener {
            val value = addNumber('8')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.nineButton.setOnClickListener {
            val value = addNumber('9')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.equalButton.setOnClickListener {
            basicNumbersVM.setDoOperation(true)
        }

        binding.plusButton.setOnClickListener {
            val value = addOperator("+")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.minusButton.setOnClickListener {
            val value = addOperator("-")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.divisionButton.setOnClickListener {
            val value = addOperator("÷")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.multiplicationButton.setOnClickListener {
            val value = addOperator("x")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.pointButton.setOnClickListener {
            val currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
            if(!basicNumbersVM.isFloatNumber()){ //Only add the "." when the number was not already a float
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

        binding.changeSignButton.setOnClickListener {
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
                }else{

                }
                //val newOperation = basicNumbersVM.getCurrentOperation().value?.dropLast(currentNumberLength) + sign + changedNumber
                val newOperation = basicNumbersVM.getCurrentOperation().value?.replaceLast(searchNumber2, (sign + changedNumber)) + ""
                basicNumbersVM.setCurrentOperation(newOperation)
                println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            }
        }
    }

    fun addNumber(number: Char): String{
        var currentValue  = basicNumbersVM.getCurrentOperation().value //Check actual operation
        if(basicNumbersVM.isNaN()){
            basicNumbersVM.resetNumberLength()
            basicNumbersVM.setFloat(false)
            currentValue = "0"
            basicNumbersVM.setNaN(false)
            basicNumbersVM.setCurrentNumber("0")
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
        }
        if(currentValue == "0"){ //If the operation is only a "0" then I'll replace it with the value of the pressed button
            basicNumbersVM.addDigit()
            basicNumbersVM.setCurrentNumber("+$number")//If the resultText is showing "0" and the user types a number it will always
            //be positive, if the user press "-" the operation will be "0-" that is the reason
            println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            return number.toString()
        }else if (!isMaximumNumberLength()){// check if the current number is not too large
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

    fun isMaximumNumberLength(): Boolean{ //The maximum digits that a number can have is 10

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

    fun displayMessage(message: String) {
        currentToast?.cancel()
        currentToast = Toast.makeText(activity,message,Toast.LENGTH_SHORT)
        currentToast?.show()
    }

    fun String.replaceLast(oldValue: String, newValue: String): String { //Function used when the changeSignButton was pressed
        val lastIndex = lastIndexOf(oldValue)
        if (lastIndex == -1) {
            return "Error"
        }
        val prefix = substring(0, lastIndex)
        val suffix = substring(lastIndex + oldValue.length)
        return "$prefix$newValue$suffix"
    }

    fun equalButtonPressed(){

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment BasicKeyboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            BasicKeyboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}