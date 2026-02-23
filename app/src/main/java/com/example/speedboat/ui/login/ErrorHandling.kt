package com.example.speedboat.ui.login

fun mapFirebaseErrorToGreek(exception: Exception?): String {
    return when (exception) {
        is com.google.firebase.auth.FirebaseAuthInvalidUserException ->
            "Ο λογαριασμός δεν υπάρχει. Παρακαλώ κάντε εγγραφή."
        is com.google.firebase.auth.FirebaseAuthInvalidCredentialsException ->
            "Λανθασμένο email ή κωδικός πρόσβασης."
        is com.google.firebase.auth.FirebaseAuthUserCollisionException ->
            "Το email χρησιμοποιείται ήδη από άλλον λογαριασμό."
        is com.google.firebase.FirebaseNetworkException ->
            "Πρόβλημα σύνδεσης στο διαδίκτυο."
        is com.google.firebase.auth.FirebaseAuthWeakPasswordException ->
            "Ο κωδικός είναι πολύ αδύναμος. Χρησιμοποιήστε τουλάχιστον 6 χαρακτήρες."
        else -> "Παρουσιάστηκε ένα απρόσμενο σφάλμα. Δοκιμάστε ξανά."
    }
}
