package com.example.timemarkinghr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.ui.fragment.PerfilFragment;
import com.example.timemarkinghr.utils.SessaoManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private Button btnEntrada , btnPausa;

    private ImageButton btnAbrirMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(insets.getInsets(WindowInsetsCompat.Type.systemBars()).left,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).top,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).right,
                    insets.getInsets(WindowInsetsCompat.Type.systemBars()).bottom);
            return insets;
        });
        inicializarComponentes();
        configurarListeners();
        btnPausa = findViewById(R.id.botaoPausa);
        btnPausa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirTela(HistoricoActivity.class, "Registros");
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (SessaoManager.estaLogado(this)) {
            Toast.makeText(this, "Bem-vindo ", Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        }

    }

    private void inicializarComponentes() {
        btnEntrada = findViewById(R.id.botaoEntrada);
        btnAbrirMenu = findViewById(R.id.btn_abrir_menu);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        if (drawerLayout == null || navigationView == null) {
            Log.e("Erro", "DrawerLayout ou NavigationView não encontrados!");
        }
    }

    private void configurarListeners() {
        if (btnEntrada != null) {
            btnEntrada.setOnClickListener(v -> startActivity(new Intent(this, RegistroPontoActivity.class)));
        } else {
            Log.e("Erro", "Botão de entrada não encontrado!");
        }

        if (btnAbrirMenu != null) {
            btnAbrirMenu.setOnClickListener(v -> {
                if (drawerLayout != null) {
                    drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    Log.e("Erro", "DrawerLayout não encontrado!");
                }
            });
        } else {
            Log.e("Erro", "Botão de abrir menu não encontrado!");
        }

        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(item -> {
                int itemId = item.getItemId();
                Log.d("DEBUG", "Item clicado: " + itemId);

                if (itemId == R.id.nav_home) {
                    abrirTela(MainActivity.class, "Início");
                } else if (itemId == R.id.nav_perfil) {
                    abrirTela(PerfilFragment.class, "Perfil");
                } else if (itemId == R.id.nav_configuracoes) {
                    abrirTela(ConfiguracoesActivity.class, "Configurações");
                } else if (itemId == R.id.nav_historico) {
                    abrirTela(HistoricoActivity.class, "Registros");
                } else if (itemId == R.id.nav_sair) {
                    realizarLogout();
                } else {
                    return false;
                }

                // Fecha o menu lateral após a seleção
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            });
        }
    }

    // Novo método para logout completo
    private void realizarLogout() {
        Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show();

        // Limpa sessão
        SessaoManager.logout(this);

        // Volta para a tela de login e remove MainActivity da pilha
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Finaliza essa activity
        finish();
    }


    // Método para abrir novas telas
    private void abrirTela(Class<?> activity, String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
