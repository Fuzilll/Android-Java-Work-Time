package com.example.timemarkinghr.data.remote;



import com.example.timemarkinghr.data.model.RegistroPonto;
import com.example.timemarkinghr.data.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Rotas de Usuário
    @POST("usuarios/cadastrar")
    Call<Void> cadastrarUsuario(@Body Usuario usuario);

    @POST("usuarios/login")
    Call<LoginResponse> login(@Body Usuario usuario);

    // Rotas de Registro de Ponto
    @POST("registros/cadastrar")
    Call<Void> registrarPonto(@Body RegistroPonto registroPonto);

    @GET("registros/usuario/{userId}")
    Call<List<RegistroPonto>> buscarRegistrosPorUsuario(@Path("userId") int userId);

    // Classe interna para tratar a resposta de login (que contém os dados do usuário logado)
    class LoginResponse {
        public String message;
        public Usuario usuario;
    }
}
