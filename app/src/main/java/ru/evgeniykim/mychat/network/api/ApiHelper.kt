package ru.evgeniykim.mychat.network.api

import kotlinx.coroutines.flow.Flow
import ru.evgeniykim.mychat.network.model.CodeModel
import ru.evgeniykim.mychat.network.model.TelModel
import ru.evgeniykim.mychat.network.model.TokenModel

interface ApiHelper {
    fun sendTel(phone: String): Flow<TelModel>
    fun sendCode(codeModel: CodeModel): Flow<TokenModel>
}