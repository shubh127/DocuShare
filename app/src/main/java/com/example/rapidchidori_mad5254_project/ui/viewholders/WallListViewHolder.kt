package com.example.rapidchidori_mad5254_project.ui.viewholders

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.ui.WallListInfo
import com.example.rapidchidori_mad5254_project.databinding.WallUploadsChildViewBinding
import com.example.rapidchidori_mad5254_project.helper.AppUtils
import com.example.rapidchidori_mad5254_project.ui.interfaces.WallListClickListener
import com.squareup.picasso.Picasso
import java.util.*


class WallListViewHolder(private val binding: WallUploadsChildViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(data: WallListInfo, listener: WallListClickListener) {
        binding.ivFileType.setImageResource(AppUtils.getImageBasedOnFileType(data.fileType))
        binding.tvName.text =
            """${data.userName} ${binding.tvName.context.getString(R.string.uploaded_a_file)}"""
        binding.tvFileName.text =
            """${binding.tvFileName.context.getString(R.string.named)} ${data.title}${
                binding.tvFileName.context.getString(
                    R.string.dot
                )
            }${data.fileType}"""
        handleProfilePictureView(data.userImageUrl)
        handleTimeView(data.uploadTime)

        binding.root.setOnClickListener { listener.onItemClick(data) }
    }

    private fun handleTimeView(uploadTime: Double) {
        val timeSince = DateUtils.getRelativeTimeSpanString(
            uploadTime.toLong(),
            Calendar.getInstance().timeInMillis,
            DateUtils.SECOND_IN_MILLIS
        ).toString()
        binding.tvTime.text = timeSince
    }

    private fun handleProfilePictureView(url: String) {
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

    companion object {
        fun create(parent: ViewGroup): WallListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                WallUploadsChildViewBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            return WallListViewHolder(binding)
        }
    }
}