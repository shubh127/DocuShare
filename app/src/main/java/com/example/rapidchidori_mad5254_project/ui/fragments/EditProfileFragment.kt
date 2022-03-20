package com.example.rapidchidori_mad5254_project.ui.fragments

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentEditProfileBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.DAT_FORMAT_DD_MM_YYYY
import com.example.rapidchidori_mad5254_project.helper.Constants.FEMALE
import com.example.rapidchidori_mad5254_project.helper.Constants.MALE
import com.example.rapidchidori_mad5254_project.helper.Constants.NULL
import com.example.rapidchidori_mad5254_project.viewmodels.EditProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class EditProfileFragment : Fragment(), View.OnClickListener {
    private lateinit var binding: FragmentEditProfileBinding
    private val viewModel: EditProfileViewModel by viewModels()
    private lateinit var dialog: Dialog
    private lateinit var cameraPermissionLauncher: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var fileSelectorLauncher: ActivityResultLauncher<Intent>
    private var uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpLaunchers()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        showLoader()
        setUpListeners()
    }

    private fun configViews() {
        binding.tvEditProfileHead.typeface =
            Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)
    }

    private fun setUpListeners() {
        binding.ibClose.setOnClickListener(this)
        binding.ibDone.setOnClickListener(this)
        binding.tvDobValue.setOnClickListener(this)
        binding.tvChangeImage.setOnClickListener(this)

        viewModel.getUserInfoFromFirebase().observe(viewLifecycleOwner) {
            hideLoader()
            setDataToViews(it)
        }

        viewModel.isUpdateSuccess().observe(viewLifecycleOwner) {
            if (it) {
                hideLoader()
                requireActivity().finish()
                Toast.makeText(
                    context, getString(R.string.data_update_success),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        viewModel.getDisplayPictureURL().observe(viewLifecycleOwner) {
            updateDataOnFirebase(it)
        }

        viewModel.getException().observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setDataToViews(userInfo: UserInfo) {
        handleDisplayPicture(userInfo.displayPicture)
        binding.tvFullName.text = userInfo.fullName
        binding.tvEmail.text = userInfo.email
        handleGenderView(userInfo.gender)
        checkNullAndSetDataToView(binding.tvDobValue, userInfo.dob)
        checkNullAndSetDataToView(binding.etCollege, userInfo.college)
        checkNullAndSetDataToView(binding.etPhone, userInfo.phoneNo)
    }

    private fun setUpLaunchers() {
        fileSelectorLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK && result.data != null) {
                result.data?.data?.let {
                    setImageUsingUri(it)
                }
            }
        }

        cameraPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                if (isGranted) {
                    handleOnCameraClick()
                } else {
                    Toast.makeText(
                        requireActivity(),
                        getString(R.string.permission_denied), Toast.LENGTH_SHORT
                    ).show()
                }
            }

        cameraLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let {
                    val file =
                        File(
                            requireContext().externalCacheDir!!.absolutePath,
                            Constants.CAMERA_IMAGE_NAME
                        )
                    val uri: Uri = FileProvider.getUriForFile(
                        requireContext(),
                        requireContext().packageName.toString() + Constants.PROVIDER_NAME,
                        file
                    )
                    setImageUsingUri(uri)
                }
            }
        }
    }

    private fun setImageUsingUri(uri: Uri?) {
        this.uri = uri
        uri?.let {
            Picasso.get()
                .load(uri)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        } ?: run {
            Picasso.get()
                .load(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        }
    }

    private fun handleDisplayPicture(url: String) {
        if (url.isNotEmpty()) {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        } else {
            Picasso.get()
                .load(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        }
    }

    private fun checkNullAndSetDataToView(view: View, value: String) {
        if (value.isNotEmpty() && value != NULL) {
            if (view is TextView) {
                view.text = value
            } else if (view is EditText) {
                view.setText(value)
            }
        }
    }

    private fun handleGenderView(gender: String) {
        if (gender == MALE) {
            binding.rbMaleValue.isChecked = true
        } else if (gender == FEMALE) {
            binding.rbFemaleValue.isChecked = true
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ibClose.id -> {
                requireActivity().finish()
            }
            binding.ibDone.id -> {
                validateAndUpdate()
            }
            binding.tvChangeImage.id -> {
                onImageChangeClick()
            }
            binding.tvDobValue.id -> {
                openDatePicker()
            }
        }
    }

    private fun onImageChangeClick() {
        val items =
            arrayOf<CharSequence>(
                getString(R.string.take_photo),
                getString(R.string.choose_from_library),
                getString(R.string.remove_photo),
                getString(R.string.cancel)
            )
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.change_photo_head))
        builder.setItems(items) { dialog, which ->
            when {
                items[which] === getString(R.string.take_photo) -> {
                    handleOnCameraClick()
                }
                items[which] === getString(R.string.choose_from_library) -> {
                    openImagePicker()
                }
                items[which] === getString(R.string.cancel) -> {
                    dialog.dismiss()
                }
                items[which] === getString(R.string.remove_photo) -> {
                    setImageUsingUri(null)
                }
            }
        }
        builder.setCancelable(false)
        builder.show()
    }

    private fun openImagePicker() {
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false)
        galleryIntent.type = Constants.IMAGE_TYPE
        fileSelectorLauncher.launch(galleryIntent)
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
            requireContext().externalCacheDir!!.absolutePath, Constants.CAMERA_IMAGE_NAME
        )
        val uri = FileProvider.getUriForFile(
            requireContext(),
            requireContext().packageName.toString() + Constants.PROVIDER_NAME,
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

    private fun openDatePicker() {
        val cal = Calendar.getInstance()

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                val sdf = SimpleDateFormat(DAT_FORMAT_DD_MM_YYYY, Locale.CANADA)
                binding.tvDobValue.text = sdf.format(cal.time)
            }

        DatePickerDialog(
            requireContext(), dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).apply {
            datePicker.maxDate = cal.timeInMillis
            show()
            getButton(AlertDialog.BUTTON_POSITIVE).setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(),
                    R.color.primary3
                )
            )
            getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.primary3
                )
            )
        }
    }

    private fun validateAndUpdate() {
        if (binding.etPhone.text.toString().trim().isNotEmpty()) {
            if (viewModel.isPhoneNumberValid(binding.etPhone.text.toString().trim())) {
                showLoader()
                uri?.let {
                    viewModel.uploadImageToServer(it)
                } ?: run {
                    updateDataOnFirebase("")
                }
            } else {
                Toast.makeText(context, getString(R.string.phone_number_error), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun updateDataOnFirebase(imageURL: String) {
        val info = UserInfo(
            "",
            imageURL,
            binding.tvFullName.text.toString(),
            getGender(),
            binding.tvDobValue.text.toString(),
            binding.etCollege.text.toString().trim(),
            binding.etPhone.text.toString().trim(),
            binding.tvEmail.text.toString().trim()
        )
        viewModel.updateDataOnFirebase(info)
    }

    private fun getGender(): String {
        return if (binding.rbMaleValue.isChecked) {
            MALE
        } else {
            FEMALE
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
}