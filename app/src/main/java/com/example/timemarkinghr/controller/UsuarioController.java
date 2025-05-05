package com.example.timemarkinghr.controller;

import android.content.Context;
import android.util.Log;

import com.example.timemarkinghr.data.model.LoginRequest;
import com.example.timemarkinghr.data.model.LoginResponse;
import com.example.timemarkinghr.data.remote.ApiService;
import com.example.timemarkinghr.data.remote.RemoteRepository;
import com.example.timemarkinghr.utils.NetworkUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioController {
    private static final String TAG = "UsuarioController";
    private final ApiService apiService;
    private final Context context;

    public UsuarioController(Context context) {
        this.context = context.getApplicationContext();
        this.apiService = RemoteRepository.getApiService();
    }

    public interface LoginCallback {
        void onSuccess(LoginResponse response);

        void onFailure(String errorMessage);
    }

    public void realizarLogin(String email, String senha, LoginCallback callback) {
        if (!validarCredenciais(email, senha, callback)) {
            return;
        }

        if (!NetworkUtils.isNetworkAvailable(context)) {
            callback.onFailure("Sem conexão com a internet");
            return;
        }

        LoginRequest loginRequest = new LoginRequest(email, senha);
        Call<LoginResponse> call = apiService.login(loginRequest);

        call.enqueue(new LoginCallbackHandler(callback));
    }

    private boolean validarCredenciais(String email, String senha, LoginCallback callback) {
        if (email.isEmpty() || senha.isEmpty()) {
            callback.onFailure("Preencha todos os campos");
            return false;
        }
        return true;
    }


    private static class LoginCallbackHandler implements Callback<LoginResponse> {
        private final LoginCallback callback;

        LoginCallbackHandler(LoginCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
            if (response.isSuccessful() && response.body() != null) {
                // Modificação principal aqui - removemos a verificação isSuccess()
                // pois o status 200 já indica sucesso
                callback.onSuccess(response.body());
            } else {
                handleErrorResponse(response);
            }
        }

        @Override
        public void onFailure(Call<LoginResponse> call, Throwable t) {
            Log.e(TAG, "Erro na chamada de API", t);
            callback.onFailure("Falha na comunicação: " + t.getMessage());
        }

        private void handleErrorResponse(Response<LoginResponse> response) {
            try {
                String errorMsg = "Erro no login";
                if (response.errorBody() != null) {
                    errorMsg = response.errorBody().string();
                } else if (response.message() != null) {
                    errorMsg = response.message();
                }
                callback.onFailure(errorMsg);
            } catch (Exception e) {
                Log.e(TAG, "Erro ao processar resposta de erro", e);
                callback.onFailure("Erro ao processar resposta");
            }
        }
    }
}