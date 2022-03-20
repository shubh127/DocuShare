package com.example.rapidchidori_mad5254_project.ui.fragments

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentOthersProfileBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.ui.adapters.UploadsListAdapter
import com.example.rapidchidori_mad5254_project.ui.interfaces.UploadsClickListener
import com.example.rapidchidori_mad5254_project.viewmodels.OtherProfileViewModel
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OthersProfileFragment : Fragment(), UploadsClickListener, View.OnClickListener {
    private lateinit var binding: FragmentOthersProfileBinding
    private val viewModel: OtherProfileViewModel by viewModels()
    private lateinit var mAdapter: UploadsListAdapter
    private var url = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentOthersProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        setUpListeners()
    }

    private fun configViews() {
        mAdapter = UploadsListAdapter(mutableListOf(), this, false)
        binding.rvUploads.apply {
            adapter = mAdapter
            layoutManager = GridLayoutManager(
                requireContext(),
                3,
                RecyclerView.VERTICAL,
                false
            )
        }
        val data: UserInfo? = arguments?.getParcelable(Constants.USER_INFO_TABLE_NAME)
        data?.let {
            setDataToViews(it)
        } ?: run {
            //todo handle null data case
        }
    }

    fun setUpListeners() {
        binding.civDisplayPicture.setOnClickListener(this)
        viewModel.getUploadsData().observe(viewLifecycleOwner) {
            populateUploadList(it)
        }
    }

    private fun setDataToViews(userInfo: UserInfo) {
        binding.tvUserName.apply {
            text = userInfo.fullName
            typeface = Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)
        }
        binding.tvName.text = userInfo.fullName
        binding.tvEmail.text = userInfo.email
        checkNullAndSetDataToView(binding.tvDobValue, userInfo.dob)
        checkNullAndSetDataToView(binding.tvCollegeValue, userInfo.college)
        checkNullAndSetDataToView(binding.tvPhoneValue, userInfo.phoneNo)
        checkNullAndSetDataToView(binding.tvGenderValue, userInfo.gender)
        showDisplayPicture(userInfo.displayPicture)
        viewModel.getUserUploads(userInfo.userID)
    }

    private fun showDisplayPicture(url: String) {
        this.url = url
        if (url.isEmpty()) {
            Picasso.get()
                .load(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        } else {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        }
    }

    private fun checkNullAndSetDataToView(view: View, value: String) {
        var txt = value
        if (value.isEmpty() || value == Constants.NULL) {
            txt = getString(R.string.not_mentioned)
        }
        if (view is TextView) {
            view.text = txt
        } else if (view is EditText) {
            view.setText(txt)
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

    override fun onItemClick(data: UploadInfo) {
        val bundle = Bundle()
        bundle.putParcelable(Constants.FILE_DATA, data)
        Navigation.findNavController(binding.root)
            .navigate(
                R.id.action_othersProfileFragment_to_openFileFragment,
                bundle
            )
    }

    override fun removeItem(fileId: String) {
        //no op
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.civDisplayPicture.id -> {
                openProfilePicture()
            }
        }
    }

    private fun openProfilePicture() {
        val bundle = Bundle()
        bundle.putString(Constants.COLUMN_FULL_NAME, binding.tvName.text.toString())
        bundle.putString(Constants.COLUMN_DISPLAY_PICTURE, url)
        Navigation.findNavController(binding.root)
            .navigate(
                R.id.action_othersProfileFragment_to_profilePictureFragment,
                bundle
            )
    }
}