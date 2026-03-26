package com.sistemaoficinawegcompleto.repository;

import com.sistemaoficinawegcompleto.model.Turma;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TurmaRepository {

    private List<Turma> turmas = new ArrayList<>();

    public void salvar(Turma turma) {
        turmas.add(turma);
    }

    public Optional<Turma> buscarPorId(int id) {
        return turmas.stream().filter(turma -> turma.getId() == id).findFirst();
    }

}