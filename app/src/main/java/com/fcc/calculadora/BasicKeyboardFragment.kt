package com.fcc.calculadora

import android.content.Intent
import android.net.Uri
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
    private lateinit var buttonsBehavior: ButtonsBehavior

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
        _binding = FragmentBasicKeyboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.calculatorButton.setOnClickListener {
            val url = "https://github.com/AlexisPaleta/AndroidCalculator"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)

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