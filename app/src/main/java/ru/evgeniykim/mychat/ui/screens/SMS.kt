package ru.evgeniykim.mychat.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.HasDefaultViewModelProviderFactory
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import ru.evgeniykim.mychat.R
import ru.evgeniykim.mychat.Routes
import ru.evgeniykim.mychat.network.contract.MainContract
import ru.evgeniykim.mychat.network.viewmodel.MainViewModel
import ru.evgeniykim.mychat.ui.App
import ru.evgeniykim.mychat.ui.utils.Constants
import ru.evgeniykim.mychat.ui.utils.GradientButton
import ru.evgeniykim.mychat.ui.utils.SMSMask
import ru.evgeniykim.mychat.ui.utils.dataStore
import ru.evgeniykim.mychat.ui.utils.setValue

@Composable
fun SMS(navController: NavController) {
    val keyboardController = LocalSoftwareKeyboardController.current
    var text by rememberSaveable { mutableStateOf("") }
    val mask = SMSMask("_ _ _ - _ _ _")
    var loading by remember { mutableStateOf(false) }

//    val viewModel = hiltViewModel<MainViewModel, MainViewModel.Factory>(
//        creationCallback = { factory ->
//            factory.create(text)
//
//        })

    var viewModel = hiltViewModel<MainViewModel>()


//    val creationCallback: (MainViewModel.Factory) -> MainViewModel = {
//        factory -> factory.create(text)
//    }

//    val viewModel = viewModel<MainViewModel>(
//        extras = requireNotNull(LocalViewModelStoreOwner.current).run {
//            if (this is HasDefaultViewModelProviderFactory) {
//                this.defaultViewModelCreationExtras.withCreationCallback(creationCallback)
//            } else {
//                CreationExtras.Empty.withCreationCallback(creationCallback)
//            }
//        }
//    )


    val scope = rememberCoroutineScope()

    fun sendCode() {

        val accessToken = stringPreferencesKey(Constants.ACCESS_TOKEN)
        val refreshToken = stringPreferencesKey(Constants.REFRESH_TOKEN)
        val userId = intPreferencesKey(Constants.USER_ID)

        viewModel.setEvent(MainContract.Event.SendCode)

        scope.launch {
            viewModel.uiState.collect {
                when(it.sendCodeState) {
                    is MainContract.SendCodeState.Idle -> loading = false
                    is MainContract.SendCodeState.Loading -> loading = true
                    is MainContract.SendCodeState.Success -> {
                        loading = false
                        App.applicationContext().dataStore.setValue(accessToken,
                            it.sendCodeState.tokenModel.access_token.toString()
                        )
                        App.applicationContext().dataStore.setValue(refreshToken,
                            it.sendCodeState.tokenModel.refresh_token
                        )
                        App.applicationContext().dataStore.setValue(userId,
                            it.sendCodeState.tokenModel.user_id
                        )
                        navController.navigate(Routes.Chat.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                }
            }
        }

        scope.launch {
            viewModel.effect.collect {
                when(it) {
                    is MainContract.Effect.ShowError -> {
                        loading = false
                        println("Code error ${MainContract.Effect.ShowError(message = it.message)}")
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {

        OutlinedTextField(
            value = text,
            onValueChange = {
                if (it.length <= 6) {
                    text = it
                    viewModel.code = it
                }
                if (it.length == 6) keyboardController?.hide()
                println("SMS size ${it.length}")
            },
            visualTransformation = mask,
            shape = RoundedCornerShape(topEnd = 12.dp, bottomStart = 12.dp),
            label = {
                Text(
                    text = "CMC",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelMedium,
                    fontFamily = FontFamily(Font(R.font.roboto))
                )
            },
            placeholder = {
                Text(
                    text = "_ _ _ - _ _ _",
                    fontFamily = FontFamily(Font(R.font.roboto))
                )
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            ),
            colors = TextFieldDefaults.colors(
                focusedPlaceholderColor = MaterialTheme.colorScheme.primary,
                unfocusedPlaceholderColor = MaterialTheme.colorScheme.primary
            ),
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth(),
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide()
                    // possible to add some action

                }
            ),
        )
        Spacer(modifier = Modifier.padding(20.dp))
        GradientButton(
            nameButton = "OK",
            roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
            modifier = Modifier.padding(16.dp),
            navController = navController,
            routes = Routes.SMS.route,
            onClick = {
                sendCode()
            }
        )

    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        // Loader
        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.width(64.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
    }
}