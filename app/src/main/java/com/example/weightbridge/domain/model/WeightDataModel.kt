package com.example.weightbridge.domain.model

import android.os.Parcel
import android.os.Parcelable

data class WeightDataModel(
    var ticketID: String = System.currentTimeMillis().toString(),
    val date: String = "",
    val driverName: String = "",
    val inboundWeight: Int = 0,
    val netWeight: Int = 0,
    val outboundWeight: Int = 0,
    val licenseNumber: String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        ticketID = parcel.readString() ?: "",
        date = parcel.readString() ?: "",
        driverName = parcel.readString() ?: "",
        inboundWeight = parcel.readInt(),
        outboundWeight = parcel.readInt(),
        netWeight = parcel.readInt(),
        licenseNumber = parcel.readString() ?: ""
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flag: Int) {
        parcel.writeString(ticketID)
        parcel.writeString(date)
        parcel.writeString(driverName)
        parcel.writeInt(inboundWeight)
        parcel.writeInt(outboundWeight)
        parcel.writeInt(netWeight)
        parcel.writeString(licenseNumber)
    }

    companion object CREATOR : Parcelable.Creator<WeightDataModel> {
        override fun createFromParcel(parcel: Parcel): WeightDataModel {
            return WeightDataModel(parcel)
        }

        override fun newArray(size: Int): Array<WeightDataModel?> {
            return arrayOfNulls(size)
        }
    }
}