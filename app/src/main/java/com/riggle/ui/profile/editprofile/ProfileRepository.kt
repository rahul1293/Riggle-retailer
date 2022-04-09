package com.riggle.ui.profile.editprofile

import com.riggle.data.DataManager
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.request.UserProfileUpdateRequest
import com.riggle.data.models.response.Earnings
import com.riggle.data.models.response.FileUploadResponse
import com.riggle.data.models.response.UserData
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfileRepository(var remoteDateSource: DataManager) {

    fun getUserDetails(): Flow<APICommonResponse<UserData>> {
        return remoteDateSource.getUserDetails()
    }

    fun updateUserProfile(request: UserProfileUpdateRequest): Flow<APICommonResponse<UserData>> {
        return remoteDateSource.updateProfile(request)
    }

    fun getUserEarnings(page: Int): Flow<Earnings> =
        remoteDateSource.getUserEarnings(page)

    fun uploadFile(
        body: MultipartBody.Part,
        type: RequestBody
    ): Flow<APICommonResponse<FileUploadResponse>> {
        return remoteDateSource.uploadFile(body, type)
    }

}