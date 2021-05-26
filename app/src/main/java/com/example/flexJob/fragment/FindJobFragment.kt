package com.example.flexJob.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.flexJob.databinding.FindJobFragmentBinding

class FindJobFragment : Fragment() {
    private var binding : FindJobFragmentBinding? = null
    private val _binding get() = binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let{
            _binding.login.setOnClickListener {

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FindJobFragmentBinding.inflate(inflater,container,false)
        return _binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}