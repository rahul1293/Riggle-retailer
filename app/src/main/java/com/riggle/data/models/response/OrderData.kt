package com.riggle.data.models.response

import com.google.gson.annotations.SerializedName

data class MyOrderDataOuter(
    /*val total_count: Int,
    val orders: ArrayList<MyOrderData>*/
    @SerializedName("id") var id: Int? = null,
    @SerializedName("update_url") var updateUrl: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("doc_id") var docId: String? = null,
    @SerializedName("delivery_date") var deliveryDate: String? = null,
    @SerializedName("final_amount") var finalAmount: Int? = null,
    @SerializedName("amount") var amount: Float? = null,
    @SerializedName("riggle_coins") var riggleCoins: Int? = null,
    @SerializedName("redeemed_riggle_coins") var redeemedRiggleCoins: Int? = null,
    @SerializedName("paid_amount") var paidAmount: Float? = null,
    @SerializedName("pending_amount") var pendingAmount: Float? = null,
    @SerializedName("payment_mode") var paymentMode: String? = null,
    @SerializedName("challan_file") var challanFile: String? = null,
    @SerializedName("cancellation_reason") var cancellationReason: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("placed_at") var placedAt: String? = null,
    @SerializedName("delivered_at") var deliveredAt: String? = null,
    @SerializedName("cancelled_at") var cancelledAt: String? = null,
    @SerializedName("confirmed_at") var confirmedAt: String? = null,
    @SerializedName("retailer") var retailer: Int? = null,
    @SerializedName("service_hub") var serviceHub: ServiceHub?/*Int?*/ = null,
    @SerializedName("products") var products: ArrayList<Products> = arrayListOf()

)

data class MyOrderData(
    val cart_id: Int,
    val order_id: String,
    val status: String,
    @SerializedName("delivery_date")
    val deliveryDate: String,
    @SerializedName("order_date")
    val orderDate: String,
    val count: String,
    val products: ArrayList<OrderProduct>
)

data class OrderProduct(
    val id: Int,
    val name: String,
    val image: String,
    @SerializedName("product_size")
    val productSize: String,

    val quantity: Int,
    val display_price: Int
)

data class OrderDetail(
    val can_edit_slot: Boolean,
    val order_id: String,
    val tracking_status: String,
    val delivery_date: String,
    val order_products: ArrayList<OrderProduct>,
    val price_detail: OrderPriceDetails
)

data class OrderPriceDetails(
    val id: Int,
    val total_price: String,
    val total_discount: String,
    val riggle_coins_discount: String,
    val final_amount: String,
    val payment_mode: String
)

data class Products(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("update_url") var updateUrl: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("ordered_quantity") var orderedQuantity: Int? = null,
    @SerializedName("quantity") var quantity: Int? = null,
    @SerializedName("rate") var rate: Double? = null,
    @SerializedName("original_rate") var originalRate: Double? = null,
    @SerializedName("riggle_coins") var riggleCoins: Int? = null,
    @SerializedName("amount") var amount: Float? = null,
    @SerializedName("free_product_quantity") var freeProductQuantity: Int? = null,
    @SerializedName("order") var order: Int? = null,
    @SerializedName("product") var product: Product? = Product(),
    @SerializedName("product_combo") var productCombo: String? = null,
    @SerializedName("free_product") var freeProduct: FreeProduct? = FreeProduct()
)

data class Product(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("update_url") var updateUrl: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("is_active") var isActive: Boolean? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("base_unit") var baseUnit: String? = null,
    @SerializedName("base_rate") var baseRate: String? = null,
    @SerializedName("base_quantity") var baseQuantity: Int? = null,
    @SerializedName("company_step") var companyStep: Int? = null,
    @SerializedName("company_rate") var companyRate: Float? = null,
    @SerializedName("retailer_step") var retailerStep: Int? = null,
    @SerializedName("expiry_in_days") var expiryInDays: Int? = null,
    @SerializedName("riggle_coins") var riggleCoins: Int? = null,
    @SerializedName("brand") var brand: Int? = null,
    @SerializedName("category") var category: Int? = null,
    @SerializedName("banner_image") var banner_image: BannerImage? = null,
    @SerializedName("inactive_regions") var inactiveRegions: ArrayList<String> = arrayListOf()
)

data class FreeProduct(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("update_url") var updateUrl: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("is_active") var isActive: Boolean? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("base_unit") var baseUnit: String? = null,
    @SerializedName("base_rate") var baseRate: String? = null,
    @SerializedName("base_quantity") var baseQuantity: Int? = null,
    @SerializedName("company_step") var companyStep: Int? = null,
    @SerializedName("company_rate") var companyRate: Float? = null,
    @SerializedName("retailer_step") var retailerStep: Int? = null,
    @SerializedName("expiry_in_days") var expiryInDays: Int? = null,
    @SerializedName("riggle_coins") var riggleCoins: Int? = null,
    @SerializedName("brand") var brand: Any? = null,
    @SerializedName("free_quantity") var free_quantity: Int? = null,
    @SerializedName("category") var category: String? = null,
    @SerializedName("inactive_regions") var inactiveRegions: ArrayList<String> = arrayListOf(),
    @SerializedName("service_hub") var service_hub: ServiceHub? = null
)

data class ServiceHub(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("update_url") var updateUrl: String? = null,
    @SerializedName("account_balance") var accountBalance: Int? = null,
    @SerializedName("riggle_coins_balance") var riggleCoinsBalance: Int? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("is_deleted") var isDeleted: Boolean? = null,
    @SerializedName("deleted_at") var deletedAt: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("warehouse_address") var warehouseAddress: String? = null,
    @SerializedName("gst_number") var gstNumber: String? = null,
    @SerializedName("fssai_number") var fssaiNumber: String? = null,
    @SerializedName("is_active") var isActive: Boolean? = null,
    @SerializedName("is_misc") var isMisc: Boolean? = null,
    @SerializedName("area") var area: Int? = null,
    @SerializedName("city") var city: String? = null,
    @SerializedName("brands") var brands: ArrayList<Int> = arrayListOf()
)

data class ActiveOrderData(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("update_url") var updateUrl: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("doc_id") var docId: String? = null,
    @SerializedName("delivery_date") var deliveryDate: String? = null,
    @SerializedName("final_amount") var finalAmount: Float? = null,
    @SerializedName("amount") var amount: Float? = null,
    @SerializedName("riggle_coins") var riggleCoins: Int? = null,
    @SerializedName("redeemed_riggle_coins") var redeemedRiggleCoins: Int? = null,
    @SerializedName("paid_amount") var paidAmount: Float? = null,
    @SerializedName("pending_amount") var pendingAmount: Float? = null,
    @SerializedName("payment_mode") var paymentMode: String? = null,
    @SerializedName("challan_file") var challanFile: String? = null,
    @SerializedName("cancellation_reason") var cancellationReason: String? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("placed_at") var placedAt: String? = null,
    @SerializedName("delivered_at") var deliveredAt: String? = null,
    @SerializedName("cancelled_at") var cancelledAt: String? = null,
    @SerializedName("confirmed_at") var confirmedAt: String? = null,
    @SerializedName("retailer") var retailer: Int? = null,
    @SerializedName("service_hub") var serviceHub: ServiceHub? = null,
    @SerializedName("products") var products: ArrayList<Products> = arrayListOf()

)

