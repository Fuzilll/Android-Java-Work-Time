package com.example.timemarkinghr.controller;

import com.example.timemarkinghr.data.model.Usuario;
import com.example.timemarkinghr.data.remote.ApiService;
import com.example.timemarkinghr.data.remote.RemoteRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsuarioController {

    public interface LoginCallback {
        void onLoginSucesso(Usuario usuario);
        void onLoginFalha(String mensagem);
    }

    public void login(String email, String senha, LoginCallback callback) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senha);

        ApiService apiService = RemoteRepository.getApiService();
        Call<ApiService.LoginResponse> call = apiService.login(usuario);

        call.enqueue(new Callback<ApiService.LoginResponse>() {
            @Override
            public void onResponse(Call<ApiService.LoginResponse> call, Response<ApiService.LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onLoginSucesso(response.body().usuario);

                } else {
                    callback.onLoginFalha("Credenciais inválidas ou erro no servidor.");
                }
            }

            @Override
            public void onFailure(Call<ApiService.LoginResponse> call, Throwable t) {
                callback.onLoginFalha("Erro de conexão: " + t.getMessage());
            }
        });
    }
}
