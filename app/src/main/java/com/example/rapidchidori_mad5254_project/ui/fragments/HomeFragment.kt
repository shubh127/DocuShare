package com.example.rapidchidori_mad5254_project.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentHomeBinding


class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var animOpen: Animation
    private lateinit var animClose: Animation
    private lateinit var animForward: Animation
    private lateinit var animBackward: Animation
    private var isFabOpen = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        setUpListeners()
    }


    private fun configViews() {
        setUpAnimations()
    }

    private fun setUpAnimations() {
        animOpen = AnimationUtils.loadAnimation(requireActivity(), R.anim.fab_open)
        animClose = AnimationUtils.loadAnimation(requireActivity(), R.anim.fab_close)
        animForward = AnimationUtils.loadAnimation(requireActivity(), R.anim.rotate_forward)
        animBackward = AnimationUtils.loadAnimation(requireActivity(), R.anim.rotate_backward)
    }

    private fun setUpListeners() {
        binding.fabFileManager.setOnClickListener(this)
        binding.fabCamera.setOnClickListener(this)
        binding.fabChooseFile.setOnClickListener(this)
        binding.fabGallery.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (view.id == binding.fabChooseFile.id) {
            animateFAB()
        }
    }

    private fun animateFAB() {
        if (isFabOpen) {
            binding.fabChooseFile.startAnimation(animBackward)
            binding.fabGallery.startAnimation(animClose)
            binding.fabCamera.startAnimation(animClose)
            binding.fabFileManager.startAnimation(animClose)
            binding.fabGallery.isClickable = false
            binding.fabCamera.isClickable = false
            binding.fabFileManager.isClickable = false
            isFabOpen = false
        } else {
            binding.fabChooseFile.startAnimation(animForward)
            binding.fabGallery.startAnimation(animOpen)
            binding.fabCamera.startAnimation(animOpen)
            binding.fabFileManager.startAnimation(animOpen)
            binding.fabGallery.isClickable = true
            binding.fabCamera.isClickable = true
            binding.fabFileManager.isClickable = true
            isFabOpen = true
        }
    }
}