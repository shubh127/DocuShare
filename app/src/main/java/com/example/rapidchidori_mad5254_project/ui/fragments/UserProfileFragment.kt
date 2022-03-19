package com.example.rapidchidori_mad5254_project.ui.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentUserProfileBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_DATA
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE_EDIT_PROFILE
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE_OPEN_FILE
import com.example.rapidchidori_mad5254_project.ui.activities.SecondaryActivity
import com.example.rapidchidori_mad5254_project.ui.adapters.UploadsListAdapter
import com.example.rapidchidori_mad5254_project.ui.interfaces.UploadsClickListener
import com.example.rapidchidori_mad5254_project.viewmodels.UserProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserProfileFragment : Fragment(), View.OnClickListener, UploadsClickListener {
    private lateinit var binding: FragmentUserProfileBinding
    private val viewModel: UserProfileViewModel by viewModels()
    private lateinit var mAdapter: UploadsListAdapter
    private lateinit var dialog: Dialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configViews()
        setUpListeners()
    }

    private fun configViews() {
        binding.tvAppName.typeface =
            Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)

        mAdapter = UploadsListAdapter(mutableListOf(), this)
        binding.rvUploads.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                3,
                RecyclerView.VERTICAL,
                false
            )
        }
    }

    private fun setUpListeners() {
        binding.btnEditProfile.setOnClickListener(this)

        viewModel.getUserInfoFromFirebase().observe(viewLifecycleOwner) {
            setDataToViews(it)
        }

        viewModel.getUploads().observe(viewLifecycleOwner) {
            populateUploadList(it)
        }

        viewModel.onDataRemoved().observe(viewLifecycleOwner) {
            hideLoader()
            if (it) {
                Toast.makeText(context, getString(R.string.removal_success), Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun hideLoader() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    private fun setDataToViews(userInfo: UserInfo) {
        binding.tvName.text = userInfo.fullName
        if (userInfo.displayPicture.isNotEmpty()) {
            Picasso.get()
                .load(userInfo.displayPicture)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        } else {
            Picasso.get()
                .load(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        }
    }

    private fun populateUploadList(data: List<UploadInfo>) {
        if (data.isNullOrEmpty()) {
            binding.tvNoUploads.visibility = View.VISIBLE
            binding.rvUploads.visibility = View.GONE
        } else {
            binding.tvUploadsCount.text = data.size.toString()
            binding.tvNoUploads.visibility = View.GONE
            binding.rvUploads.visibility = View.VISIBLE
            mAdapter.setUploadsData(data)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.btnEditProfile.id -> {
                openEditProfileScreen()
            }
        }
    }

    private fun openEditProfileScreen() {
        val i = Intent(requireActivity(), SecondaryActivity::class.java)
        i.putExtra(FRAGMENT_TYPE, FRAGMENT_TYPE_EDIT_PROFILE)
        startActivity(i)
    }

    override fun onItemClick(data: UploadInfo) {
        val i = Intent(requireActivity(), SecondaryActivity::class.java)
        i.putExtra(FILE_DATA, data)
        i.putExtra(FRAGMENT_TYPE, FRAGMENT_TYPE_OPEN_FILE)
        startActivity(i)
    }

    override fun removeItem(fileId: String) {
        showAlertDialog(fileId)
    }

    private fun showAlertDialog(fileId: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
        builder.setTitle(getString(R.string.are_you_sure))
            .setCancelable(false)
        builder.setPositiveButton(
            getString(R.string.yes)
        ) { _, _ ->
            showLoader()
            viewModel.removeItemFromDataBase(fileId)
        }
        builder.setNegativeButton(
            getString(R.string.no)
        ) { dialogBox, _ -> dialogBox.cancel() }
        builder.create().apply {
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
}