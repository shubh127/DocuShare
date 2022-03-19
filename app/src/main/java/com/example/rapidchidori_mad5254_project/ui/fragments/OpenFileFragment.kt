package com.example.rapidchidori_mad5254_project.ui.fragments

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentOpenFileBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.BASE_URL
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_DATA
import com.example.rapidchidori_mad5254_project.helper.Constants.UTF_8
import dagger.hilt.android.AndroidEntryPoint
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


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
        configViews()
        setUpListeners()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun configViews() {
        val data = arguments?.getParcelable<UploadInfo>(FILE_DATA)
        binding.tvFileName.apply {
            text = data?.title
            typeface = Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)
        }
        binding.wvFileOpen.apply {
            webViewClient = WebViewClient()
            settings.setSupportZoom(true)
            settings.javaScriptEnabled = true
        }
        var url = ""
        try {
            url = URLEncoder.encode(data!!.url, UTF_8)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        binding.wvFileOpen.loadUrl(BASE_URL + url)
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