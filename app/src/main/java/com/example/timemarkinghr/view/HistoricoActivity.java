package com.example.timemarkinghr.view;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.adapter.PontoAdapter;
import com.example.timemarkinghr.controller.RegistroPontoController;
import com.example.timemarkinghr.model.RegistroPonto;

import java.util.ArrayList;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private PontoAdapter adapter;
    private List<RegistroPonto> listaPontos;
    private RegistroPontoController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        recyclerView = findViewById(R.id.recyclerViewPontos);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listaPontos = new ArrayList<>();
        adapter = new PontoAdapter(listaPontos);
        recyclerView.setAdapter(adapter);

        controller = new RegistroPontoController();
        carregarHistoricoPontos();
    }

    private void carregarHistoricoPontos() {
        controller.carregarHistoricoPontos(pontos -> {
            if (pontos != null) {
                listaPontos.clear();
                listaPontos.addAll(pontos);
                adapter.notifyDataSetChanged();
            } else {
                Toast.makeText(this, "Erro ao carregar histórico", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
