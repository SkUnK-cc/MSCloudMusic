package com.example.hp.mycloudmusic.userinfo

import android.os.Parcel
import android.os.Parcelable

data class LoginInfo(
        var code: Int,
        var msg: String,
        var data: User
) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readParcelable<User>(User::class.java.classLoader)
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(code)
        writeString(msg)
        writeParcelable(data, 0)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<LoginInfo> = object : Parcelable.Creator<LoginInfo> {
            override fun createFromParcel(source: Parcel): LoginInfo = LoginInfo(source)
            override fun newArray(size: Int): Array<LoginInfo?> = arrayOfNulls(size)
        }
    }
}