package com.example.timemarkinghr.data.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("usuario")
    private Usuario usuario;

    @SerializedName("message")
    private String message;

    // Getters e Setters
    public String getToken() { return token; }
    public Usuario getUsuario() { return usuario; }
    public String getMessage() { return message; }
}