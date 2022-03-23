package com.example.rapidchidori_mad5254_project.ui.fragments

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
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.databinding.FragmentConnectionListBinding
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.CONNECTION_TYPE
import com.example.rapidchidori_mad5254_project.helper.Constants.FRAGMENT_TYPE
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_ID
import com.example.rapidchidori_mad5254_project.ui.activities.SecondaryActivity
import com.example.rapidchidori_mad5254_project.ui.adapters.ConnectionListAdapter
import com.example.rapidchidori_mad5254_project.ui.interfaces.ConnectionClickListener
import com.example.rapidchidori_mad5254_project.viewmodels.ConnectionListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectionListFragment : Fragment(), View.OnClickListener, ConnectionClickListener {
    private lateinit var binding: FragmentConnectionListBinding
    private lateinit var dialog: Dialog
    private val viewModel: ConnectionListViewModel by viewModels()
    private lateinit var mAdapter: ConnectionListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConnectionListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configViews()
        setUpListeners()
    }

    private fun configViews() {
        mAdapter = ConnectionListAdapter(mutableListOf(), this)
        val connectionType = arguments?.getString(CONNECTION_TYPE)
        binding.tvConnectionHead.typeface =
            Typeface.createFromAsset(requireActivity().assets, Constants.FONT_NAME)
        binding.tvConnectionHead.text = connectionType
        binding.rvConnections.apply {
            adapter = mAdapter
            layoutManager = LinearLayoutManager(context)
        }
        showLoader()
        connectionType?.let { viewModel.getConnectionIds(it) }
    }

    private fun setUpListeners() {
        binding.ibClose.setOnClickListener(this)

        viewModel.getConnectionIdLiveData().observe(viewLifecycleOwner) {
            viewModel.getAdditionalData(it)
        }

        viewModel.getConnectionListLiveData().observe(viewLifecycleOwner) {
            hideLoader()
            mAdapter.updateData(it)
        }
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.ibClose.id -> {
                requireActivity().onBackPressed()
            }
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

    override fun onConnectionClick(userID: String) {
        val i = Intent(requireActivity(), SecondaryActivity::class.java)
        i.putExtra(USER_ID, userID)
        i.putExtra(FRAGMENT_TYPE, Constants.FRAGMENT_TYPE_OTHER_PROFILE)
        startActivity(i)
    }
}