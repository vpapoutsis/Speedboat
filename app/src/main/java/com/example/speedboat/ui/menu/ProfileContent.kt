package com.example.speedboat.ui.menu

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExitToApp
//import androidx.compose.material.icons.automirrored.filled.ExitToApp
//import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
//import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProfileScreenContent(userName: String, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar Section
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = Color.White
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = userName.take(1).uppercase(),
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF003366)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = userName, fontSize = 24.sp, color = Color.White, fontWeight = FontWeight.Bold)
        Text(text = "Μέλος", color = Color.White.copy(alpha = 0.7f))

        Spacer(modifier = Modifier.height(32.dp))

        // Grid με 4 Στατιστικά (2x2)
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) { ProfileStatCard("3", "Συνολικά Τεστ", Icons.Default.Description) }
                Box(modifier = Modifier.weight(1f)) { ProfileStatCard("0", "Επιτυχίες", Icons.Default.CheckCircle) }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) { ProfileStatCard("0", "Αποτυχίες", Icons.Default.Cancel) }
                Box(modifier = Modifier.weight(1f)) { ProfileStatCard("0%", "Ποσοστό", Icons.AutoMirrored.Filled.TrendingUp) }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Progress Bar Card
        /*
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.9f)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Συνολική Πρόοδος", fontWeight = FontWeight.Bold, color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { 0.0f },
                    modifier = Modifier.fillMaxWidth().height(8.dp),
                    color = Color(0xFF00BFFF),
                    trackColor = Color.LightGray,
                    strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
                )
            }
        }
        */
        // Υποθέτουμε ότι το successRate είναι Int (π.χ. 65)
        val successRate = 65 // Αυτό αργότερα θα έρχεται από τη βάση σου
        val (feedbackMessage, feedbackColor) = getProgressFeedback(successRate)

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Συνολική Πρόοδος",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "$successRate%",
                        fontWeight = FontWeight.Bold,
                        color = feedbackColor
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Η μπάρα προόδου
                LinearProgressIndicator(
                    progress = { successRate / 100f }, // Μετατροπή σε 0.0 - 1.0
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp),
                    color = feedbackColor,
                    trackColor = Color.LightGray.copy(alpha = 0.3f),
                    strokeCap = StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Το δυναμικό κείμενο (π.χ. Χρειάζεται λίγο ακόμα διάβασμα)
                Text(
                    text = feedbackMessage,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // Κουμπί Αποσύνδεσης
        //TextButton(onClick = onLogout) {
        //    Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.White)
        //    Spacer(modifier = Modifier.width(8.dp))
        //    Text(text = "Αποσύνδεση", color = Color.White, fontSize = 16.sp)
        //}
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            TextButton(
                onClick = onLogout,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ExitToApp, // Ή Icons.Default.ExitToApp
                        contentDescription = null,
                        tint = Color(0xFFD32F2F) // Κόκκινο χρώμα
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Αποσύνδεση",
                        color = Color(0xFFD32F2F), // Το ίδιο κόκκινο χρώμα
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun getProgressFeedback(percentage: Int): Pair<String, Color> {
    return when {
        percentage == 0 -> "Δεν έχεις ξεκινήσει ακόμα. Πάμε!" to Color.Gray
        percentage < 50 -> "Χρειάζεται αρκετό διάβασμα ακόμα..." to Color(0xFFFF5252) // Κόκκινο
        percentage < 80 -> "Είσαι σε καλό δρόμο, συνέχισε!" to Color(0xFFFFA000) // Πορτοκαλί
        percentage < 95 -> "Σχεδόν έτοιμος για το δίπλωμα!" to Color(0xFF4CAF50) // Πράσινο
        else -> "Είσαι ειδικός! Έτοιμος για θάλασσα!" to Color(0xFF00BFFF) // Μπλε
    }
}
