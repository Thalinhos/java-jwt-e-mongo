package com.example.demo.Model;

public class PessoaDto{
    private String nome;
    private String senha;
    private String role;

    public PessoaDto(String nome, String senha, String role) {
        this.nome = nome;
        this.senha = senha;
        this.role = role;
    }

    public PessoaDto() {
    }

    public String getUserName() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getPassword() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getRole() {
        return role;
    }
    public void setRole(String role) {
        this.role = role;
    }
}