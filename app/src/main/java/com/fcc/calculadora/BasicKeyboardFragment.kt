package com.fcc.calculadora

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.fcc.calculadora.databinding.FragmentBasicKeyboardBinding

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
            basicNumbersVM.setCurrent("0") //Reset the value of the operation to "0"
        }

        binding.zeroButton.setOnClickListener {
            val currentValue  = basicNumbersVM.getCurrent().value //Check actual operation
            if(currentValue != null && currentValue != "0" && currentValue.length < 10)//Only add a zero if there is not a unique zero
                //and the limit of elements on screen is 10
                basicNumbersVM.setCurrent(currentValue + "0")

        }

        binding.oneButton.setOnClickListener {
            val value = addNumber("1")
            basicNumbersVM.setCurrent(value)
        }

        binding.twoButton.setOnClickListener {
            val value = addNumber("2")
            basicNumbersVM.setCurrent(value)
        }

        binding.threeButton.setOnClickListener {
            val value = addNumber("3")
            basicNumbersVM.setCurrent(value)
        }

        binding.fourButton.setOnClickListener {
            val value = addNumber("4")
            basicNumbersVM.setCurrent(value)
        }

        binding.fiveButton.setOnClickListener {
            val value = addNumber("5")
            basicNumbersVM.setCurrent(value)
        }

        binding.sixButton.setOnClickListener {
            val value = addNumber("6")
            basicNumbersVM.setCurrent(value)
        }

        binding.sevenButton.setOnClickListener {
            val value = addNumber("7")
            basicNumbersVM.setCurrent(value)
        }

        binding.eightButton.setOnClickListener {
            val value = addNumber("8")
            basicNumbersVM.setCurrent(value)
        }

        binding.nineButton.setOnClickListener {
            val value = addNumber("9")
            basicNumbersVM.setCurrent(value)
        }

        binding.equalButton.setOnClickListener {
            basicNumbersVM.setDoOperation(true)
        }

        binding.plusButton.setOnClickListener {
            val value = addOperator("+")
            basicNumbersVM.setCurrent(value)
        }

        binding.minusButton.setOnClickListener {
            val value = addOperator("—")
            basicNumbersVM.setCurrent(value)
        }

        binding.divisionButton.setOnClickListener {
            val value = addOperator("÷")
            basicNumbersVM.setCurrent(value)
        }

        binding.multiplicationButton.setOnClickListener {
            val value = addOperator("x")
            basicNumbersVM.setCurrent(value)
        }
    }

    fun addNumber(number: String): String{
        val currentValue  = basicNumbersVM.getCurrent().value //Check actual operation
        if(currentValue == "0"){ //If the operation is only a "0" then I'll replace it with the value of the pressed button
            return number
        }else if (currentValue != null && currentValue.length < 100){// Only ten elements can appear on screen
            return currentValue + number
        }else{
            return currentValue + ""
        }
    }

    fun addOperator(operator: String): String{
        val currentValue  = basicNumbersVM.getCurrent().value //Check actual operation
        if (currentValue == null)
            return "ERROR"
        val lastElement = currentValue.get(currentValue.length - 1).toString()
        val operators = listOf("+","—","÷","x")

        //Check if the last element of the current operation is a sign, in that case it'll be replaced with the new operator
        if(lastElement in operators){
            return currentValue.dropLast(1) + operator //Drops last character and replaces with the new operator
        }else{
            return currentValue + operator
        }

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