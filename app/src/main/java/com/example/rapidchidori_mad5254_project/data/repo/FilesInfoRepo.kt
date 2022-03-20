package com.example.rapidchidori_mad5254_project.data.repo

import android.net.Uri
import com.example.rapidchidori_mad5254_project.data.models.response.UploadInfo
import com.example.rapidchidori_mad5254_project.helper.Constants.DELAY_2_SEC
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_ID
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_INFO_TABLE_NAME
import com.example.rapidchidori_mad5254_project.helper.Constants.FILE_TYPE
import com.example.rapidchidori_mad5254_project.helper.Constants.TITLE
import com.example.rapidchidori_mad5254_project.helper.Constants.URL
import com.example.rapidchidori_mad5254_project.helper.Constants.USER_ID
import com.example.rapidchidori_mad5254_project.helper.SingleLiveEvent
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.schedule


class FilesInfoRepo @Inject constructor() {
    private val exceptionInfo: SingleLiveEvent<String> = SingleLiveEvent()
    private val isUploadSuccess: SingleLiveEvent<Boolean> = SingleLiveEvent()
    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()
    private val databaseReference = database.reference.child(FILE_INFO_TABLE_NAME)
    private val uploadsData: SingleLiveEvent<List<UploadInfo>> = SingleLiveEvent()
    private val onDataRemoved: SingleLiveEvent<Boolean> = SingleLiveEvent()

    fun uploadFileToDB(uri: Uri?, ext: String?, title: String) {
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
                    updateDB(downloadUri, ext, title)
                }
                isUploadSuccess.value = true
            }
        }
    }

    private fun updateDB(downloadUri: Task<Uri>, fileExt: String?, title: String) {
        if (downloadUri.isSuccessful) {
            val user = auth.currentUser
            val fileDb =
                databaseReference.child(Calendar.getInstance().timeInMillis.toString())
            fileDb.child(FILE_ID).setValue(Calendar.getInstance().timeInMillis.toString())
            fileDb.child(USER_ID).setValue(user?.uid)
            fileDb.child(FILE_TYPE).setValue(fileExt?.replace("/", ""))
            fileDb.child(TITLE).setValue(title)
            fileDb.child(URL).setValue(downloadUri.result.toString())
        }
    }

    fun getExceptionInfo(): SingleLiveEvent<String> {
        return exceptionInfo
    }

    fun isUploadSuccess(): SingleLiveEvent<Boolean> {
        return isUploadSuccess
    }

    fun getUploads(id: String = ""): SingleLiveEvent<List<UploadInfo>> {
        var userId = id
        val user = auth.currentUser
        if (id.isEmpty()) {
            userId= user!!.uid
        }
        FirebaseDatabase.getInstance().getReference(FILE_INFO_TABLE_NAME)
            .orderByChild(USER_ID)
            .equalTo(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val uploads = mutableListOf<UploadInfo>()
                    if (snapshot.exists()) {
                        for (child in snapshot.children) {
                            val upload = child.getValue(UploadInfo::class.java)
                            upload?.let { uploads.add(it) }
                        }
                    }
                    uploadsData.value = uploads
                }

                override fun onCancelled(error: DatabaseError) {
                    //no op
                }
            })

        return uploadsData
    }

    fun getUploadsLiveData(): SingleLiveEvent<List<UploadInfo>> {
        return uploadsData
    }

    fun removeItemFromDatabase(fileId: String) {
        databaseReference.orderByChild(FILE_ID).equalTo(fileId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (data in dataSnapshot.children) {
                        data.ref.removeValue()
                    }
                    onDataRemoved.value = true
                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })
    }

    fun onDataRemoved(): SingleLiveEvent<Boolean> {
        return onDataRemoved
    }
}