package com.fcc.calculadora

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.fcc.calculadora.databinding.FragmentLandscapeResultsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LandscapeResultsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandscapeResultsFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var _binding: FragmentLandscapeResultsBinding? = null
    private val binding get() = _binding!!
    private lateinit var basicNumbersVM: BasicNumbersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        basicNumbersVM = ViewModelProvider(requireActivity()).get(BasicNumbersViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentLandscapeResultsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentNumberObserver = Observer<String> { currentOperation ->
            binding.landscapeResultsText.text = currentOperation
            binding.landscapeResultsLayout.post{
                binding.landscapeResultsLayout.smoothScrollTo(0, binding.landscapeResultsLayout.bottom)
            }
        }

        basicNumbersVM.getCurrentOperation().observe(viewLifecycleOwner, currentNumberObserver)

        val previousNumberObserver = Observer<String> { previousOperation ->
            binding.previousLandscapeOperationText.text = previousOperation
        }

        basicNumbersVM.getPreviousOperation().observe(viewLifecycleOwner, previousNumberObserver)
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
         * @return A new instance of fragment LandscapeResultsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            LandscapeResultsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}