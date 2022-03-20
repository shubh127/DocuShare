package com.example.rapidchidori_mad5254_project.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        if (binding.tietOldPassword.text.toString().trim() == "" ||
            binding.tietOldPassword.text.toString().trim().length < 6
        ) {
            Toast.makeText(context, getString(R.string.old_password_error), Toast.LENGTH_SHORT)
                .show()
        } else if (binding.tietNewPassword.text.toString().trim() == "" ||
            binding.tietNewPassword.text.toString().trim().length < 6
        ) {
            Toast.makeText(
                context, getString(R.string.new_password_error), Toast.LENGTH_SHORT
            ).show()
        } else if (binding.tietConfirmPassword.text.toString().trim() == "" ||
            binding.tietConfirmPassword.text.toString().trim().length < 6
        ) {
            Toast.makeText(
                context, getString(R.string.confirm_password_error), Toast.LENGTH_SHORT
            ).show()
        } else if (binding.tietNewPassword.text.toString().trim() ==
            binding.tietConfirmPassword.text.toString().trim()
        ) {
            Toast.makeText(
                context, getString(R.string.password_doesnot_match_error), Toast.LENGTH_SHORT
            ).show()
        } else {
            viewModel.changePassword(
                arguments?.getString(Constants.EMAIL),
                binding.tietOldPassword.text.toString().trim(),
                binding.tietNewPassword.text.toString().trim()
            )
        }
    }
}