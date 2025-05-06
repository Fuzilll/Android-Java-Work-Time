package com.example.timemarkinghr.data.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroPonto {
    private int id_funcionario;
    private String tipo;
    private double latitude;
    private double longitude;
    private String foto; // Agora armazena a URL em vez de Base64

    private String dispositivo;
    private String data_hora;

    public RegistroPonto(int id_funcionario, String tipo, double latitude, double longitude, String foto, String dispositivo) {
        this.id_funcionario = id_funcionario;
        this.tipo = tipo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foto = foto;
        this.dispositivo = dispositivo;
        this.data_hora = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
    }

    // Getters
    public int getId_funcionario() {
        return id_funcionario;
    }

    public String getTipo() {
        return tipo;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getFotoBase64() {
        return foto;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public String getData_hora() {
        return data_hora;
    }
}