package com.riggle.ui.other

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.riggle.R
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.OrderDetailsUpload
import com.riggle.data.models.request.StoreInfo
import com.riggle.data.models.request.UploadOrder
import com.riggle.data.models.response.*
import com.riggle.data.network.ApiResponseListener
import com.riggle.services.eventbus.EventBusEvents.OnOrderConfirmed
import com.riggle.services.eventbus.GlobalBus
import com.riggle.ui.base.activity.CustomAppCompatActivityViewImpl
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.dialogs.LoadingDialog
import com.riggle.ui.other.adapter.DeliveryDatesAdapter
import com.riggle.ui.other.adapter.DeliveryDatesAdapter.DeliveryDateListener
import com.riggle.ui.other.adapter.DeliveryTimeAdapter
import com.riggle.ui.other.adapter.DeliveryTimeAdapter.DeliveryTimeListener
import com.riggle.ui.other.adapter.SubAreaAdapter
import com.riggle.utils.UserProfileSingleton
import com.riggle.utils.getServerFormat
import kotlinx.android.synthetic.main.activity_select_delivery_slot.*
import kotlinx.android.synthetic.main.activity_select_delivery_slot.etPinCode
import kotlinx.android.synthetic.main.layout_appbar.*
import org.koin.android.ext.android.inject
import java.text.SimpleDateFormat
import java.util.*


class SelectDeliverySlot : CustomAppCompatActivityViewImpl(), CustomAppViewConnector,
    DeliveryDateListener, DeliveryTimeListener {

    private var area_id = 0

    private val userPreference: UserProfileSingleton by inject()

    private var isRiggleCoinApplied = false
    private var finalAmount: String = "0"
    private var redeemRiggle: Double = 0.0
    private var datesAdapter: DeliveryDatesAdapter? = null
    private var timeAdapter: DeliveryTimeAdapter? = null
    private var deliveryDates: ArrayList<DeliveryDateSlots> = ArrayList<DeliveryDateSlots>()
    private var timeSlots: ArrayList<String> = ArrayList<String>()
    private var selectedDate: String = ""
    private var selectedDateValue: String = ""
    private var selectedTime: String = ""
    private var storeInfo: StoreInfo? = null
    private var loadingDialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        connectViewToParent(this)
        super.onCreate(savedInstanceState)
    }

    override fun setView(): Int {
        return R.layout.activity_select_delivery_slot
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.add_address)
        val bundle = intent.extras
        isRiggleCoinApplied = bundle?.getBoolean(KEY_RIGGLE_COINS, false) ?: false
        finalAmount = bundle?.getString(KEY_FINAL_AMOUNT, "0") ?: "0"
        redeemRiggle = bundle?.getDouble(KEY_REDEEM_RIGGLE) ?: 0.0
        setAddressData()

        addOnClickListeners()

        //fetchData()
        //deliveryDates = getDeliverySlot()
        //setDeliverySlotData()
        //getRegionList()
    }

    private fun getDeliverySlot(): ArrayList<DeliveryDateSlots> {
        val data = ArrayList<DeliveryDateSlots>()
        data.add(DeliveryDateSlots("Day 1", "dd-MM-yyyy", "", ArrayList()))
        data.add(DeliveryDateSlots("Day 2", "dd-MM-yyyy", "", ArrayList()))
        data.add(DeliveryDateSlots("Day 3", "dd-MM-yyyy", "", ArrayList()))
        data.add(DeliveryDateSlots("Day 4", "dd-MM-yyyy", "", ArrayList()))
        return data
    }

    private fun getRegionList() {
        showHideLoader(true)
        val map = HashMap<String, String>()
        map.put("page", "1")
        map.put("page_size", "20")
        map.put("search", "")
        map.put("type", "sub_area")
        dataManager.getRegion(object : ApiResponseListener<APICommonResponse<List<RegionsBean>>> {
            override fun onSuccess(response: APICommonResponse<List<RegionsBean>>) {
                showHideLoader(false)
                if (response != null) {
                    response.results?.let {
                        if (it.size > 0) {
                            setSubArea(it)
                        }
                    }
                }
            }

            override fun onError(apiError: ApiError?) {
                showHideLoader(false)
                Log.i("TAG", ":::::" + apiError?.message)
            }

        }, map)
    }

    private fun setSubArea(results: List<RegionsBean>) {
        var datesAdapter = SubAreaAdapter(this, results)
        rvSubArea?.layoutManager =
            LinearLayoutManager(this)
        rvSubArea?.adapter = datesAdapter
        datesAdapter.setListener(object : SubAreaAdapter.DeliveryDateListener {
            override fun dateSelected(bean: RegionsBean) {
                area_id = bean.id
                tvCity?.text = bean.name
                rvSubArea?.visibility = View.GONE
            }
        })
    }

    private fun addOnClickListeners() {
        btn_proceed.setOnClickListener {
            proceed()
        }

        tvCity.setOnClickListener {
            rvSubArea?.visibility = View.VISIBLE
        }

        tvDeliverDate.setOnClickListener {
            showDatePicker()
        }

    }

    private fun showDatePicker() {
        /*var calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, 1)
        var calendarMax = Calendar.getInstance()
        calendarMax.set(Calendar.DATE, 5)*/
        var datePicker = DatePickerDialog(
            this, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.datePicker.setMinDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000 - 1000)
        datePicker.datePicker.setMaxDate(System.currentTimeMillis() + 24 * 60 * 60 * 1000 * 7 - 1000)
        //datePicker.datePicker.maxDate = calendarMax.timeInMillis
        datePicker.show()

    }

    var cal = Calendar.getInstance()
    val dateSetListener =
        DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            val myFormat = "yyyy-MM-dd" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            tvDeliverDate.text = sdf.format(cal.time)
            activateBtn()
        }

    fun proceed() {
        val details = userPreference.userData?.retailer?.id?.let {
            UploadOrder(
                Math.round(redeemRiggle),
                tvDeliverDate.text.toString().trim(),
                it
            )
        }
        details?.let { placeOrder(it) }

        /* if (userPreference.isAddressAvailable) {
             val details = OrderDetailsUpload(
                 isRiggleCoinApplied,
                 selectedDateValue.getServerFormat() ?: "",
                 selectedTime,
                 finalAmount
             )
             //            PaymentActivity.start(this, details);
             //placeOrder(details)
         } else {
             storeInfo = StoreInfo()
             storeInfo?.apply {
                 address = etAddress?.text.toString()
                 pin_code = userPreference
                     .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.PINCODE)
                 store_name = userPreference
                     .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.STORE_NAME)
             }
             editProfile()
         }*/
    }

    private fun setAddressData() {
        if (userPreference.isAddressAvailable) {
            llAddressAvailable?.visibility = View.VISIBLE
            llAddAddress?.visibility = View.GONE
            tvStoreName?.text = userPreference
                .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.STORE_NAME)
            tvStoreAddress?.text = (userPreference
                .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.ADDRESS)
                    + userPreference
                .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.CITY)
                    + userPreference
                .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.STATE))
            tvStorePhone?.text = userPreference
                .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.MOBILE)
        } else {
            llAddAddress?.visibility = View.VISIBLE
            llAddressAvailable?.visibility = View.GONE
            etPinCode?.setText(
                userPreference
                    .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.PINCODE)
            )
            tvState?.setText(
                userPreference
                    .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.STATE)
            )
            tvCity?.text = userPreference
                .getProfileData(UserProfileSingleton.PROFILE_PROPERTIES.CITY)
            etAddress?.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    charSequence: CharSequence,
                    i: Int,
                    i1: Int,
                    i2: Int
                ) {
                }

                override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                    if (charSequence.isNotEmpty()) {
                        activateBtn()
                    }
                }

                override fun afterTextChanged(editable: Editable) {}
            })
        }
    }

    private fun editProfile() {
        showHideLoader(true)

        val data = HashMap<String, String>()
        data.put("name", userPreference.userData?.retailer?.name.toString())
        data.put("pincode", etPinCode?.text.toString())
        data.put("address", etAddress?.text.toString())
        data.put("landmark", "")
        data.put("sub_area", area_id.toString())
        data.put("store_type", "")
        data.put("store_location", ""/*"23.82781,74.484839"*/)//lat,lon

        /*dataManager.updateRetailerOne(
            object : ApiResponseListener<JsonElement> {
                override fun onSuccess(response: JsonElement) {
                    if (response != null) {

                        var retailerDetails =
                            Gson().fromJson(response.toString(), RetailerDetails::class.java)

                        *//*var usrData = userPreference.userData
                        usrData?.session_key?.let {
                            response.session_key = it
                            response.retailer = usrData.retailer
                        }
                        userPreference
                            .updateUserData(response)*//*

                        val details = userPreference.userData?.retailer?.id?.let {
                            UploadOrder(
                                0,
                                "2021-12-23",
                                it
                            )
                        }
                        //            PaymentActivity.start(this, details);
                        details?.let { placeOrder(it) }

                    } else Toast.makeText(this@SelectDeliverySlot, "", Toast.LENGTH_SHORT)
                        .show()
                }

                override fun onError(apiError: ApiError?) {
                    showHideLoader(false)
                }
            },
            userPreference.userData?.retailer?.id, data
        )*/

        /*storeInfo?.apply {

            dataManager.editProfile(object : ApiResponseListener<APICommonResponse<UserData>> {
                override fun onSuccess(response: APICommonResponse<UserData>) {
                    if (response.isSuccess) {
                        userPreference
                            .updateUserData(response.data)
                        proceed()
                        showHideLoader(false)
                    }
                }

                override fun onError(apiError: ApiError?) {
                    showHideLoader(false)
                }
            }, this)

        } ?: run {
            showLongToast(this@SelectDeliverySlot, getString(R.string.error_store_info))
        }*/

    }

    private fun fetchData() {
        dataManager.getDeliverySlots(object :
            ApiResponseListener<APICommonResponse<DeliverySlots>> {
            override fun onSuccess(response: APICommonResponse<DeliverySlots>) {
                if (response.isSuccess && response.data != null) {
                    response.data?.weekdata?.let {
                        deliveryDates = it
                        setDeliverySlotData()
                    }

                    if (userPreference.isAddressAvailable) activateBtn() else deactivateBtn()
                }
            }

            override fun onError(apiError: ApiError?) {}
        })
    }

    private fun setDeliverySlotData() {
        if (deliveryDates.size > 0) {
            datesAdapter = DeliveryDatesAdapter(this, deliveryDates)
            rvDeliverySlots?.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            rvDeliverySlots?.adapter = datesAdapter
            datesAdapter?.setListener(this)
            dateSelected(0)
        }
    }

    override fun dateSelected(pos: Int) {
        deliveryDates?.let {
            val (_, sub_title, value, timeSlots1) = it[pos]
            datesAdapter?.selectedPosition(pos)
            datesAdapter?.notifyDataSetChanged()
            selectedDate = sub_title
            selectedDateValue = value
            timeSlots = timeSlots1
            timeAdapter = DeliveryTimeAdapter(this, timeSlots)
            rvDeliveryTimeSlots?.layoutManager = LinearLayoutManager(this)
            rvDeliveryTimeSlots?.adapter = timeAdapter
            timeAdapter?.setListener(this)
            timeSelected(0)
        }

    }

    override fun timeSelected(pos: Int) {
        timeAdapter?.selectedPosition(pos)
        timeAdapter?.notifyDataSetChanged()
        //selectedTime = timeSlots[pos]
    }

    private fun deactivateBtn() {
        btn_proceed?.alpha = 0.2f
        btn_proceed?.isClickable = false
    }

    private fun activateBtn() {
        btn_proceed?.alpha = 1f
        btn_proceed?.isClickable = true
    }

    private fun showHideLoader(state: Boolean) {
        if (loadingDialog != null) {
            if (state) loadingDialog?.show() else loadingDialog?.dismiss()
        } else {
            loadingDialog = LoadingDialog(activity)
            showHideLoader(state)
        }
    }

    private fun placeOrder(details: UploadOrder) {
        showHideLoader(true)
        userPreference.userData?.let { data ->
            dataManager.placeOrder(object : ApiResponseListener<JsonElement> {
                override fun onSuccess(response: JsonElement) {
                    showHideLoader(false)
                    sharedPreferencesUtil.isOrderPlaced = true
                    Toast.makeText(this@SelectDeliverySlot, "Order placed", Toast.LENGTH_SHORT)
                        .show()

                    //notify cart screen to update it's cart
                    val orderConfirmed = OnOrderConfirmed(true)
                    GlobalBus.bus?.post(orderConfirmed)
                    MyOrdersActivity.Companion.start(this@SelectDeliverySlot)
                    finish()
                }

                override fun onError(apiError: ApiError?) {
                    showHideLoader(false)
                    if (apiError?.statusCode == 201) {
                        //notify cart screen to update it's cart
                        val orderConfirmed = OnOrderConfirmed(true)
                        GlobalBus.bus?.post(orderConfirmed)
                        MyOrdersActivity.Companion.start(this@SelectDeliverySlot)
                        finish()
                    } else {
                        Toast.makeText(this@SelectDeliverySlot, apiError?.msg, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }, data.session_key, details)
        }
    }

    companion object {
        const val KEY_RIGGLE_COINS = "riggle_coins"
        const val KEY_FINAL_AMOUNT = "final_amount"
        const val KEY_REDEEM_RIGGLE = "redeem_riggle"
        const val COPOUN_CODE = "copound_code"
        fun start(
            context: Context,
            riggle_coins: Boolean,
            final_amount: String?,
            redeem_coin: Double?,
            couponCode : String
        ) {
            val bundle = Bundle()
            bundle.putBoolean(KEY_RIGGLE_COINS, riggle_coins)
            bundle.putString(KEY_FINAL_AMOUNT, final_amount)
            bundle.putString(COPOUN_CODE, couponCode)
            redeem_coin?.let { bundle.putDouble(KEY_REDEEM_RIGGLE, it) }
            val intent = Intent(context, SelectDeliverySlot::class.java)
            intent.putExtras(bundle)
            context.startActivity(intent)
        }
    }
}