package com.example.timemarkinghr.data.remote;

import com.example.timemarkinghr.data.model.LoginRequest;
import com.example.timemarkinghr.data.model.LoginResponse;
import com.example.timemarkinghr.data.model.RegistroPonto;
import com.example.timemarkinghr.data.model.Usuario;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    // Rotas de Autenticação
    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @POST("auth/recuperar-senha")
    Call<Void> solicitarRecuperacaoSenha(@Body LoginRequest loginRequest);

    @POST("auth/resetar-senha")
    Call<Void> resetarSenha(@Body LoginRequest loginRequest);

    @GET("auth/sessao")
    Call<Usuario> verificarSessao(@Header("Authorization") String token);

    @POST("auth/logout")
    Call<Void> logout(@Header("Authorization") String token);

    // Rotas de Registro de Ponto
    @POST("registros/registrar")
    Call<Void> registrarPonto(
            @Header("Authorization") String token,
            @Body RegistroPonto registroPonto
    );

    @GET("registros/usuario/{userId}")
    Call<List<RegistroPonto>> buscarRegistrosPorUsuario(
            @Header("Authorization") String token,
            @Path("userId") int userId
    );
}