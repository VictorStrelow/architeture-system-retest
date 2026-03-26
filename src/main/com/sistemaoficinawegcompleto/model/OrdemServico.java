package com.sistemaoficinawegcompleto.model;

import com.sistemaoficinawegcompleto.enums.Prioridade;
import com.sistemaoficinawegcompleto.enums.StatusOS;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class OrdemServico {

    private int id;
    private String equipamento;
    private String numeroPatrimonio;
    private String defeitoRelatado;
    private String materiaisUsados;
    private String conclusaoTecnica;

    private Professor professorResponsavel;
    private List<Aluno> alunosEscalados;

    private StatusOS status;
    private Prioridade prioridade;

    private LocalDateTime dataAbertura;
    private LocalDateTime dataFechamento;

    public OrdemServico(int id, String equipamento, String numeroPatrimonio, String defeitoRelatado, Prioridade prioridade, Professor professorResponsavel) {
        this.id = id;
        this.equipamento = equipamento;
        this.numeroPatrimonio = numeroPatrimonio;
        this.defeitoRelatado = defeitoRelatado;
        this.prioridade = prioridade;
        this.professorResponsavel = professorResponsavel;

        this.alunosEscalados = new ArrayList<>();
        this.status = StatusOS.ABERTA;
        this.dataAbertura = LocalDateTime.now();
    }

    // Regra de Negócio
    public void escalarAluno(Aluno aluno) {
        if (this.alunosEscalados.size() >= 3) {
            throw new IllegalStateException("Limite de 3 alunos por OS excedido.");
        }

        if (this.alunosEscalados.contains(aluno)) {
            throw new IllegalArgumentException("Aluno já está escalado nesta OS.");
        }

        this.alunosEscalados.add(aluno);
    }

    public void registrarExecucao(String materiaisUsados, String conclusaoTecnica) {
        this.materiaisUsados = materiaisUsados;
        this.conclusaoTecnica = conclusaoTecnica;
        this.status = StatusOS.AGUARDANDO_APROVACAO;
    }

    public void AprovarEEncerrar() {
        this.status = StatusOS.CONCLUIDA;
        this.dataFechamento =  LocalDateTime.now();
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getEquipamento() {
        return equipamento;
    }

    public StatusOS getStatus() {
        return status;
    }

    public Professor getProfessorResponsavel() {
        return professorResponsavel;
    }

    public List<Aluno> getAlunosEscalados() {
        return new ArrayList<>(alunosEscalados);
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy  HH:mm");

        return String.format("OS #%04d | %s (Patrimônio: %s) | Status: %s | Prioridade: %s | Abertura: %s ",
                id, equipamento, numeroPatrimonio, status, prioridade, dataAbertura.format(dtf));
    }

}