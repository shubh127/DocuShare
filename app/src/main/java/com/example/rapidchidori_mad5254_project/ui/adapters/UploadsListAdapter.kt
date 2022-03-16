package com.example.rapidchidori_mad5254_project.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.R
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo

class UploadsListAdapter(var data: List<UploadInfo>) :
    RecyclerView.Adapter<UploadsListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
//        val textView: TextView
//        init {
//            textView = view.findViewById(R.id.textView)
//        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.uploads_child_view, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        //todo figure out how to name a file
    }

    override fun getItemCount() = data.size

    fun setUploadsData(data: List<UploadInfo>) {
        this.data = data
    }
}
