package com.sistemaoficinawegcompleto.repository;

import com.sistemaoficinawegcompleto.model.OrdemServico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdemServiceRepository {

    private List<OrdemServico> ordemServico = new ArrayList<>();
    private int proximoId = 1000;

    public void salvar(OrdemServico os) {
        ordemServico.add(os);
    }

    public Optional<OrdemServico> buscarPorId(int id) {
        return ordemServico.stream().filter(s -> s.getId() == id).findFirst();
    }

    private int gerarId() {
        return proximoId++;
    }

    public List<OrdemServico> listarTodas() {
        return new  ArrayList<>(ordemServico);
    }

}