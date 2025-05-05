package com.example.timemarkinghr.controller;

import android.content.Context;
import android.util.Log;

import com.example.timemarkinghr.data.model.RegistroPonto;
import com.example.timemarkinghr.data.remote.ApiService;
import com.example.timemarkinghr.data.remote.RemoteRepository;
import com.example.timemarkinghr.utils.NetworkUtils;
import com.example.timemarkinghr.utils.SessaoManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroPontoController {
    private static final String TAG = "RegistroPontoController";
    private final ApiService apiService;
    private final Context context;

    public RegistroPontoController(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RemoteRepository.getApiService();
    }

    public interface RegistroCallback {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public void registrarPonto(RegistroPonto registro, RegistroCallback callback) {
        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onFailure("Sem conexão com a internet");
            return;
        }

        String token = SessaoManager.getToken(context);
        if (token == null) {
            callback.onFailure("Sessão expirada");
            return;
        }

        Call<Void> call = apiService.registrarPonto(token, registro);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    String errorMsg = "Erro ao registrar ponto";
                    if (response.errorBody() != null) {
                        try {
                            errorMsg = response.errorBody().string();
                        } catch (Exception e) {
                            Log.e(TAG, "Erro ao ler errorBody", e);
                        }
                    }
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(TAG, "Falha na chamada de API", t);
                callback.onFailure("Falha na comunicação: " + t.getMessage());
            }
        });
    }
}