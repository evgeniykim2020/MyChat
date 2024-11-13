package ru.evgeniykim.mychat.network.viewmodel

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.viewModelScope
import dagger.Binds
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.evgeniykim.mychat.network.api.ApiHelperImpl
import ru.evgeniykim.mychat.network.api.RetrofitBuilder
import ru.evgeniykim.mychat.network.contract.MainContract
import ru.evgeniykim.mychat.network.model.CodeModel
import ru.evgeniykim.mychat.ui.App
import ru.evgeniykim.mychat.ui.utils.Constants
import ru.evgeniykim.mychat.ui.utils.dataStore
import ru.evgeniykim.mychat.ui.utils.getValueFlow

@HiltViewModel(assistedFactory = MainViewModel.Factory::class)
class MainViewModel @AssistedInject constructor(
    @Assisted var code: String
): BaseViewModel<MainContract.Event, MainContract.State, MainContract.Effect>() {

    @AssistedFactory interface Factory {
        fun create(code: String): MainViewModel
    }

    companion object {
        private val PHONE = stringPreferencesKey(Constants.PHONE)
    }

    init {
        getPhone()
    }

    private val apiHelper = ApiHelperImpl(RetrofitBuilder.retrofitApiService)
    private var phoneNum = ""

    override fun createInitialState(): MainContract.State {
        return MainContract.State(
            sendDataState = MainContract.SendDataState.Idle,
            sendCodeState = MainContract.SendCodeState.Idle
        )
    }

    override fun handleEvent(event: MainContract.Event) {
        when(event) {
            is MainContract.Event.SendClicked -> {
                sendPhone(phoneNum)
                println("Send clicked")
            }
            is MainContract.Event.SendCode -> {
                sendCode(code = CodeModel(code = code, phone = phoneNum))
                println("Code is $code")
            }

            else -> {}
        }
    }

    private fun sendPhone(phone: String) {
        viewModelScope.launch {
            setState { copy(sendDataState = MainContract.SendDataState.Loading) }
            try {
                println("Sent phone $phone")
                apiHelper.sendTel(phone).collect {
                    if (it.is_success) {
                        setState { copy(sendDataState = MainContract.SendDataState.Success(success = true)) }
                    } else {
                        setEffect { MainContract.Effect.ShowError(message = "TEL NUM SUCCESS FALSE") }
                    }
                    println("Tel revert $it")
                }
            } catch (e: Exception) {
                setEffect { MainContract.Effect.ShowError(message = e.message.toString()) }
            }
        }
    }

    private fun getPhone() {
        viewModelScope.launch {
             App.applicationContext().dataStore.getValueFlow(PHONE, "")
                .collect {
                    phoneNum = it
                    println("Saved phone $it")
                }
        }
    }

    private fun sendCode(code: CodeModel) {
        viewModelScope.launch {
            setState { copy(sendCodeState = MainContract.SendCodeState.Loading) }
            try {
                println("Phone and code $code")
                apiHelper.sendCode(codeModel = code).collect{
                    setState { copy(sendCodeState = MainContract.SendCodeState.Success(tokenModel = it)) }
                }
            } catch (e: Exception) {
                setEffect { MainContract.Effect.ShowError(message = e.message.toString()) }
            }
        }
    }
}