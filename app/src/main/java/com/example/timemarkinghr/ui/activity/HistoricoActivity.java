package com.example.timemarkinghr.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.timemarkinghr.R;
import com.example.timemarkinghr.data.model.RegistroPonto;
import com.example.timemarkinghr.data.remote.ApiService;
import com.example.timemarkinghr.data.remote.RemoteRepository;
import com.example.timemarkinghr.ui.adapter.HistoricoPontoAdapter;
import com.example.timemarkinghr.utils.SessaoManager;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoActivity extends AppCompatActivity {
    private RecyclerView recyclerViewHistorico;
    private HistoricoPontoAdapter adapter;
    private List<RegistroPonto> listaPontos = new ArrayList<>();
    private int userId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        recyclerViewHistorico = findViewById(R.id.recyclerViewHistoricoPontos);
        recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(this));

        userId = SessaoManager.obterIdUsuario(getApplicationContext());
        if (userId == -1) {
            Toast.makeText(this, "Erro: Usuário não encontrado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        apiService = RemoteRepository.getApiService();
        if (apiService == null) {
            Toast.makeText(this, "Erro ao conectar com o servidor", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        adapter = new HistoricoPontoAdapter(listaPontos);
        recyclerViewHistorico.setAdapter(adapter);

        carregarDados();
    }

    private void carregarDados() {
        Log.d("HistoricoActivity", "Carregando registros para userId: " + userId);

        Call<List<RegistroPonto>> call = apiService.buscarRegistrosPorUsuario(userId);
        call.enqueue(new Callback<List<RegistroPonto>>() {
            @Override
            public void onResponse(Call<List<RegistroPonto>> call, Response<List<RegistroPonto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPontos.clear();
                    listaPontos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("HistoricoActivity", "Registros carregados: " + listaPontos.size());
                } else {
                    Toast.makeText(HistoricoActivity.this, "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
                    Log.e("HistoricoActivity", "Erro na resposta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<RegistroPonto>> call, Throwable t) {
                Toast.makeText(HistoricoActivity.this, "Erro ao buscar registros: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HistoricoActivity", "Falha ao carregar registros", t);
            }
        });
    }
}
