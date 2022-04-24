package com.riggle.data.network

import com.google.gson.JsonElement
import com.riggle.data.models.APICommonResponse
import com.riggle.data.models.request.*
import com.riggle.data.models.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

interface ApiService {
    @POST(APIUrlConstants.LOGIN_PHONE)
    fun loginPhone(@Body phone: Login): Call<APICommonResponse<LoginResponse>>

    @POST(APIUrlConstants.RE_SEND_OTP)
    fun reSendOtp(@Body phone: Login): Call<APICommonResponse<LoginResponse>>

    @POST(APIUrlConstants.VERIFY_OTP)
    fun verifyOtp(@Body otp: OTPVerification): Call<APICommonResponse<UserData>>

    @POST(APIUrlConstants.EDIT_PROFILE)
    fun editProfile(@Body storeInfo: StoreInfo): Call<APICommonResponse<UserData>>

    @GET(APIUrlConstants.GET_MEMBER_LIST)
    fun getMemberList(@Query("retailer") retailer: String): Call<APICommonResponse<ArrayList<MemberList>>>

    @GET(APIUrlConstants.REMOVE_MEMBER)
    fun removeMember(@Query("user_id") id: Int): Call<APICommonResponse<String>>

    @POST(APIUrlConstants.ADD_MEMBER)
    fun addMember(@Body addMember: AddMember): Call<APICommonResponse<String>>

    @GET(APIUrlConstants.GET_PRODUCTS)
    fun getProducts(@Query("type") type: String?): Call<APICommonResponse<ArrayList<ProductsData>>>

    /*New Api*/
    @GET(APIUrlConstants.GET_PRODUCTS)
    fun getProductsList(@QueryMap type: HashMap<String, String>): Call<APICommonResponse<List<ProductsData>>>

    @GET(APIUrlConstants.GET_PRODUCTS)
    fun getProducts(
        @Query("type") type: String?,
        @Query("id") id: Int
    ): Call<APICommonResponse<ArrayList<ProductsData>>>

    @GET(APIUrlConstants.GET_BRANDS)
    fun getBrands(): Call<APICommonResponse<ArrayList<BrandsCategoryData>>>

    @GET(APIUrlConstants.GET_TOTAL_RIGGLE_COINS)
    fun getTotalRiggleCoins(): Call<APICommonResponse<RiggleCoinsResponse>>

    @GET(APIUrlConstants.GET_CATEGORIES)
    fun getCategories(): Call<APICommonResponse<ArrayList<BrandsCategoryData>>>

    @GET(APIUrlConstants.GET_VARIANTS)
    fun getVariants(@Query("product_id") id: Int): Call<APICommonResponse<ArrayList<ProductsData>>>

    @POST(APIUrlConstants.ADD_CARTS)
    fun addCart(@Body data: RequestCartData): Call<APICommonResponse<ProductsData>>

    @GET(APIUrlConstants.FETCH_CART)
    fun fetchCart(@Path("id") id: Int, @Query("expand") expand: String): Call<ResponseCartData>

    @GET(APIUrlConstants.creditLineStatus)
    fun creditLineStatus(
        @Path("id") id: Int
    ): Call<CreditResponse>

    @POST(APIUrlConstants.EDIT_CART)
    fun editCart(@Body cartData: RequestCartData): Call<APICommonResponse<EditCartResponse>>

    @GET(APIUrlConstants.DELIVERY_SLOTS)
    fun getDeliverySlots(): Call<APICommonResponse<DeliverySlots>>

    @POST(APIUrlConstants.ADD_ORDER)
    fun addOrder(@Body details: OrderDetailsUpload): Call<APICommonResponse<String>>

    @GET(APIUrlConstants.PRODUCT_DETAIL)
    fun getProductDetail(
        @Path("id") id: Int,
        @Query("expand") schemes: String,
        @Query("retailer") retailer_id: String
    ): Call<JsonElement>

    @GET(APIUrlConstants.MY_ORDERS)
    fun getMyOrders(
        @Query("retailer") page: Int,
        @Query("expand") expand: String
    ): Call<APICommonResponse<List<MyOrderDataOuter>>>

    @GET(APIUrlConstants.ORDER_DETAIL)
    fun getOrderDetail(@Query("cart_id") cart_id: Int): Call<APICommonResponse<OrderDetail>>

    @GET(APIUrlConstants.SEARCH)
    fun search(@Query("q") query: String): Call<APICommonResponse<SearchData>>

    @GET(APIUrlConstants.userDetails)
    suspend fun getUserDetails(): APICommonResponse<UserData>

    @POST(APIUrlConstants.EDIT_PROFILE)
    suspend fun updateProfile(@Body storeInfo: UserProfileUpdateRequest): APICommonResponse<UserData>

    @GET(APIUrlConstants.myEarnings)
    suspend fun getUserEarnings(@Path("id") page: Int): Earnings

    @GET(APIUrlConstants.topSearches)
    suspend fun getTopSearches(): APICommonResponse<TopSearches>

    @Multipart
    @POST(APIUrlConstants.uploadFile)
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("type") type: RequestBody
    ): APICommonResponse<FileUploadResponse>

    /* New Api Integration */

    @GET(APIUrlConstants.getRegion)
    fun getRegion(@QueryMap data: Map<String, String>): Call<APICommonResponse<List<RegionsBean>>>

    @FormUrlEncoded
    @PATCH(APIUrlConstants.updateRetails)
    fun updateRetailer(
        @Path("id") id: Int?,
        @FieldMap data: HashMap<String, String>
    ): Call<UserData>

    @POST(APIUrlConstants.placeOrder)
    fun placeOrder(
        @Body orderBean: UploadOrder
    ): Call<JsonElement>


    @GET(APIUrlConstants.getCategoryList)
    fun getCategoryList(@QueryMap data: Map<String, String>): Call<APICommonResponse<List<BrandsCategoryData>>>

    @GET(APIUrlConstants.pingDetails)
    fun getPingDetails(@Path("id") data: Int?, @Query("expand") expand: String): Call<JsonElement>

    @POST(APIUrlConstants.addToCart)
    fun addCartItems(
        @Path("id") id: Int?,
        @Body request: ProductCartRequest
    ): Call<List<APICommonResponse<ProductsData>>>

    @FormUrlEncoded
    @PATCH(APIUrlConstants.updateRetails)
    fun updateRetailerOne(
        @Path("id") id: Int?,
        @FieldMap data: HashMap<String, String>,
    ): Call<JsonElement>

    @Multipart
    @PATCH(APIUrlConstants.updateRetails)
    fun updateRetailerDetails(
        @Path("id") id: Int?,
        @PartMap data: HashMap<String, RequestBody>,
        @Part file: MultipartBody.Part
    ): Call<JsonElement>

    @GET(APIUrlConstants.MY_ORDERS)
    fun getMyOrdersOne(
        @Query("retailer") page: Int,
        @Query("expand") expand: String
    ): Call<JsonElement>

    @POST(APIUrlConstants.addUser)
    fun addUsers(@Body request: AddMembers): Call<JsonElement>

    @GET(APIUrlConstants.coreConstants)
    fun getCoreConstants(): Call<CoreConstantsResponse>

    @GET(APIUrlConstants.pinLookup)
    fun pinCodeLookup(@Query("code") expand: String): Call<PinCodeLookupResponse>


}