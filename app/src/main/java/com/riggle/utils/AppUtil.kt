package com.riggle.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Environment
import android.text.method.LinkMovementMethod
import android.view.Window
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import com.riggle.R
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.listener.ActionDialogCallback
import com.riggle.utils.Constants.DataKeys.DATE_TIME
import com.riggle.utils.Constants.DataKeys.DATE_TIME_TIMEZONE
import kotlinx.android.synthetic.main.dialog_action.*
import kotlinx.android.synthetic.main.dialog_info.*
import kotlinx.android.synthetic.main.dialog_info.closeImageView
import kotlinx.android.synthetic.main.dialog_info.descriptionTextView
import kotlinx.android.synthetic.main.dialog_info.titleTextView
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun GetAppVersion(context: Context): String {
    var version = ""
    try {
        val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
        version = pInfo.versionName
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
    }

    return version
}


fun showLongToast(context: Context?, message: String ){
    context?.let {
        Toast.makeText(
            context,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}


// var currentPhotoPath: String = ""
fun createImageFile(context: Activity): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    )/*.apply {
            // Save a file: path for use with ACTION_VIEW intents
            absolutePath
        }*/
}

fun compressImage(bitmap: Bitmap, highCompress: Boolean): ByteArray {

    val stream = ByteArrayOutputStream()
    if (highCompress) {
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream)
    } else {
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    }
    val arrayFile = stream.toByteArray()
    stream.close()
    return arrayFile
}


fun showInfoDialog(
        context: Context?,
        title: String,
        description: String
) {
    context?.let {
        val dialog = Dialog(it)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_info)
        dialog.window?.setBackgroundDrawable( ColorDrawable(context.resources.getColor(R.color.transparent)));


        dialog.descriptionTextView.movementMethod = LinkMovementMethod.getInstance()

        dialog.titleTextView.text = title
        dialog.descriptionTextView.text = description

        dialog.closeImageView.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show()
    }
}
fun showActionDialog(
        context: Context?,
        title: String,
        description: String,
        yesButton: String,
        NoButton:String,
       callback: ActionDialogCallback
) {
    context?.let {
        val dialog = Dialog(it)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_action)
        dialog.window?.setBackgroundDrawable( ColorDrawable(context.resources.getColor(R.color.transparent)));



        dialog.titleTextView.text = title
        dialog.yesTextView.text = yesButton

        dialog.closeImageView.setOnClickListener {
            dialog.dismiss()
        }
        dialog.yesTextView.setOnClickListener {
            callback.onConfirmed()
        }

        dialog.show()
    }
}


fun openUrl(context: Context? , url:String?){
    context?.let {
        url?.let {

            val  builder =  CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()

            val colorInt = Color.parseColor("#FF0000") //red
            builder.setToolbarColor(colorInt)

            customTabsIntent.launchUrl(context, Uri.parse(url));
        }
    }
}
