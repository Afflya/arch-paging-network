package com.afflyas.fwcwallpapers.repository

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

/**
 *
 * Object that contains general information about image provided by Pixabay api
 *
 * Implemented Parcelable to be able to put objects into Bundle and pass it
 * as argument when navigating to ImageFragment
 *
 */
class PixabayImage : Parcelable{

    @SerializedName("id") val id: Int

    @SerializedName("largeImageURL") val largeImageURL: String?

    @SerializedName("webformatURL") val webformatURL: String?


    constructor(parcel: Parcel) {
        id = parcel.readInt()
        largeImageURL = parcel.readString()
        webformatURL = parcel.readString()
    }

    constructor(id: Int, largeImageURL: String, webformatURL: String) {
        this.id = id
        this.largeImageURL = largeImageURL
        this.webformatURL = webformatURL
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(largeImageURL)
        parcel.writeString(webformatURL)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PixabayImage> {
        override fun createFromParcel(parcel: Parcel): PixabayImage {
            return PixabayImage(parcel)
        }

        override fun newArray(size: Int): Array<PixabayImage?> {
            return arrayOfNulls(size)
        }
    }

}