package com.example.timemarkinghr.controller;

import com.example.timemarkinghr.dao.FirestoreDAO;
import java.util.Map;

public class RegistroPontoController {
    private FirestoreDAO firestoreDAO;

    public RegistroPontoController() {
        this.firestoreDAO = new FirestoreDAO();
    }

    public void registrarPonto(String userId, Map<String, Object> ponto, FirestoreDAO.OnFirestoreCallback callback) {
        firestoreDAO.salvarPonto(userId, ponto, callback);
    }
}