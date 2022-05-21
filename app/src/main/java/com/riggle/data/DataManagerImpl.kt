package com.riggle.data

import com.google.gson.JsonElement
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.request.*
import com.riggle.data.models.response.*
import com.riggle.data.network.ApiService
import com.riggle.data.network.ApiResponseListener
import com.riggle.utils.ErrorUtils
import com.riggle.utils.RiggleLogger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import java.util.*
import kotlin.collections.ArrayList

class DataManagerImpl(
    private val apiInterface: ApiService,
    private val retrofit: Retrofit
) : DataManager {
    override fun loginPhone(
        apiResponseListener: ApiResponseListener<APICommonResponse<LoginResponse>>,
        phone: Login
    ) {
        executeApiCall(
            apiInterface.loginPhone(phone),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun reSend(
        apiResponseListener: ApiResponseListener<APICommonResponse<LoginResponse>>,
        phone: Login
    ) {
        executeApiCall(
            apiInterface.reSendOtp(phone),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun verifyOtp(
        apiResponseListener: ApiResponseListener<APICommonResponse<UserData>>,
        otp: OTPVerification
    ) {
        executeApiCall(
            apiInterface.verifyOtp(otp),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun editProfile(
        apiResponseListener: ApiResponseListener<APICommonResponse<UserData>>,
        storeInfo: StoreInfo
    ) {
        executeApiCall(
            apiInterface.editProfile(storeInfo),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getMemberList(
        apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<MemberList>>>,
        retailer: String
    ) {
        executeApiCall(
            apiInterface.getMemberList(retailer),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun removeMember(
        apiResponseListener: ApiResponseListener<APICommonResponse<String>>,
        user_id: Int
    ) {
        executeApiCall(
            apiInterface.removeMember(user_id),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun addMember(
        apiResponseListener: ApiResponseListener<APICommonResponse<String>>,
        member: AddMember
    ) {
        executeApiCall(
            apiInterface.addMember(member),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getProducts(
        apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<ProductsData>>>,
        type: String?
    ) {
        executeApiCall(
            apiInterface.getProducts(type),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getProducts(
        apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<ProductsData>>>,
        type: String?,
        id: Int
    ) {
        executeApiCall(
            apiInterface.getProducts(type, id),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getBrands(apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<BrandsCategoryData>>>) {
        executeApiCall(
            apiInterface.getBrands(),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getTotalRegalCoins(apiResponseListener: ApiResponseListener<APICommonResponse<RiggleCoinsResponse>>) {
        executeApiCall(
            apiInterface.getTotalRiggleCoins(),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getCategories(apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<BrandsCategoryData>>>) {
        executeApiCall(
            apiInterface.getCategories(),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getVariants(
        apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<ProductsData>>>,
        product_id: Int
    ) {
        executeApiCall(
            apiInterface.getVariants(product_id),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun addCart(
        apiResponseListener: ApiResponseListener<APICommonResponse<ProductsData>>,
        cartData: RequestCartData
    ) {
        executeApiCall(
            apiInterface.addCart(cartData),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun editCart(
        apiResponseListener: ApiResponseListener<APICommonResponse<EditCartResponse>>,
        cartData: RequestCartData
    ) {
        executeApiCall(
            apiInterface.editCart(cartData),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getDeliverySlots(apiResponseListener: ApiResponseListener<APICommonResponse<DeliverySlots>>) {
        executeApiCall(
            apiInterface.getDeliverySlots(),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun addOrder(
        apiResponseListener: ApiResponseListener<APICommonResponse<String>>,
        details: OrderDetailsUpload
    ) {
        executeApiCall(
            apiInterface.addOrder(details),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getMyOrders(
        apiResponseListener: ApiResponseListener<APICommonResponse<List<MyOrderDataOuter>>>,
        retailer_id: Int, expand: String
    ) {
        executeApiCall(
            apiInterface.getMyOrders(retailer_id, expand),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getOrderDetail(
        apiResponseListener: ApiResponseListener<APICommonResponse<OrderDetail>>,
        cart_id: Int
    ) {
        executeApiCall(
            apiInterface.getOrderDetail(cart_id),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun search(
        apiResponseListener: ApiResponseListener<APICommonResponse<SearchData>>,
        query: String
    ) {
        executeApiCall(
            apiInterface.search(query),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }


    private fun <T> executeApiCall(
        commonResponseCall: Call<APICommonResponse<T>>,
        apiResponseListener: ApiResponseListener<APICommonResponse<T>>,
        startTime: Long
    ) {
        commonResponseCall.enqueue(object : Callback<APICommonResponse<T>> {
            override fun onResponse(
                call: Call<APICommonResponse<T>>,
                response: Response<APICommonResponse<T>>
            ) {
                if (response.isSuccessful) {
                    RiggleLogger.d("fatal::API_CALL", "onResponse::SUCCESS")
                    if (response.body() != null) {
                        apiResponseListener.onSuccess(response.body()!!)
                    } else {
                        RiggleLogger.d("fatal::API_CALL", "onResponse::PARSING ERROR")
                    }

                } else {
                    val error = ErrorUtils.parseError(retrofit, response)
                    RiggleLogger.d(
                        "fatal::API_CALL",
                        "onResponse::Error:message::" + error.message + "::path::" + retrofit.baseUrl().encodedPath
                    )
                    apiResponseListener.onError(error)
                    ErrorUtils.handleAPIError(
                        error,
                        getApiMethodForAnalytics(commonResponseCall.request().url.toString())
                    )
                }
            }

            override fun onFailure(call: Call<APICommonResponse<T>>, throwable: Throwable) {
                call.request().headers
                val error = ErrorUtils.resolveNetworkError(throwable)
                RiggleLogger.d("fatal::API_CALL", "onFailure::Error:message::" + error.message)
                apiResponseListener.onError(error)
                ErrorUtils.handleAPIError(
                    error,
                    getApiMethodForAnalytics(commonResponseCall.request().url.toString())
                )
            }
        })
    }


    private fun <T> executeApiCallOne(
        commonResponseCall: Call<T>,
        apiResponseListener: ApiResponseListener<T>,
        startTime: Long
    ) {
        commonResponseCall.enqueue(object : Callback<T> {
            override fun onResponse(
                call: Call<T>,
                response: Response<T>
            ) {
                if (response.isSuccessful) {
                    RiggleLogger.d("fatal::API_CALL", "onResponse::SUCCESS")
                    if (response.body() != null) {
                        apiResponseListener.onSuccess(response.body()!!)
                    } else {
                        RiggleLogger.d("fatal::API_CALL", "onResponse::PARSING ERROR")
                    }

                } else {
                    val error = ErrorUtils.parseError(retrofit, response)
                    RiggleLogger.d(
                        "fatal::API_CALL",
                        "onResponse::Error:message::" + error.message + "::path::" + retrofit.baseUrl().encodedPath
                    )
                    apiResponseListener.onError(error)
                    ErrorUtils.handleAPIError(
                        error,
                        getApiMethodForAnalytics(commonResponseCall.request().url.toString())
                    )
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                call.request().headers
                val error = ErrorUtils.resolveNetworkError(throwable)
                RiggleLogger.d("fatal::API_CALL", "onFailure::Error:message::" + error.message)
                apiResponseListener.onError(error)
                //val error1 = ErrorUtils.parseError(retrofit, call.execute())
                ErrorUtils.handleAPIError(
                    error,
                    getApiMethodForAnalytics(commonResponseCall.request().url.toString())
                )
            }
        })
    }

    private fun getApiMethodForAnalytics(url: String): String {
        return if (url.length > 100) {
            url.substring(0, 100)
        } else {
            url
        }
    }

    private fun <T> executeApiCallVoid(
        commonResponseCall: Call<T>,
        apiResponseListener: ApiResponseListener<T?>,
        startTime: Long,
        apiResponseInstrumentation: Boolean
    ) {
        commonResponseCall.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    RiggleLogger.d("fatal::API_CALL", "" + response.headers()["X-Response-Time"])
                    RiggleLogger.d("fatal::API_CALL", "onResponse::SUCCESS")
                    apiResponseListener.onSuccess(response.body())
                } else {
                    val error = ErrorUtils.parseError(retrofit, response)
                    RiggleLogger.d(
                        "fatal::API_CALL",
                        "onResponse::Error:message::" + error.message + "::path::" + retrofit.baseUrl().encodedPath
                    )
                    apiResponseListener.onError(error)
                }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                val error = ErrorUtils.resolveNetworkError(throwable)
                RiggleLogger.d("fatal::API_CALL", "onFailure::Error:message::" + error.message)
                apiResponseListener.onError(error)
            }
        })
    }

    override fun getUserDetails(): Flow<APICommonResponse<UserData>> = flow {
        emit(apiInterface.getUserDetails())
    }.flowOn(Dispatchers.IO)

    override fun updateProfile(request: UserProfileUpdateRequest): Flow<APICommonResponse<UserData>> =
        flow {
            emit(apiInterface.updateProfile(request))
        }.flowOn(Dispatchers.IO)

    override fun getUserEarnings(page: Int): Flow<Earnings> = flow {
        emit(apiInterface.getUserEarnings(page))
    }.flowOn(Dispatchers.IO)


    override fun getTopSearches(): Flow<APICommonResponse<TopSearches>> = flow {
        emit(apiInterface.getTopSearches())
    }.flowOn(Dispatchers.IO)

    override fun uploadFile(
        body: MultipartBody.Part,
        type: RequestBody
    ): Flow<APICommonResponse<FileUploadResponse>> = flow {
        emit(apiInterface.uploadFile(body, type))
    }.flowOn(Dispatchers.IO)

    override fun getRegion(
        apiResponseListener: ApiResponseListener<APICommonResponse<List<RegionsBean>>>,
        data: Map<String, String>
    ) {
        executeApiCall(
            apiInterface.getRegion(data),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun updateRetailer(
        apiResponseListener: ApiResponseListener<UserData>,
        id: Int?,
        data: HashMap<String, String>
    ) {
        executeApiCallOne(
            apiInterface.updateRetailer(id, data),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun updateRetailerOne(
        apiResponseListener: ApiResponseListener<JsonElement>,
        id: Int?,
        data: HashMap<String, String>
    ) {
        executeApiCallOne(
            apiInterface.updateRetailerOne(id, data),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun updateRetailerDetails(
        apiResponseListener: ApiResponseListener<JsonElement>,
        id: Int?,
        data: HashMap<String, RequestBody>,
        file: MultipartBody.Part
    ) {
        executeApiCallOne(
            apiInterface.updateRetailerDetails(id, data, file),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getMyOrdersOne(
        apiResponseListener: ApiResponseListener<JsonElement>,
        /*page: Int,
        expand: String*/
        params: HashMap<String, String>
    ) {
        executeApiCallOne(
            apiInterface.getMyOrdersOne(params/*page, expand*/),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun fetchCart(
        apiResponseListener: ApiResponseListener<ResponseCartData>,
        id: Int,
        expand: String
    ) {
        executeApiCallOne(
            apiInterface.fetchCart(id, expand),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun creditStatus(apiResponseListener: ApiResponseListener<CreditResponse>, id: Int) {
        executeApiCallOne(
            apiInterface.creditLineStatus(id),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun placeOrder(
        apiResponseListener: ApiResponseListener<JsonElement>,
        header: String,
        orderBean: UploadOrder
    ) {
        executeApiCallOne(
            apiInterface.placeOrder(header, orderBean),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getProductDetail(
        apiResponseListener: ApiResponseListener<JsonElement>,
        id: Int, schemes: String, retailer_id: String
    ) {
        executeApiCallOne(
            apiInterface.getProductDetail(id, schemes, retailer_id),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getCategoryList(

        apiResponseListener: ApiResponseListener<APICommonResponse<List<BrandsCategoryData>>>,
        data: Map<String, String>
    ) {
        executeApiCallOne(
            apiInterface.getCategoryList(data),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getProductsList(
        apiResponseListener: ApiResponseListener<APICommonResponse<List<ProductsData>>>,
        data: HashMap<String, String>
    ) {
        executeApiCallOne(
            apiInterface.getProductsList(data),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getPingDetails(
        apiResponseListener: ApiResponseListener<JsonElement>,
        data: Int?, expand: String
    ) {
        executeApiCallOne(
            apiInterface.getPingDetails(data, expand),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun addCartItems(
        apiResponseListener: ApiResponseListener<List<APICommonResponse<ProductsData>>>,
        id: Int?, request: ProductCartRequest
    ) {
        executeApiCallOne(
            apiInterface.addCartItems(id, request),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun addUsers(
        apiResponseListener: ApiResponseListener<JsonElement>,
        request: AddMembers
    ) {
        executeApiCallOne(
            apiInterface.addUsers(request),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getCoreConstants(apiResponseListener: ApiResponseListener<CoreConstantsResponse>) {
        executeApiCallOne(
            apiInterface.getCoreConstants(),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun pinCodeLookup(
        apiResponseListener: ApiResponseListener<PinCodeLookupResponse>,
        pinCode: String
    ) {
        executeApiCallOne(
            apiInterface.pinCodeLookup(pinCode),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getOrderDetails(
        apiResponseListener: ApiResponseListener<OrderDetailsResponse>,
        header: String,
        page: Int,
        query: Map<String, String>
    ) {
        executeApiCallOne(
            apiInterface.getOrderDetails(header, page, query),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun editProductItem(
        apiResponseListener: ApiResponseListener<ProductResponse>,
        header: String,
        orderId: Int,
        productId: Int,
        query: Map<String, String>
    ) {
        executeApiCallOne(
            apiInterface.editProductItem(
                header, orderId, productId, query
            ),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getConstants(apiResponseListener: ApiResponseListener<ConstantsResponse>) {
        executeApiCallOne(
            apiInterface.getConstants(),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun cancelOrder(
        apiResponseListener: ApiResponseListener<CancelOrderResponse>,
        header: String,
        page: Int,
        data: Map<String, String>
    ) {
        executeApiCallOne(
            apiInterface.cancelOrder(header, page, data),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun getCoupons(apiResponseListener: ApiResponseListener<List<CouponBean>>) {
        executeApiCallOne(
            apiInterface.getCoupons(),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun editComboProductItem(
        apiResponseListener: ApiResponseListener<List<ComboUpdateResponse>>,
        orderId: Int?,
        request: RequestComboUpdate
    ) {
        executeApiCallOne(
            apiInterface.editComboProductItem(orderId, request),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

    override fun postCartApi(
        apiResponseListener: ApiResponseListener<JsonElement>,
        orderId: Int?,
        request: RequestCouponApply
    ) {
        executeApiCallOne(
            apiInterface.postCartApi(orderId, request),
            apiResponseListener,
            Calendar.getInstance().timeInMillis
        )
    }

}