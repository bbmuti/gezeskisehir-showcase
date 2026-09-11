package com.example.gezeskisehir.showcase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/** Firestore kullanıcı belgesindeki rol alanını güvenli biçimde kontrol eder. */
public final class AdminAuthorizationExample {

    public interface ResultCallback {
        void onResult(boolean isAdmin);
    }

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public AdminAuthorizationExample(FirebaseAuth auth, FirebaseFirestore firestore) {
        this.auth = auth;
        this.firestore = firestore;
    }

    public void check(ResultCallback callback) {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            callback.onResult(false);
            return;
        }

        firestore.collection("users")
                .document(user.getUid())
                .get()
                .addOnSuccessListener(document ->
                        callback.onResult("admin".equals(document.getString("role"))))
                .addOnFailureListener(error -> callback.onResult(false));
    }
}
