package com.example.timemarkinghr.model;

public class Usuario {
    private String uid;          // Identificador único do usuário
    private String nome;         // Nome do usuário
    private String email;        // Email do usuário
    private String senha;        // Senha do usuário
    private String fotoUrl;      // URL da foto do usuário (Firebase Storage)
    private String tipoUsuario;  // Tipo de usuário (Funcionário, Gerente, Administrador)
    private Boolean status;      // Status do usuário (Ativo/Inativo)
    private Long dataCriacao;    // Data de criação da conta
    private Long ultimoAcesso;   // Último acesso do usuário
    private String fcmToken;     // Token de notificação

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getFotoUrl() {
        return fotoUrl;
    }

    public void setFotoUrl(String fotoUrl) {
        this.fotoUrl = fotoUrl;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public Long getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Long dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    public Long getUltimoAcesso() {
        return ultimoAcesso;
    }

    public void setUltimoAcesso(Long ultimoAcesso) {
        this.ultimoAcesso = ultimoAcesso;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

}
