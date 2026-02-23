package com.example.speedboat.ui.quiz

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore


class QuizViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    // State για τη λίστα των ερωτήσεων
    private val _questions = mutableStateOf<List<Question>>(emptyList())
    val questions: State<List<Question>> = _questions

    // State για να ξέρουμε αν φορτώνουν τα δεδομένα
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        db.collection("Questions")
            .get()
            .addOnSuccessListener { result ->
                val allQuestions = result.toObjects(Question::class.java)
                // Επιλογή 20 τυχαίων ερωτήσεων
                _questions.value = allQuestions.shuffled().take(20)
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isLoading.value = false
                // Εδώ μπορείς να προσθέσεις error handling
            }
    }
}

data class Question (
    val id: Int = 0,
    val text: String = "",
    val options: List<String> = listOf(),
    val correctAnswerIndex: Int = 0
)