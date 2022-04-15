package com.riggle.data

import com.google.gson.JsonElement
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.request.*
import com.riggle.data.models.response.*
import com.riggle.data.network.ApiResponseListener
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.Path
import retrofit2.http.Query
import java.util.*

interface DataManager {
    fun loginPhone(
        apiResponseListener: ApiResponseListener<APICommonResponse<LoginResponse>>,
        phone: Login
    )

    fun verifyOtp(
        apiResponseListener: ApiResponseListener<APICommonResponse<UserData>>,
        otp: OTPVerification
    )

    fun editProfile(
        apiResponseListener: ApiResponseListener<APICommonResponse<UserData>>,
        storeInfo: StoreInfo
    )

    fun getMemberList(
        apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<MemberList>>>,
        retailer: String
    )

    fun removeMember(
        apiResponseListener: ApiResponseListener<APICommonResponse<String>>,
        user_id: Int
    )

    fun addMember(
        apiResponseListener: ApiResponseListener<APICommonResponse<String>>,
        member: AddMember
    )

    fun getProducts(
        apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<ProductsData>>>,
        type: String?
    )

    fun getProducts(
        apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<ProductsData>>>,
        type: String?,
        id: Int
    )

    fun getBrands(apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<BrandsCategoryData>>>)
    fun getTotalRegalCoins(apiResponseListener: ApiResponseListener<APICommonResponse<RiggleCoinsResponse>>)
    fun getCategories(apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<BrandsCategoryData>>>)
    fun getVariants(
        apiResponseListener: ApiResponseListener<APICommonResponse<ArrayList<ProductsData>>>,
        product_id: Int
    )

    fun addCart(
        apiResponseListener: ApiResponseListener<APICommonResponse<ProductsData>>,
        cartData: RequestCartData
    )

    fun editCart(
        apiResponseListener: ApiResponseListener<APICommonResponse<EditCartResponse>>,
        cartData: RequestCartData
    )

    fun getDeliverySlots(apiResponseListener: ApiResponseListener<APICommonResponse<DeliverySlots>>)
    fun addOrder(
        apiResponseListener: ApiResponseListener<APICommonResponse<String>>,
        details: OrderDetailsUpload
    )

    fun getProductDetail(
        apiResponseListener: ApiResponseListener<JsonElement>,
        id: Int, schemes: String, retailer_id: String
    )

    fun getMyOrders(
        apiResponseListener: ApiResponseListener<APICommonResponse<List<MyOrderDataOuter>>>,
        retailer_id: Int,
        expand: String
    )

    fun getOrderDetail(
        apiResponseListener: ApiResponseListener<APICommonResponse<OrderDetail>>,
        cart_id: Int
    )

    fun search(
        apiResponseListener: ApiResponseListener<APICommonResponse<SearchData>>,
        query: String
    )

    fun getUserDetails(): Flow<APICommonResponse<UserData>>
    fun updateProfile(request: UserProfileUpdateRequest): Flow<APICommonResponse<UserData>>

    fun getUserEarnings(page: Int): Flow<Earnings>

    fun getTopSearches(): Flow<APICommonResponse<TopSearches>>

    fun uploadFile(
        body: MultipartBody.Part,
        type: RequestBody
    ): Flow<APICommonResponse<FileUploadResponse>>

    fun getRegion(
        apiResponseListener: ApiResponseListener<APICommonResponse<List<RegionsBean>>>,
        data: Map<String, String>
    )

    fun updateRetailer(
        apiResponseListener: ApiResponseListener<UserData>,
        id: Int?,
        data: HashMap<String, String>
    )

    fun updateRetailerOne(
        apiResponseListener: ApiResponseListener<JsonElement>,
        id: Int?,
        data: HashMap<String, String>
    )

    fun getMyOrdersOne(
        apiResponseListener: ApiResponseListener<JsonElement>,
        page: Int,
        expand: String
    )

    fun fetchCart(
        apiResponseListener: ApiResponseListener<ResponseCartData>,
        id: Int,
        expand: String
    )
    fun creditStatus(
        apiResponseListener: ApiResponseListener<CreditResponse>,
        id: Int
    )

    fun placeOrder(
        apiResponseListener: ApiResponseListener<JsonElement>,
        orderBean: UploadOrder
    )

    fun getCategoryList(
        apiResponseListener: ApiResponseListener<APICommonResponse<List<BrandsCategoryData>>>,
        data: Map<String, String>
    )

    fun getProductsList(
        apiResponseListener: ApiResponseListener<APICommonResponse<List<ProductsData>>>,
        type: HashMap<String, String>
    )

    fun getPingDetails(
        apiResponseListener: ApiResponseListener<JsonElement>,
        data: Int?, expand: String
    )

    fun addCartItems(
        apiResponseListener: ApiResponseListener<List<APICommonResponse<ProductsData>>>,
        id: Int?, request: ProductCartRequest
    )

    fun addUsers(apiResponseListener: ApiResponseListener<JsonElement>, request: AddMembers)

}