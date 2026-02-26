package com.example.speedboat.ui.navigation

import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.speedboat.ui.login.LoginScreen
import com.example.speedboat.ui.login.RegisterScreen
import com.example.speedboat.ui.menu.MainMenuScreen
import com.example.speedboat.ui.quiz.QuizErrorScreen
import com.example.speedboat.ui.quiz.QuizScreen
import com.example.speedboat.ui.quiz.QuizViewModel
import com.example.speedboat.ui.quiz.saveUserStats
import com.example.speedboat.ui.summary.SummaryScreen
import com.google.firebase.auth.FirebaseAuth

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

        // Οθόνη Κύριου Μενού
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

            val myContext = LocalContext.current

            if (isLoading) {
                // Εμφάνιση ενός Loading Spinner μέχρι να έρθουν οι ερωτήσεις
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color(0xFF003366))
                }
            } else if (questions.isNotEmpty()) {
                QuizScreen(
                    questions = questions,
                    //onQuizFinish = { correct, wrong ->
                    //    // Αποθήκευση στατιστικών και επιστροφή
                    //    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    //    if (userId != null) {
                    //        saveUserStats(userId, wrong <= 2) // Επιτυχία αν λάθη <= 2
                    //    }
                    //    navController.popBackStack()
                    //}
                    onQuizFinish = { correct, wrong ->
                        // 1. Αποθήκευση στατιστικών στη βάση
                        val userId = FirebaseAuth.getInstance().currentUser?.uid
                        if (userId != null) {
                            saveUserStats(userId, wrong <= 2) // Επιτυχία αν λάθη <= 2
                        }

                        // 2. Μετάβαση στην οθόνη Summary με πέρασμα των δεδομένων
                        // Χρησιμοποιούμε το route που ορίσαμε: "summary/{correct}/{wrong}"
                        navController.navigate("summary/$correct/$wrong") {
                            // Αυτό το κομμάτι είναι ΠΟΛΥ ΣΗΜΑΝΤΙΚΟ:
                            // Καθαρίζει το Quiz από το ιστορικό (backstack) ώστε αν ο χρήστης
                            // πατήσει το πίσω κουμπί από το Summary, να μην ξαναμπεί στο ίδιο Quiz.
                            popUpTo("quiz") { inclusive = true }
                        }
                    }
                )
            } else {
                // Περίπτωση σφάλματος ή άδειας βάσης
                QuizErrorScreen(
                    message = "Δεν μπορέσαμε να φορτώσουμε τις ερωτήσεις. Βεβαιώσου ότι έχεις ίντερνετ ή ότι υπάρχουν ερωτήσεις στη βάση.",
                    onRetry = {
                        Toast.makeText(myContext, "Προσπάθεια επανασύνδεσης...", Toast.LENGTH_SHORT).show()
                        viewModel.loadQuestions()
                    },
                    onBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = "summary/{correct}/{wrong}",
            arguments = listOf(
                navArgument("correct") { type = NavType.IntType },
                navArgument("wrong") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val correct = backStackEntry.arguments?.getInt("correct") ?: 0
            val wrong = backStackEntry.arguments?.getInt("wrong") ?: 0

            SummaryScreen(
                correctAnswers = correct,
                wrongAnswers = wrong,
                onNewTest = {
                    navController.navigate("quiz") {
                        popUpTo("main_menu") // Καθαρίζει το stack για να μη γυρνάει πίσω στο παλιό summary
                    }
                },
                onHome = {
                    navController.navigate("main_menu") {
                        popUpTo("main_menu") { inclusive = true }
                    }
                }
            )
        }

    }
}


