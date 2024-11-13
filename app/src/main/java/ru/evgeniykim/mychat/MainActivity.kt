package ru.evgeniykim.mychat

import android.app.Activity
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import ru.evgeniykim.mychat.network.viewmodel.MainViewModel
import ru.evgeniykim.mychat.ui.screens.Login
import ru.evgeniykim.mychat.ui.screens.Registration
import ru.evgeniykim.mychat.ui.screens.SMS
import ru.evgeniykim.mychat.ui.theme.MyChatTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<MainViewModel.Factory> { factory ->
                factory.create("abc")
            }
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            MyChatTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {



                    LoginApplication(viewModel = viewModel)
                }
            }
            val activity = (LocalContext.current as Activity)
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Composable
fun LoginApplication(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Routes.Login.route,
        builder = {
            composable(Routes.Login.route, content = { Login(navController = navController, viewModel = viewModel)})
            composable(Routes.SMS.route, content = { SMS(navController = navController) })
            composable(Routes.Registration.route, content = { Registration(navController = navController) })
            composable(Routes.Chat.route, content = {  })
            composable(Routes.Profile.route, content = {  })
    })
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview(viewModel: MainViewModel) {
    MyChatTheme {
        //Greeting("Android")
        LoginApplication(viewModel = viewModel)
    }
}