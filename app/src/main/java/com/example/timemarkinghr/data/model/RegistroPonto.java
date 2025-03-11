package com.example.timemarkinghr.data.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class RegistroPonto {
    private String userId;
    private double latitude;
    private double longitude;
    private String fotoBase64;
    private String dataHora;

    public RegistroPonto(String userId, double latitude, double longitude, String fotoBase64) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.fotoBase64 = fotoBase64;
    }

    // Método para obter LocalDateTime formatado
    public LocalDateTime getDataHoraAsLocalDateTime() {
        if (dataHora != null) {
            return LocalDateTime.parse(dataHora, DateTimeFormatter.ISO_DATE_TIME);
        }
        return null;
    }
    // Getters e setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public String getFotoBase64() { return fotoBase64; }
    public void setFotoBase64(String fotoBase64) { this.fotoBase64 = fotoBase64; }
}
