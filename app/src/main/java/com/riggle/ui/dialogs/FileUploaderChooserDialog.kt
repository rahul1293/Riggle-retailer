package com.riggle.ui.dialogs

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatDialog
import com.riggle.R
import com.riggle.ui.listener.FileChooserListener
import kotlinx.android.synthetic.main.dialog_photo_chooser.*

class FileUploaderChooserDialog(private val mContext: Context?, var listener: FileChooserListener) :
    AppCompatDialog(
        mContext
    ) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_photo_chooser)

        setOnClickListeners()

    }

    private fun setOnClickListeners() {
        galleryTextView.setOnClickListener {
            listener.onGallerySelected()
        }

        cameraTextView.setOnClickListener {
            listener.onCameraSelected()
        }

        tvCancel.setOnClickListener {
            dismiss()
        }
    }

}