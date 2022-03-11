package com.example.rapidchidori_mad5254_project.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentLoginBinding
import com.example.rapidchidori_mad5254_project.helper.AppUtils


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
        binding.clOnBoardingTopLayout.tvBack.setOnClickListener(this)
        binding.clOnBoardingTopLayout.tvSignup.setOnClickListener(this)
        binding.clOnBoardingBottomLayout.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.clOnBoardingTopLayout.tvBack.id -> {
                Navigation.findNavController(binding.root).navigateUp()
            }
            binding.clOnBoardingTopLayout.tvSignup.id -> {
                onSignUpClicked()
            }
            binding.clOnBoardingBottomLayout.btnLogin.id -> {
                loginUser()
            }
        }
    }

    private fun loginUser() {
        if (AppUtils.isNetworkAvailable(requireContext())) {
            //showLoader
            //loginUser
        }else{
            Toast.makeText(
                requireContext(),
                "Please check your internet connectivity!!!",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun onSignUpClicked() {
        Navigation.findNavController(binding.root).popBackStack(
            R.id.entryFragment,
            false
        )
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_entryFragment_to_signUpFragment)
    }
}