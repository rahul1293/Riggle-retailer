package com.riggle.ui.dialogs

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.content.ContextCompat
import com.google.gson.JsonElement
import com.riggle.R
import com.riggle.baseClass.RiggleApplication
import com.riggle.data.DataManager
import com.riggle.data.firebase.FirebaseConfig
import com.riggle.data.firebase.FirebaseRemoteConfigUtil
import com.riggle.data.firebase.RoleType
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.AddMember
import com.riggle.data.models.request.AddMembers
import com.riggle.data.network.ApiResponseListener
import com.riggle.ui.dialogs.listener.AddRemoveMemberDialogListener
import com.riggle.ui.other.registration.RoleTypeAdapter
import com.riggle.utils.UserProfileSingleton
import com.riggle.utils.hideKeyboardDialog
import kotlinx.android.synthetic.main.dialog_add_members.*
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject


class AddMemberDialog(private val mContext: Context?) : AppCompatDialog(
    mContext
), KoinComponent {
    /*@JvmField
    @Inject
    var dataManager: DataManager? = null*/

    private var firebaseConfig: FirebaseConfig? = null
    private var roleTypeList: List<RoleType>? = null
    private var adapter: RoleTypeAdapter? = null
    private var etPhone: EditText? = null
    private var tvCancel: TextView? = null
    private var tvAdd: TextView? = null
    private var spinner: AppCompatSpinner? = null
    private var ivCross: AppCompatImageView? = null
    private var selectedRoleKey: String? = null
    private var listener: AddRemoveMemberDialogListener? = null
    private val userPreference: UserProfileSingleton by inject()

    val dataManager: DataManager by inject()
    fun setListener(listener: AddRemoveMemberDialogListener?) {
        this.listener = listener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_add_members)
        initViews()
    }

    private fun initViews() {
        if (firebaseConfig == null) {
            firebaseConfig = FirebaseRemoteConfigUtil.instance?.fireBaseConfigValues
        }
        etPhone = findViewById(R.id.etPhone)
        spinner = findViewById(R.id.spinnerRole)
        tvCancel = findViewById(R.id.tvCancel)
        tvAdd = findViewById(R.id.tvAdd)
        ivCross = findViewById(R.id.ivCross)
        etPhone?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                checkValidation()
            }

            override fun afterTextChanged(editable: Editable) {}
        })
        setRoleSpinner()
        tvCancel?.setOnClickListener { dismiss() }
        tvAdd?.setOnClickListener { addMember() }
        ivCross?.setOnClickListener { dismiss() }
    }

    private fun addMember() {
        userPreference.userData?.retailer?.id?.let {
            val member = AddMembers(
                etName?.text.toString(),
                "Riggle",
                etPhone?.text.toString(),
                it,
                "retailer"/*selectedRoleKey*/
            )
            dataManager.addUsers(object : ApiResponseListener<JsonElement> {
                override fun onSuccess(response: JsonElement) {
                    Toast.makeText(mContext, "Member added", Toast.LENGTH_SHORT).show()
                    if (listener != null) listener?.userAddRemoved()
                    dismiss()
                }

                override fun onError(apiError: ApiError?) {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }, member)
        }

        /*dataManager.addMember(object : ApiResponseListener<APICommonResponse<String>> {
                override fun onSuccess(response: APICommonResponse<String>) {
                    Toast.makeText(mContext, "Member added", Toast.LENGTH_SHORT).show()
                    if (listener != null) listener?.userAddRemoved()
                    dismiss()
                }

                override fun onError(apiError: ApiError?) {
                    Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show()
                }
            }, member)*/
    }

    private fun setRoleSpinner() {
        roleTypeList = firebaseConfig?.role_type
        adapter = RoleTypeAdapter(mContext, roleTypeList)
        spinner?.adapter = adapter
        spinner?.setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_DOWN) hideKeyboardDialog((mContext as Activity))
            false
        }
        spinner?.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                val (_, key) = adapterView.getItemAtPosition(i) as RoleType
                selectedRoleKey = key
                checkValidation()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
        }
    }

    private fun checkValidation() {
        mContext?.let {
            if (etPhone?.text.toString().length == 10/* && selectedRoleKey != null*/) {
                tvAdd?.isEnabled = true
                tvAdd?.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary))
            } else {
                tvAdd?.isEnabled = false
                tvAdd?.setTextColor(ContextCompat.getColor(mContext, R.color.txt_disabled))
            }
        }

    }
}