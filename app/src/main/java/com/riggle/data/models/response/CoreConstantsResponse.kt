package com.riggle.data.models.response

data class CoreConstantsResponse(
    val retailer_proof_document_types: List<RetailerProofDocumentType>,
    val user_roles: List<UserRole>
)

data class RetailerProofDocumentType(
    val name: String,
    val value: String
)

data class UserRole(
    val name: String,
    val value: String
)

data class ConstantsResponse(
    val base_units: List<BaseUnit>,
    val category_types: List<CategoryType>,
    val dispatch_plan_statuses: List<DispatchPlanStatuse>,
    val order_cancellation_reasons: List<OrderCancellationReason>,
    val order_statuses: List<OrderStatuse>,
    val pack_units: List<PackUnit>,
    val payment_modes: List<PaymentMode>,
    val payment_reschedule_reasons: List<PaymentRescheduleReason>,
    val payment_statuses: List<PaymentStatuse>,
    val purchase_order_statuses: List<PurchaseOrderStatuse>,
    val region_types: List<RegionType>
)

data class BaseUnit(
    val name: String,
    val value: String
)

data class CategoryType(
    val name: String,
    val value: String
)

data class DispatchPlanStatuse(
    val name: String,
    val value: String
)

data class OrderCancellationReason(
    val name: String,
    val value: String,
    var check: Boolean = false
)

data class OrderStatuse(
    val name: String,
    val value: String
)

data class PackUnit(
    val name: String,
    val value: String
)

data class PaymentMode(
    val name: String,
    val value: String
)

data class PaymentRescheduleReason(
    val name: String,
    val value: String
)

data class PaymentStatuse(
    val name: String,
    val value: String
)

data class PurchaseOrderStatuse(
    val name: String,
    val value: String
)

data class RegionType(
    val name: String,
    val value: String
)