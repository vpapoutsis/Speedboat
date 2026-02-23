package com.example.speedboat.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Lock
//import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.speedboat.R
import com.example.speedboat.ui.theme.SpeedboatTheme


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") } // Το Firebase θέλει email
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000033),
                        Color(0xFF000080),
                        Color(0xFF00BFFF)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_boat),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(bottom = 16.dp),
                tint = Color.White
            )
            Text(
                text = "Quiz Ταχύπλοου",
                style = TextStyle(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Προετοιμασία για την άδεια χειριστή",
                style = TextStyle(
                    fontSize = 16.sp,
                    color = Color.White
                ),
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Σύνδεση / Εγγραφή", style = TextStyle(fontSize = 24.sp, fontWeight = FontWeight.Bold))

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") }, // Το Firebase λειτουργεί καλύτερα με Email
                        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                    )

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Κωδικός") },
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                    )

                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = Color.Red, fontSize = 12.sp)
                    }

                    // ΚΟΥΜΠΙ LOGIN με Validation
                    Button(
                        onClick = {
                            // 1. Έλεγχος αν τα πεδία είναι κενά
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Παρακαλώ συμπληρώστε email και κωδικό"
                            } else {
                                auth.signInWithEmailAndPassword(email.trim(), password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            onLoginSuccess()
                                        } else {
                                            // Χρήση της helper συνάρτησης για ελληνικά
                                            errorMessage = mapFirebaseErrorToGreek(task.exception)
                                        }
                                    }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000080))
                    ) {
                        Text(text = "Είσοδος")
                    }

                    // ΚΟΥΜΠΙ ΕΓΓΡΑΦΗΣ
                    //androidx.compose.material3.TextButton(
                    //    onClick = {
                    //        if (email.isNotEmpty() && password.isNotEmpty()) {
                    //            auth.createUserWithEmailAndPassword(email, password)
                    //                .addOnCompleteListener { task ->
                    //                    if (task.isSuccessful) onAuthSuccess()
                    //                    else errorMessage = "Αποτυχία εγγραφής: ${task.exception?.message}"
                    //                }
                    //        } else {
                    //            errorMessage = "Συμπληρώστε email και κωδικό"
                    //        }
                    //    },
                    //    modifier = Modifier.padding(top = 8.dp)
                    //) {
                    //    Text(text = "Δεν έχετε λογαριασμό; Εγγραφή", color = Color.Gray)
                    //}
                    TextButton(onClick = onNavigateToRegister) {
                        Text("Δεν έχετε λογαριασμό; Εγγραφή", color = Color.Gray)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    SpeedboatTheme {
        LoginScreen(
            onLoginSuccess = {},
            onNavigateToRegister = {}
        )
    }
}
