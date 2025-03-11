package com.example.timemarkinghr.data.model;

public class LoginResponse {
    private boolean sucess;
    private String token;
    private String mensagem;

    public boolean isSucess(){
        return sucess;
    }
    public String getToken() {
        return token;
    }

    public String getMensagem() {
        return mensagem;
    }
}
