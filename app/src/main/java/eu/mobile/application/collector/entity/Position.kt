package eu.mobile.application.collector.entity

import android.os.Parcel
import android.os.Parcelable
import android.graphics.Bitmap
import java.util.Date

class Position() : Parcelable {
    var Id: Int? = null
    var name: String? = null
    var categoryId: Int? = null
    var imagePath: String? = null
    var description: String? = null
    var total: Int? = null
    var addDate: String? = null
    var updateDate: String? = null
    var producent: String? = null
    var price: Int? = null
    var condition: String? = null
    var serial: String? = null
    var origin: String? = null
    var notes: String? = null
    var imageBitMap: Bitmap? = null


    constructor(parcel: Parcel) : this() {
        Id = parcel.readInt()
        name = parcel.readString()
        categoryId = parcel.readInt()
        imagePath = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        Id?.let { p0.writeInt(Id!!) }
        name?.let { p0.writeString(name!!) }
        categoryId?.let { p0.writeInt(categoryId!!) }
        imagePath?.let { p0.writeString(imagePath!!) }
    }

    companion object CREATOR : Parcelable.Creator<Position> {
        override fun createFromParcel(parcel: Parcel): Position {
            return Position(parcel)
        }

        override fun newArray(size: Int): Array<Position?> {
            return arrayOfNulls(size)
        }
    }
}