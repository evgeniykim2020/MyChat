package ru.evgeniykim.mychat.network.api

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import ru.evgeniykim.mychat.network.model.CodeModel
import ru.evgeniykim.mychat.network.model.TelModel
import ru.evgeniykim.mychat.network.model.TelRequest
import ru.evgeniykim.mychat.network.model.TokenModel

class ApiHelperImpl(private val myApi: MyApi) : ApiHelper {
    override fun sendTel(phone: String) = flow {
        emit(myApi.sendPhone(TelRequest(phone = phone)))
    }

    override fun sendCode(codeModel: CodeModel) = flow {
        emit(myApi.sendCode(codeModel))
    }
}