package com.riggle.ui.profile.editprofile

import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.riggle.data.models.request.UserProfileUpdateRequest
import com.riggle.data.models.response.RetailerDetailBean
import com.riggle.data.models.response.UserData
import com.riggle.data.network.ApiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class EditProfileViewModel(
    var repository: ProfileRepository
) : ViewModel() {

    var userData = ObservableField<UserData>()
    var retailerData = ObservableField<RetailerDetailBean>()
    var storeTypeValue = ObservableField("")
    var documentUrl = ObservableField("")

    private val _userDetailsApiStateFlow: MutableStateFlow<ApiState> =
        MutableStateFlow(ApiState.Empty)
    val userDetailsApiStateFlow: StateFlow<ApiState> = _userDetailsApiStateFlow


    var imagesFileArrayList: ObservableList<DocumentsItemListViewModel> = ObservableArrayList()


    fun getUserDetails() = viewModelScope.launch {
        repository.getUserDetails()
            .onStart {
                _userDetailsApiStateFlow.value = ApiState.Loading
            }.catch { e ->
                e.printStackTrace()
                _userDetailsApiStateFlow.value = ApiState.Failure(e)
            }.collect {
                setData(it.data, true)
                _userDetailsApiStateFlow.value = ApiState.Success(it.data, "get_profile")

            }
    }

    fun saveProfile() = viewModelScope.launch {
        var userProfileUpdateRequest: UserProfileUpdateRequest? = null
        userData.get()?.let {
            userProfileUpdateRequest = UserProfileUpdateRequest(
                it.pincode,
                it.store_name,
                it.store_type,
                it.address,
                it.name,
                it.alternate_no,
                it.gstn_no,
                it.g_latitude,
                it.g_longitude,
                it.category_of_products,
                it.image_path,
                it.role_type,
                documentUrl.get() ?: ""
            )
        }

        userProfileUpdateRequest?.let {
            repository.updateUserProfile(it)
                .onStart {
                    _userDetailsApiStateFlow.value = ApiState.Loading
                }.catch { e ->
                    e.printStackTrace()
                    _userDetailsApiStateFlow.value = ApiState.Failure(e)
                }.collect {
                    setData(it.data, false)
                    _userDetailsApiStateFlow.value = ApiState.Success(it.data, "update_profile")

                }
        }

    }

    private fun setData(data: UserData?, updateDocumentUrl: Boolean) {
        data?.let {
            userData.set(data)
            if (updateDocumentUrl)
                documentUrl.set(data.gstn_doc)
        }
    }

    fun uploadFile(body: MultipartBody.Part, type: RequestBody) = viewModelScope.launch {
        repository.uploadFile(body, type).onStart {
            _userDetailsApiStateFlow.value = ApiState.Loading
        }.catch { e ->
            _userDetailsApiStateFlow.value = ApiState.Failure(e)
        }.collect {
            _userDetailsApiStateFlow.value = ApiState.Success(it.data, "upload_image")
        }
    }


}