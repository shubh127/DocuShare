package com.example.rapidchidori_mad5254_project.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentOpenFileBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_DATA
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OpenFileFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentOpenFileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOpenFileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromIntent()
        configViews()
        setUpListeners()
    }

    private fun getDataFromIntent() {
        val data = arguments?.getParcelable<UploadInfo>(FILE_DATA)
        setDataToViews(data)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configViews() {
        binding.tvFileName.typeface =
            Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)

        binding.wvFileOpen.settings.apply {
            useWideViewPort = false
            javaScriptEnabled = true
            builtInZoomControls = true
        }
    }

    private fun setDataToViews(data: UploadInfo?) {
        binding.tvFileName.text = data?.title
        data?.url?.let {
            //todo get a solution for this
        }
    }


    private fun setUpListeners() {
        binding.ibBack.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ibBack.id -> {
                requireActivity().finish()
            }
        }
    }

}