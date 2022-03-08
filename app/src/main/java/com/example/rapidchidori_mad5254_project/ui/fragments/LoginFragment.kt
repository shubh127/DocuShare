package com.example.rapidchidori_mad5254_project.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.rapidchidori_mad5254_project.databinding.FragmentLoginBinding

class LoginFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
    }

    private fun setUpListeners() {
        binding.tvCancel.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == binding.tvCancel.id) {
            Navigation.findNavController(binding.root).navigateUp()
        }
    }

}