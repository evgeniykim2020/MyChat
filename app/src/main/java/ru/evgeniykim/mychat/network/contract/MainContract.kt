package ru.evgeniykim.mychat.network.contract

import ru.evgeniykim.mychat.network.interfaces.UiEffect
import ru.evgeniykim.mychat.network.interfaces.UiEvent
import ru.evgeniykim.mychat.network.interfaces.UiState
import ru.evgeniykim.mychat.network.model.CodeModel
import ru.evgeniykim.mychat.network.model.TokenModel

class MainContract {

    // User Events
    sealed class Event : UiEvent {
        object SendClicked : Event()
        object SendCode : Event()
    }

    data class State(
        val sendDataState: SendDataState,
        val sendCodeState: SendCodeState
    ) : UiState


    sealed class SendDataState {
        object Idle : SendDataState()
        object Loading : SendDataState()
        data class Success(val success: Boolean) : SendDataState()
    }

    sealed class SendCodeState {
        object Idle : SendCodeState()
        object Loading : SendCodeState()
        data class Success(val tokenModel: TokenModel) : SendCodeState()
    }

    // Errors
    sealed class Effect : UiEffect {
        data class ShowError(val message: String) : Effect()
    }
}