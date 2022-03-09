package com.example.rapidchidori_mad5254_project.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rapidchidori_mad5254_project.data.models.SignUpQuestionsInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentSingleQuestionBinding
import com.example.rapidchidori_mad5254_project.helper.Constants.QUESTION_INFO
import com.google.android.material.textfield.TextInputLayout


class SingleQuestionFragment : Fragment() {
    private lateinit var binding: FragmentSingleQuestionBinding
    private var questionInfo: SignUpQuestionsInfo? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSingleQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromArguments()
        configViews()
    }

    private fun getDataFromArguments() {
        questionInfo = arguments?.getParcelable(QUESTION_INFO)
    }

    private fun configViews() {
        binding.tvQuestionHead.text = questionInfo?.questionTxt
        binding.tilInput.hint = questionInfo?.hintTxt
        binding.tietInput.inputType = questionInfo?.contentType!!

        if (questionInfo?.contentType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            binding.tilInput.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        } else {
            binding.tilInput.endIconMode = TextInputLayout.END_ICON_NONE
        }
    }
}