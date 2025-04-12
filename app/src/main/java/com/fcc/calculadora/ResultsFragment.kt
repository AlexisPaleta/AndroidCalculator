package com.fcc.calculadora

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fcc.calculadora.databinding.FragmentResultsBinding
import org.mariuszgromada.math.mxparser.*
import java.math.BigDecimal
import java.math.RoundingMode

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ResultsFragment : Fragment() { //This fragment is for the basic calculator functionality
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var basicNumbersVM: BasicNumbersViewModel

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
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentNumberObserver = Observer<String> { currentOperation ->
            binding.resultsText.text = currentOperation
            binding.resultsLayout.post{
                binding.resultsLayout.smoothScrollTo(0, binding.resultsLayout.bottom)
            }

        }

        basicNumbersVM.getCurrentOperation().observe(viewLifecycleOwner, currentNumberObserver)

        val doOperationObserver = Observer<Boolean> { _ ->
            checkOperation()
        }

        basicNumbersVM.getDoOperation().observe(viewLifecycleOwner, doOperationObserver)

    }

    fun checkOperation(){
        //mXparser.setEpsilon(1e-20)
        //mXparser.disableCanonicalRounding()
        println("CurrentOperation: " + basicNumbersVM.getCurrentOperation().value)
        val cleaned = checkEmptyPoints()
        val checked = checkFinalCharacter(cleaned)
        val formatted = changeFormat(checked)
        println("Formatted: " + formatted)
        val expression = Expression(formatted)
        val result = expression.calculate()

        //The isFloatNumber() is for only limit the length of the written number,
        // to know if the result is a float another variable is needed.It is necessary to
        //check the result value returned by mXparser.For example if the written operation is 1÷2, the isFloatNumber() function
        //will not be called, but the result is will be a float so the resultText will wrongly write an int value, that is the reason
        //why this comprobation is necessary. Other case that is considered as a non float number is when it is very big
        //and is written in scientific format like 1.2E20
        val isIntFloat: Boolean
        if(result.toString().endsWith(".0") || result.toString().contains('E')){
            isIntFloat = true
        }else{
            isIntFloat = false
        }

        println("Result: $result")

        //Check if the result is not NaN
        if(result.toString() == "NaN"){
            basicNumbersVM.setNaN(true)
        }

        if(isIntFloat){
            val finalResult: String

            if(result.toString().contains('E')){//Check if the number is in scientific format or not
                //this is to print it correctly
                finalResult = cleanAfterPoint(result, true)
            }else{
                finalResult = result.toInt().toString()
            }

            binding.previousOperationText.text = checked
            basicNumbersVM.setCurrentOperation(finalResult)
            basicNumbersVM.setFloat(false)
            var numberLength = finalResult.length
            if(finalResult.startsWith('-')){//It is necessary to know the sign of the result to save it correctly on the viewModel
                //this is for the changeSignButton
                basicNumbersVM.setCurrentNumber(finalResult)
                numberLength -= 1 //The minus is because there is a "-"
                println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            }else{
                basicNumbersVM.setCurrentNumber("+$finalResult")
                println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            }

            if(finalResult.contains('E')){
                //I want to know the length of the result because when after it appears on the
                //resultText the user could add digits, but I will allow it only if the length
                //is < 11 for not float numbers and < 15 for float numbers
                val numberWithoutScientificNotation = finalResult.split('E')[0]
                numberLength = numberWithoutScientificNotation.length - 1//Minus 1 because there is a "."
            }else{
                numberLength = finalResult.length
            }
            basicNumbersVM.setNumberLength(numberLength)
        }else{
            val finalResult: String = cleanAfterPoint(result, false)
            binding.previousOperationText.text = checked
            basicNumbersVM.setCurrentOperation(finalResult)
            basicNumbersVM.setFloat(true)
            var numberLength = finalResult.length - 1//The minus is because there is a "."
            if(finalResult.startsWith('-')){//It is necessary to know the sign of the result to save it correctly on the viewModel
                //this is for the changeSignButton
                basicNumbersVM.setCurrentNumber(finalResult)
                numberLength -= 1 //The minus is because there is a "-"
                println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            }else{
                basicNumbersVM.setCurrentNumber("+$finalResult")
                println("Current number: ${basicNumbersVM.getCurrentNumber()}")
            }

            basicNumbersVM.setNumberLength(numberLength)


        }

    }

    fun checkEmptyPoints(): String{
        var currentOperation = basicNumbersVM.getCurrentOperation().value
        if (currentOperation == null)
            return "ERROR"

        currentOperation = currentOperation.replace(".+",".0+")
        currentOperation = currentOperation.replace(".-",".0-")
        currentOperation = currentOperation.replace(".÷",".0÷")
        currentOperation = currentOperation.replace(".x",".0x")

        return currentOperation
    }

    fun changeFormat(op: String): String{
        var operation = op
        operation = operation.replace("x","*")
        operation = operation.replace("÷","/")
        operation = operation.replace("—","-")
        return operation
    }

    fun cleanAfterPoint(result: Number, isInScientificNotat: Boolean): String{

        if(isInScientificNotat && result.toString().length>11){
            val dividedNumber = result.toString().split('E')//Separate the number from the scientific notation 'E' exponent
            val reducedNumber = BigDecimal(dividedNumber[0]).setScale((9), RoundingMode.HALF_UP).toString()
            var reformattedResult = "E" + dividedNumber[1]//First reAdd the 'E' that the split omitted
            return reducedNumber + reformattedResult

        }

        if(result.toString().length>14){//Check if the float result is too large, count all the
            //characters (including the ".") and if the condition is true then round the number at the
            //decimal that is the limit of the length permitted
            val dividedNumber = result.toString().split('.')//obtain the int part and the decimal part
            val permittedDecimals = 14 - dividedNumber[0].length//subtraction of 14 - the length of the decimal part
            //that is gonna be the quantity of permittedDecimals
            return BigDecimal(result.toString()).setScale((permittedDecimals), RoundingMode.HALF_UP).toString()
        }else{
            return result.toString()
        }
    }

    fun checkFinalCharacter(cleaned: String): String{//This function is to fix the operations like "1.", "1x", "1÷"
        //because they cause a NaN result, that apparently happens only when it is the final of the operation
        //I mean, when the operation is like "1.+1" the result does not fails
        if (cleaned.endsWith('.')){
            return cleaned + '0'
        }else if(cleaned.endsWith('x') || cleaned.endsWith('÷')){
            return cleaned + '1'
        }else{
            return cleaned
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ResultsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ResultsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}