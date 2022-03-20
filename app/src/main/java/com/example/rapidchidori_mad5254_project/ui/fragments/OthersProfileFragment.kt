package com.example.rapidchidori_mad5254_project.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rapidchidori_mad5254_project.databinding.FragmentOthersProfileBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersProfileFragment : Fragment() {
    private lateinit var binding: FragmentOthersProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOthersProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

}