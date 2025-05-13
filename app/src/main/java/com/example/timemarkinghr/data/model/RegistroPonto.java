package com.example.timemarkinghr.data.model;

import com.google.gson.annotations.SerializedName;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RegistroPonto {
    @SerializedName("id")
    private int id;

    @SerializedName("id_funcionario")
    private int id_funcionario;

    @SerializedName("tipo")
    private String tipo;

    @SerializedName("latitude")
    private double latitude;

    @SerializedName("longitude")
    private double longitude;

    @SerializedName("foto_url")
    private String foto; // Mantido como 'foto' mas com SerializedName para 'foto_url'

    @SerializedName("dispositivo")
    private String dispositivo;

    @SerializedName("data_hora")
    private String data_hora;

    @SerializedName("status")
    private String status;

    @SerializedName("justificativa")
    private String justificativa;

    public RegistroPonto(int id_funcionario, String tipo, double latitude, double longitude, String foto, String dispositivo) {
        this.id_funcionario = id_funcionario;
        this.tipo = tipo;
        this.latitude = latitude;
        this.longitude = longitude;
        this.foto = foto;
        this.dispositivo = dispositivo;
        this.data_hora = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        this.status = "Pendente"; // Valor padrão
    }

    // Getters e Setters existentes (mantidos exatamente como estavam)
    public int getId_funcionario() {
        return id_funcionario;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setId_funcionario(int id_funcionario) {
        this.id_funcionario = id_funcionario;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public void setData_hora(String data_hora) {
        this.data_hora = data_hora;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJustificativa() {
        return justificativa;
    }

    public void setJustificativa(String justificativa) {
        this.justificativa = justificativa;
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

    public String getFotoUrl() {
        return foto;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public String getData_hora() {
        return data_hora;
    }

    // Novos métodos adicionados (opcionais, mas úteis)

    /**
     * Formata a data para exibição amigável
     */
    public String getDataFormatada() {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(data_hora, DateTimeFormatter.ISO_DATE_TIME);
            return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        } catch (Exception e) {
            return data_hora;
        }
    }

    /**
     * Verifica se é um registro de entrada
     */
    public boolean isEntrada() {
        return "Entrada".equalsIgnoreCase(tipo);
    }

    @Override
    public String toString() {
        return "RegistroPonto{" +
                "id=" + id +
                ", id_funcionario=" + id_funcionario +
                ", tipo='" + tipo + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", foto='" + foto + '\'' +
                ", dispositivo='" + dispositivo + '\'' +
                ", data_hora='" + data_hora + '\'' +
                ", status='" + status + '\'' +
                ", justificativa='" + justificativa + '\'' +
                '}';
    }
}