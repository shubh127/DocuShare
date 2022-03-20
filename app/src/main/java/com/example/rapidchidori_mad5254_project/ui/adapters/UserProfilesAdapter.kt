package com.example.rapidchidori_mad5254_project.ui.adapters

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rapidchidori_mad5254_project.data.models.response.UserInfo
import com.example.rapidchidori_mad5254_project.ui.interfaces.UserProfileClickListener
import com.example.rapidchidori_mad5254_project.ui.viewholders.UserProfilesViewHolder

class UserProfilesAdapter(
    var data: List<UserInfo>,
    private val listener: UserProfileClickListener
) :
    RecyclerView.Adapter<UserProfilesViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserProfilesViewHolder {
        return UserProfilesViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: UserProfilesViewHolder, position: Int) {
        data[position].let { data ->
            holder.bind(data, listener)
        }
    }

    override fun getItemCount() = data.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(data: List<UserInfo>) {
        this.data = data
        notifyDataSetChanged()
    }
}
