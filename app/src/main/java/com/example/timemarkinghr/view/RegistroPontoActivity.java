package com.example.timemarkinghr.view;

import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timemarkinghr.R;

public class RegistroPontoActivity extends AppCompatActivity {
    private Button botaoEntrada;
    private Button botaoPausa;
    private Button botaoRetorno;
    private Button botaoSaida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registro_ponto);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Referenciando os botões
        botaoEntrada = findViewById(R.id.botaoEntrada);
        botaoPausa = findViewById(R.id.botaoPausa);
        botaoRetorno = findViewById(R.id.botaoRetorno);
        botaoSaida = findViewById(R.id.botaoSaida);

        // Configurando os listeners de clique
        botaoEntrada.setOnClickListener(v -> abrirPontoActivity("Entrada"));
        botaoPausa.setOnClickListener(v -> abrirPontoActivity("Pausa"));
        botaoRetorno.setOnClickListener(v -> abrirPontoActivity("Retorno"));
        botaoSaida.setOnClickListener(v -> abrirPontoActivity("Saída"));
    }

    private void abrirPontoActivity(String tipoPonto) {
        Intent intent = new Intent(RegistroPontoActivity.this, PontoActivity.class);
        intent.putExtra("tipoPonto", tipoPonto);
        startActivity(intent);
    }
}
