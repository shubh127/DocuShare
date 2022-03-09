package com.example.rapidchidori_mad5254_project.ui.fragments

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.SignUpQuestionsInfo
import com.example.rapidchidori_mad5254_project.data.models.request.UserDetailInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentSignUpBinding
import com.example.rapidchidori_mad5254_project.helper.Constants.ALIAS_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.CONFIRM_PASSWORD
import com.example.rapidchidori_mad5254_project.helper.Constants.EMAIL
import com.example.rapidchidori_mad5254_project.helper.Constants.FULL_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.PASSWORD
import com.example.rapidchidori_mad5254_project.ui.adapters.QuestionsAdapter
import com.example.rapidchidori_mad5254_project.ui.interfaces.QuestionNextListener
import com.example.rapidchidori_mad5254_project.ui.viewmodels.SignUpViewModel


class SignUpFragment : Fragment(), View.OnClickListener, QuestionNextListener {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var questionsAdapter: QuestionsAdapter
    private val viewModel: SignUpViewModel by viewModels()
    private val userDetail = UserDetailInfo()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        setUpListeners()
    }

    private fun configViews() {
        binding.clOnBoardingTopLayout.tvSignup.text = getString(R.string.login)
        questionsAdapter = QuestionsAdapter(
            this, viewModel.getSignUpQuestionsData(),
            this
        )
        binding.vpQuestions.apply {
            adapter = questionsAdapter
            isUserInputEnabled = false
        }
    }

    private fun setUpListeners() {
        binding.clOnBoardingTopLayout.tvBack.setOnClickListener(this)
        binding.clOnBoardingTopLayout.tvSignup.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.clOnBoardingTopLayout.tvBack.id -> {
                onCancelClicked()
            }
            binding.clOnBoardingTopLayout.tvSignup.id -> {
                Navigation.findNavController(binding.root).popBackStack(
                    R.id.entryFragment,
                    false
                )
                Navigation.findNavController(binding.root)
                    .navigate(R.id.action_entryFragment_to_loginFragment)
            }
        }
    }

    private fun onCancelClicked() {
        if (binding.vpQuestions.currentItem == 0) {
            Navigation.findNavController(binding.root).navigateUp()
        } else {
            binding.vpQuestions.currentItem = binding.vpQuestions.currentItem - 1
        }
    }

    override fun onNextClick(
        isLastQuestion: Boolean,
        questionInfo: SignUpQuestionsInfo?,
        input: String
    ) {
        val isValid: Boolean = when (questionInfo?.hintTxt) {
            EMAIL -> {
                checkEmailValidity(input)
            }
            PASSWORD -> {
                checkPasswordValidity(input)
            }
            CONFIRM_PASSWORD -> {
                checkConfirmPasswordValidity(input)
            }
            ALIAS_NAME -> {
                checkAliasNameValidity(input)
            }
            FULL_NAME -> {
                checkNameValidity(input)
            }
            else -> {
                false
            }
        }
        if (isValid) {
            handleNextClick(isLastQuestion)
        }
    }

    private fun checkConfirmPasswordValidity(input: String): Boolean {
        val password = input.trim()
        return when {
            password.isEmpty() -> {
                Toast.makeText(context, getString(R.string.empty_password), Toast.LENGTH_SHORT)
                    .show()
                false
            }
            password.length < 7 -> {
                Toast.makeText(
                    context,
                    getString(R.string.small_password),
                    Toast.LENGTH_SHORT
                ).show()
                false

            }
            password != userDetail.password -> {
                Toast.makeText(
                    context,
                    getString(R.string.password_mismatch_error),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
    }

    private fun handleNextClick(isLastQuestion: Boolean) {
        if (isLastQuestion) {
            //todo save data to server
            Toast.makeText(context, "Chla ja!!!", Toast.LENGTH_SHORT).show()
        } else {
            binding.vpQuestions.currentItem = binding.vpQuestions.currentItem + 1
        }
    }

    private fun checkNameValidity(input: String): Boolean {
        val name = input.trim()
        if (name.isEmpty()) {
            Toast.makeText(context, getString(R.string.empty_name), Toast.LENGTH_SHORT).show()
        } else {
            userDetail.name = name
        }
        return name.isNotEmpty()
    }

    private fun checkAliasNameValidity(input: String): Boolean {
        val aliasName = input.trim()
        return when {
            aliasName.isEmpty() -> {
                Toast.makeText(context, getString(R.string.empty_alias), Toast.LENGTH_SHORT)
                    .show()
                false
            }
            aliasName.contains(getString(R.string.space)) -> {
                Toast.makeText(
                    context, getString(R.string.space_error_alias),
                    Toast.LENGTH_SHORT
                )
                    .show()
                false
            }
            else -> {
                userDetail.aliasName = aliasName
                true
            }
        }
    }

    private fun checkPasswordValidity(input: String): Boolean {
        val password = input.trim()
        return when {
            password.isEmpty() -> {
                Toast.makeText(context, getString(R.string.empty_password), Toast.LENGTH_SHORT)
                    .show()
                false
            }
            password.length < 7 -> {
                Toast.makeText(
                    context,
                    getString(R.string.small_password),
                    Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                userDetail.password = password
                true
            }
        }
    }

    private fun checkEmailValidity(input: String): Boolean {
        val valid = input.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(input).matches()
        if (valid) {
            userDetail.email = input.trim()
        } else {
            Toast.makeText(context, getString(R.string.invalid_email), Toast.LENGTH_SHORT)
                .show()
        }
        return valid
    }
}