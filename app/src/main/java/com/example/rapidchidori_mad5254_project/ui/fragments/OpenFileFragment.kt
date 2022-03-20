package com.example.rapidchidori_mad5254_project.ui.fragments

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentOpenFileBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.BASE_URL
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_DATA
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_JPG
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_PNG
import com.example.rapidchidori_mad5254_project.helper.Constants.UTF_8
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


@AndroidEntryPoint
class OpenFileFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentOpenFileBinding
    private var data: UploadInfo? = null

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

    private fun configViews() {
        data = arguments?.getParcelable(FILE_DATA)
        binding.tvFileName.apply {
            text = data?.title
            typeface = Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)
        }
        if (data?.fileType == FILE_TYPE_JPG || data?.fileType == FILE_TYPE_PNG) {
            showImageData(data!!.url)
        } else {
            showFileData(data!!.url)
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showFileData(url: String) {
        binding.ivImage.visibility = View.GONE
        binding.wvFileOpen.apply {
            visibility = View.VISIBLE
            webViewClient = WebViewClient()
            settings.setSupportZoom(true)
            settings.javaScriptEnabled = true
        }
        var temp = ""
        try {
            temp = URLEncoder.encode(url, UTF_8)
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        binding.wvFileOpen.loadUrl(BASE_URL + temp)
    }

    private fun showImageData(url: String) {
        binding.ivImage.visibility = View.VISIBLE
        binding.wvFileOpen.visibility = View.GONE
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(binding.ivImage)
    }

    private fun setUpListeners() {
        binding.ibBack.setOnClickListener(this)
        binding.ibDownload.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ibBack.id -> {
                requireActivity().finish()
            }
            binding.ibDownload.id -> {
                startDownload()
            }
        }
    }

    private fun startDownload() {
        val request: DownloadManager.Request =
            DownloadManager.Request(Uri.parse(data!!.url)).apply {
                setTitle(data?.title)
                setNotificationVisibility(
                    DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                )
                setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, data?.title)
                setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_MOBILE
                            or DownloadManager.Request.NETWORK_WIFI
                )
            }
        val downloadManager =
            requireContext().getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }
}