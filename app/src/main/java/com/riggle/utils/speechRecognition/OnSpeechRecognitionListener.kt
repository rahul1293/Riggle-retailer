package com.riggle.utils.speechRecognition

interface OnSpeechRecognitionListener {
    fun onSpeechReceived(text: String?)
}