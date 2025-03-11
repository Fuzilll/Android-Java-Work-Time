package com.example.timemarkinghr.controller;


import com.example.timemarkinghr.data.model.Usuario;
import com.example.timemarkinghr.data.remote.ApiService;
import com.example.timemarkinghr.data.remote.RemoteRepository;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginController {

    public void login(String email, String senha) {
        Usuario usuario = new Usuario();
        usuario.setEmail(email);
        usuario.setSenha(senha);

        ApiService apiService = RemoteRepository.getApiService();
        Call<ApiService.LoginResponse> call = apiService.login(usuario);

        call.enqueue(new Callback<ApiService.LoginResponse>() {
            @Override
            public void onResponse(Call<ApiService.LoginResponse> call, Response<ApiService.LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Usuario logado = response.body().usuario;
                    // Salva no SharedPreferences e navega para tela principal
                } else {
                    // Mostra erro no login
                }
            }

            @Override
            public void onFailure(Call<ApiService.LoginResponse> call, Throwable t) {
                // Trata erro de rede (timeout, etc.)
            }
        });
    }
}
