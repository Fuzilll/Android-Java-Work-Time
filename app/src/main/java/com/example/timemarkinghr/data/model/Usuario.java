package com.example.timemarkinghr.data.model;

public class Usuario {
    private int id;
    private String nome;
    private String email;
    private String nivel;
    private Integer id_empresa;

    // Getters e Setters
    public int getId() { return id; }
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getNivel() { return nivel; }
    public Integer getIdEmpresa() { return id_empresa; }

    public void setId(int id) { this.id = id; }
    public void setNome(String nome) { this.nome = nome; }
    public void setEmail(String email) { this.email = email; }
    public void setNivel(String nivel) { this.nivel = nivel; }
    public void setIdEmpresa(Integer id_empresa) { this.id_empresa = id_empresa; }
}