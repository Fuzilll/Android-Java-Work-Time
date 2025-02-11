package com.example.timemarkinghr.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timemarkinghr.R;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timemarkinghr.controller.AuthController;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private AuthController authController;

    private EditText et_email, et_password;
    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //componentes
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        //  AuthController
        authController = new AuthController();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logar(et_email.getText().toString(), et_password.getText().toString());
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Verifica se o usuário já está autenticado
        FirebaseUser usuarioAtual = authController.getUsuarioAtual();
        if (usuarioAtual != null) {
            // Se o usuário estiver autenticado, recarrega os dados do usuário
            recarregarUsuario(usuarioAtual);
        }
    }

    // Método para realizar o login
    private void logar(String email, String password) {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }
        authController.logar(email, password, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Se o login for bem-sucedido
                    FirebaseUser usuario = authController.getUsuarioAtual();
                    if (usuario != null) {
                        // Recarrega os dados do usuario
                        recarregarUsuario(usuario);
                    }
                } else {
                    // Se o login falhar
                    Toast.makeText(LoginActivity.this, "Email ou senha incorretos", Toast.LENGTH_LONG).show();
                    updateUI(null); // Atualiza a interface para "não logado"
                }
            }
        });
    }

    // Método para recarregar os dados do usuário
    private void recarregarUsuario(FirebaseUser usuario) {
        authController.recarregarUsuario(usuario, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    // Se os dados forem recarregados com sucesso, atualiza a interface
                    FirebaseUser usuarioAtualizado = authController.getUsuarioAtual();
                    updateUI(usuarioAtualizado);
                } else {
                    // Se houver falha ao recarregar, exibe uma mensagem de erro
                    Toast.makeText(LoginActivity.this, "Falha ao recarregar dados do usuário", Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }
            }
        });
    }

    // atualiza a interface do usuário com base no estado de autenticação
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            // Se o usuário estiver autenticado, redireciona para a próxima tela
            startActivity(new Intent(LoginActivity.this, RegistroPontoActivity.class));
            finish(); // Finaliza a LoginActivity
        } else {
            // Se o usuário não estiver autenticado, limpa os campos de entrada
            et_email.setText("");
            et_password.setText("");
        }
    }
}