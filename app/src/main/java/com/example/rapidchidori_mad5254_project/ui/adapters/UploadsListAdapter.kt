package com.example.rapidchidori_mad5254_project.ui.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.ui.viewholders.UploadsListViewHolder

class UploadsListAdapter(var data: List<UploadInfo>) :
    RecyclerView.Adapter<UploadsListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UploadsListViewHolder {
        return UploadsListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UploadsListViewHolder, position: Int) {
        data[position].let { data ->
            holder.bind(data)
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun setUploadsData(data: List<UploadInfo>) {
        this.data = data
        notifyDataSetChanged()
    }
}
