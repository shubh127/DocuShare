package com.example.rapidchidori_mad5254_project.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.rapidchidori_mad5254_project.R


class AppUtils {
    companion object {
        fun isNetworkAvailable(context: Context) =
            (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
                getNetworkCapabilities(activeNetwork)?.run {
                    hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                            || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
                } ?: false
            }

        fun getImageBasedOnFileType(fileType: String): Int {
            when (fileType) {
                Constants.FILE_TYPE_PDF -> {
                    return R.drawable.pdf
                }
                Constants.FILE_TYPE_TXT -> {
                    return R.drawable.text
                }
                Constants.FILE_TYPE_DOC, Constants.FILE_TYPE_DOCS -> {
                    return R.drawable.doc
                }
                Constants.FILE_TYPE_PPT, Constants.FILE_TYPE_PPTX -> {
                    return R.drawable.ppt
                }
                Constants.FILE_TYPE_XLS -> {
                    return R.drawable.exel
                }
                Constants.FILE_TYPE_JPG -> {
                    return R.drawable.jpg
                }
                Constants.FILE_TYPE_PNG -> {
                    return R.drawable.png
                }
                else -> {
                    return R.drawable.placeholder
                }
            }
        }
    }
}