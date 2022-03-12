package com.example.rapidchidori_mad5254_project.ui.fragments

import android.app.Dialog
import android.content.Intent
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
import com.example.rapidchidori_mad5254_project.data.models.request.UserDetailInfo
import com.example.rapidchidori_mad5254_project.data.models.ui.SignUpQuestionsInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentSignUpBinding
import com.example.rapidchidori_mad5254_project.helper.AppUtils
import com.example.rapidchidori_mad5254_project.ui.activities.DashBoardActivity
import com.example.rapidchidori_mad5254_project.ui.adapters.QuestionsAdapter
import com.example.rapidchidori_mad5254_project.ui.interfaces.QuestionNextListener
import com.example.rapidchidori_mad5254_project.viewmodels.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment(), View.OnClickListener, QuestionNextListener {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var questionsAdapter: QuestionsAdapter
    private val viewModel: SignUpViewModel by viewModels()
    private val userDetail = UserDetailInfo()

    private lateinit var dialog: Dialog

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

        viewModel.getIsSuccessLiveData().observe(viewLifecycleOwner) {
            openDashBoard(it)
        }
    }

    private fun openDashBoard(isSuccess: Boolean) {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
        if (isSuccess) {
            startActivity(Intent(requireContext(), DashBoardActivity::class.java))
            requireActivity().finish()
        }
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
            getString(R.string.email) -> {
                viewModel.checkEmailValidity(input)
            }
            getString(R.string.password) -> {
                viewModel.checkPasswordValidity(input)
            }
            getString(R.string.confirm_password) -> {
                viewModel.checkConfirmPasswordValidity(input)
            }
            getString(R.string.last_name) -> {
                viewModel.checkLastNameValidity(input)
            }
            getString(R.string.first_name) -> {
                viewModel.checkNameValidity(input)
            }
            else -> {
                false
            }
        }
        if (isValid) {
            handleNextClick(isLastQuestion)
        }
    }

    private fun handleNextClick(isLastQuestion: Boolean) {
        if (isLastQuestion) {
            if (AppUtils.isNetworkAvailable(requireContext())) {
                showLoader()
                viewModel.registerUser(userDetail)
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.check_internet_msg),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            binding.vpQuestions.currentItem = binding.vpQuestions.currentItem + 1
        }
    }

    private fun showLoader() {
        dialog = Dialog(requireActivity())
        dialog.apply {
            requestWindowFeature(Window.FEATURE_NO_TITLE)
            window?.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
            setContentView(R.layout.view_loading_dialog)
            setCancelable(false)
            setCanceledOnTouchOutside(false)
            show()
        }
    }
}