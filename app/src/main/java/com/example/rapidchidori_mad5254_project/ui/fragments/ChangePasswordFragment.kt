package com.example.rapidchidori_mad5254_project.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentChangePasswordBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.viewmodels.ChangePasswordViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentChangePasswordBinding
    private val viewModel: ChangePasswordViewModel by viewModels()
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        setUpListeners()
    }

    private fun configViews() {
        binding.tvHeading.typeface =
            Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)
    }

    private fun setUpListeners() {
        binding.btnCancel.setOnClickListener(this)
        binding.btnConfirm.setOnClickListener(this)

        viewModel.getPassUpdateLiveData().observe(viewLifecycleOwner) {
            hideLoader()
            onPasswordUpdate(it)
        }
    }

    private fun onPasswordUpdate(it: Int?) {
        Toast.makeText(context, getString(it!!), Toast.LENGTH_SHORT).show()
        if (it == R.string.password_updated_success) {
            requireActivity().onBackPressed()
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.btnCancel.id -> {
                requireActivity().onBackPressed()
            }
            binding.btnConfirm.id -> {
                validateInputs()
            }
        }
    }

    private fun validateInputs() {
        val oldPassword = binding.tietOldPassword.text.toString().trim()
        val newPassword = binding.tietNewPassword.text.toString().trim()
        val confirmPassword = binding.tietConfirmPassword.text.toString().trim()
        if (oldPassword == "" || oldPassword.length < 6) {
            Toast.makeText(context, getString(R.string.old_password_error), Toast.LENGTH_SHORT)
                .show()
        } else if (newPassword == "" || newPassword.length < 6) {
            Toast.makeText(
                context, getString(R.string.new_password_error), Toast.LENGTH_SHORT
            ).show()
        } else if (confirmPassword == "" || confirmPassword.length < 6) {
            Toast.makeText(
                context, getString(R.string.confirm_password_error), Toast.LENGTH_SHORT
            ).show()
        } else if (newPassword != confirmPassword) {
            Toast.makeText(
                context, getString(R.string.password_doesnot_match_error), Toast.LENGTH_SHORT
            ).show()
        } else {
            showLoader()
            viewModel.changePassword(
                arguments?.getString(Constants.EMAIL),
                binding.tietOldPassword.text.toString().trim(),
                binding.tietNewPassword.text.toString().trim()
            )
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

    private fun hideLoader() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }
}