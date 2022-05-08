package com.riggle.data.models.response

import com.google.gson.annotations.SerializedName
import kotlin.collections.ArrayList

data class ProductsData(
    val brand_id: Int = 0,
    /*val code: String,*/
    /*val name: String,*/
    @SerializedName("primary_unit") val primaryUnit: String,
    @SerializedName("primary_unit_dimension") val primaryUnitDimension: String,
    @SerializedName("secondary_unit") val secondaryUnit: String,
    @SerializedName("pack_size") val packSize: Int,
    @SerializedName("is_custom") val isCustom: String,
    @SerializedName("unit_mrp") val unitMrp: Int,
    @SerializedName("retailer_unit_price") val retailerUnitPrice: Double,
    @SerializedName("expiry_in_days") val expiryInDays: Int,
    @SerializedName("is_default") val isDefault: String,
    val status: String,
    @SerializedName("is_deleted") val isDeleted: Int,
    val created: String,
    val modified: String,
    @SerializedName("created_by") val createdBy: Any? = null,
    @SerializedName("is_trending") val isTrending: Boolean? = null,
    /*val id: Int,*/
    @SerializedName("product_detail") val productDetail: String? = null,
    val image: String,
    @SerializedName("product_size") val productSize: String,
    /*val moq: Int,*/
    @SerializedName("pack_mrp") val packMrp: Int,
    @SerializedName("retailer_price") val retailer_price: Int,
    @SerializedName("sibling_id") val siblingID: Int,
    @SerializedName("sibling_info") val siblingInfo: String,
    @SerializedName("badge_name") val badgeName: String,
    @SerializedName("badge_color") val badgeColor: String,
    @SerializedName("badge_text_color") val badgeTextColor: String,
    /*val description: String? = null,*/
    @SerializedName("strike_price") val strikePrice: Int? = null,
    val profit: Int? = null,
    val discount: Int? = null,
    /*val riggle_coins: Int? = null,*/
    @SerializedName("display_price") var displayPrice: Int? = null,
    var item_cart: Int? = null,
    val is_variant_key: Boolean? = null,
    @SerializedName(value = "units", alternate = ["products"])
    val units: ArrayList<ProductsData>? = null,
    val variants: ArrayList<Variants>? = null,
    val image_arr: ArrayList<String>? = null,

/* new data*/
    val id: Int,
    val update_url: String,
    val created_at: String,
    val updated_at: String,
    val code: String,
    val name: String,
    val is_active: Boolean,
    val description: String,
    val base_unit: String,
    val base_rate: String,
    val base_quantity: String,
    val company_step: Int,
    val company_rate: Float,
    //val moq: Float,
    @SerializedName("retailer_step")
    var moq: Int,
    var step: Int,
    /*val expiry_in_days: Int,*/
    val riggle_coins: Int,
    val free_product_quantity: Int,
    @SerializedName("brand")
    val brand: Any,
    val category: Int,
    val schemes: ArrayList<SchemesBean>,
    //for cart
    val product: ProductsData?,
    var quantity: Int = 0,
    val amount: Float,
    var rate: Float,
    var margin: Float,
    var banner_image: BannerImage,
    var images: ArrayList<BannerImage>,
    var combo_products: ArrayList<ComboProducts>,
    var free_product: FreeProduct,
    var combo_count: Int = 0,
    var delivery_tat_days: Int = 0,
    @SerializedName("service_hub") var service_hub: ServiceHub? = null

    /*val inactive_regions:List<Objects>*/
/*{
    "id": 4,
    "update_url": "https://api.riggleapp.in/api/v1/core/products/4/",
    "created_at": "2021-11-25T12:21:02.943776",
    "updated_at": "2021-12-01T15:32:09.239601",
    "code": "RPRO4",
    "name": "Ala Fresh Litchi 150 ml",
    "is_active": true,
    "description": "Refreshing range of Fruit Juices.\n\nProduct of Thailand.",
    "base_unit": "ml",
    "base_rate": "10",
    "base_quantity": "150",
    "company_step": 96,
    "company_rate": 7.4,
    "retailer_step": 24,
    "moq": 96.0,
    "expiry_in_days": 365,
    "riggle_coins": 0,
    "brand": 3,
    "category": 3,
    "inactive_regions": []
}*/
)

data class SchemesBean(
    val id: Int,
    val update_url: String,
    val created_at: String,
    val updated_at: String,
    val min_quantity: Int,
    val max_quantity: Int,
    val rate: Float,
    val free_quantity: Int,
    val is_active: Boolean,
    val product: Int,
    val free_product: FreeProduct
)

data class Variants(
    val id: Int,
    val image: String
)

data class BannerImage(
    val id: Int,
    val update_url: String,
    val created_at: String,
    val updated_at: String,
    val doc_id: String,
    val image: String,
    val is_banner: Boolean,
    val product: Int
)

data class ComboProducts(
    /*val id: Int,
    val update_url: String,
    val created_at: String,
    val updated_at: String,
    val code: String,
    val name: String,
    val is_active: Boolean,
    val step: Int,
    val products: ArrayList<ProductsData>*/
    val code: String,
    val created_at: String,
    val id: Int,
    val is_active: Boolean,
    val name: String,
    val products: List<ProductsData>,
    val step: Int,
    val update_url: String,
    val updated_at: String
)

data class SearchData(val products: ArrayList<ProductsData>)

data class BrandResponse(
    val code: String,
    val company: Int,
    val created_at: String,
    val doc_id: String,
    val id: Int,
    val image: String,
    val is_active: Boolean,
    val name: String,
    val update_url: String,
    val updated_at: String
)

