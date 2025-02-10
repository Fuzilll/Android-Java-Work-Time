package com.example.timemarkinghr.controller;

import com.example.timemarkinghr.dao.FirestoreDAO;
import com.example.timemarkinghr.model.RegistroPonto;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.function.Consumer;

public class RegistroPontoController {
    private final FirestoreDAO firestoreDAO;

    public RegistroPontoController() {
        this.firestoreDAO = new FirestoreDAO();
    }

    public void carregarHistoricoPontos(Consumer<List<RegistroPonto>> callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestoreDAO.buscarRegistrosPonto(userId, callback);
    }
}