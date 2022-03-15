package com.example.rapidchidori_mad5254_project.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentHomeBinding
import com.example.rapidchidori_mad5254_project.helper.Constants.CAMERA_IMAGE_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.IMAGE_TYPE
import com.example.rapidchidori_mad5254_project.helper.Constants.PROVIDER_NAME
import com.example.rapidchidori_mad5254_project.viewmodels.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.io.File


@AndroidEntryPoint
class HomeFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var animOpen: Animation
    private lateinit var animClose: Animation
    private lateinit var animForward: Animation
    private lateinit var animBackward: Animation
    private var isFabOpen = false
    private lateinit var imageSelectorLauncher: ActivityResultLauncher<Intent>
    private lateinit var fileSelectorLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var dialog: Dialog
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpLaunchers()
    }

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

        viewModel.getExceptionInfo().observe(viewLifecycleOwner) {
            hideLoader()
            Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
        }

        viewModel.isUploadSuccess().observe(viewLifecycleOwner) {
            if (it) {
                hideLoader()
                Toast.makeText(
                    requireContext(), getString(R.string.image_upload_successfull),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun setUpLaunchers() {
        imageSelectorLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                showLoader()
                result.data?.data?.let {
                    viewModel.onFileSelect(
                        it,
                        it.getFileExtension(requireContext())
                    )
                }
            }
        }

        fileSelectorLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                showLoader()
                result.data?.data?.let {
                    viewModel.onFileSelect(
                        it,
                        it.getFileExtension(requireContext())
                    )
                }
            }
        }

        cameraPermissionLauncher =
            registerForActivityResult(RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    handleOnCameraClick()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.permission_denied), Toast.LENGTH_SHORT
                    )
                        .show()
                }
            }

        cameraLauncher = registerForActivityResult(
            StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    showLoader()
                    val file =
                        File(requireContext().externalCacheDir!!.absolutePath, CAMERA_IMAGE_NAME)
                    val uri: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().packageName.toString() + PROVIDER_NAME,
                        file
                    )
                    viewModel.onFileSelect(
                        uri,
                        uri.getFileExtension(requireContext())
                    )
                }

            }
        }
    }

    override fun onClick(view: View) {
        when (view.id) {
            binding.fabChooseFile.id -> {
                animateFAB()
            }
            binding.fabGallery.id -> {
                openImagePicker()
                binding.fabChooseFile.callOnClick()
            }
            binding.fabCamera.id -> {
                handleOnCameraClick()
                binding.fabChooseFile.callOnClick()
            }
            binding.fabFileManager.id -> {
                handleOnFileManagerClick()
                binding.fabChooseFile.callOnClick()
            }
        }
    }

    private fun handleOnFileManagerClick() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        intent.addCategory(Intent.CATEGORY_OPENABLE)
        fileSelectorLauncher.launch(intent)
    }

    private fun handleOnCameraClick() {
        if (hasCameraPermissions()) {
            openCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val file = File(
            requireContext().externalCacheDir!!.absolutePath, CAMERA_IMAGE_NAME
        )
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().packageName.toString() + PROVIDER_NAME,
            file
        )
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        cameraLauncher.launch(cameraIntent)
    }

    private fun hasCameraPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireActivity(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun openImagePicker() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        galleryIntent.type = IMAGE_TYPE
        imageSelectorLauncher.launch(galleryIntent)
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

    private fun hideLoader() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    private fun Uri.getFileExtension(context: Context): String? {
        return MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(context.contentResolver.getType(this))
    }
}