package com.example.flexJob.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flexJob.databinding.FindEmployeeFragmentBinding

class FindEmployeeFragment : Fragment() {
    private var binding : FindEmployeeFragmentBinding? = null
    private val _binding get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FindEmployeeFragmentBinding.inflate(inflater, container, false)
        return _binding.root


    }
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}