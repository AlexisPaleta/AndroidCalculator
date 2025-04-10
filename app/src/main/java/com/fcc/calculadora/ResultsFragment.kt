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
    private var changingDoOperation = false //I want to modify the viewModel value in the observer process

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

        val cleaned = checkEmptyPoints()
        val formatted = changeFormat(cleaned)
        val expression = Expression(formatted)
        val result = expression.calculate()

        //The isFloatNumber() functions is activated only when a point is used to do an operation, but It is necessary to also
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

        println(result)

        if(!basicNumbersVM.isFloatNumber() && isIntFloat){
            val finalResult: String

            if(result.toString().contains('E')){//Check if the number is in scientific format or not
                //this is to print it correctly
                finalResult = cleanAfterPoint(result, true)
            }else{
                finalResult = result.toInt().toString()
            }

            binding.previousOperationText.text = cleaned
            basicNumbersVM.setCurrentOperation(finalResult)
        }else{
            val finalResult: String = cleanAfterPoint(result, false)
            binding.previousOperationText.text = cleaned
            basicNumbersVM.setCurrentOperation(finalResult)
        }

    }

    fun checkEmptyPoints(): String{
        var currentOperation = basicNumbersVM.getCurrentOperation().value
        if (currentOperation == null)
            return "ERROR"

        currentOperation = currentOperation.replace(".+",".0+")
        currentOperation = currentOperation.replace(".—",".0—")
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

        if(result.toString().length>11){//Check if the float result is too large, count all the
            //characters (including the ".") and if the condition is true then round the number at the
            //decimal that is the limit of the length permitted
            val dividedNumber = result.toString().split('.')//obtain the int part and the decimal part
            val permittedDecimals = 10 - dividedNumber[0].length//subtraction of 11 - the length of the decimal part
            //that is gonna be the quantity of permittedDecimals
            return BigDecimal(result.toString()).setScale((permittedDecimals), RoundingMode.HALF_UP).toString()
        }else{
            return result.toString()
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