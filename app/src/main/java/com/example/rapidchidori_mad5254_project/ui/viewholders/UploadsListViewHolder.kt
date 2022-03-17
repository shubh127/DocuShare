package com.example.rapidchidori_mad5254_project.ui.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.databinding.UploadsChildViewBinding
import com.example.rapidchidori_mad5254_project.helper.AppUtils.Companion.getImageBasedOnFileType

class UploadsListViewHolder(private val binding: UploadsChildViewBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(data: UploadInfo) {
        binding.tvFileName.text = data.title

        binding.ivFileType.setImageResource(getImageBasedOnFileType(data.fileType))
    }

    companion object {
        fun create(parent: ViewGroup): UploadsListViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                UploadsChildViewBinding.inflate(
                    layoutInflater,
                    parent,
                    false
                )
            return UploadsListViewHolder(binding)
        }
    }
}