package com.example.timemarkinghr.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.ui.fragment.PerfilFragment;
import com.example.timemarkinghr.ui.fragment.HistoricoFragment;
import com.example.timemarkinghr.ui.fragment.RegistroPontoFragment;
import com.example.timemarkinghr.utils.SessaoManager;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private LinearLayout btnEntrada, btnPausa, btnRetornoDaPausa, btnSaida;
    private TextView tvTitulo;
    private ImageButton btnAbrirMenu;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private boolean isFragmentOpen = false; // Flag para indicar se há um fragmento aberto

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

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (isFragmentOpen) {
                    getSupportFragmentManager().popBackStack();
                    isFragmentOpen = false;
                    exibirComponentes(true); // Reexibe os botões ao sair do fragmento
                } else {
                    finish(); // Fecha a activity se não houver fragmentos
                }
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
        btnPausa = findViewById(R.id.botaoPausa);
        btnRetornoDaPausa = findViewById(R.id.botaoRetorno);
        btnSaida = findViewById(R.id.botaoSaida);
        btnAbrirMenu = findViewById(R.id.btn_abrir_menu);
        tvTitulo = findViewById(R.id.tvTitulo);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        if (drawerLayout == null || navigationView == null) {
            Log.e("Erro", "DrawerLayout ou NavigationView não encontrados!");
        }
    }

    private void configurarListeners() {
        if (btnEntrada != null) {
            btnEntrada.setOnClickListener(v -> abrirFragmentComTipoPonto("entrada"));
            btnPausa.setOnClickListener(v -> abrirFragmentComTipoPonto("pausa"));
            btnRetornoDaPausa.setOnClickListener(v -> abrirFragmentComTipoPonto("retorno"));
            btnSaida.setOnClickListener(v -> abrirFragmentComTipoPonto("saida"));
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
                    abrirFragment(new PerfilFragment());
                } else if (itemId == R.id.nav_configuracoes) {
                } else if (itemId == R.id.nav_historico) {
                    abrirFragment(new HistoricoFragment());
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

    private void realizarLogout() {
        Toast.makeText(this, "Saindo...", Toast.LENGTH_SHORT).show();
        SessaoManager.logout(this);
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void abrirFragment(Fragment fragment) {
        exibirComponentes(false); // Esconde os componentes
        isFragmentOpen = true;
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void abrirFragmentComTipoPonto(String tipoPonto) {
        RegistroPontoFragment fragment = new RegistroPontoFragment();

        Bundle args = new Bundle();
        args.putString("tipo_ponto", tipoPonto);
        fragment.setArguments(args);

        exibirComponentes(false); // Esconde os componentes
        isFragmentOpen = true;

        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    private void exibirComponentes(boolean exibir) {
        int visibilidade = exibir ? View.VISIBLE : View.GONE;
        if (btnEntrada != null) btnEntrada.setVisibility(visibilidade);
        if (btnPausa != null) btnPausa.setVisibility(visibilidade);
        if (tvTitulo != null) tvTitulo.setVisibility(visibilidade);
        if (btnRetornoDaPausa != null) btnRetornoDaPausa.setVisibility(visibilidade);
        if (btnSaida != null) btnSaida.setVisibility(visibilidade);
    }

    private void abrirTela(Class<?> activity, String mensagem) {
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }
}
