package com.example.rapidchidori_mad5254_project.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentProfilePictureBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfilePictureFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentProfilePictureBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfilePictureBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        setUpListeners()
    }

    private fun configViews() {
        binding.tvFullName.apply {
            typeface =
                Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)
            text = arguments?.getString(Constants.COLUMN_FULL_NAME)
            val url = arguments?.getString(Constants.COLUMN_DISPLAY_PICTURE)
            if (url!!.isNotEmpty()) {
                Picasso.get()
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.placeholder)
                    .into(binding.ivDisplayPicture)
            }
        }
    }

    private fun setUpListeners() {
        binding.ibClose.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ibClose.id -> {
                requireActivity().finish()
            }
        }
    }
}