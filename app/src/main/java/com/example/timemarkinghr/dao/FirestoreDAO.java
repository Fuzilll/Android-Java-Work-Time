package com.example.timemarkinghr.dao;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;

public class FirestoreDAO {
    private FirebaseFirestore db;

    public FirestoreDAO() {
        db = FirebaseFirestore.getInstance();
    }

    public void salvarPonto(String userId, Map<String, Object> ponto, OnFirestoreCallback callback) {
        db.collection("Pontos").document(userId)
                .set(ponto)
                .addOnSuccessListener(unused -> callback.onSuccess())
                .addOnFailureListener(callback::onFailure);
    }

    public interface OnFirestoreCallback {
        void onSuccess();
        void onFailure(Exception e);
    }
}