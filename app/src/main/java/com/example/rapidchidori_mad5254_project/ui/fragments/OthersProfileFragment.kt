package com.example.rapidchidori_mad5254_project.ui.fragments

import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
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
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_ID
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
    private var uID = ""
    private var fcmId = ""
    private lateinit var dialog: Dialog

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
        showLoader()
        val data: UserInfo? = arguments?.getParcelable(Constants.USER_INFO_TABLE_NAME)
        data?.let {
            setDataToViews(it)
        } ?: run {
            viewModel.getUserInfo(arguments?.getString(USER_ID))
        }
    }

    fun setUpListeners() {
        binding.civDisplayPicture.setOnClickListener(this)
        binding.btnFollow.setOnClickListener(this)
        viewModel.getUserInfoLiveData().observe(viewLifecycleOwner) {
            setDataToViews(it)
        }

        viewModel.isFollowing().observe(viewLifecycleOwner) {
            handleFollowBtn(it)
        }
        viewModel.getUploadsData().observe(viewLifecycleOwner) {
            populateUploadList(it)
        }

        viewModel.getFollowingCountLiveData().observe(viewLifecycleOwner) {
            updateFollowingCount(it)
        }

        viewModel.getFollowersCountLiveData().observe(viewLifecycleOwner) {
            updateFollowersCount(it)
        }
    }

    private fun updateFollowersCount(count: Int) {
        binding.tvFollowingCount.text = count.toString()
        hideLoader()
    }

    private fun updateFollowingCount(count: Int) {
        binding.tvFollowersCount.text = count.toString()
        viewModel.getFollowersCount()
    }

    private fun handleFollowBtn(isFollowing: Boolean?) {
        if (isFollowing == true) {
            binding.btnFollow.text = getString(R.string.unFollow)
        } else {
            binding.btnFollow.text = getString(R.string.follow)
        }
        viewModel.getFollowingCount()
    }

    private fun setDataToViews(userInfo: UserInfo) {
        fcmId = userInfo.fcmID
        uID = userInfo.userID
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
        viewModel.getIsFollowing(uID)
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

    override fun removeItem(fileId: Double) {
        //no op
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.civDisplayPicture.id -> {
                openProfilePicture()
            }
            binding.btnFollow.id -> {
                onFollowUnfollowClick()
            }
        }
    }

    private fun onFollowUnfollowClick() {
        val isFollowing = binding.btnFollow.text == getString(R.string.follow)
        viewModel.updateConnection(
            isFollowing,
            uID
        )
        handleFollowBtn(isFollowing)
        if (isFollowing) {
            viewModel.sendConnectionNotification(fcmId)
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