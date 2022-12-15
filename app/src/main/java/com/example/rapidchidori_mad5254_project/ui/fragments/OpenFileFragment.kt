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
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.MediaController
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentOpenFileBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.BASE_URL
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_DATA
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_JPG
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_MP4
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_PNG
import com.example.rapidchidori_mad5254_project.helper.Constants.UTF_8
import com.example.rapidchidori_mad5254_project.viewmodels.OpenFileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.UnsupportedEncodingException
import java.net.URLEncoder


@AndroidEntryPoint
class OpenFileFragment : Fragment(), View.OnClickListener {
    private val viewModel: OpenFileViewModel by viewModels()
    private lateinit var binding: FragmentOpenFileBinding
    private var data: UploadInfo? = null
    private var isLiked = false

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
        when (data?.fileType) {
            FILE_TYPE_JPG, FILE_TYPE_PNG -> {
                showImageData(data!!.url)
            }
            FILE_TYPE_MP4 -> {
                showVideoData(data!!.url)
            }
            else -> {
                showFileData(data!!.url)
            }
        }

        viewModel.getLikeData(data!!.fileId)
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun showFileData(url: String) {
        binding.vvVideo.visibility = View.GONE
        binding.ivImage.visibility = View.GONE
        binding.wvFileOpen.apply {
            visibility = View.VISIBLE
            webChromeClient = WebChromeClient()
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
        binding.vvVideo.visibility = View.GONE
        binding.wvFileOpen.visibility = View.GONE
        Picasso.get()
            .load(url)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(binding.ivImage)
    }

    private fun showVideoData(url: String) {
        binding.vvVideo.visibility = View.VISIBLE
        binding.ivImage.visibility = View.GONE
        binding.wvFileOpen.visibility = View.GONE

        val uri = Uri.parse(url)

        binding.vvVideo.setVideoURI(uri)
        val mediaController = MediaController(activity)
        mediaController.setAnchorView(binding.vvVideo)
        mediaController.setMediaPlayer(binding.vvVideo)
        binding.vvVideo.setMediaController(mediaController)
        binding.vvVideo.start()
    }

    private fun setUpListeners() {
        binding.ibBack.setOnClickListener(this)
        binding.ibDownload.setOnClickListener(this)
        binding.ibLike.setOnClickListener(this)

        viewModel.getLikeCountLiveData().observe(viewLifecycleOwner) {
            if (it > 0) {
                binding.tvLikeCount.text = it.toString()
            }
        }

        viewModel.getIsLikedLiveData().observe(viewLifecycleOwner) {
            if (it) {
                isLiked = it
                binding.ibLike.setImageResource(R.drawable.ic_like_filled)
            } else {
                isLiked = it
                binding.ibLike.setImageResource(R.drawable.ic_like_outline)
            }
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ibBack.id -> {
                requireActivity().onBackPressed()
            }
            binding.ibDownload.id -> {
                startDownload()
            }
            binding.ibLike.id -> {
                handleLikeDislike()
            }
        }
    }

    private fun handleLikeDislike() {
        isLiked = if (isLiked) {
            binding.ibLike.setImageResource(R.drawable.ic_like_outline)
            binding.tvLikeCount.text =
                (Integer.parseInt(binding.tvLikeCount.text.toString()) - 1).toString()
            false
        } else {
            binding.ibLike.setImageResource(R.drawable.ic_like_filled)
            true
        }

        viewModel.updateLikeCount(
            isLiked,
            data!!.fileId
        )
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