package com.okashitech.githubkotlinlist.presentation.details

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WebNavArgs(
    val title: String,
    val webUrl: String
) : Parcelable