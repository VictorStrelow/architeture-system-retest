package com.sistemaoficinawegcompleto.repository;

import com.sistemaoficinawegcompleto.model.OrdemServico;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdemServicoRepository {

    private List<OrdemServico> ordens = new ArrayList<>();
    private int proximoId = 1;

    public void salvar(OrdemServico os) {
        ordens.add(os);
    }

    public int gerarId() {
        return proximoId++;
    }

    public Optional<OrdemServico> buscarPorId(int id) {
        return ordens.stream().filter(s -> s.getId() == id).findFirst();
    }

    public List<OrdemServico> listarTodas() {
        return new  ArrayList<>(ordens);
    }

}