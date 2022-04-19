package com.riggle.ui.promo

import android.util.Log
import android.view.View
import androidx.databinding.ObservableArrayList
import androidx.databinding.ObservableField
import androidx.databinding.ObservableList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.riggle.data.models.ApiError
import com.riggle.data.models.request.UserProfileUpdateRequest
import com.riggle.data.models.response.ResponseCartData
import com.riggle.data.models.response.RetailerDetailBean
import com.riggle.data.models.response.UserData
import com.riggle.data.models.response.UserDetails
import com.riggle.data.network.ApiResponseListener
import com.riggle.data.network.ApiState
import com.riggle.ui.profile.editprofile.ProfileRepository
import kotlinx.android.synthetic.main.fragment_cart.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PromoViewModel(
    var repository: ProfileRepository
) : ViewModel() {


}