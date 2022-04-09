package com.riggle.data.models.response

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.versionedparcelable.VersionedParcelize
import com.google.android.gms.common.internal.safeparcel.SafeParcelReader.readByte

@VersionedParcelize
data class BrandsCategoryData(
    val id: Int,
    val image: String?,
    val name: String?,
    val product_count: Int,
    val description: String?,

    val code: String?,
    val doc_id: String?,
    /*val is_active: Boolean?,*/
    val type: String?,
    val belongs: String?,
    val update_url: String?


) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",

        parcel.readString() ?: "",
        parcel.readString() ?: "",
        /*parcel.readByte() ?: 0, */
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(image)
        parcel.writeString(name)
        parcel.writeInt(product_count)
        parcel.writeString(description)

        parcel.writeString(code)
        parcel.writeString(doc_id)
        /*parcel.writeBoolean(is_active ?: false)*/
        parcel.writeString(type)
        parcel.writeString(belongs)
        parcel.writeString(update_url)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BrandsCategoryData> {
        override fun createFromParcel(parcel: Parcel): BrandsCategoryData {
            return BrandsCategoryData(parcel)
        }

        override fun newArray(size: Int): Array<BrandsCategoryData?> {
            return arrayOfNulls(size)
        }
    }
}