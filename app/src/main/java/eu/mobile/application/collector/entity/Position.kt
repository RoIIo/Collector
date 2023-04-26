package eu.mobile.application.collector.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep

class Position() : Parcelable {
    var Id: Int? = null
    var name: String? = null

    constructor(parcel: Parcel) : this() {
        Id = parcel.readValue(Int::class.java.classLoader) as? Int
        name = parcel.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(p0: Parcel, p1: Int) {
        Id?.let { p0.writeInt(Id!!) }
        name?.let { p0.writeString(name) }
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