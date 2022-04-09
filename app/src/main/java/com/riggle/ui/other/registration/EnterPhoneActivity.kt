package com.riggle.ui.other.registration

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.SpannableStringBuilder
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import com.riggle.R
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.utils.openUrl
import kotlinx.android.synthetic.main.activity_enter_phone.*

class EnterPhoneActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {

    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)

        btnContinue.setOnClickListener {
            EnterOTPActivity.start(this, etPhone?.text.toString())
        }
        setTermsAndPolicyText()

    }

    override fun setView(): Int {
        return R.layout.activity_enter_phone
    }

    fun setTermsAndPolicyText() {


        val spanText =
            SpannableStringBuilder("By clicking on \"Login\" button, you are accepting our Terms of Use and Privacy Policy")


        val clickableTermsOfUse = object : ClickableSpan() {
            override fun onClick(view: View) {
                openUrl(this@EnterPhoneActivity, "https://assets.riggleapp.in/static/tnc.html"/*"https://riggleapp.in/termsofuse"*/)
            }
        }

        val clickablePrivacyPolicy = object : ClickableSpan() {
            override fun onClick(view: View) {
                openUrl(
                    this@EnterPhoneActivity,
                    "https://assets.riggleapp.in/static/privacy-policy.html"/*"https://riggleapp.in/privacy-policy"*/
                )
            }
        }
        spanText.setSpan(
            clickableTermsOfUse,
            53,
            65,
            0
        )

        spanText.setSpan(
            clickablePrivacyPolicy,
            70, // start
            spanText.length, // end
            0
        )

        spanText.setSpan(
            clickablePrivacyPolicy,
            70, // start
            spanText.length, // end
            0
        )

        spanText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this,R.color.colorPrimary)),
            70, // start
            spanText.length, // end
            0
        )

        spanText.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this,R.color.colorPrimary)),
            53,
            65,
            0
        )
        tVTermsAndPrivacyPolicy.setText(spanText, TextView.BufferType.SPANNABLE)
        tVTermsAndPrivacyPolicy.movementMethod = LinkMovementMethod.getInstance();
    }


    override fun initializeViews(savedInstanceState: Bundle?) {

        etPhone?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (charSequence.length == 10) activateBtn() else deactivateBtn()
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

    private fun deactivateBtn() {
        btnContinue?.alpha = 0.2f
        btnContinue?.isClickable = false
    }

    private fun activateBtn() {
        btnContinue?.alpha = 1f
        btnContinue?.isClickable = true
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, EnterPhoneActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}