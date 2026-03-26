package com.sistemaoficinawegcompleto.model;

public class Coordenador extends Professor {

    private String areaCoordenacao;

    public Coordenador(int id, String nome, String matriculaWeg, String emailCorporativo, String telefone, String especialidade, String departamento, String areaCoordenacao) {
        super(id, nome, matriculaWeg, emailCorporativo, telefone, especialidade, departamento);
        this.areaCoordenacao = areaCoordenacao;
    }

    public String getAreaCoordenacao() {
        return areaCoordenacao;
    }

    @Override
    public String getPapelSistema() {
        return "COORDENADOR";
    }

    @Override
    public String toString() {
        return getPapelSistema() + " : " + getNome() + " | Area: " + areaCoordenacao;
    }

}