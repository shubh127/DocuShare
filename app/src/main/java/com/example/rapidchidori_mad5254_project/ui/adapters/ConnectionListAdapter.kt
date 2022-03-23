package com.example.rapidchidori_mad5254_project.ui.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.data.models.ui.ConnectionsListInfo
import com.example.rapidchidori_mad5254_project.ui.interfaces.ConnectionClickListener
import com.example.rapidchidori_mad5254_project.ui.viewholders.ConnectionListViewHolder

class ConnectionListAdapter(
    var data: List<ConnectionsListInfo>,
    private val listener: ConnectionClickListener
) :
    RecyclerView.Adapter<ConnectionListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ConnectionListViewHolder {
        return ConnectionListViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ConnectionListViewHolder, position: Int) {
        data[position].let { data ->
            holder.bind(data, listener)
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<ConnectionsListInfo>) {
        this.data = data
        notifyDataSetChanged()
    }
}
