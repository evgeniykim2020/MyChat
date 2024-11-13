package ru.evgeniykim.mychat.network.model

data class TokenModel(
    val access_token: String,
    val is_user_exists: Boolean,
    val refresh_token: String,
    val user_id: Int
)