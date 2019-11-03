package com.rolandoamarillo.addi.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * Model representing a prospect
 */
@Entity(tableName = "prospect", primaryKeys = ["doc_type", "doc_number"])
data class Prospect(

    /**
     * Document type
     */
    @ColumnInfo(name = "doc_type") val docType: String,

    /**
     * Document number
     */
    @ColumnInfo(name = "doc_number") val docNumber: String,

    /**
     * Document expedition date
     */
    @ColumnInfo(name = "doc_expedition") val docExpedition: String?,

    /**
     * Name
     */
    @ColumnInfo(name = "name") val name: String?,

    /**
     * Last name
     */
    @ColumnInfo(name = "lastname") val lastname: String?,

    /**
     * Phone
     */
    @ColumnInfo(name = "phone") val phone: String?,

    /**
     * Email
     */
    @ColumnInfo(name = "email") val email: String?,

    /**
     * Determines if prospect is a contact or not
     */
    @ColumnInfo(name = "contact") var contact: Boolean = false

) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(docType)
        parcel.writeString(docNumber)
        parcel.writeString(docExpedition)
        parcel.writeString(name)
        parcel.writeString(lastname)
        parcel.writeString(phone)
        parcel.writeString(email)
        parcel.writeByte(if (contact) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Prospect> {
        override fun createFromParcel(parcel: Parcel): Prospect {
            return Prospect(parcel)
        }

        override fun newArray(size: Int): Array<Prospect?> {
            return arrayOfNulls(size)
        }
    }
}