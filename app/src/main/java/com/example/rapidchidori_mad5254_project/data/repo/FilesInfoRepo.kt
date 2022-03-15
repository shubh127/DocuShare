package com.example.rapidchidori_mad5254_project.data.repo

import android.net.Uri
import com.example.rapidchidori_mad5254_project.helper.Constants
import com.example.rapidchidori_mad5254_project.helper.Constants.DELAY_2_SEC
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


class FilesInfoRepo @Inject constructor() {
    private val exceptionInfo: SingleLiveEvent<String> = SingleLiveEvent()
    private val isUploadSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference.child(Constants.FILE_INFO_TABLE_NAME)

    fun uploadFileToDB(uri: Uri?, ext: String?) {
        val storageReference =
            FirebaseStorage.getInstance().getReference(
                ext + "/" +
                        Calendar.getInstance().timeInMillis
            )
        uri?.let {
            storageReference.putFile(it).addOnFailureListener { exception ->
                exceptionInfo.value = exception.message
            }.addOnSuccessListener { task ->
                val downloadUri: Task<Uri> = task.storage.downloadUrl
                Timer().schedule(DELAY_2_SEC) {
                    updateDB(downloadUri, ext)
                }
                isUploadSuccess.value = true
            }
        }
    }

    private fun updateDB(downloadUri: Task<Uri>, fileExt: String?) {
        if (downloadUri.isSuccessful) {
            val user = auth.currentUser
            val fileDb =
                databaseReference.child(Calendar.getInstance().timeInMillis.toString())
            fileDb.child(Constants.USER_ID).setValue(user?.uid)
            fileDb.child(Constants.FILE_TYPE).setValue(fileExt)
            fileDb.child(Constants.URL).setValue(downloadUri.result.toString())
        }
    }

    fun getExceptionInfo(): SingleLiveEvent<String> {
        return exceptionInfo
    }

    fun isUploadSuccess(): SingleLiveEvent<Boolean> {
        return isUploadSuccess
    }
}