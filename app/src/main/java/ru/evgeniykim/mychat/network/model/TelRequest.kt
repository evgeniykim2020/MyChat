package ru.evgeniykim.mychat.network.model

import com.google.gson.annotations.SerializedName

data class TelRequest(
    @SerializedName("phone")
    val phone: String
)
