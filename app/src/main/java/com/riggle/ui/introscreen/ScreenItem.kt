package com.riggle.ui.introscreen

import android.os.Parcel
import android.os.Parcelable

class ScreenItem(var title: Int, var description: Int, var screenImg: Int) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(title)
        parcel.writeInt(description)
        parcel.writeInt(screenImg)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ScreenItem> {
        override fun createFromParcel(parcel: Parcel): ScreenItem {
            return ScreenItem(parcel)
        }

        override fun newArray(size: Int): Array<ScreenItem?> {
            return arrayOfNulls(size)
        }
    }
}