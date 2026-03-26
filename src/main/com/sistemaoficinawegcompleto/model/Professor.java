package com.sistemaoficinawegcompleto.model;

public class Professor extends Usuario{

    private String especialidade;
    private String departamento;

    // Contrutor
    public Professor(int id, String nome, String matriculaWeg, String emailCorporativo, String telefone, String especialidade, String departamento) {
        super(id, nome, matriculaWeg, emailCorporativo, telefone);
        this.especialidade = especialidade;
        this.departamento = departamento;
    }

    // Getters
    public String getEspecialidade() {
        return especialidade;
    }

    public String getDepartamento() {
        return departamento;
    }

    @Override
    public boolean possuiPermissaoGerencial() {
        return true;
    }

    @Override
    public String getPapelSistema() {
        return "PROFESSOR";
    }

    @Override
    public String toString() {
        return getPapelSistema() + " " + getNome() + " (" + especialidade + ") - " + getEmailCorporativo();
    }

}