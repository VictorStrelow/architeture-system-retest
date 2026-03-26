package com.sistemaoficinawegcompleto.model;

public class Turma {

    private int id;
    private String codigoTurma;
    private String nome;
    private String turno;

    // Construtor
    public Turma(int id, String codigoTurma, String nome, String turno) {
        this.id = id;
        this.codigoTurma = codigoTurma;
        this.nome = nome;
        this.turno = turno;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getCodigoTurma() {
        return codigoTurma;
    }

    public String getNome() {
        return nome;
    }

    @Override
    public String toString() {
        return nome + " (" + codigoTurma + " - " + turno + ")";
    }

}