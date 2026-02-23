package com.example.speedboat.ui.quiz

import com.google.firebase.firestore.FirebaseFirestore

fun saveUserStats(userId: String, isSuccess: Boolean) {
    val db = FirebaseFirestore.getInstance()
    val userRef = db.collection("Statistics").document(userId)

    db.runTransaction { transaction ->
        val snapshot = transaction.get(userRef)

        // Αν δεν υπάρχει το έγγραφο, το δημιουργούμε
        if (!snapshot.exists()) {
            val initialData = hashMapOf(
                "totalTests" to 1,
                "successes" to if (isSuccess) 1 else 0,
                "failures" to if (isSuccess) 0 else 1
            )
            transaction.set(userRef, initialData)
        } else {
            // Αν υπάρχει, ενημερώνουμε τις τιμές
            val newTotal = (snapshot.getLong("totalTests") ?: 0) + 1
            transaction.update(userRef, "totalTests", newTotal)

            if (isSuccess) {
                val newSuccesses = (snapshot.getLong("successes") ?: 0) + 1
                transaction.update(userRef, "successes", newSuccesses)
            } else {
                val newFailures = (snapshot.getLong("failures") ?: 0) + 1
                transaction.update(userRef, "failures", newFailures)
            }
        }
    }.addOnSuccessListener {
        println("Stats updated successfully!")
    }
}