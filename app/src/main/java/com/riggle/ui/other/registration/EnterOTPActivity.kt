package com.riggle.ui.other.registration

import `in`.aabhasjindal.otptextview.OTPListener
import `in`.aabhasjindal.otptextview.OtpTextView
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.*
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.Login
import com.riggle.data.models.request.OTPVerification
import com.riggle.data.models.response.LoginResponse
import com.riggle.data.models.response.UserData
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.home.HomeActivity.Companion.start
import com.riggle.ui.other.registration.EnterOTPActivity
import com.riggle.utils.UserProfileSingleton
import kotlinx.android.synthetic.main.activity_enter_otp.*
import org.koin.android.ext.android.inject


class EnterOTPActivity : CustomAppCompatActivityViewImpl(), CustomAppViewConnector {
    //completed

    private var token: String = ""
    private val userPreference: UserProfileSingleton by inject()

    @JvmField
    @BindView(R.id.tvPhone)
    var tvPhone: TextView? = null

    @JvmField
    @BindView(R.id.otp_view)
    var otpTextView: OtpTextView? = null

    @JvmField
    @BindView(R.id.btn_verify)
    var btn_verify: Button? = null
    private var phoneNo: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_enter_otp
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        ButterKnife.bind(this)
        val bundle = intent.extras
        if (bundle != null) {
            phoneNo = bundle.getString("phone") ?: ""
            sendOTP()
        }
        startCountDown()
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
        })
    }

    var countDown: CountDownTimer? = null
    private fun startCountDown() {
        countDown = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvCountDown.text = "Resend OTP in : " + millisUntilFinished / 1000 + " sec"
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                tvResend.visibility = View.VISIBLE
                tvCountDown.visibility = View.GONE
            }
        }.start()
    }

    private fun sendOTP() {
        tvPhone?.text = "+91- $phoneNo"
        callOTPApi()
        otpTextView?.otpListener = object : OTPListener {
            override fun onInteractionListener() {}
            override fun onOTPComplete(otp: String) {
                activateBtn()
            }
        }
    }

    private fun callOTPApi() {
        val phone = Login(phoneNo, token, true)
        dataManager.loginPhone(object : ApiResponseListener<APICommonResponse<LoginResponse>> {
            override fun onSuccess(response: APICommonResponse<LoginResponse>) {
                if (!response.isSuccess) Toast.makeText(
                    this@EnterOTPActivity,
                    response.message,
                    Toast.LENGTH_SHORT
                ).show()

                sharedPreferencesUtil.saveSupportNumber(response.data?.contact_support ?: "")
            }

            override fun onError(apiError: ApiError?) {
                Toast.makeText(
                    this@EnterOTPActivity,
                    apiError?.message ?: "Server error, please contact support.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, phone)
    }

    private fun deactivateBtn() {
        btn_verify?.alpha = 0.2f
        btn_verify?.isClickable = false
    }

    private fun activateBtn() {
        btn_verify?.alpha = 1f
        btn_verify?.isClickable = true
    }

    @OnClick(R.id.btn_verify)
    fun verifyOtp() {
        callOtpVerification()
    }

    @OnClick(R.id.ivBack)
    fun onBack() {
        onBackPressed()
    }

    @OnClick(R.id.tvResend)
    fun reSendOtp() {
        if (phoneNo.isNotEmpty())
            reSendOTPApi()
    }

    private fun reSendOTPApi() {
        val phone = Login(phoneNo, token,true)
        dataManager.reSend(object : ApiResponseListener<APICommonResponse<LoginResponse>> {
            override fun onSuccess(response: APICommonResponse<LoginResponse>) {
                if (!response.isSuccess) Toast.makeText(
                    this@EnterOTPActivity,
                    response.message,
                    Toast.LENGTH_SHORT
                ).show()
                startCountDown()
                //sharedPreferencesUtil.saveSupportNumber(response.data?.contact_support ?: "")
            }

            override fun onError(apiError: ApiError?) {
                Toast.makeText(
                    this@EnterOTPActivity,
                    apiError?.message ?: "Server error, please contact support.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, phone)
    }

    private fun callOtpVerification() {
        val otp = OTPVerification(phoneNo, otpTextView?.otp ?: "", "Retailer 1", true)
        /*dataManager.verifyOtp(object : ApiResponseListener<APICommonResponse<UserData>> {
            override fun onSuccess(response: APICommonResponse<UserData>) {
                if (response.isSuccess) {
                    response.data?.let {
                        if (response.data != null && response.data?.store_name != null && it.store_name.isNotEmpty()) {
                            userPreference
                                .updateUserData(response.data)
                            start(applicationContext)
                        } else {
                            userPreference
                                .updateUserData(response.data)
                            WelcomeScreen.Companion.start(applicationContext)
                        }
                        finish()
                    }

                } else Toast.makeText(this@EnterOTPActivity, response.message, Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onError(apiError: ApiError?) {
                Toast.makeText(this@EnterOTPActivity, apiError?.message?:"Server error, please contact support.", Toast.LENGTH_SHORT).show()
            }
        }, otp)*/
        dataManager.verifyOtp(object : ApiResponseListener<APICommonResponse<UserData>> {
            override fun onSuccess(response: APICommonResponse<UserData>) {
                /*if (response.isSuccess) {*/
                response.user?.let {
                    /*Start Addition Work*/
                    var usrData = it
                    usrData.session_key = response.session_id.toString()
                    /*End Addition Work*/

                    if (response.user != null && (userPreference.userData?.retailer?.account_status.equals("completed") || (userPreference.userData?.retailer?.store_type!=null && !userPreference.userData?.retailer?.store_type.equals("")))/*response.user?.retailer?.account_status.equals("completed")*/) {
                        userPreference
                            .updateUserData(usrData/*response.user*/)
                        start(applicationContext, false)
                    } else {
                        userPreference
                            .updateUserData(usrData/*response.user*/)
                        WelcomeScreen.Companion.start(applicationContext)
                    }
                    finish()
                }

                /*} else Toast.makeText(this@EnterOTPActivity, response.message, Toast.LENGTH_SHORT)
                    .show()*/
            }

            override fun onError(apiError: ApiError?) {
                Toast.makeText(
                    this@EnterOTPActivity,
                    apiError?.message ?: "Server error, please contact support.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }, otp)
    }

    companion object {
        fun start(context: Context, phone: String?) {
            val bundle = Bundle()
            bundle.putString("phone", phone)
            val intent = Intent(context, EnterOTPActivity::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }

    override fun onDestroy() {
        countDown?.cancel()
        super.onDestroy()
    }

}