package com.riggle.data.models.response

data class MemberListOuter(val member_count: Int, val data: ArrayList<MemberList>)

class MemberList {
    val id: Int? = null
    val mobile: String? = null
    val user_type: String? = null
}
