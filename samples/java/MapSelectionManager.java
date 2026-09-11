package com.example.gezeskisehir.showcase;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Kullanıcının seçtiği yerleri kendi kullanıcı kimliği altında yönetir.
 * Portföy için sadeleştirilmiş örnektir.
 */
public final class MapSelectionManager {

    private static final String COLLECTION_NAME = "user_selected_places";

    private final FirebaseAuth auth;
    private final FirebaseFirestore firestore;

    public MapSelectionManager(FirebaseAuth auth, FirebaseFirestore firestore) {
        this.auth = auth;
        this.firestore = firestore;
    }

    public void addPlace(String placeId) {
        FirebaseUser user = requireUser();
        String safePlaceId = requirePlaceId(placeId);

        Map<String, Object> selection = new HashMap<>();
        selection.put("userUid", user.getUid());
        selection.put("placeId", safePlaceId);
        selection.put("selectedAt", FieldValue.serverTimestamp());

        firestore.collection(COLLECTION_NAME)
                .document(documentId(user.getUid(), safePlaceId))
                .set(selection);
    }

    public void removePlace(String placeId) {
        FirebaseUser user = requireUser();
        String safePlaceId = requirePlaceId(placeId);

        firestore.collection(COLLECTION_NAME)
                .document(documentId(user.getUid(), safePlaceId))
                .delete();
    }

    private FirebaseUser requireUser() {
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            throw new IllegalStateException("Bu işlem için kullanıcı girişi gerekir.");
        }
        return user;
    }

    private String requirePlaceId(String placeId) {
        if (placeId == null || placeId.trim().isEmpty()) {
            throw new IllegalArgumentException("Yer kimliği boş olamaz.");
        }
        return placeId.trim();
    }

    private String documentId(String userUid, String placeId) {
        return userUid + "_" + placeId;
    }
}
