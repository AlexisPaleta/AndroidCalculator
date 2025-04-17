package com.fcc.calculadora

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fcc.calculadora.databinding.FragmentLanscapeKeyboardBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LandscapeKeyboardFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandscapeKeyboardFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentLanscapeKeyboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var basicNumbersVM: BasicNumbersViewModel
    private lateinit var buttonsBehavior: ButtonsBehavior
    private lateinit var sin: String
    private lateinit var cos: String
    private lateinit var tan: String
    private lateinit var sinh: String
    private lateinit var cosh: String
    private lateinit var tanh: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        basicNumbersVM = ViewModelProvider(requireActivity()).get(BasicNumbersViewModel::class.java)
        buttonsBehavior = ButtonsBehavior(basicNumbersVM, requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLanscapeKeyboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radiansModeObserver = Observer<Boolean>{value ->
            if (value){
                binding.DegButton.text = "Rad"
                binding.DegButton.setBackgroundResource(R.drawable.activated_button) //Changing the Deg button style
            }else{
                binding.DegButton.text = "Deg"
                binding.DegButton.setBackgroundResource(R.drawable.rounded_button) //Changing the Deg button style
            }
        }

        basicNumbersVM.isRadiansMode().observe(viewLifecycleOwner, radiansModeObserver)

        val inverseTrigonometric = Observer<Boolean>{value ->
           inverseOperations(value)
        }

        basicNumbersVM.isInverseTrigonometric().observe(viewLifecycleOwner, inverseTrigonometric)

        binding.ACbutton.setOnClickListener {
            buttonsBehavior.acButtonFunction()
        }

        binding.zeroButton.setOnClickListener {
            buttonsBehavior.zeroButtonFunction()
        }

        binding.oneButton.setOnClickListener {
            val value = buttonsBehavior.addNumber('1')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.twoButton.setOnClickListener {
            val value = buttonsBehavior.addNumber('2')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.threeButton.setOnClickListener {
            val value = buttonsBehavior.addNumber('3')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.fourButton.setOnClickListener {
            val value = buttonsBehavior.addNumber('4')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.fiveButton.setOnClickListener {
            val value = buttonsBehavior.addNumber('5')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.sixButton.setOnClickListener {
            val value = buttonsBehavior.addNumber('6')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.sevenButton.setOnClickListener {
            val value = buttonsBehavior.addNumber('7')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.eightButton.setOnClickListener {
            val value = buttonsBehavior.addNumber('8')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.nineButton.setOnClickListener {
            val value = buttonsBehavior.addNumber('9')
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.equalButton.setOnClickListener {
            basicNumbersVM.setDoOperation(true)
        }

        binding.plusButton.setOnClickListener {
            val value = buttonsBehavior.addOperator("+")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.minusButton.setOnClickListener {
            val value = buttonsBehavior.addOperator("-")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.divisionButton.setOnClickListener {
            val value = buttonsBehavior.addOperator("÷")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.multiplicationButton.setOnClickListener {
            val value = buttonsBehavior.addOperator("x")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.pointButton.setOnClickListener {
            buttonsBehavior.pointFunction()
        }

        binding.changeSignButton.setOnClickListener {
            buttonsBehavior.changeSignFunction()
        }

        binding.percentageButton.setOnClickListener {
            val value = buttonsBehavior.percentageFunctionality()
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.leftParenthesisButton.setOnClickListener {
            val value = buttonsBehavior.leftParenthesisFunction()
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.rightParenthesisButton.setOnClickListener {
            val value = buttonsBehavior.rightParenthesis()
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.powerTwoButton.setOnClickListener {
            val value = buttonsBehavior.specificExponentFunction("2")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.powerThreeButton.setOnClickListener {
            val value = buttonsBehavior.specificExponentFunction("3")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.xYButton.setOnClickListener {
            val value = buttonsBehavior.customExponent("")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.oneDivXButton.setOnClickListener {
            val value = buttonsBehavior.specificExponentFunction("-1")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.root2Button.setOnClickListener {
            val value = buttonsBehavior.specificExponentFunction("1/2")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.root3Button.setOnClickListener {
            val value = buttonsBehavior.specificExponentFunction("1/3")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.rootYButton.setOnClickListener {
            val value = buttonsBehavior.customExponent("1/")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.PiButton.setOnClickListener {
            val value = buttonsBehavior.specialNumbers("π")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.eButton.setOnClickListener {
            val value = buttonsBehavior.specialNumbers("\uD835\uDC52")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.tenXButton.setOnClickListener {
            val value = buttonsBehavior.scientificNotation()
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.eXButton.setOnClickListener {
            val value = buttonsBehavior.operationsWithOpenParenthesis("\uD835\uDC52^")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.lnButton.setOnClickListener {
            val value = buttonsBehavior.operationsWithOpenParenthesis("ln")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.logButton.setOnClickListener {
            val value = buttonsBehavior.operationsWithOpenParenthesis("log")
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.sinButton.setOnClickListener {
            val value = buttonsBehavior.operationsWithOpenParenthesis(sin)
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.cosButton.setOnClickListener {
            val value = buttonsBehavior.operationsWithOpenParenthesis(cos)
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.tanButton.setOnClickListener {
            val value = buttonsBehavior.operationsWithOpenParenthesis(tan)
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.sinhButton.setOnClickListener {
            val value = buttonsBehavior.operationsWithOpenParenthesis(sinh)
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.coshButton.setOnClickListener {
            val value = buttonsBehavior.operationsWithOpenParenthesis(cosh)
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.tanhButton.setOnClickListener {
            val value = buttonsBehavior.operationsWithOpenParenthesis(tanh)
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.xExclamationButton.setOnClickListener {
            val value = buttonsBehavior.factorial()
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.RandButton.setOnClickListener {
            val value = buttonsBehavior.randomNumber()
            basicNumbersVM.setCurrentOperation(value)
        }

        binding.DegButton.setOnClickListener {
            val value = basicNumbersVM.isRadiansMode().value
            if(value == true){
                basicNumbersVM.setRadiansMode(false)
            }else if(value == false){
                basicNumbersVM.setRadiansMode(true)
            }
        }

        binding.secoNDButton.setOnClickListener {
            val value = basicNumbersVM.isInverseTrigonometric().value
            if(value == true){
                basicNumbersVM.setInverseTrigonometric(false)
            }else if(value == false){
                basicNumbersVM.setInverseTrigonometric(true)
            }
        }

        binding.calculatorButton.setOnClickListener {
            val url = "https://github.com/AlexisPaleta/AndroidCalculator"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

        }
    }

    fun inverseOperations(value: Boolean){
        if (value){ //If the second button is pressed the value is true, the trigonometric buttons now show inverse operations
            //and the strings that the buttons execute are the inverse ones
            sin = getString(R.string.asin)
            cos = getString(R.string.acos)
            tan = getString(R.string.atan)
            sinh = getString(R.string.asinh)
            cosh = getString(R.string.acosh)
            tanh = getString(R.string.atanh)

            binding.sinButton.text = getString(R.string.asinButton)
            binding.cosButton.text = getString(R.string.acosButton)
            binding.tanButton.text = getString(R.string.atanButtoon)
            binding.sinhButton.text = getString(R.string.asinhButton)
            binding.coshButton.text = getString(R.string.acoshButton)
            binding.tanhButton.text = getString(R.string.atanhButton)

            binding.secoNDButton.setBackgroundResource(R.drawable.activated_button) //Changing the 2nd button style
        }else{
            sin = getString(R.string.sin)
            cos = getString(R.string.cos)
            tan = getString(R.string.tan)
            sinh = getString(R.string.sinh)
            cosh = getString(R.string.cosh)
            tanh = getString(R.string.tanh)

            binding.sinButton.text = sin
            binding.cosButton.text = cos
            binding.tanButton.text = tan
            binding.sinhButton.text = sinh
            binding.coshButton.text = cosh
            binding.tanhButton.text = tanh

            binding.secoNDButton.setBackgroundResource(R.drawable.rounded_button) //Changing the 2nd button style
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment LanscapeKeyboardFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LandscapeKeyboardFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}