package com.example.speedboat.ui.menu

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreenContent(userName: String, onStartQuiz: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("Καλησπέρα, $userName!", fontSize = 28.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Text("Ετοιμάσου για το τεστ ταχύπλοου", color = Color.White.copy(alpha = 0.8f))

        Spacer(modifier = Modifier.height(24.dp))

        // Τα 3 μικρά κουτάκια στατιστικών
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            StatCard("3", "Τεστ", Icons.Default.Description)
            StatCard("0", "Επιτυχίες", Icons.Default.CheckCircle)
            StatCard("0%", "Ποσοστό", Icons.AutoMirrored.Filled.TrendingUp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Info Cards
        InfoCard(title = "20 Ερωτήσεις", subtitle = "Τυχαίες ερωτήσεις πολλαπλής επιλογής", icon = Icons.AutoMirrored.Filled.Help)
        InfoCard(title = "Max 2 Λάθη", subtitle = "Επιτρέπονται μέχρι 2 λάθη για επιτυχία", icon = Icons.Default.Lock)
        InfoCard(title = "Ανακατεμένες Απαντήσεις", subtitle = "Οι απαντήσεις εμφανίζονται με τυχαία σειρά", icon = Icons.Default.Shuffle)

        Spacer(modifier = Modifier.height(40.dp))

        // Κουμπί Έναρξης
        Button(
            onClick = onStartQuiz,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000080)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Ξεκίνα Νέο Τεστ", fontSize = 18.sp)
        }
    }
}
