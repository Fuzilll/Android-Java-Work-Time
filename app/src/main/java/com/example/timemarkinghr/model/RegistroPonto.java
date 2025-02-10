package com.example.timemarkinghr.model;

public class RegistroPonto {
    private String imageUrl;
    private double latitude;
    private double longitude;
    private String tipoPonto;
    private long timestamp;
    private String userId;

    public RegistroPonto() { }

    public RegistroPonto(String imageUrl, double latitude, double longitude, String tipoPonto, long timestamp, String userId) {
        this.imageUrl = imageUrl;
        this.latitude = latitude;
        this.longitude = longitude;
        this.tipoPonto = tipoPonto;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getTipoPonto() {
        return tipoPonto;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getUserId() {
        return userId;
    }
}
