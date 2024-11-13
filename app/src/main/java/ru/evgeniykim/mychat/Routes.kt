package ru.evgeniykim.mychat

sealed class Routes(val route: String) {
    object Login : Routes("Login")
    object SMS : Routes("SMS")
    object Registration : Routes("Registration")
    object Chat : Routes("Chat")
    object Profile : Routes("Profile")
}