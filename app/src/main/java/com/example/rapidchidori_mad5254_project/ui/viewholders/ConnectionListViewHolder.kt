package com.example.rapidchidori_mad5254_project.ui.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.ui.ConnectionsListInfo
import com.example.rapidchidori_mad5254_project.databinding.ConnectionListChildViewBinding
import com.example.rapidchidori_mad5254_project.ui.interfaces.ConnectionClickListener
import com.squareup.picasso.Picasso

class ConnectionListViewHolder(private val binding: ConnectionListChildViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: ConnectionsListInfo, listener: ConnectionClickListener) {
        binding.tvName.text = data.fullName
        handleProfilePictureView(data.displayPicture)
    }

    private fun handleProfilePictureView(url: String) {
        if (url.isNotEmpty()) {
            Picasso.get()
                .load(url)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.civConnectionImage)
        } else {
            Picasso.get()
                .load(R.drawable.placeholder)
                .into(binding.civConnectionImage)
        }
    }

    companion object {
        fun create(parent: ViewGroup): ConnectionListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                ConnectionListChildViewBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            return ConnectionListViewHolder(binding)
        }
    }
}