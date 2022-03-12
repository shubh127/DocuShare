package com.example.rapidchidori_mad5254_project.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Patterns
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
import com.example.rapidchidori_mad5254_project.helper.Constants.CONFIRM_PASSWORD
import com.example.rapidchidori_mad5254_project.helper.Constants.EMAIL
import com.example.rapidchidori_mad5254_project.helper.Constants.FIRST_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.LAST_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.PASSWORD
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_INFO_TABLE_NAME
import com.example.rapidchidori_mad5254_project.ui.activities.DashBoardActivity
import com.example.rapidchidori_mad5254_project.ui.adapters.QuestionsAdapter
import com.example.rapidchidori_mad5254_project.ui.interfaces.QuestionNextListener
import com.example.rapidchidori_mad5254_project.ui.viewmodels.SignUpViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class SignUpFragment : Fragment(), View.OnClickListener, QuestionNextListener {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var questionsAdapter: QuestionsAdapter
    private val viewModel: SignUpViewModel by viewModels()
    private val userDetail = UserDetailInfo()
    private lateinit var auth: FirebaseAuth
    private var databaseReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference?.child(USER_INFO_TABLE_NAME)
    }

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
            LAST_NAME -> {
                checkLastNameValidity(input)
            }
            FIRST_NAME -> {
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
            password.length < 6 -> {
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
            if (AppUtils.isNetworkAvailable(requireContext())) {
                showLoader()
                registerUser()
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

    //todo this should be moved to repository layer somehow
    private fun registerUser() {
        auth.createUserWithEmailAndPassword(userDetail.email!!, userDetail.password!!)
            .addOnCompleteListener {
                if (dialog.isShowing) {
                    dialog.dismiss()
                }
                if (it.isSuccessful) {
                    val user = auth.currentUser
                    val currentUserDb = databaseReference?.child(user!!.uid)
                    currentUserDb?.child("firstName")?.setValue(userDetail.firstName)
                    currentUserDb?.child("lastName")?.setValue(userDetail.lastName)

                    startActivity(Intent(requireContext(), DashBoardActivity::class.java))
                    requireActivity().finish()
                } else {
                    Toast.makeText(
                        requireContext(),
                        it.exception?.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
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

    private fun checkNameValidity(input: String): Boolean {
        val firstName = input.trim()
        when {
            firstName.isEmpty() -> {
                Toast.makeText(context, getString(R.string.empty_name), Toast.LENGTH_SHORT).show()
            }
            firstName.contains(getString(R.string.space)) -> {
                Toast.makeText(
                    context,
                    getString(R.string.space_error_first_name),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            else -> {
                userDetail.firstName = firstName
            }
        }
        return firstName.isNotEmpty()
    }

    private fun checkLastNameValidity(input: String): Boolean {
        val lastName = input.trim()
        return when {
            lastName.isEmpty() -> {
                Toast.makeText(context, getString(R.string.empty_last_name), Toast.LENGTH_SHORT)
                    .show()
                false
            }
            lastName.contains(getString(R.string.space)) -> {
                Toast.makeText(
                    context, getString(R.string.space_error_last_name),
                    Toast.LENGTH_SHORT
                )
                    .show()
                false
            }
            else -> {
                userDetail.lastName = lastName
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
            password.length < 6 -> {
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