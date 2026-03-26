package com.sistemaoficinawegcompleto.repository;

import com.sistemaoficinawegcompleto.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    private List<Usuario> usuarios = new ArrayList<>();

    public void salvar(Usuario usuario) {
        usuarios.add(usuario);
    }

    public Optional<Usuario> buscarPorId(int id) {
        return usuarios.stream().filter(usuario -> usuario.getId() == id).findFirst();
    }

    public List<Usuario> listarTodos() {
        return new ArrayList<>(usuarios);
    }

}