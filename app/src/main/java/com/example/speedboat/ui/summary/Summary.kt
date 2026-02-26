package com.example.speedboat.ui.summary

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SentimentDissatisfied
import androidx.compose.material.icons.filled.SentimentSatisfiedAlt
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SummaryScreen(
    correctAnswers: Int,
    wrongAnswers: Int,
    onNewTest: () -> Unit,
    onHome: () -> Unit
) {
    val totalQuestions = correctAnswers + wrongAnswers
    val scorePercentage = if (totalQuestions > 0) (correctAnswers * 100 / totalQuestions) else 0
    val isPassed = wrongAnswers <= 2 // Επιτυχία με μέχρι 2 λάθη

    // Χρώματα βάσει αποτελέσματος
    val statusColor = if (isPassed) Color(0xFF4CAF50) else Color(0xFFD32F2F)
    val statusText = if (isPassed) "Επιτυχία" else "Αποτυχία"
    val statusSubText = if (isPassed) "Συγχαρητήρια! Πέρασες το τεστ." else "Δυστυχώς δεν πέρασες. Προσπάθησε ξανά!"
    val statusIcon = if (isPassed) Icons.Default.SentimentSatisfiedAlt else Icons.Default.SentimentDissatisfied

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA)) // Ελαφρύ γκρι φόντο όπως στην εικόνα
            .padding(24.dp)
            .systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Εικονίδιο Κατάστασης (Κόκκινος/Πράσινος κύκλος)
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = statusColor
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = statusIcon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Τίτλος Αποτελέσματος
        Text(
            text = statusText,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = statusColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Υπότιτλος
        Text(
            text = statusSubText,
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 20.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Κάρτα Στατιστικών
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(label = "Σωστές", value = "$correctAnswers", color = Color(0xFF4CAF50), icon = Icons.Default.Check)
                HorizontalDivider(
                    modifier = Modifier.height(40.dp).width(1.dp),
                    thickness = DividerDefaults.Thickness,
                    color = Color(0xFFEEEEEE)
                )
                StatItem(label = "Λάθη", value = "$wrongAnswers", color = Color(0xFFD32F2F), icon = Icons.Default.Close)
                HorizontalDivider(
                    modifier = Modifier.height(40.dp).width(1.dp),
                    thickness = DividerDefaults.Thickness,
                    color = Color(0xFFEEEEEE)
                )
                StatItem(label = "Βαθμός", value = "$scorePercentage%", color = Color(0xFF003366), icon = Icons.AutoMirrored.Filled.List)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Πληροφορία για το όριο επιτυχίας
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color(0xFFE3F2FD),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Επιτυχία με μέχρι 2 λάθη στις 20 ερωτήσεις",
                    fontSize = 13.sp,
                    color = Color(0xFF1976D2)
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Κουμπί Νέο Τεστ (Με Gradient)
        Button(
            onClick = onNewTest,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    brush = Brush.horizontalGradient(colors = listOf(Color(0xFF001A4D), Color(0xFF4A90E2))),
                    shape = RoundedCornerShape(16.dp)
                ),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            shape = RoundedCornerShape(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Νέο Τεστ", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Κουμπί Αρχική
        OutlinedButton(
            onClick = onHome,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFEEEEEE))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Home, contentDescription = null, tint = Color(0xFF003366))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Αρχική", fontSize = 18.sp, color = Color(0xFF003366))
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, color: Color, icon: ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(12.dp),
            color = color.copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}