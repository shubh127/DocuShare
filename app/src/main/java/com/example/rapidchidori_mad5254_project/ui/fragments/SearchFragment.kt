package com.example.rapidchidori_mad5254_project.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.SearchView.OnQueryTextListener
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.databinding.FragmentSearchBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.ui.activities.SecondaryActivity
import com.example.rapidchidori_mad5254_project.ui.adapters.UserProfilesAdapter
import com.example.rapidchidori_mad5254_project.ui.interfaces.UserProfileClickListener
import com.example.rapidchidori_mad5254_project.viewmodels.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment(), UserProfileClickListener {
    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var dialog: Dialog
    private lateinit var mAdapter: UserProfilesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        setUpListeners()
    }

    private fun configViews() {
        mAdapter = UserProfilesAdapter(mutableListOf(), this)
        binding.rvProfileList.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun setUpListeners() {
        viewModel.getProfilesLiveData().observe(viewLifecycleOwner) {
            showHideProfileList(it)
        }

        binding.svSearch.setOnQueryTextListener(object : OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                handleOnSearch(s)
                return false
            }

            override fun onQueryTextChange(s: String): Boolean {
                return false
            }
        })
    }

    private fun handleOnSearch(input: String) {
        if (input.length < 3) {
            Toast.makeText(
                context,
                R.string.enter_at_least_3_characters_to_get_search_results,
                Toast.LENGTH_SHORT
            ).show()
        } else {
            showLoader()
            viewModel.getProfiles(input)
        }
    }

    private fun showHideProfileList(list: List<UserInfo>?) {
        hideLoader()
        if (list.isNullOrEmpty()) {
            binding.tvMsg.visibility = View.VISIBLE
            binding.rvProfileList.visibility = View.GONE
            binding.tvMsg.text = getString(R.string.no_results)
        } else {
            binding.tvMsg.visibility = View.GONE
            binding.rvProfileList.visibility = View.VISIBLE
            mAdapter.updateData(list)
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

    override fun onProfileClick(info: UserInfo) {
        val i = Intent(requireActivity(), SecondaryActivity::class.java)
        i.putExtra(Constants.USER_INFO_TABLE_NAME, info)
        i.putExtra(Constants.FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_OTHER_PROFILE)
        startActivity(i)
    }
}