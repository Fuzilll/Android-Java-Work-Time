package com.example.timemarkinghr.dao;

import com.example.timemarkinghr.model.RegistroPonto;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FirestoreDAO {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public void buscarRegistrosPonto(String userId, Consumer<List<RegistroPonto>> callback) {
        db.collection("pontos")
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<RegistroPonto> listaPontos = new ArrayList<>();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        RegistroPonto ponto = document.toObject(RegistroPonto.class);
                        listaPontos.add(ponto);
                    }
                    callback.accept(listaPontos);
                })
                .addOnFailureListener(e -> callback.accept(null));
    }
}
