package com.sistemaoficinawegcompleto.service;

import com.sistemaoficinawegcompleto.enums.Prioridade;
import com.sistemaoficinawegcompleto.enums.StatusOS;
import com.sistemaoficinawegcompleto.model.*;
import com.sistemaoficinawegcompleto.repository.OrdemServicoRepository;

import java.util.List;
import java.util.Optional;

public class OrdemServicoService {

    private final OrdemServicoRepository ordemServicoRepository;

    public OrdemServicoService(OrdemServicoRepository ordemServicoRepository) {
        this.ordemServicoRepository = ordemServicoRepository;
    }

    // Caso de Uso 1 -> Abrir OS (apenas professor)
    public OrdemServico abrirOS(Usuario solicitante, String equipamento, String numeroPatrimonio, String defeito, Prioridade prioridade) {
        validarPermissaoGerencial(solicitante, "abrir uma OS");

        Professor professor = (Professor)  solicitante;
        int id = ordemServicoRepository.gerarId();

        OrdemServico ordemServico = new OrdemServico(id, equipamento, numeroPatrimonio, defeito, prioridade, professor);
        ordemServicoRepository.salvar(ordemServico);
        return ordemServico;
    }

    // Caso de Uso 2 -> Escalar aluno para a OS
    public void escalarAluno(Usuario solicitante, int osId, Aluno aluno) {
        validarPermissaoGerencial(solicitante, "escalar alunos");

        OrdemServico ordemServico = buscarOuFalhar(osId);
        ordemServico.escalarAluno(aluno);
    }

    // Caso de Uso 3 -> Aluno registra execução
    public void registrarExecucao(int osId, Aluno aluno, String materiais, String conclusaoTecnica) {
        OrdemServico ordemServico = buscarOuFalhar(osId);

        // Validação para apenas alunos escalados
        boolean estaEscalado = ordemServico.getAlunosEscalados().contains(aluno);
        if (!estaEscalado) {
            throw new SecurityException(
                    "Acesso Negado: " + aluno.getNome() + " não está escalado para a OS #" + osId + "."
            );
        }

        // Validação de estado
        if (ordemServico.getStatus() != StatusOS.EXECUTANDO) {
            throw new IllegalArgumentException(
                    "OS #" + osId + " não está em execução. Status atual: " + ordemServico.getStatus()
            );
        }

        ordemServico.registrarExecucao(materiais, conclusaoTecnica);
    }

    // Caso de Uso 4 -> Aprovar e Encerrar OS (professor responsavel ou coordenador)
    public void aprovarEEncerrar(Usuario solicitante, int osId) {
        validarPermissaoGerencial(solicitante, "encerrar uma OS");
        OrdemServico ordemServico = buscarOuFalhar(osId);

        // Validação responsavel ou coordenador
        boolean ehResponsavel = ordemServico.getProfessorResponsavel().getId() == solicitante.getId();
        boolean ehCoordenador = solicitante instanceof Coordenador;

        if (!ehResponsavel && !ehCoordenador) {
            throw new SecurityException(
                    "Acesso Negado: apenos o professor responsavel ou um coordenador pode encerrar a OS #" + osId + "."
            );
        }

        if (ordemServico.getStatus() != StatusOS.AGUARDANDO_APROVACAO) {
            throw new IllegalStateException(
                    "OS #" + osId + " não está aguardando aprovação. Status atual: " + ordemServico.getStatus()
            );
        }

        ordemServico.aprovarEEncerrar();
    }

    // Consultas
    public List<OrdemServico> listarTodas() {
        return ordemServicoRepository.listarTodas();
    }

    public Optional<OrdemServico> buscarPorId(int id) {
        return ordemServicoRepository.buscarPorId(id);
    }

    // Métodos Auxiliares
    private void validarPermissaoGerencial(Usuario usuario, String acao) {
        if (!usuario.possuiPermissaoGerencial()) {
            throw new SecurityException(
                    "Acesso Negado: apenas professores pode " + acao + "."
            );
        }
    }

    private OrdemServico buscarOuFalhar(int osId) {
        return ordemServicoRepository.buscarPorId(osId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "OS #" + osId + " não encontrada."
                ));
    }

}