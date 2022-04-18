package com.riggle.ui.other.registration.addressdetails

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.drjacky.imagepicker.ImagePicker
import com.riggle.BR
import com.riggle.R
import com.riggle.baseClass.SimpleRecyclerViewAdapter
import com.riggle.data.firebase.StoreType
import com.riggle.data.models.ApiError
import com.riggle.data.models.MenuBean
import com.riggle.data.models.response.CoreConstantsResponse
import com.riggle.data.models.response.RetailerProofDocumentType
import com.riggle.data.network.ApiResponseListener
import com.riggle.databinding.ItemImageListBinding
import com.riggle.ui.base.connector.CustomAppViewConnector
import com.riggle.ui.base.fragment.CustomAppFragmentViewImpl
import com.riggle.ui.dialogs.FileUploaderChooserDialog
import com.riggle.ui.listener.FileChooserListener
import com.riggle.ui.other.registration.StoreTypeAdapter
import com.riggle.ui.other.registration.WelcomeScreen
import com.riggle.utils.FileUtil
import com.riggle.utils.UserProfileSingleton
import com.riggle.utils.hideKeyboard
import kotlinx.android.synthetic.main.fragment_personal_details.*
import kotlinx.android.synthetic.main.fragment_personal_details.storeTypeSpinner
import org.koin.android.ext.android.inject
import java.io.File

class PersonalDetailsFragment : CustomAppFragmentViewImpl(), CustomAppViewConnector {

    private var selectedStoreKey = 0
    private var selectedStoreType = ""

    private val userPreference: UserProfileSingleton by inject()

    companion object {
        fun newInstance(): PersonalDetailsFragment {
            val fragment = PersonalDetailsFragment()
            return fragment
        }
    }

    private var storeList = arrayListOf<StoreType>()
    private var adapter: StoreTypeAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        connectViewToParent(this)
        super.onCreateView(inflater, container, savedInstanceState)
        return view
    }

    override fun setView(): Int {
        return R.layout.fragment_personal_details
    }

    override fun initializeViews(savedInstanceState: Bundle?) {
        userPreference.userData?.let {
            etContactNumber.setText(it.mobile)
        }
        setSpinner()
        setUpRecyclerView()
        onClickListener()
        getCoreConstant()
        onTextChanged()
    }

    private fun onTextChanged() {
        etContactPerson.doOnTextChanged { text, start, before, count ->
            WelcomeScreen.retailerRequest.username = text.toString()
        }
        etStoreName.doOnTextChanged { text, start, before, count ->
            WelcomeScreen.retailerRequest.name = text.toString()
        }
    }

    private fun onClickListener() {
        etProof.setOnClickListener {
            if (retailerProofDocumentTypes.isNotEmpty())
                showWindowPop(it, retailerProofDocumentTypes)
        }
    }

    private fun showWindowPop(
        it: View?,
        retailerProofDocumentTypes: ArrayList<RetailerProofDocumentType>
    ) {
        val popup = PopupMenu(requireContext(), it)
        for (index in retailerProofDocumentTypes) {
            popup.menu.add(index.name)
        }
        popup.setOnMenuItemClickListener { menuItem ->
            val strs = menuItem.title.toString()
            etProof.setText(strs)
            WelcomeScreen.retailerRequest.proof_document_type = strs
            true
        }
        popup.show()
    }

    val retailerProofDocumentTypes = ArrayList<RetailerProofDocumentType>()
    private fun getCoreConstant() {
        dataManager.getCoreConstants(object : ApiResponseListener<CoreConstantsResponse> {
            override fun onSuccess(response: CoreConstantsResponse) {
                if (response != null) {
                    retailerProofDocumentTypes.clear()
                    retailerProofDocumentTypes.addAll(response.retailer_proof_document_types)
                }
            }

            override fun onError(apiError: ApiError?) {
                Toast.makeText(requireContext(), apiError.toString(), Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun setSpinner() {
        storeList?.apply {
            add(0, StoreType(getString(R.string.store_type), 0))
            add(StoreType("General Store", 1))
            add(StoreType("Dairy", 1))
            add(StoreType("Medical Store", 1))
            add(StoreType("Supermarket", 1))
            add(StoreType("QSR / Food Joint", 1))
            add(StoreType("24 hr convenience store", 1))
            add(StoreType("Other", 1))
        }

        val hint = StoreType(getString(R.string.store_type), 0)
        storeList.add(0, hint)

        adapter = StoreTypeAdapter(requireContext(), storeList)
        storeTypeSpinner?.adapter = adapter
        storeTypeSpinner?.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) hideKeyboard(requireActivity())
            false
        }
        storeTypeSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val (_, key) = adapterView.getItemAtPosition(i) as StoreType
                selectedStoreKey = key
                selectedStoreType = storeList.get(i).store_type
                WelcomeScreen.retailerRequest.store_type = storeList.get(i).store_type
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }

    }

    var adapterImages: SimpleRecyclerViewAdapter<MenuBean, ItemImageListBinding>? = null
    var dialog: FileUploaderChooserDialog? = null
    private fun setUpRecyclerView() {
        adapterImages = SimpleRecyclerViewAdapter<MenuBean, ItemImageListBinding>(
            R.layout.item_image_list, BR.bean
        ) { v, m, pos ->
            when (pos) {
                0 -> {
                    dialog = FileUploaderChooserDialog(requireContext(), object :
                        FileChooserListener {
                        override fun onCameraSelected() {
                            launcher.launch(
                                ImagePicker.with(requireActivity())
                                    .cameraOnly() // or galleryOnly()
                                    .createIntent()
                            )
                            dialog?.dismiss()
                        }

                        override fun onGallerySelected() {
                            launcher.launch(
                                ImagePicker.with(requireActivity())
                                    .galleryOnly() // or cameraOnly()
                                    .createIntent()
                            )
                            dialog?.dismiss()
                        }

                    })
                    dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog?.setCancelable(true)
                    dialog?.show()
                }
            }
        }
        val layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        rvImageList.layoutManager = layoutManager
        rvImageList.adapter = adapterImages
        adapterImages?.list = getList()
    }

    private fun getList(): MutableList<MenuBean> {
        val dataList = ArrayList<MenuBean>()
        dataList.apply {
            add(MenuBean(1, "", 0, false))
        }
        return dataList
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val uri = it.data?.data!!
                WelcomeScreen.uploadFile = FileUtil.from(requireContext(), uri)
                val data = getList().apply {
                    add(MenuBean(1, WelcomeScreen.uploadFile?.absolutePath.toString(), 1, false))
                }
                WelcomeScreen.isImageSelected = WelcomeScreen.uploadFile?.absolutePath.toString()
                adapterImages?.list = data
            }
        }

}