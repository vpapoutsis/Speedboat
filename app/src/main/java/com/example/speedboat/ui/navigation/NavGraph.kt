package com.example.speedboat.ui.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.speedboat.ui.login.LoginScreen
import com.example.speedboat.ui.login.RegisterScreen
import com.example.speedboat.ui.menu.MainMenuScreen
import com.example.speedboat.ui.quiz.QuizScreen
import com.example.speedboat.ui.quiz.QuizViewModel
import com.example.speedboat.ui.quiz.saveUserStats
import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore

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
            MainMenuScreen(
                onStartQuiz = { navController.navigate("quiz") },
                onLogout = {
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate("login") {
                        popUpTo("main_menu") { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // Οθόνη Quiz
        composable("quiz") {
            val viewModel: QuizViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
            val questions by viewModel.questions
            val isLoading by viewModel.isLoading

            if (isLoading) {
                // Εμφάνιση ενός Loading Spinner μέχρι να έρθουν οι ερωτήσεις
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF003366))
                }
            } else if (questions.isNotEmpty()) {
                QuizScreen(
                    questions = questions,
                    onQuizFinish = { correct, wrong ->
                        // Αποθήκευση στατιστικών και επιστροφή
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            saveUserStats(userId, wrong <= 2) // Επιτυχία αν λάθη <= 2
                        }
                        navController.popBackStack()
                    }
                )
            } else {
                // Περίπτωση σφάλματος ή άδειας βάσης
                Text("Δεν βρέθηκαν ερωτήσεις. Ελέγξτε τη σύνδεση.")
            }
        }
    }
}


