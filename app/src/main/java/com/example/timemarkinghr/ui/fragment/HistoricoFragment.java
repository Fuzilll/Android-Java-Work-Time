package com.example.timemarkinghr.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.data.model.Usuario;
import com.example.timemarkinghr.ui.adapter.HistoricoPontoAdapter;
import com.example.timemarkinghr.data.model.RegistroPonto;
import com.example.timemarkinghr.data.remote.ApiService;
import com.example.timemarkinghr.utils.SessaoManager;
import com.example.timemarkinghr.data.remote.RemoteRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoricoFragment extends Fragment {

    private RecyclerView recyclerViewHistorico;
    private HistoricoPontoAdapter adapter;
    private List<RegistroPonto> listaPontos = new ArrayList<>();
    private int userId;
    private ApiService apiService;
    private AutoCompleteTextView editTextFiltroMes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historico, container, false);

        recyclerViewHistorico = view.findViewById(R.id.recyclerViewHistorico);
        recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoricoPontoAdapter(listaPontos);
        recyclerViewHistorico.setAdapter(adapter);

        // Campo de filtro mês/ano
        editTextFiltroMes = view.findViewById(R.id.tvSelecionarMesAno);
        SharedPreferences prefs = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        int mesSalvo = prefs.getInt("mesSelecionado", Calendar.getInstance().get(Calendar.MONTH));
        int anoSalvo = prefs.getInt("anoSelecionado", Calendar.getInstance().get(Calendar.YEAR));
        editTextFiltroMes.setText(String.format("%02d/%d", mesSalvo + 1, anoSalvo));

        editTextFiltroMes.setOnClickListener(v -> exibirDialogoAnoMes());

        Usuario usuario = SessaoManager.getUsuario(requireContext());
        if (usuario == null) {
            Toast.makeText(getContext(), "Sessão expirada. Faça login novamente.", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return view;
        }
        userId = usuario.getId();

        apiService = RemoteRepository.getApiService();
        if (apiService == null) {
            Toast.makeText(getContext(), "Erro ao conectar com o servidor", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return view;
        }

        carregarDados();
        return view;
    }

    private void exibirDialogoAnoMes() {
        final Calendar hoje = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(requireContext(),
                (view, year, month, dayOfMonth) -> {
                    // Formata a data para exibição: MM/YYYY
                    String mesAnoFormatado = String.format("%02d/%d", month + 1, year);
                    editTextFiltroMes.setText(mesAnoFormatado);

                    // Salva mês e ano
                    SharedPreferences.Editor editor = requireContext()
                            .getSharedPreferences("AppPrefs", Context.MODE_PRIVATE).edit();
                    editor.putInt("mesSelecionado", month);
                    editor.putInt("anoSelecionado", year);
                    editor.apply();

                    Log.d("FiltroMes", "Mês/Ano selecionado: " + mesAnoFormatado);

                    // Aqui você pode aplicar o filtro com base no ano/mês
                    carregarDados(); // ou filtrarPorMesAno(year, month)
                },
                hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), hoje.get(Calendar.DAY_OF_MONTH));

        dialog.setTitle("Selecione mês e ano");
        dialog.show();
    }


    private void carregarDados() {
        Log.d("carregarDados", "Iniciando carregamento dos dados");

        String token = SessaoManager.getToken(requireContext());

        if (token == null) {
            Log.w("carregarDados", "Token não encontrado ou sessão expirada");
            Toast.makeText(getContext(), "Sessão expirada. Faça login novamente.", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return;
        }

        Call<List<RegistroPonto>> call = apiService.listarMeusRegistros(
                "Bearer " + token,
                userId
        );

        call.enqueue(new Callback<List<RegistroPonto>>() {
            @Override
            public void onResponse(Call<List<RegistroPonto>> call, Response<List<RegistroPonto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPontos.clear();
                    listaPontos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Erro ao carregar dados: " + response.code(), Toast.LENGTH_SHORT).show();
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "sem corpo de erro";
                        Log.e("carregarDados", "Erro na resposta da API: " + errorBody);
                    } catch (IOException e) {
                        Log.e("carregarDados", "Erro ao ler corpo de erro", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<RegistroPonto>> call, Throwable t) {
                Toast.makeText(getContext(), "Falha na conexão com o servidor", Toast.LENGTH_SHORT).show();
                Log.e("carregarDados", "Falha na requisição à API", t);
            }
        });
    }
}
