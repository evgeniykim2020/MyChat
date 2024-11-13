package ru.evgeniykim.mychat.network.api

import android.provider.ContactsContract.CommonDataKinds.Phone
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST
import retrofit2.http.Query
import ru.evgeniykim.mychat.network.model.CodeModel
import ru.evgeniykim.mychat.network.model.TelModel
import ru.evgeniykim.mychat.network.model.TelRequest
import ru.evgeniykim.mychat.network.model.TokenModel

interface MyApi {
    @POST("api/v1/users/send-auth-code/")
    suspend fun sendPhone(@Body phone: TelRequest): TelModel

    @POST("api/v1/users/check-auth-code/")
    suspend fun sendCode(@Body code: CodeModel): TokenModel
}