# SistemaOficinaWeg — Refatorado

Sistema de gerenciamento de manutenção de equipamentos da escola técnica WEG, refatorado do monolito original para uma arquitetura orientada a objetos com SOLID e Clean Code.

## Princípios SOLID aplicados

### S — Single Responsibility Principle (SRP)
Cada classe tem uma única responsabilidade:

| Classe | Responsabilidade |
|---|---|
| `Usuario`, `Professor`, `Aluno`, `Turma`, `OrdemServico` | Representar dados e regras internas da entidade |
| `OrdemServicoService` | Orquestrar regras de negócio e autorização |
| `*Repository` | Persistência em memória |
| `MenuPrincipal` | Interação com o usuário (entrada/saída) |
| `Main` | Inicialização e injeção de dependências |

No monolito original, **tudo isso estava em um único método `main()`** de ~150 linhas.

### O — Open/Closed Principle (OCP)
A verificação de permissão usa `possuiPermissaoGerencial()` em `Usuario`.
Para adicionar um novo tipo de usuário privilegiado, basta criar uma nova subclasse que retorne `true` nesse método — **sem modificar `OrdemServicoService`**.

### L — Liskov Substitution Principle (LSP)
`Coordenador extends Professor`. Um `Coordenador` pode ser usado em qualquer lugar que aceite `Professor` sem quebrar o comportamento — inclusive no `OrdemServicoService`, que o trata como `Usuario` via polimorfismo.

### I — Interface Segregation Principle (ISP)
Os métodos abstratos em `Usuario` (`possuiPermissaoGerencial()`, `getPapelSistema()`) são específicos e mínimos. Nenhuma subclasse é forçada a implementar métodos que não usa.

### D — Dependency Inversion Principle (DIP)
`OrdemServicoService` recebe `OrdemServicoRepository` pelo construtor — não instancia ele mesmo. Isso desacopla o serviço da implementação concreta de persistência.

---

## Como a nova arquitetura resolve os problemas do código original

### Problema: God Class com vetores estáticos
**Original:** tudo em arrays estáticos (`usuariosNomes[]`, `osStatus[]`, etc.) com tamanho fixo 100.
**Solução:** cada entidade virou uma classe própria. Repositórios usam `List<T>` com `ArrayList` — escalam dinamicamente sem limite fixo.

### Problema: Regras de autorização espalhadas
**Original:** verificações de permissão com `if/else` repetidos em cada `case` do `switch`.
**Solução:** método `validarPermissaoGerencial()` centralizado no `OrdemServicoService`. Um único ponto de mudança para alterar a lógica de acesso.

### Problema: Segurança frágil no encerramento de OS
**Original:** apenas comparava `osProfessorResponsavel[id] == idProfessor`, sem suporte a coordenador.
**Solução:** `aprovarEEncerrar()` no serviço verifica `ehResponsavel || ehCoordenador` de forma explícita. A classe `Coordenador` implementa o LSP para ser reconhecida sem if/else adicionais.

### Problema: Nome de método com erro de convenção
**Original:** `AprovarEEncerrar()` — nome começando com maiúscula, violando camelCase Java.
**Solução:** renomeado para `aprovarEEncerrar()`.