package com.example.rapidchidori_mad5254_project.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentOthersProfileBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersProfileFragment : Fragment() {
    private lateinit var binding: FragmentOthersProfileBinding
    private var data: UserInfo? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOthersProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
    }

    private fun configViews() {
        data = arguments?.getParcelable(Constants.USER_INFO_TABLE_NAME)

        binding.tvUserName.apply {
            text = data?.fullName
            typeface = Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)
        }
        binding.tvName.text = data?.fullName
        binding.tvEmail.text = data?.email
        data?.dob?.let { checkNullAndSetDataToView(binding.tvDobValue, it) }
        data?.college?.let { checkNullAndSetDataToView(binding.tvCollegeValue, it) }
        data?.phoneNo?.let { checkNullAndSetDataToView(binding.tvPhoneValue, it) }
        data?.gender?.let { checkNullAndSetDataToView(binding.tvGenderValue, it) }
    }

    private fun checkNullAndSetDataToView(view: View, value: String) {
        var txt = value
        if (value.isEmpty() || value == Constants.NULL) {
            txt = getString(R.string.not_mentioned)
        }
        if (view is TextView) {
            view.text = txt
        } else if (view is EditText) {
            view.setText(txt)
        }
    }
}