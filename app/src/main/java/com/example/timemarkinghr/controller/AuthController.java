package com.example.timemarkinghr.controller;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import com.example.timemarkinghr.dao.FirebaseAuthDAO;

public class AuthController {
    private FirebaseAuthDAO firebaseAuthDAO;

    public AuthController() {
        firebaseAuthDAO = new FirebaseAuthDAO();
    }

    // Método para realizar o login
    public void logar(String email, String password, OnCompleteListener<AuthResult> listener) {
        firebaseAuthDAO.logar(email, password, listener);
    }

    // Método para recarregar os dados do usuário
    public void recarregarUsuario(FirebaseUser usuario, OnCompleteListener<Void> listener) {
        firebaseAuthDAO.recarregarUsuario(usuario, listener);
    }

    // Método para obter o usuário atual
    public FirebaseUser getUsuarioAtual() {
        return firebaseAuthDAO.getUsuarioAtual();
    }
}