package com.example.rapidchidori_mad5254_project.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentEntryBinding

class EntryFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentEntryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpListeners()
    }

    private fun setUpListeners() {
        binding.btnLogin.setOnClickListener(this)
        binding.btnSignUp.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view?.id == binding.btnLogin.id) {
            onLoginClicked()
        } else if (view?.id == binding.btnSignUp.id) {
            onSignUpClicked()
        }
    }

    private fun onSignUpClicked() {
        //todo handle on signup clicked
    }

    private fun onLoginClicked() {
        Navigation.findNavController(binding.root)
            .navigate(
                R.id.action_entryFragment_to_loginFragment
            )
    }
}