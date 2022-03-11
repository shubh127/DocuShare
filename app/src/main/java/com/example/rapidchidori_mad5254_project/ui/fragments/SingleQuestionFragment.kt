package com.example.rapidchidori_mad5254_project.ui.fragments

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.ui.SignUpQuestionsInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentSingleQuestionBinding
import com.example.rapidchidori_mad5254_project.helper.Constants.IS_LAST_QUESTION
import com.example.rapidchidori_mad5254_project.helper.Constants.QUESTION_INFO
import com.example.rapidchidori_mad5254_project.ui.interfaces.QuestionNextListener
import com.google.android.material.textfield.TextInputLayout

class SingleQuestionFragment(
    private val listener: QuestionNextListener,
) : Fragment() {
    private lateinit var binding: FragmentSingleQuestionBinding
    private var questionInfo: SignUpQuestionsInfo? = null
    private var isLastQuestion = false

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
        isLastQuestion = arguments?.getBoolean(IS_LAST_QUESTION) == true
    }

    private fun configViews() {
        binding.tvQuestionHead.text = questionInfo?.questionTxt
        binding.tilInput.hint = questionInfo?.hintTxt
        binding.tietInput.inputType = questionInfo?.contentType!!
        binding.clOnBoardingBottomLayout.tvForgotPassword.visibility = View.GONE

        if (questionInfo?.contentType == InputType.TYPE_TEXT_VARIATION_PASSWORD) {
            binding.tilInput.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        } else {
            binding.tilInput.endIconMode = TextInputLayout.END_ICON_NONE
        }

        if (isLastQuestion) {
            binding.clOnBoardingBottomLayout.btnLogin.text = getString(R.string.signup)

        } else {
            binding.clOnBoardingBottomLayout.btnLogin.text = getString(R.string.next)
        }

        binding.clOnBoardingBottomLayout.btnLogin.setOnClickListener {
            listener.onNextClick(isLastQuestion, questionInfo, binding.tietInput.text.toString())
        }
    }
}