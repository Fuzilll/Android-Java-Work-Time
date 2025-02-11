package com.example.timemarkinghr.model;

import java.util.Date;

public class RegistroPonto {
    private String userId;
    private String tipoPonto;
    private String dataHora;
    private String localizacao;
    private String imageUrl;

    // Construtor vazio necessário para o Firestore
    public RegistroPonto() {}

    public RegistroPonto(String userId, String tipoPonto, String dataHora, String localizacao, String imageUrl) {
        this.userId = userId;
        this.tipoPonto = tipoPonto;
        this.dataHora = dataHora;
        this.localizacao = localizacao;
        this.imageUrl = imageUrl;
    }

    // Getters e Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTipoPonto() {
        return tipoPonto;
    }

    public void setTipoPonto(String tipoPonto) {
        this.tipoPonto = tipoPonto;
    }

    public String getDataHora() {
        return dataHora;
    }

    public void setDataHora(String dataHora) {
        this.dataHora = dataHora;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public void setLocalizacao(String localizacao) {
        this.localizacao = localizacao;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}