package com.riggle.data.models.request

import android.os.Parcel
import android.os.Parcelable
import androidx.versionedparcelable.VersionedParcelize

@VersionedParcelize
data class OrderDetailsUpload(val riggle_coins: Boolean,
                              val date: String,
                              val time_slot: String,
                              val final_amount: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readByte() != 0.toByte(),
            parcel.readString() ?: "",
            parcel.readString() ?: "",
            parcel.readString() ?: "") {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeByte(if (riggle_coins) 1 else 0)
        parcel.writeString(date)
        parcel.writeString(time_slot)
        parcel.writeString(final_amount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetailsUpload> {
        override fun createFromParcel(parcel: Parcel): OrderDetailsUpload {
            return OrderDetailsUpload(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetailsUpload?> {
            return arrayOfNulls(size)
        }
    }
}