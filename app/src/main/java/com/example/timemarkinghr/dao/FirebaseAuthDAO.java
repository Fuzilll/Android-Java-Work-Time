package com.example.timemarkinghr.dao;




import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthDAO {
    private FirebaseAuth mAuth;

    public FirebaseAuthDAO() {
        mAuth = FirebaseAuth.getInstance();
    }

    // Método para fazer login com email e senha
    public void logar(String email, String password, OnCompleteListener<AuthResult> listener) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(listener);
    }

    // Método para recarregar os dados do usuário
    public void recarregarUsuario(FirebaseUser usuario, OnCompleteListener<Void> listener) {
        usuario.reload().addOnCompleteListener(listener);
    }

    // Método para obter o usuário atual
    public FirebaseUser getUsuarioAtual() {
        return mAuth.getCurrentUser();
    }
}