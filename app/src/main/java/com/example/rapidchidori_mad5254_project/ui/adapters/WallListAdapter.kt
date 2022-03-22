package com.example.rapidchidori_mad5254_project.ui.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.data.models.ui.WallListInfo
import com.example.rapidchidori_mad5254_project.ui.interfaces.WallListClickListener
import com.example.rapidchidori_mad5254_project.ui.viewholders.WallListViewHolder

class WallListAdapter(
    var data: List<WallListInfo>,
    private val listener: WallListClickListener
) :
    RecyclerView.Adapter<WallListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WallListViewHolder {
        return WallListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: WallListViewHolder, position: Int) {
        data[position].let { data ->
            holder.bind(data, listener)
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<WallListInfo>) {
        this.data = data.sortedBy { it.uploadTime }.reversed()
        notifyDataSetChanged()
    }
}
