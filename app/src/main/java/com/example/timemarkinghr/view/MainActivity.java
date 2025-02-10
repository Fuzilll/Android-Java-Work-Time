package com.example.timemarkinghr.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timemarkinghr.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser usuarioAtual = mAuth.getCurrentUser();

        if (usuarioAtual != null) {
            // Se o usuário já está logado, vai para a tela de Registro de Ponto
            startActivity(new Intent(this, RegistroPontoActivity.class));
        } else {
            // Se não estiver logado, vai para a tela de Login
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish(); // Fecha a MainActivity para não ficar na pilha de navegação

    }
}