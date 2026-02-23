package com.example.speedboat.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.speedboat.ui.login.LoginScreen
import com.example.speedboat.ui.login.RegisterScreen
import com.example.speedboat.ui.menu.MainMenuScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login" // Η οθόνη εκκίνησης
    ) {
        // LOGIN
        composable("login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("main_menu") { popUpTo("login") { inclusive = true } }
                },
                onNavigateToRegister = {
                    navController.navigate("register")
                }
            )
        }

        // REGISTER
        composable("register") {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate("main_menu") { popUpTo("login") { inclusive = true } }
                },
                onBackToLogin = {
                    navController.popBackStack()
                }
            )
        }

        // Οθόνη Κύριου Μενού (Placeholder μέχρι να τη φτιάξεις)
        composable("main_menu") {
            MainMenuScreen(onStartQuiz = {
                navController.navigate("quiz")
            })
        }

        // Οθόνη Quiz
        composable("quiz") {
            // Εδώ θα καλέσεις την QuizScreen όταν την ετοιμάσουμε
        }
    }
}
