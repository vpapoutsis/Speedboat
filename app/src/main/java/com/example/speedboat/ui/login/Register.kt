package com.example.speedboat.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegisterScreen(onRegisterSuccess: () -> Unit, onBackToLogin: () -> Unit) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val auth = com.google.firebase.auth.FirebaseAuth.getInstance()

    Box(modifier = Modifier.fillMaxSize().background(
        Brush.verticalGradient(
        colors = listOf(Color(0xFF000033), Color(0xFF000080), Color(0xFF00BFFF))
    ))) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("Δημιουργία Λογαριασμού", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlinedTextField(value = firstName, onValueChange = { firstName = it }, label = { Text("Όνομα") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = lastName, onValueChange = { lastName = it }, label = { Text("Επίθετο") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Κωδικός") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (errorMessage != null) {
                        Text(text = errorMessage!!, color = Color.Red, fontSize = 12.sp)
                    }

                    Button(
                        onClick = {
                            // Αναλυτικός έλεγχος κενών πεδίων
                            errorMessage = when {
                                firstName.trim().isEmpty() -> "Παρακαλώ εισάγετε το Όνομά σας"
                                lastName.trim().isEmpty() -> "Παρακαλώ εισάγετε το Επίθετό σας"
                                email.trim().isEmpty() -> "Το Email δεν μπορεί να είναι κενό"
                                password.isEmpty() -> "Πρέπει να ορίσετε έναν κωδικό"
                                password.length < 6 -> "Ο κωδικός πρέπει να είναι τουλάχιστον 6 χαρακτήρες"
                                else -> null
                            }
                            
                            if (errorMessage == null) {
                                auth.createUserWithEmailAndPassword(email.trim(), password)
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            onRegisterSuccess()
                                        } else {
                                            // Χρήση της helper συνάρτησης
                                            errorMessage = mapFirebaseErrorToGreek(task.exception)
                                        }
                                    }
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
                    ) {
                        Text("Εγγραφή")
                    }

                    TextButton(onClick = onBackToLogin, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Text("Έχετε ήδη λογαριασμό; Σύνδεση", color = Color.Gray)
                    }
                }
            }
        }
    }
}