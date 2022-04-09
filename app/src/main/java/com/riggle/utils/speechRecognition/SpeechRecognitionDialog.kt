package com.riggle.utils.speechRecognition

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.riggle.R
import com.skyfishjy.library.RippleBackground

class SpeechRecognitionDialog(
    var activity: Activity,
    var onSpeechRecognitionListener: OnSpeechRecognitionListener
) : RecognitionListener {
    var mBottomSheetDialog: BottomSheetDialog? = null
    var rbSpeech: RippleBackground? = null
    var etSpeech: EditText? = null
    private var mSpeechRecognizer: SpeechRecognizer? = null
    private var mSpeechRecognizerIntent: Intent? = null
    fun initDialog() {
        mBottomSheetDialog = BottomSheetDialog(activity)
        val sheetView = LayoutInflater.from(activity).inflate(R.layout.layout_speech_to_text, null)
        rbSpeech = sheetView.findViewById(R.id.rbSpeech)
        etSpeech = sheetView.findViewById(R.id.etSpeech)
        mBottomSheetDialog?.setContentView(sheetView)
        (sheetView.parent as View).setBackgroundColor(activity.resources.getColor(R.color.transparent))
        etSpeech?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                etSpeech?.setSelection(editable.toString().length)
            }
        })
    }

    fun showDialog() {
        rbSpeech?.startRippleAnimation()
        startSpeechRecognition()
        mBottomSheetDialog?.show()
    }

    fun dismissDialog() {
        mBottomSheetDialog?.let {
            if (it.isShowing) {
                rbSpeech?.stopRippleAnimation()
                stopSpeechRecognition()
                it.dismiss()
                etSpeech?.setText("")
            }
        }

    }

    override fun onReadyForSpeech(bundle: Bundle) {}
    override fun onBeginningOfSpeech() {}
    override fun onRmsChanged(v: Float) {}
    override fun onBufferReceived(bytes: ByteArray) {}
    override fun onEndOfSpeech() {}
    override fun onError(i: Int) {}
    override fun onResults(bundle: Bundle) {
        val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (matches != null && matches.size > 0) {
            onSpeechRecognitionListener.onSpeechReceived(matches[0])
        } else {
            Toast.makeText(activity, "Nothing happened", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onPartialResults(bundle: Bundle) {
        val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (matches != null && matches.size > 0) {
            etSpeech?.setText(matches[0])
        } else {
            Toast.makeText(activity, "Nothing happened", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEvent(i: Int, bundle: Bundle) {}
    fun createSpeechRecognition() {
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity)
        mSpeechRecognizer?.setRecognitionListener(this)
        mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mSpeechRecognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
        )
        mSpeechRecognizerIntent?.putExtra(
            RecognizerIntent.EXTRA_CALLING_PACKAGE,
            activity.packageName
        )
        mSpeechRecognizerIntent?.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        mSpeechRecognizerIntent?.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
    }

    fun startSpeechRecognition() {
        mSpeechRecognizer?.startListening(mSpeechRecognizerIntent)
    }

    fun stopSpeechRecognition() {
        if (mSpeechRecognizer != null) {
            mSpeechRecognizer?.stopListening()
        }
    }

    init {
        initDialog()
        createSpeechRecognition()
    }
}