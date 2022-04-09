package com.riggle.utils

import android.Manifest
import android.R
import android.annotation.TargetApi
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by bhaveshmisri on 25/03/17.
 */
object Utility {
    const val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123
    const val REQUEST_GALLERY_IMAGES = 127

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    fun checkPermission(context: Context?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (context as Activity?)!!,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                ) {
                    val alertBuilder = AlertDialog.Builder(context)
                    alertBuilder.setCancelable(true)
                    alertBuilder.setTitle("Permission necessary")
                    alertBuilder.setMessage("External storage permission is necessary")
                    alertBuilder.setPositiveButton(R.string.yes) { dialog, which ->
                        ActivityCompat.requestPermissions(
                            (context as Activity?)!!, arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.CAMERA,
                                Manifest.permission.RECORD_AUDIO,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            ), MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                        )
                    }
                    val alert = alertBuilder.create()
                    alert.show()
                } else {
                    ActivityCompat.requestPermissions(
                        (context as Activity?)!!,
                        arrayOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        ),
                        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                    )
                }
                false
            } else {
                true
            }
        } else {
            true
        }
    }

    fun checkPermissionForMic(context: Context?): Boolean {
        val currentAPIVersion = Build.VERSION.SDK_INT
        return if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(
                    context!!,
                    Manifest.permission.RECORD_AUDIO
                ) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    (context as Activity?)!!,
                    arrayOf(
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ),
                    1
                )
                false
            } else {
                true
            }
        } else {
            true
        }
    }


    //PERMISSION TO GET IMAGES FROM
    fun isGalleryPermissionProvided(context: Context?): Boolean {

        (context as? Activity)?.let {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    context,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    REQUEST_GALLERY_IMAGES
                )

            } else {
                // Permission has already been granted
                return true
            }
            return false
        }
        return false
    }

    fun convertDate(date: String?): String {
        var nwDate = ""
        //val oldFormat = "yyyy-MM-dd'T'HH:mm:ssz"
        val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
        val newFormat = "yyyy-MM-dd"
        val sdf = SimpleDateFormat(oldFormat)
        try {
            val newDate: Date = sdf.parse(date)
            sdf.applyPattern(newFormat)
            nwDate = sdf.format(newDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return nwDate
    }

    fun convertTime(date: String?): String {
        var nwDate = ""
        //val oldFormat = "yyyy-MM-dd'T'HH:mm:ssz"
        val oldFormat = "yyyy-MM-dd'T'HH:mm:ss"
        val newFormat = "hh:mm a"
        val sdf = SimpleDateFormat(oldFormat)
        try {
            val newDate: Date = sdf.parse(date)
            sdf.applyPattern(newFormat)
            nwDate = sdf.format(newDate)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return nwDate
    }

}