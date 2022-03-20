package com.example.rapidchidori_mad5254_project.ui.viewholders

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.databinding.SearchListChildViewBinding
import com.example.rapidchidori_mad5254_project.ui.interfaces.UserProfileClickListener
import com.squareup.picasso.Picasso

class UserProfilesViewHolder(private val binding: SearchListChildViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(data: UserInfo, listener: UserProfileClickListener) {
        binding.tvName.text = data.fullName
        binding.tvEmail.text = data.email
        if (data.college.isEmpty()) {
            data.college = binding.tvCollege.context.getString(R.string.not_mentioned)
        }
        binding.tvCollege.text =
            binding.tvCollege.context.getString(R.string.college_name) + data.college
        if (data.displayPicture.isNotEmpty()) {
            Picasso.get()
                .load(data.displayPicture)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.civDisplayPicture)
        }
        binding.root.setOnClickListener {
            listener.onProfileClick(data)
        }
    }

    companion object {
        fun create(parent: ViewGroup): UserProfilesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                SearchListChildViewBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            return UserProfilesViewHolder(binding)
        }
    }
}