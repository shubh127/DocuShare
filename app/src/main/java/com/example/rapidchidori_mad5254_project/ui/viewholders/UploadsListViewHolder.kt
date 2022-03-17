package com.example.rapidchidori_mad5254_project.ui.viewholders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.databinding.UploadsChildViewBinding
import com.example.rapidchidori_mad5254_project.helper.AppUtils.Companion.getImageBasedOnFileType
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_DOC
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_DOCS
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_JPG
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_PDF
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_PNG
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_PPT
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_PPTX
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_TXT
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE_XLS

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