package com.sistemaoficinawegcompleto.model;

import java.util.Objects;

public abstract class Usuario {

    private int id;
    private String nome;
    private String matriculaWeg;
    private String emailCorporativo;
    private String telefone;

    // Contrutor
    public Usuario(int id, String nome, String matriculaWeg, String emailCorporativo, String telefone) {
        this.id = id;
        this.nome = nome;
        this.matriculaWeg = matriculaWeg;
        this.emailCorporativo = emailCorporativo;
        this.telefone = telefone;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getMatriculaWeg() {
        return matriculaWeg;
    }

    public String getEmailCorporativo() {
        return emailCorporativo;
    }

    public String getTelefone() {
        return telefone;
    }

    // Métodos abstratos para poliformismo nas filhas
    public abstract boolean possuiPermissaoGerencial();
    public abstract String getPapelSistema();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}