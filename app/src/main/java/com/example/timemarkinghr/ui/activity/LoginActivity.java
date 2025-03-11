package com.example.timemarkinghr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.controller.UsuarioController;
import com.example.timemarkinghr.data.model.Usuario;
import com.example.timemarkinghr.utils.SessaoManager;

public class LoginActivity extends AppCompatActivity {
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

        // Componentes
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(v -> {
            String email = et_email.getText().toString().trim();
            String senha = et_password.getText().toString().trim();

            if (email.isEmpty() || senha.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Preencha todos os campos", Toast.LENGTH_SHORT).show();
            } else {
                realizarLogin(email, senha);
            }
        });
    }

    private void realizarLogin(String email, String senha) {
        UsuarioController usuarioController = new UsuarioController();
        usuarioController.login(email, senha, new UsuarioController.LoginCallback() {
            @Override
            public void onLoginSucesso(Usuario usuario) {
                Log.d("001", "Login bem-sucedido!");

                // Salvar sessão do usuário
                SessaoManager.salvarSessao(LoginActivity.this, "TOKEN_AQUI", usuario);

                Toast.makeText(LoginActivity.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();

                // Direcionar para a MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onLoginFalha(String mensagem) {
                Toast.makeText(LoginActivity.this, "Erro: " + mensagem, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
