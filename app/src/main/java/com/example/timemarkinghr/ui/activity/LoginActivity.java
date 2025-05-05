package com.example.timemarkinghr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.controller.UsuarioController;
import com.example.timemarkinghr.data.model.LoginResponse;
import com.example.timemarkinghr.data.model.Usuario;
import com.example.timemarkinghr.utils.SessaoManager;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText etEmail, etSenha;
    private Button btnLogin;
    private UsuarioController usuarioController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inicializarViews();
        configurarListeners();
        usuarioController = new UsuarioController(this);

        verificarSessaoAtiva();
    }

    private void inicializarViews() {
        etEmail = findViewById(R.id.et_email);
        etSenha = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
    }

    private void configurarListeners() {
        btnLogin.setOnClickListener(v -> validarERealizarLogin());
    }

    private void verificarSessaoAtiva() {
        if (SessaoManager.estaLogado(this)) {
            redirecionarParaMainActivity();
        }
    }

    private void validarERealizarLogin() {
        String email = etEmail.getText().toString().trim();
        String senha = etSenha.getText().toString().trim();

        if (email.isEmpty() || senha.isEmpty()) {
            mostrarMensagem("Preencha todos os campos");
            return;
        }

        realizarLogin(email, senha);
    }

    private void realizarLogin(String email, String senha) {
        btnLogin.setEnabled(false); // Desabilita o botão durante a requisição
        Log.d(TAG, "Iniciando login para: " + email); // LOG 1 - Início do processo

        usuarioController.realizarLogin(email, senha, new UsuarioController.LoginCallback() {
            @Override
            public void onSuccess(LoginResponse response) {
                runOnUiThread(() -> {
                    if (response != null && response.getToken() != null) {
                        salvarSessaoERedirecionar(response);
                    } else {
                        Log.e(TAG, "Resposta inválida do servidor");
                        mostrarMensagem("Erro no login");
                    }
                    btnLogin.setEnabled(true);
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                runOnUiThread(() -> {
                    String error = errorMessage != null ? errorMessage : "Erro desconhecido";
                    Log.e(TAG, "Falha no login: " + error);
                    mostrarMensagem(error);
                    btnLogin.setEnabled(true);
                });
            }
        });
    }

    private void salvarSessaoERedirecionar(LoginResponse response) {
        try {
            String token = response.getToken();
            Usuario usuario = response.getUsuario();

            SessaoManager.salvarSessao(LoginActivity.this, token, usuario);
            Log.d(TAG, "Usuário autenticado: " + usuario.getEmail()); // LOG 3 - Dados do usuário

            Log.d(TAG, "Redirecionando para MainActivity..."); // LOG 4 - Antes do redirecionamento
            redirecionarParaMainActivity();
        } catch (Exception e) {
            Log.e(TAG, "Erro ao salvar sessão", e);
            mostrarMensagem("Erro ao processar login");
        }
    }

    private void redirecionarParaMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void mostrarMensagem(String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_LONG).show();
    }
}