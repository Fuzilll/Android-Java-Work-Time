package com.example.timemarkinghr.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.timemarkinghr.R;
import com.example.timemarkinghr.ui.adapter.HistoricoPontoAdapter;
import com.example.timemarkinghr.data.model.RegistroPonto;
import com.example.timemarkinghr.data.remote.ApiService;
import com.example.timemarkinghr.utils.SessaoManager;
import com.example.timemarkinghr.data.remote.RemoteRepository;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoricoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoricoFragment extends Fragment {
    private RecyclerView recyclerViewHistorico;
    private HistoricoPontoAdapter adapter;
    private List<RegistroPonto> listaPontos = new ArrayList<>();
    private int userId;
    private ApiService apiService;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoricoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoricoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoricoFragment newInstance(String param1, String param2) {
        HistoricoFragment fragment = new HistoricoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historico, container, false);

        recyclerViewHistorico = view.findViewById(R.id.recyclerViewHistorico);
        recyclerViewHistorico.setLayoutManager(new LinearLayoutManager(getContext()));

        userId = SessaoManager.obterIdUsuario(requireContext());
        if (userId == -1) {
            Toast.makeText(getContext(), "Erro: Usuário não encontrado.", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return view;
        }

        apiService = RemoteRepository.getApiService();
        if (apiService == null) {
            Toast.makeText(getContext(), "Erro ao conectar com o servidor", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return view;
        }

        adapter = new HistoricoPontoAdapter(listaPontos);
        recyclerViewHistorico.setAdapter(adapter);

        carregarDados();

        return view;
    }

    private void carregarDados() {
        Log.d("HistoricoFragment", "Carregando registros para userId: " + userId);

        Call<List<RegistroPonto>> call = apiService.buscarRegistrosPorUsuario(userId);
        call.enqueue(new Callback<List<RegistroPonto>>() {
            @Override
            public void onResponse(Call<List<RegistroPonto>> call, Response<List<RegistroPonto>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listaPontos.clear();
                    listaPontos.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("HistoricoFragment", "Registros carregados: " + listaPontos.size());
                } else {
                    Toast.makeText(getContext(), "Nenhum registro encontrado", Toast.LENGTH_SHORT).show();
                    Log.e("HistoricoFragment", "Erro na resposta: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<RegistroPonto>> call, Throwable t) {
                Toast.makeText(getContext(), "Erro ao buscar registros: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("HistoricoFragment", "Falha ao carregar registros", t);
            }
        });
    }
}