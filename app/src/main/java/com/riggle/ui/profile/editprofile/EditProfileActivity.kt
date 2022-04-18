package com.riggle.ui.profile.editprofile

import android.Manifest
import android.app.Activity
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.riggle.R
import com.riggle.baseClass.BaseActivity
import com.riggle.data.firebase.FirebaseConfig
import com.riggle.data.firebase.StoreType
import com.riggle.data.location.LocationHandler
import com.riggle.data.location.LocationResultListener
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.response.*
import com.riggle.data.network.ApiResponseListener
import com.riggle.data.network.ApiState
import com.riggle.data.permission.PermissionHandler
import com.riggle.data.permission.Permissions
import com.riggle.databinding.ActivityEditProfileBinding
import com.riggle.ui.dialogs.ContactUsDialog
import com.riggle.ui.dialogs.FileUploaderChooserDialog
import com.riggle.ui.listener.FileChooserListener
import com.riggle.ui.other.adapter.SubAreaAdapter
import com.riggle.utils.Constants.DataKeys.PICK_IMAGE_GALLERY
import com.riggle.utils.UserProfileSingleton
import com.riggle.utils.Utility.REQUEST_GALLERY_IMAGES
import com.riggle.utils.Utility.isGalleryPermissionProvided
import com.riggle.utils.compressImage
import com.riggle.utils.createImageFile
import com.riggle.utils.showLongToast
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.cbAtStore
import kotlinx.android.synthetic.main.activity_edit_profile.etStoreName
import kotlinx.android.synthetic.main.activity_edit_profile.rvSubArea
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*


class EditProfileActivity : BaseActivity(), LocationResultListener {

    private val viewModel: EditProfileViewModel by viewModel()
    private val firebaseConfig: FirebaseConfig by inject()
    private val userPreference: UserProfileSingleton by inject()

    val scrollBounds = Rect()
    private var mlocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit_profile)

        val binding = DataBindingUtil.setContentView<ActivityEditProfileBinding>(
            this@EditProfileActivity,
            R.layout.activity_edit_profile
        )
        binding.check = true
        binding.viewModel = viewModel

        addOnClickListeners()
        setTabLayout()
        setupNestedScrollView()

        addApiStateListeners()
        getUserDetails()

    }

    private fun addApiStateListeners() {
        lifecycleScope.launchWhenCreated {
            viewModel.userDetailsApiStateFlow.collect { apiState ->
                when (apiState) {

                    is ApiState.Loading -> {
                        showHideLoader(true)
                    }

                    is ApiState.Success<*> -> {
                        showHideLoader(false)

                        apiState.data?.let { data ->
                            when (apiState.type) {
                                "get_profile" -> {
                                    getStoreNameFromFirebase()
                                    (data as? UserData)?.apply {
                                        userPreference.updateUserData(this)
                                    }
                                }
                                "update_profile" -> {
                                    showLongToast(this@EditProfileActivity, "Profile saved.")
                                }
                                "upload_image" -> {
                                    setImage(data as FileUploadResponse)
                                }
                                else -> {
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private fun setImage(data: FileUploadResponse) {
        viewModel.documentUrl.set(data.url)
    }

    private fun getUserDetails() {
        userPreference.userData?.let {
            viewModel.userData.set(it)
        }
        //viewModel.getUserDetails()
        getDetails()
    }

    private fun getStoreNameFromFirebase() {
        try {
            val storeList = firebaseConfig?.store_type ?: ArrayList<StoreType>()
            viewModel.userData.get()?.store_type?.let {

                if (storeList.size > 0) {
                    val store = storeList[viewModel.userData.get()!!.store_type.toInt()]
                    viewModel.storeTypeValue.set(store.store_type)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setupNestedScrollView() {
        /**
         * added getHitRect to check for the headers visibility and change tabLayout's position
         */
        nestedScrollView.getHitRect(scrollBounds)
        nestedScrollView.setOnScrollChangeListener(scrollChangeListener)
    }

    private fun addOnClickListeners() {

        uploadPhotoTextView.setOnClickListener {
            openGallery()
            /*val dialog = FileUploaderChooserDialog(this, object : FileChooserListener {
                override fun onCameraSelected() {
                    openCamera()
                }

                override fun onGallerySelected() {
                    if (isGalleryPermissionProvided(this@EditProfileActivity)) {
                        openGallery()
                    }
                }

            })
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(true)
            dialog.show()*/
        }


        tvSave.setOnClickListener {
            if (verifyFields()) {
                //viewModel.saveProfile()

                val data = HashMap<String, String>()
                data.put("name", etStoreName?.text.toString())
                data.put("pincode", etPincode?.text.toString())
                data.put("store_type", etStoreType?.text.toString())
                //data.put("sub_area", sub_area_id.toString())
                if (cbAtStore.isChecked) {
                    if (mlocation != null) {
                        mlocation?.let {
                            data.put(
                                "store_location",
                                it.latitude.toString() + "," + it.longitude.toString()
                            )//lat,lon
                        }
                    } else {
                        data.put("store_location", ""/*"23.82781,74.484839"*/)//lat,lon
                    }
                } else {
                    data.put("store_location", ""/*"23.82781,74.484839"*/)//lat,lon
                }
                userPreference.userData?.retailer?.id?.let { id ->
                    dataManager.updateRetailerOne(
                        object : ApiResponseListener<JsonElement> {
                            override fun onSuccess(response: JsonElement) {
                                if (response != null) {
                                    var retailerDetails =
                                        Gson().fromJson(
                                            response.toString(),
                                            UserDetails::class.java
                                        )
                                    var usrData = userPreference.userData
                                    usrData?.retailer = retailerDetails
                                    userPreference
                                        .updateUserData(usrData)
                                    userPreference
                                        .saveRetailerDetails(retailerDetails)
                                    Toast.makeText(
                                        this@EditProfileActivity,
                                        "Updated Successfully",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                } else Toast.makeText(
                                    this@EditProfileActivity,
                                    "",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }

                            override fun onError(apiError: ApiError?) {
                                Toast.makeText(
                                    this@EditProfileActivity,
                                    apiError?.message ?: "Server error, please contact support.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, id, data
                    )
                }

            }
        }

        aboutStoreClickableView.setOnClickListener {
            nestedScrollView.setOnScrollChangeListener(scrollChangeListenerDoesNothing)
            nestedScrollView.smoothScrollTo(0, tvAboutYourStoreHeader.top)
            tabLayout.getTabAt(0)?.select()
            Handler(Looper.getMainLooper()).postDelayed({
                nestedScrollView.setOnScrollChangeListener(scrollChangeListener)
            }, 300)
        }
        /*  addressClickableView.setOnClickListener {
              nestedScrollView.setOnScrollChangeListener(scrollChangeListenerDoesNothing)
              tabLayout.getTabAt(1)?.select()
              nestedScrollView.smoothScrollTo(0, tvAddressHeader.top)
              Handler(Looper.getMainLooper()).postDelayed({
                  nestedScrollView.setOnScrollChangeListener(scrollChangeListener)
              },300)
          }*/
        additionalInfoClickableView.setOnClickListener {
            nestedScrollView.setOnScrollChangeListener(scrollChangeListenerDoesNothing)
            tabLayout.getTabAt(1)?.select()
            nestedScrollView.smoothScrollTo(0, tvAdditionalInfoHeader.top)
            Handler(Looper.getMainLooper()).postDelayed({
                nestedScrollView.setOnScrollChangeListener(scrollChangeListener)
            }, 300)
        }

        etArea.setOnClickListener {
            getRegionList(1)
            rvSubArea?.visibility = View.VISIBLE
        }

        etSubArea.setOnClickListener {
            getRegionList(2)
            rvSubArea?.visibility = View.VISIBLE
        }

    }


    private fun getRegionList(type: Int) {
        showHideLoader(true)
        val map = HashMap<String, String>()
        map.put("page", "1")
        map.put("page_size", "20")
        map.put("search", "")
        map.put("is_active", "True")
        if (type == 1) {
            map.put("type", "area")
        } else if (type == 2) {
            map.put("belongs__id", area_id.toString())
            map.put("type", "sub_area")
        }
        dataManager.getRegion(object : ApiResponseListener<APICommonResponse<List<RegionsBean>>> {
            override fun onSuccess(response: APICommonResponse<List<RegionsBean>>) {
                showHideLoader(false)
                if (response != null) {
                    response.results?.let {
                        if (it.size > 0) {
                            setSubArea(it, type)
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

    private var area_id = 0
    private var sub_area_id = 0
    private fun setSubArea(results: List<RegionsBean>, type: Int) {
        var datesAdapter = SubAreaAdapter(this, results)
        rvSubArea?.layoutManager =
            LinearLayoutManager(this)
        rvSubArea?.adapter = datesAdapter
        datesAdapter.setListener(object : SubAreaAdapter.DeliveryDateListener {
            override fun dateSelected(bean: RegionsBean) {
                if (type == 1) {
                    area_id = bean.id
                    etArea?.setText(bean.name)
                    //getRegionList(2)
                } else if (type == 2) {
                    sub_area_id = bean.id
                    etSubArea?.setText(bean.name)
                }
                rvSubArea?.visibility = View.GONE
            }
        })
    }

    private fun verifyFields(): Boolean {
        if (TextUtils.isEmpty(etStoreName.text.isEmpty().toString().trim())) {
            etStoreName.error = "Required"
            return false
        }
        if (TextUtils.isEmpty(etStoreType.text.isEmpty().toString().trim())) {
            etStoreType.error = "Required"
            return false
        }
        /*if (TextUtils.isEmpty(etArea.text.isEmpty().toString().trim())) {
            etArea.error = "Required"
            return false
        }
        if (TextUtils.isEmpty(etSubArea.text.isEmpty().toString().trim())) {
            etSubArea.error = "Required"
            return false
        }*/
        return true
    }

    private fun openGallery() {
        //open gallery intent
        if (isGalleryPermissionProvided(this@EditProfileActivity)) {

            openGalleryToTakePicture(this@EditProfileActivity, false)
        }
    }

    private fun openCamera() {
        //open camera intent


    }


    //multiImageAllowed check to allow and disallow multi selection of images from gallery
    private fun openGalleryToTakePicture(context: Activity, multiImageAllowed: Boolean) {
        val intent = Intent()
        intent.action = Intent.ACTION_PICK
        intent.type = "image/*"
        if (multiImageAllowed) intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        context.startActivityForResult(intent, PICK_IMAGE_GALLERY)
    }


    var scrollChangeListener =
        NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->
            if (tvAboutYourStoreHeader.getLocalVisibleRect(scrollBounds)) {
                // imageView is within the visible window
                Log.i(
                    "EditProfileActivity",
                    "Tab visible  : 1  : diff : ${scrollY}  : view eight : ${tvAboutYourStoreHeader.height}"
                )
                tabLayout.getTabAt(0)?.select()
            }/* else if (tvAddressHeader.getLocalVisibleRect(scrollBounds)) {
                // imageView is not within the visible window
                Log.i("EditProfileActivity", "Tab visible  : 2")
                tabLayout.getTabAt(1)?.select()

            }*/ else {
                Log.i("EditProfileActivity", "Tab visible  : 3")
                // nestedScrollView.smoothScrollTo(0, tvAdditionalInfoHeader.top)
                tabLayout.getTabAt(1)?.select()

            }
        }

    var scrollChangeListenerDoesNothing =
        NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY, oldScrollX, oldScrollY ->

        }

    var touchableList: ArrayList<View>? = ArrayList()

    fun disableTabs() {
        touchableList = tabLayout?.touchables
        touchableList?.forEach { it.isEnabled = false }
    }

    private fun setTabLayout() {
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.about_store)))
        //tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.address)))
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.additional_info)))

        disableTabs()
    }

    fun onFieldLockClicked(view: View) {

        val dialog = ContactUsDialog(this@EditProfileActivity as? Context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.show()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {

            REQUEST_GALLERY_IMAGES -> if ((grantResults.isNotEmpty() &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED)
            ) {
                // Permission is granted. Continue the action or workflow
                // in your app.
                openGallery()
            } else {
                //permission denied
            }
        }
        return
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE_GALLERY) {
                data?.let { it ->
                    if (it.data != null) {
                        if (it.clipData != null) {
                            data.clipData?.let { clipData ->
                                addFileToList(clipData, null)
                            }
                        } else {
                            it.data?.let {
                                addFileToList(null, it)
                            }
                        }
                    } else {
                        data.clipData?.let { clipData ->
                            addFileToList(clipData, null)
                        }
                    }
                }
            }
        }
    }


    fun addFileToList(clipData: ClipData?, uri: Uri?) {

        if (clipData != null) {
            for (i in 0 until clipData.itemCount) {
                clipData.getItemAt(i)?.uri?.let {
                    loadImages(it, viewModel.imagesFileArrayList.size + clipData.itemCount)
                }
            }

        } else {
            uri?.let {
                loadImages(uri, 1)
            }

        }


    }

    fun loadImages(uri: Uri, totalCount: Int) {
        //showHideLoader(true)
        lifecycleScope.launch {
            setPic(uri)
        }

    }


    fun setPic(uriPath: Uri?) {

        uriPath?.let {

            var input: InputStream? = null
            try {
                input = contentResolver?.openInputStream(uriPath)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }

            // Get the dimensions of the View
            val targetW = 300
            val targetH = 300

            val bmOptions = BitmapFactory.Options()
            bmOptions.inJustDecodeBounds = true
            BitmapFactory.decodeStream(input!!, null, bmOptions)
            try {
                input.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val photoW: Int = bmOptions.outWidth
            val photoH: Int = bmOptions.outHeight

            // Determine how much to scale down the image
            val scaleFactor = Math.min(photoW / targetW, photoH / targetH)

            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false
            bmOptions.inSampleSize = scaleFactor

            try {
                input = contentResolver?.openInputStream(uriPath)

            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
            val bitmap = BitmapFactory.decodeStream(input, null, bmOptions)

            //galleryImageView.setImageBitmap(bitmap);

            val fileToUpload = createImageFile(this@EditProfileActivity)
            bitmap?.let {
                val fos = FileOutputStream(fileToUpload)
                fos.write(compressImage(it, false))
                fos.flush()
                fos.close()


                //viewModel.imagesFileArrayList.add(DocumentsItemListViewModel(fileToUpload))
                uploadImageToServer(fileToUpload)
            }
        }
    }


    //type = gstn_doc - for document upload
    //type = profile_img - for profile image upload
    private fun uploadImageToServer(fileToUpload: File) {
        val MEDIA_TYPE_IMAGE: MediaType? = "image/*".toMediaTypeOrNull()
        val requestBody = fileToUpload.asRequestBody(MEDIA_TYPE_IMAGE)

        //MultipartBody.Part is used to send also the actual file name
        val body = MultipartBody.Part.createFormData("store_img", fileToUpload.name, requestBody)
        val type = "gstn_doc";
        val text = "text/plain"

        val typeRequestBody = type.toRequestBody(text.toMediaTypeOrNull())

        viewModel.uploadFile(body, typeRequestBody)

    }

    override fun onStart() {
        super.onStart()
        Permissions.check(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            0,
            object : PermissionHandler() {
                override fun onGranted() {
                    getLocationData()
                }
            })
    }

    private fun getLocationData() {
        var locationHandler = LocationHandler(this@EditProfileActivity, this)
        locationHandler.getUserLocation()
    }

    override fun getLocation(location: Location) {
        this.mlocation = location
    }

    private fun getDetails() {
        showHideLoader(true)
        userPreference.userData?.retailer?.id?.let {
            dataManager.getPingDetails(
                object :
                    ApiResponseListener<JsonElement> {
                    override fun onSuccess(response: JsonElement) {
                        showHideLoader(false)
                        response?.let {
                            var usrData =
                                Gson().fromJson(it.toString(), RetailerDetailBean::class.java)

                            var usr = userPreference.userData
                            var retailer = usr?.retailer
                            retailer?.name = usrData.name
                            retailer?.store_type = usrData.store_type
                            retailer?.riggle_coins_balance = usrData.riggle_coins_balance
                            retailer?.is_serviceable = usrData.is_serviceable
                            retailer?.let {
                                usr?.retailer = it
                            }
                            userPreference
                                .updateUserData(usr)

                            usrData?.let {
                                viewModel.retailerData.set(it)
                                it.store_location?.let { lat ->
                                    cbAtStore.isChecked = !lat.equals("", false)
                                }
                            }
                            try {
                                sub_area_id = usrData.sub_area.id
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }

                    override fun onError(apiError: ApiError?) {
                        showHideLoader(false)
                        Log.i("TAG", "::::" + apiError?.message)
                    }
                },
                it, "sub_area.belongs"
            )
            //expand = sub_area
        }
    }

}