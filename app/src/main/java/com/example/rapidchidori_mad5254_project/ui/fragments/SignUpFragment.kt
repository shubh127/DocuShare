package com.example.rapidchidori_mad5254_project.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentSignUpBinding
import com.example.rapidchidori_mad5254_project.ui.adapters.QuestionsAdapter
import com.example.rapidchidori_mad5254_project.ui.viewmodels.SignUpViewModel


class SignUpFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var questionsAdapter: QuestionsAdapter
    private val viewModel: SignUpViewModel by viewModels()

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
        binding.clOnBoardingBottomLayout.btnLogin.text = getString(R.string.next)
        questionsAdapter = QuestionsAdapter(this, viewModel.getSignUpQuestionsData())
        binding.vpQuestions.apply {
            adapter = questionsAdapter
            isUserInputEnabled = false

            registerOnPageChangeCallback(object : OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    if (position + 1 == questionsAdapter.itemCount) {
                        binding.clOnBoardingBottomLayout.btnLogin.text = getString(R.string.signup)
                    } else {
                        binding.clOnBoardingBottomLayout.btnLogin.text = getString(R.string.next)
                    }
                    super.onPageSelected(position)
                }
            })
        }
    }

    private fun setUpListeners() {
        binding.clOnBoardingBottomLayout.btnLogin.setOnClickListener(this)
        binding.clOnBoardingTopLayout.tvBack.setOnClickListener(this)
        binding.clOnBoardingTopLayout.tvSignup.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == binding.clOnBoardingBottomLayout.btnLogin.id) {
            onNextButtonClicked()
        } else if (view?.id == binding.clOnBoardingTopLayout.tvBack.id) {
            onCancelClicked()
        } else if (view?.id == binding.clOnBoardingTopLayout.tvSignup.id) {
            Navigation.findNavController(binding.root).popBackStack(
                R.id.entryFragment,
                false
            )
            Navigation.findNavController(binding.root)
                .navigate(R.id.action_entryFragment_to_loginFragment)
        }
    }

    private fun onCancelClicked() {
        if (binding.vpQuestions.currentItem == 0) {
            Navigation.findNavController(binding.root).navigateUp()
        } else {
            binding.vpQuestions.currentItem = binding.vpQuestions.currentItem - 1
        }
    }

    private fun onNextButtonClicked() {
        binding.vpQuestions.currentItem = binding.vpQuestions.currentItem + 1
    }
}