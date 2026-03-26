package com.sistemaoficinawegcompleto.view;

import com.sistemaoficinawegcompleto.enums.Prioridade;
import com.sistemaoficinawegcompleto.model.*;
import com.sistemaoficinawegcompleto.repository.UsuarioRepository;
import com.sistemaoficinawegcompleto.service.OrdemServicoService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MenuPrincipal {

    private final Scanner sc;
    private final UsuarioRepository usuarioRepository;
    private final OrdemServicoService ordemServicoService;

    public MenuPrincipal(UsuarioRepository usuarioRepository, OrdemServicoService osService) {
        this.sc = new Scanner(System.in);
        this.usuarioRepository = usuarioRepository;
        this.ordemServicoService = osService;
    }

    // Inicia o loop principal do menu
    public void iniciar() {
        int opcao = -1;
        while (opcao != 0) {
            exibirCabecalho();
            opcao = lerInt("Escolha uma opcao: ");

            switch (opcao) {
                case 1: sinalizarProblema(); break;
                case 2: abrirOS();           break;
                case 3: registrarExecucao(); break;
                case 4: aprovarEEncerrar();  break;
                case 5: listarTurmas();      break;
                case 6: listarOS();          break;
                case 0: System.out.println("\nEncerrando sistema. Ate logo!"); break;
                default: System.out.println("Opcao invalida.");
            }
        }
        sc.close();
    }

    // Opcoes do menu
    private void exibirCabecalho() {
        System.out.println("\n========================================");
        System.out.println("   SISTEMA DE MANUTENCAO - ESCOLA WEG  ");
        System.out.println("========================================");
        System.out.println("1 - Sinalizar Problema");
        System.out.println("2 - Abrir Ordem de Servico  (Professor)");
        System.out.println("3 - Registrar Execucao      (Aluno)");
        System.out.println("4 - Aprovar e Encerrar OS   (Professor)");
        System.out.println("5 - Listar Turmas e Alunos");
        System.out.println("6 - Listar Ordens de Servico");
        System.out.println("0 - Sair");
    }

    // Qualquer usuario pode sinalizar um problema, sem autenticacao necessaria
    private void sinalizarProblema() {
        System.out.print("Seu nome: ");
        String nome = sc.nextLine();
        System.out.print("Equipamento com defeito: ");
        String equip = sc.nextLine();
        System.out.print("Descricao do problema: ");
        String desc = sc.nextLine();
        System.out.println("\n[REGISTRO] " + nome + " sinalizou falha em: " + equip);
        System.out.println("Descricao: " + desc);
        System.out.println("(Aguardando professor abrir OS.)");
    }

    // Fluxo de abertura de OS -> autentica professor, coleta dados, escala alunos
    private void abrirOS() {
        System.out.println("\n-- Abrir Ordem de Servico --");

        int idProf = lerInt("Seu ID de Professor: ");
        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorId(idProf);

        if (usuarioOpt.isEmpty() || !usuarioOpt.get().possuiPermissaoGerencial()) {
            System.out.println("ERRO: ID invalido ou usuario nao e professor.");
            return;
        }

        System.out.print("Equipamento: ");
        String equip = sc.nextLine();
        System.out.print("Numero de patrimonio: ");
        String patrimonio = sc.nextLine();
        System.out.print("Defeito encontrado: ");
        String defeito = sc.nextLine();

        System.out.println("Prioridade: 1-BAIXA  2-MEDIA  3-ALTA  4-URGENTE");
        int prio = lerInt("Escolha: ");

        Prioridade prioridade = Prioridade.values()[Math.max(0, Math.min(prio - 1, 3))];

        try {
            OrdemServico os = ordemServicoService.abrirOS(
                    usuarioOpt.get(), equip, patrimonio, defeito, prioridade);

            // Escalar alunos para a OS recem criada
            System.out.println("Escalar alunos (maximo 3). Digite 0 para parar.");

            for (int i = 0; i < 3; i++) {
                int idAluno = lerInt("ID do Aluno " + (i + 1) + " (0 para parar): ");
                if (idAluno == 0) break;

                Optional<Usuario> alunoOpt = usuarioRepository.buscarPorId(idAluno);
                if (alunoOpt.isPresent() && alunoOpt.get() instanceof Aluno) {
                    try {
                        ordemServicoService.escalarAluno(usuarioOpt.get(), os.getId(), (Aluno) alunoOpt.get());
                        System.out.println("  Aluno " + alunoOpt.get().getNome() + " escalado.");

                    } catch (Exception e) {
                        System.out.println("  AVISO: " + e.getMessage());
                        i--;
                    }

                } else {
                    System.out.println("  ID invalido ou nao e aluno.");
                    i--;
                }
            }

            System.out.println("\n[OK] OS #" + os.getId() + " aberta com sucesso!");
            System.out.println(os);

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    // Fluxo de execucao -> aluno registra materiais e laudo
    private void registrarExecucao() {
        System.out.println("\n-- Registrar Execucao --");
        int idOS    = lerInt("ID da OS: ");
        int idAluno = lerInt("Seu ID de Aluno: ");

        Optional<Usuario> usuarioOpt = usuarioRepository.buscarPorId(idAluno);
        if (usuarioOpt.isEmpty() || !(usuarioOpt.get() instanceof Aluno)) {
            System.out.println("ERRO: Aluno nao encontrado.");
            return;
        }

        System.out.print("Materiais e quantidades utilizados: ");
        String materiais = sc.nextLine();
        System.out.print("Laudo tecnico (descricao do que foi feito): ");
        String conclusao = sc.nextLine();

        try {
            ordemServicoService.registrarExecucao(idOS, (Aluno) usuarioOpt.get(), materiais, conclusao);
            System.out.println("[OK] Execucao registrada. OS enviada para aprovacao do professor.");

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    // Fluxo de encerramento -> professor (responsavel ou coordenador) aprova a OS
    private void aprovarEEncerrar() {
        System.out.println("\n-- Aprovar e Encerrar OS --");
        int idOS   = lerInt("ID da OS: ");
        int idProf = lerInt("Seu ID de Professor: ");

        Optional<Usuario> professorOpt = usuarioRepository.buscarPorId(idProf);
        if (professorOpt.isEmpty() || !professorOpt.get().possuiPermissaoGerencial()) {
            System.out.println("ERRO: ID invalido ou usuario nao e professor.");
            return;
        }

        try {
            ordemServicoService.aprovarEEncerrar(professorOpt.get(), idOS);

            ordemServicoService.buscarPorId(idOS).ifPresent(os -> {
                System.out.println("\n========== OS ENCERRADA COM SUCESSO ==========");
                System.out.println("Equipamento : " + os.getEquipamento());
                System.out.println("Patrimonio  : " + os.getNumeroPatrimonio());
                System.out.println("Defeito     : " + os.getDefeitoRelatado());
                System.out.println("Materiais   : " + os.getMateriaisUsados());
                System.out.println("Laudo       : " + os.getConclusaoTecnica());
                System.out.println("==============================================");
            });

        } catch (Exception e) {
            System.out.println("ERRO: " + e.getMessage());
        }
    }

    // Lista turmas agrupando alunos por turma usando Streams
    private void listarTurmas() {
        System.out.println("\n-- Turmas e Alunos --");

        usuarioRepository.listarTodos().stream()
                .filter(u -> u instanceof Aluno)
                .map(u -> (Aluno) u)
                .collect(Collectors.groupingBy(a -> a.getTurma().toString()))
                .forEach((turma, alunos) -> {
                    System.out.println("\n" + turma);
                    alunos.forEach(a -> System.out.println("  ID " + a.getId() + ": " + a.getNome()));
                });
    }

    // Lista todas as OS cadastradas
    private void listarOS() {
        System.out.println("\n-- Ordens de Servico --");
        List<OrdemServico> lista = ordemServicoService.listarTodas();

        if (lista.isEmpty()) {
            System.out.println("Nenhuma OS cadastrada.");
            return;
        }

        lista.forEach(System.out::println);
    }

    // Utilitario de leitura segura
    private int lerInt(String prompt) {
        System.out.print(prompt);
        while (!sc.hasNextInt()) {
            sc.nextLine();
            System.out.print("Entrada invalida. " + prompt);
        }

        int val = sc.nextInt();
        sc.nextLine();

        return val;
    }

}