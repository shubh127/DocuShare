package com.example.rapidchidori_mad5254_project.viewmodels

import androidx.lifecycle.ViewModel
import com.example.rapidchidori_mad5254_project.data.repo.LikeInfoRepo
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OpenFileViewModel @Inject constructor(
    private val likeInfoRepo: LikeInfoRepo
) : ViewModel() {

    fun updateLikeCount(isLiked: Boolean, fileId: Double) {
        if (isLiked) {
            likeInfoRepo.addLike(fileId)
        } else {
            likeInfoRepo.removeLike(fileId)
        }
    }

    fun getLikeData(fileId: Double) {
        likeInfoRepo.getLikeCount(fileId)
        likeInfoRepo.getIsLiked(fileId)
    }

    fun getLikeCountLiveData(): SingleLiveEvent<Int> {
        return likeInfoRepo.getLikeCountLiveData()
    }

    fun getIsLikedLiveData(): SingleLiveEvent<Boolean> {
        return likeInfoRepo.getIsLikedLiveData()
    }
}