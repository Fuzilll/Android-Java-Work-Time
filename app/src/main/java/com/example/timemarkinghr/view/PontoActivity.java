package com.example.timemarkinghr.view;


import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.timemarkinghr.R;
import com.example.timemarkinghr.controller.RegistroPontoController;
import com.example.timemarkinghr.dao.FirestoreDAO;
import com.example.timemarkinghr.model.RegistroPonto;
import com.example.timemarkinghr.model.Usuario;
import com.example.timemarkinghr.service.LocalizacaoService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PontoActivity extends AppCompatActivity {
    private Button btn_marcar_ponto;

    private RegistroPontoController controller;
    private LocalizacaoService localizacaoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ponto);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btn_marcar_ponto = findViewById(R.id.btnMarcarPonto);

        controller = new RegistroPontoController();
        localizacaoService = new LocalizacaoService(this);
        Button btnMarcarPonto = findViewById(R.id.btnMarcarPonto);

        btn_marcar_ponto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                marcarPonto();
            }
        });

    }

    private void marcarPonto() {
        String localizacao = localizacaoService.obterLocalizacao();
        String dataHoraAtual = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
        Usuario usuario = new Usuario("002", "Teste2", "fer@gmail.com");
        RegistroPonto registroPonto = new RegistroPonto(usuario.getUid(), "Entrada", dataHoraAtual, localizacao, "teste.jpg");

        Map<String, Object> ponto = new HashMap<>();
        ponto.put("usuario", Map.of("uid", usuario.getUid(), "nome", usuario.getNome(), "email", usuario.getEmail()));
        ponto.put("registroPonto", Map.of("userId", registroPonto.getUserId(), "tipoPonto", registroPonto.getTipoPonto(), "dataHora", registroPonto.getDataHora(), "localizacao", registroPonto.getLocalizacao(), "imageUrl", registroPonto.getImageUrl()));

        controller.registrarPonto(usuario.getUid(), ponto, new FirestoreDAO.OnFirestoreCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(PontoActivity.this, "Dados enviados com sucesso", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(PontoActivity.this, "Erro ao enviar os dados", Toast.LENGTH_LONG).show();
            }
        });
    }
}
