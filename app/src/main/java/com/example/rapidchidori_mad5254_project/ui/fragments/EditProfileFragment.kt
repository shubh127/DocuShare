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
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentEditProfileBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.FEMALE
import com.example.rapidchidori_mad5254_project.helper.Constants.MALE
import com.example.rapidchidori_mad5254_project.helper.Constants.NULL
import com.example.rapidchidori_mad5254_project.viewmodels.EditProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        showLoader()
        setUpListeners()
    }

    private fun configViews() {
        binding.tvEditProfileHead.typeface =
            Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)
    }

    private fun setUpListeners() {
        binding.ibClose.setOnClickListener(this)
        binding.ibDone.setOnClickListener(this)

        viewModel.getUserInfoFromFirebase().observe(viewLifecycleOwner) {
            hideLoader()
            setDataToViews(it)
        }

        viewModel.isUpdateSuccess().observe(viewLifecycleOwner) {
            if (it) {
                hideLoader()
                requireActivity().finish()
                Toast.makeText(
                    context, getString(R.string.data_update_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setDataToViews(userInfo: UserInfo) {
        handleDisplayPicture(userInfo.displayPicture)
        binding.tvFullName.text = userInfo.fullName
        binding.tvEmail.text = userInfo.email
        handleGenderView(userInfo.gender)
        checkNullAndSetDataToView(binding.tvDobValue, userInfo.dob)
        checkNullAndSetDataToView(binding.etCollege, userInfo.college)
        checkNullAndSetDataToView(binding.etPhone, userInfo.phoneNo)
    }

    private fun handleDisplayPicture(url: String) {
        if (url.isNotEmpty()) {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        }
    }

    private fun checkNullAndSetDataToView(view: View, value: String) {
        if (value.isNotEmpty() && value != NULL) {
            if (view is TextView) {
                view.text = value
            } else if (view is EditText) {
                view.setText(value)
            }
        }
    }

    private fun handleGenderView(gender: String) {
        if (gender == MALE) {
            binding.rbMaleValue.isChecked = true
        } else if (gender == FEMALE) {
            binding.rbFemaleValue.isChecked = true
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ibClose.id -> {
                requireActivity().finish()
            }
            binding.ibDone.id -> {
                updateDataOnFirebase()
            }
        }
    }

    private fun updateDataOnFirebase() {
        if (binding.etPhone.text.toString().trim().isNotEmpty()) {
            if (viewModel.isPhoneNumberValid(binding.etPhone.text.toString().trim())) {
                showLoader()
                val info = UserInfo(
                    "",
                    binding.tvFullName.text.toString(),
                    getGender(),
                    binding.tvDobValue.text.toString(),
                    binding.etCollege.text.toString().trim(),
                    binding.etPhone.text.toString().trim(),
                    binding.tvEmail.text.toString().trim()
                )
                viewModel.updateDataOnFirebase(info)
            } else {
                Toast.makeText(context, getString(R.string.phone_number_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getGender(): String {
        return if (binding.rbMaleValue.isChecked) {
            MALE
        } else {
            FEMALE
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