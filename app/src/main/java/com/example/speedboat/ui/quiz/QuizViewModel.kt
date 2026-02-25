package com.example.speedboat.ui.quiz

import android.util.Log
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

    fun loadQuestions() {
        // 1. Καθαρίζουμε τα πάντα για να αναγκάσουμε το UI να κάνει recompose
        _questions.value = emptyList()
        _isLoading.value = true

        db.collection("Questions") // Βεβαιώσου ότι το "Q" είναι κεφαλαίο αν έτσι το έχεις στη βάση
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    _questions.value = emptyList()
                } else {
                    val allQuestions = result.toObjects(Question::class.java)

                    _questions.value = allQuestions.shuffled().take(20).map { q ->
                        // Ειδική λογική για το ανακάτεμα των options
                        val currentOptions = q.options
                        val specialPhrase = "Α και Β"

                        // Ελέγχουμε αν υπάρχει η φράση στις επιλογές
                        val hasSpecial = currentOptions.any { it.contains(specialPhrase, ignoreCase = true) }

                        val newOptions = if (hasSpecial && currentOptions.size >= 3) {
                            // 1. Βρίσκουμε την ειδική απάντηση
                            val specialOption = currentOptions.first { it.contains(specialPhrase, ignoreCase = true) }
                            // 2. Παίρνουμε τις υπόλοιπες
                            val otherOptions = currentOptions.filter { it != specialOption }
                            // 3. Ανακατεύουμε μόνο τις άλλες και προσθέτουμε την ειδική στο τέλος (3η θέση)
                            otherOptions.shuffled() + specialOption
                        } else {
                            // Αν δεν υπάρχει η φράση, ανακάτεμα κανονικά σε όλες
                            currentOptions.shuffled()
                        }

                        q.copy(options = newOptions)
                    }
                }
                _isLoading.value = false
            }
            .addOnFailureListener {
                _isLoading.value = false
            }
    }
}

data class Question (
    val text: String = "",
    val options: List<String> = listOf(),
    val correctAnswer: String = ""
)

//correctAnswerIndex