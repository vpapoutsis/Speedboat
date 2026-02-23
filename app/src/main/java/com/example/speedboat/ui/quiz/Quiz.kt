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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
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

    if (currentQuestion == null) {
        // Τέλος Quiz
        onQuizFinish(correctCount, wrongCount)
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp)
    ) {
        // Header: Back button, Progress 1/20, Stats
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Close, contentDescription = null, tint = Color.Gray)
            Text(
                text = buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)) {
                        append("${currentIndex + 1}")
                    }
                    append("/${questions.size}")
                },
                color = Color.Gray
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                StatBadge(text = "$correctCount", color = Color(0xFF4CAF50), icon = Icons.Default.Check)
                Spacer(modifier = Modifier.width(8.dp))
                StatBadge(text = "$wrongCount", color = Color(0xFFD32F2F), icon = Icons.Default.Close)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Progress Bar
        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / questions.size },
            modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
            color = Color(0xFF003366),
            trackColor = Color.LightGray.copy(alpha = 0.3f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Question Text
        Text(
            text = currentQuestion.text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 28.sp,
            color = Color.Black
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Options List
        currentQuestion.options.forEachIndexed { index, option ->
            val letter = when(index) { 0 -> "A"; 1 -> "B"; 2 -> "C"; else -> "D" }

            OptionCard(
                letter = letter,
                text = option,
                isSelected = selectedAnswer == index,
                isAnswered = isAnswered,
                isCorrect = index == currentQuestion.correctAnswerIndex,
                onClick = {
                    if (!isAnswered) {
                        selectedAnswer = index
                        isAnswered = true
                        if (index == currentQuestion.correctAnswerIndex) correctCount++ else wrongCount++
                    }
                }
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // "Επόμενη" Button - Εμφανίζεται μόνο μετά την απάντηση
        if (isAnswered) {
            Button(
                onClick = {
                    currentIndex++
                    selectedAnswer = null
                    isAnswered = false
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1A237E)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Επόμενη", fontSize = 18.sp, color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
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
    isCorrect: Boolean,
    onClick: () -> Unit
) {
    // Καθορισμός χρωμάτων βάσει της εικόνας 15 & 16
    val backgroundColor = when {
        !isAnswered -> Color.White
        isCorrect -> Color(0xFFE8F5E9) // Απαλό πράσινο
        isSelected && !isCorrect -> Color(0xFFFFEBEE) // Απαλό κόκκινο
        else -> Color.White
    }

    val borderColor = when {
        !isAnswered -> if (isSelected) Color(0xFF003366) else Color(0xFFF0F0F0)
        isCorrect -> Color(0xFF4CAF50)
        isSelected && !isCorrect -> Color(0xFFD32F2F)
        else -> Color(0xFFF0F0F0)
    }

    val contentColor = when {
        !isAnswered -> Color.Black
        isCorrect -> Color(0xFF2E7D32)
        isSelected && !isCorrect -> Color(0xFFC62828)
        else -> Color.Gray
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, borderColor),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Letter Circle (A, B, C)
            Surface(
                modifier = Modifier.size(32.dp),
                shape = CircleShape,
                color = if (isAnswered && (isCorrect || (isSelected && !isCorrect))) contentColor else Color(0xFFF5F5F5)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(letter, color = if (isAnswered && (isCorrect || isSelected)) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Text(text = text, modifier = Modifier.weight(1f), color = contentColor, fontSize = 16.sp)

            if (isAnswered) {
                if (isCorrect) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
                } else if (isSelected) {
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
