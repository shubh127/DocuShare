package com.example.rapidchidori_mad5254_project.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentForgotPasswordBinding
import com.example.rapidchidori_mad5254_project.viewmodels.ForgotPasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotPasswordFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentForgotPasswordBinding
    private val viewModel: ForgotPasswordViewModel by viewModels()
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        setUpListeners()
    }

    private fun configViews() {
        binding.clOnBoardingTopLayout.tvSignup.visibility = View.GONE
        binding.clOnBoardingBottomLayout.tvForgotPassword.visibility = View.GONE
        binding.clOnBoardingBottomLayout.btnLogin.text = getString(R.string.submit)
    }

    private fun setUpListeners() {
        binding.clOnBoardingTopLayout.tvBack.setOnClickListener(this)
        binding.clOnBoardingBottomLayout.btnLogin.setOnClickListener(this)

        viewModel.getIsPasswordRestLiveData().observe(viewLifecycleOwner) {
            showToastAndLoginPage(it)
            Navigation.findNavController(binding.root).navigateUp()
        }

        viewModel.getPasswordResetException().observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun showToastAndLoginPage(isSuccess: Boolean?) {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        if (isSuccess == true) {
            Toast.makeText(
                requireContext(),
                getString(R.string.pass_reset_email_success),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.clOnBoardingTopLayout.tvBack.id -> {
                Navigation.findNavController(binding.root).navigateUp()
            }
            binding.clOnBoardingBottomLayout.btnLogin.id -> {
                validateInput()
            }
        }
    }

    private fun validateInput() {
        if (viewModel.checkEmailValidity(binding.tietInput.text.toString().trim())) {
            showLoader()
            viewModel.sendPasswordResetEmail(binding.tietInput.text.toString().trim())
        } else {
            Toast.makeText(context, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showLoader() {
        dialog = Dialog(requireActivity())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.view_loading_dialog)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
    }

}