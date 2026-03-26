package com.sistemaoficinawegcompleto;

import com.sistemaoficinawegcompleto.model.Aluno;
import com.sistemaoficinawegcompleto.model.Coordenador;
import com.sistemaoficinawegcompleto.model.Professor;
import com.sistemaoficinawegcompleto.model.Turma;
import com.sistemaoficinawegcompleto.repository.OrdemServicoRepository;
import com.sistemaoficinawegcompleto.repository.TurmaRepository;
import com.sistemaoficinawegcompleto.repository.UsuarioRepository;
import com.sistemaoficinawegcompleto.service.OrdemServicoService;
import com.sistemaoficinawegcompleto.view.MenuPrincipal;

public class Main {
    public static void main(String[] args) {

        UsuarioRepository usuarioRepository = new UsuarioRepository();
        TurmaRepository turmaRepository = new TurmaRepository();
        OrdemServicoRepository ordemServicoRepository = new OrdemServicoRepository();

        OrdemServicoService ordemServicoService = new OrdemServicoService(ordemServicoRepository);

        // Popula dados iniciais
        popularSistema(usuarioRepository, turmaRepository);

        MenuPrincipal menu = new MenuPrincipal(usuarioRepository, ordemServicoService);
        menu.iniciar();

    }

    private static void popularSistema(UsuarioRepository usuarioRepository, TurmaRepository turmaRepository) {
        // Professores (IDs 1 e 2)
        Professor professor1 = new Professor(1, "Lucas", "CTW001", "lucas@email.com", "(47) 91111-2222", "Desenvolvimento de Sistemas", "Lab TI");
        Professor professor2 = new Professor(2, "Bruno", "CTW002", "bruno@email.com", "(47) 93333-4444", "Mecanica", "Lab Mecanica");

        // Coordenador (ID 3)
        Coordenador coordenador = new Coordenador(3, "Coord. Ana Silva", "CTW003", "ana@email.com", "(47) 95555-6666", "Gestão", "Coordenação", "Coordenação Geral");

        usuarioRepository.salvar(professor1);
        usuarioRepository.salvar(professor2);
        usuarioRepository.salvar(coordenador);

        // 4 turmas com 10, 11, 12 e 13 alunos (IDs começam em 10)
        String[] nomesTurmas = {"Desenvolvimento de Sistemas T1", "Desenvolvimento de Sistemas T2", "Mecanica T3", "Mecanica T4"};
        String[] codigos = {"T001",  "T002", "T003", "T004"};
        String[] turnos = {"Tarde/Noite", "Manhã/Tarde", "Tarde/Noite", "Manhã/Tarde"};

        int idAluno = 10;
        for (int t = 0; t < 4; t++) {
            Turma turma = new Turma(t + 1, codigos[t], nomesTurmas[t], turnos[t]);
            turmaRepository.salvar(turma);

            int qtndAlunos = 10 + t; // 10, 11, 12 e 13 alunos por turma
            for (int i = 0; i < qtndAlunos; i++) {
                Aluno aluno = new Aluno(
                        idAluno,
                        "Estudante_" + idAluno,
                        "ALU" + String.format("%03d", idAluno),
                        "aluno" + idAluno + "email.com",
                        "(47) 90000-" + String.format("%04d", idAluno),
                        turma,
                        "2026-03-26"
                );

                usuarioRepository.salvar(aluno);
                idAluno++;
            }
        }

        int totalAlunos = idAluno - 10;
        System.out.println("Sistema WEG Inicializado: ");
        System.out.println(" - 2 Professores (IDs 1 e 2)");
        System.out.println(" - 1 Coordenador (ID 3)");
        System.out.println(" - " + totalAlunos + " Alunos (IDs 10 a " + (idAluno - 1) + ")");

    }

}