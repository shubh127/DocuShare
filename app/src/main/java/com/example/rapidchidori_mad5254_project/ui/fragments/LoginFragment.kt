package com.example.rapidchidori_mad5254_project.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentLoginBinding
import com.example.rapidchidori_mad5254_project.helper.AppUtils
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.ui.activities.DashBoardActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dialog: Dialog
    private var database: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference?.child(Constants.USER_INFO_TABLE_NAME)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
    }

    private fun setUpListeners() {
        binding.clOnBoardingTopLayout.tvBack.setOnClickListener(this)
        binding.clOnBoardingTopLayout.tvSignup.setOnClickListener(this)
        binding.clOnBoardingBottomLayout.btnLogin.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.clOnBoardingTopLayout.tvBack.id -> {
                Navigation.findNavController(binding.root).navigateUp()
            }
            binding.clOnBoardingTopLayout.tvSignup.id -> {
                onSignUpClicked()
            }
            binding.clOnBoardingBottomLayout.btnLogin.id -> {
                validateInputAndLogin()
            }
        }
    }

    private fun validateInputAndLogin() {
        if (binding.tietEmail.text?.trim()?.isNotEmpty() == true
            && binding.tietPassword.text?.trim()?.isNotEmpty() == true
        ) {
            loginUser()
        } else {
            Toast.makeText(
                context,
                getString(R.string.login_fields_empty),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }

    private fun loginUser() {
        if (AppUtils.isNetworkAvailable(requireContext())) {
            showLoader()
            doLogin()
        } else {
            Toast.makeText(
                requireContext(),
                getString(R.string.check_internet_msg),
                Toast.LENGTH_SHORT
            ).show()
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

    //todo this should be moved to repository layer somehow
    private fun doLogin() {
        auth.signInWithEmailAndPassword(
            binding.tietEmail.text?.toString()?.trim()!!,
            binding.tietPassword.text?.toString()?.trim()!!
        ).addOnCompleteListener {
            if (dialog.isShowing) {
                dialog.dismiss()
            }
            if (it.isSuccessful) {
                //todo take user to main dashboard
                //get user profile data
//                val user = auth.currentUser
//                val userReference = databaseReference?.child(user?.uid!!)
//                userReference?.addValueEventListener(object : ValueEventListener {
//                    override fun onDataChange(snapshot: DataSnapshot) {
//                        Toast.makeText(
//                            requireContext(),
//                            snapshot.child("name").value.toString(),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//
//                    override fun onCancelled(error: DatabaseError) {
//                        //no op
//                    }
//
//                })
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

    private fun onSignUpClicked() {
        Navigation.findNavController(binding.root).popBackStack(
            R.id.entryFragment,
            false
        )
        Navigation.findNavController(binding.root)
            .navigate(R.id.action_entryFragment_to_signUpFragment)
    }
}