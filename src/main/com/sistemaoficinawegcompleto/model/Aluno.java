package com.sistemaoficinawegcompleto.model;

public class Aluno extends Usuario {

    private Turma turma;
    private String dataMatricula;

    // Construtor
    public Aluno(int id, String nome, String matriculaWeg, String emailCorporativo, String telefone, Turma turma, String dataMatricula) {
        super(id, nome, matriculaWeg, emailCorporativo, telefone);
        this.turma = turma;
        this.dataMatricula = dataMatricula;
    }

    // Getter
    public Turma getTurma() {
        return turma;
    }


    @Override
    public boolean possuiPermissaoGerencial() {
        return false;
    }

    @Override
    public String getPapelSistema() {
        return "ALUNO";
    }

    @Override
    public String toString() {
        return getPapelSistema() + ": " + getNome() + " | Turma: " + turma.getNome();
    }
}