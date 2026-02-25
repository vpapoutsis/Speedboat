package com.example.speedboat.ui.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.getOrNull

@Composable
fun QuizScreen(questions: List<Question>, onQuizFinish: (Int, Int) -> Unit) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<Int?>(null) }
    var isAnswered by remember { mutableStateOf(false) }
    var correctCount by remember { mutableIntStateOf(0) }
    var wrongCount by remember { mutableIntStateOf(0) }

    val currentQuestion = questions.getOrNull(currentIndex)

    // Έλεγχος ασφαλείας: Αν η λίστα είναι άδεια, δείξε μήνυμα ή γύρνα πίσω
    if (questions.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Φόρτωση ερωτήσεων...")
        }
        return
    }

    if (currentQuestion == null) {
        // Τέλος Quiz
        onQuizFinish(correctCount, wrongCount)
        return
    }

    // 1. Το Gradient Background σε όλη την οθόνη
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF003366), Color(0xFF00BFFF))
                )
            )
    ) {
        // 2. Το κεντρικό Column που "κρατάει" τα πάντα στη μέση
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .systemBarsPadding(),
            verticalArrangement = Arrangement.Center, // Κεντράρισμα καθ' ύψος
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // 3. Πρόοδος και Stats στην ίδια σειρά
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / questions.size },
                    modifier = Modifier
                        .weight(1f)
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp)),
                    color = Color.White,
                    trackColor = Color.White.copy(alpha = 0.2f)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StatBadge(text = "$correctCount", color = Color(0xFF4CAF50), icon = Icons.Default.Check)
                    Spacer(modifier = Modifier.width(4.dp))
                    StatBadge(text = "$wrongCount", color = Color(0xFFFF5252), icon = Icons.Default.Close)
                }
            }

            // Αριθμός ερώτησης
            Text(
                text = "Ερώτηση ${currentIndex + 1}/${questions.size}",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 4. Η Ερώτηση (Μέσα σε Κάρτα για να ξεχωρίζει)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.95f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = currentQuestion.text,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 28.sp,
                    color = Color(0xFF003366),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(24.dp).fillMaxWidth()
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Λίστα Επιλογών
            currentQuestion.options.forEachIndexed { index, option ->
                val letter = when(index) { 0 -> "A"; 1 -> "B"; 2 -> "C"; else -> "D" }
                val isThisOptionCorrect = option == currentQuestion.correctAnswer

                OptionCard(
                    letter = letter,
                    text = option,
                    isSelected = selectedAnswer == index,
                    isAnswered = isAnswered,
                    isCorrect = isThisOptionCorrect,
                    onClick = {
                        if (!isAnswered) {
                            selectedAnswer = index
                            isAnswered = true
                            if (isThisOptionCorrect) correctCount++ else wrongCount++
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 6. Κουμπί Επόμενη (Με σταθερό Box για να μην αναπηδά το UI)
            Box(modifier = Modifier.height(56.dp).fillMaxWidth()) {
                if (isAnswered) {
                    Button(
                        onClick = {
                            currentIndex++
                            selectedAnswer = null
                            isAnswered = false
                        },
                        modifier = Modifier.fillMaxSize(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF000080)),
                        shape = RoundedCornerShape(12.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        //Text("Επόμενη", fontSize = 18.sp, color = Color(0xFF003366), fontWeight = FontWeight.Bold)
                        Text("Επόμενη", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null
                            //tint = Color(0xFF003366)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OptionCard(
    letter: String,
    text: String,
    isSelected: Boolean,
    isAnswered: Boolean,
    isCorrect: Boolean, // Αυτό πλέον υπολογίζεται ως (option == question.correctAnswer)
    onClick: () -> Unit
) {
    // 1. Καθορισμός Χρωμάτων (Σύμφωνα με τις εικόνες 15 & 16)
    val backgroundColor = when {
        !isAnswered -> Color.White
        isCorrect -> Color(0xFFE8F5E9) // Απαλό πράσινο αν είναι η σωστή
        isSelected -> Color(0xFFFFEBEE) // Απαλό κόκκινο αν επιλέχθηκε λάθος (το isCorrect ελέγχθηκε παραπάνω)
        else -> Color.White
    }

    val borderColor = when {
        !isAnswered -> if (isSelected) Color(0xFF003366) else Color(0xFFF0F0F0)
        isCorrect -> Color(0xFF4CAF50)
        isSelected -> Color(0xFFD32F2F)
        else -> Color(0xFFF0F0F0)
    }

    val contentColor = when {
        !isAnswered -> Color.Black
        isCorrect -> Color(0xFF2E7D32)
        isSelected -> Color(0xFFC62828)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(enabled = !isAnswered) { onClick() }, // Απενεργοποίηση κλικ μετά την απάντηση
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Κύκλος με το Γράμμα (A, B, C)
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                // Εδώ αφαιρέθηκε το περιττό !isCorrect
                color = if (isAnswered && (isCorrect || isSelected)) contentColor else Color(0xFFF5F5F5)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = letter,
                        color = if (isAnswered && (isCorrect || isSelected)) Color.White else Color.Gray,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(
                text = text,
                modifier = Modifier.weight(1f),
                color = contentColor,
                fontSize = 16.sp,
                fontWeight = if (isAnswered && (isCorrect || isSelected)) FontWeight.Bold else FontWeight.Normal
            )

            // Εικονίδια Check ή Cancel
            if (isAnswered) {
                if (isCorrect) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                } else if (isSelected) {
                    // Εδώ η Kotlin ξέρει ότι το isCorrect είναι false
                    Icon(Icons.Default.Cancel, contentDescription = null, tint = Color(0xFFD32F2F))
                }
            }
        }
    }
}

@Composable
fun StatBadge(text: String, color: Color, icon: ImageVector) {
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.2f))
    ) {
        Row(modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = text, color = color, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
    }
}
