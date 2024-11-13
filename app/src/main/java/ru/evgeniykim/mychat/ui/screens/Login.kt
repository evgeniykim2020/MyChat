package ru.evgeniykim.mychat.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import ru.evgeniykim.mychat.R
import ru.evgeniykim.mychat.Routes
import ru.evgeniykim.mychat.network.api.ApiHelperImpl
import ru.evgeniykim.mychat.network.api.RetrofitBuilder
import ru.evgeniykim.mychat.network.contract.MainContract
import ru.evgeniykim.mychat.network.viewmodel.MainViewModel
import ru.evgeniykim.mychat.ui.utils.GradientButton
import ru.evgeniykim.mychat.ui.utils.LoginText
import ru.evgeniykim.mychat.ui.utils.PasswordText
import ru.evgeniykim.mychat.ui.utils.PhoneText

@Composable
fun Login(navController: NavController, viewModel: MainViewModel){

    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }

    fun readDataResponse() {
        viewModel.setEvent(MainContract.Event.SendClicked)

        scope.launch {
            viewModel.uiState.collect {
                when(it.sendDataState) {
                    is MainContract.SendDataState.Idle -> { loading = false }
                    is MainContract.SendDataState.Loading -> { loading = true }
                    is MainContract.SendDataState.Success -> {
                        loading = false
                        navController.navigate(Routes.SMS.route) {
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
                        println("Tel error ${MainContract.Effect.ShowError(message = it.message)}")
                    }
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(
                color = Color.Transparent
            )
    ) {
        Box(
           modifier = Modifier
               .align(Alignment.Center)
        ){

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                
            //text
                Text(
                    text = "Получить СМС",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily(Font(R.font.roboto))
                )

                // login text
                Spacer(modifier = Modifier.height(8.dp))
                PhoneText("Телефон")
                Spacer(modifier = Modifier.padding(20.dp))

                // Next button
                GradientButton(
                    nameButton = "Логин",
                    roundedCornerShape = RoundedCornerShape(topStart = 30.dp, bottomEnd = 30.dp),
                    modifier = Modifier.padding(16.dp),
                    navController = navController,
                    routes = Routes.SMS.route) {

                    readDataResponse()
                    println("Button clicked")
                }

                Spacer(modifier = Modifier.padding(10.dp))
                TextButton(onClick = {
                    navController.navigate(Routes.Registration.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                }) {
                    Text(
                        text = "Регистрация",
                        letterSpacing = 1.sp,
                        style = MaterialTheme.typography.labelLarge,
                        fontFamily = FontFamily(Font(R.font.roboto))
                    )
                }
                Spacer(modifier = Modifier.padding(5.dp))
            }
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



}


