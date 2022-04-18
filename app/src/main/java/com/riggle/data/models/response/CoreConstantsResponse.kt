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