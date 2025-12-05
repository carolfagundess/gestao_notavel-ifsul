# Gestão Notável - Sistema de Gestão de Terapias Infantis

## 📋 Sumário

1. [Visão Geral](#visão-geral)
2. [Arquitetura do Sistema](#arquitetura-do-sistema)
3. [Tecnologias Utilizadas](#tecnologias-utilizadas)
4. [Estrutura do Projeto](#estrutura-do-projeto)
5. [Modelo de Dados](#modelo-de-dados)
6. [Camadas da Aplicação](#camadas-da-aplicação)
7. [Funcionalidades](#funcionalidades)
8. [Segurança e Controle de Acesso](#segurança-e-controle-de-acesso)
9. [Configuração e Execução](#configuração-e-execução)
10. [Manutenção e Suporte](#manutenção-e-suporte)

---

## 🎯 Visão Geral

O **Gestão Notável** é um sistema desktop desenvolvido para otimizar a gestão de terapias infantis, proporcionando controle integrado de pacientes, especialistas, atendimentos, voluntariado e movimentações financeiras. A aplicação foi projetada para instituições que prestam atendimento terapêutico a crianças, oferecendo uma interface intuitiva e recursos robustos para o gerenciamento completo das operações.

### Objetivos do Sistema

- Centralizar informações de pacientes e seus responsáveis
- Gerenciar agendamentos de atendimentos com especialistas
- Controlar atividades de voluntariado com sistema de créditos
- Registrar e acompanhar movimentações financeiras
- Gerar relatórios gerenciais e operacionais
- Garantir segurança e privacidade dos dados sensíveis
- Implementar controle de acesso baseado em perfis de usuário

---

## 🏗️ Arquitetura do Sistema

O sistema segue uma **arquitetura em camadas** (Layered Architecture), promovendo a separação de responsabilidades e facilitando a manutenção e escalabilidade.

```
┌─────────────────────────────────────┐
│    Camada de Apresentação (UI)      │
│         JavaFX + FXML + CSS         │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│    Camada de Controle (Controller)  │
│       Gerenciamento de Eventos      │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  Camada de Serviço (Business Logic) │
│    Validações e Regras de Negócio   │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│  Camada de Persistência (DAO/JPA)   │
│      Acesso ao Banco de Dados       │
└──────────────┬──────────────────────┘
               │
┌──────────────▼──────────────────────┐
│       Banco de Dados (MySQL)        │
│         Armazenamento de Dados      │
└─────────────────────────────────────┘
```

### Princípios Arquiteturais

- **Separação de Responsabilidades**: Cada camada possui funções bem definidas
- **Baixo Acoplamento**: Dependências gerenciadas através de interfaces e fábricas
- **Alta Coesão**: Classes com responsabilidades únicas e bem delimitadas
- **Inversão de Controle**: Uso de injeção de dependências via ServiceFactory

---

## 💻 Tecnologias Utilizadas

### Core

| Tecnologia | Versão | Propósito |
|-----------|--------|-----------|
| **Java** | 11 | Linguagem de programação base |
| **JavaFX** | 19 | Framework para interface gráfica |
| **Maven** | - | Gerenciamento de dependências e build |

### Persistência

| Tecnologia | Versão | Propósito |
|-----------|--------|-----------|
| **Hibernate** | 5.6.15.Final | Framework ORM (Object-Relational Mapping) |
| **JPA** | 2.2 | API de persistência Java |
| **MySQL** | 8.x | Sistema de gerenciamento de banco de dados |
| **MySQL Connector/J** | 8.0.33 | Driver JDBC para MySQL |

### Segurança

| Tecnologia | Versão | Propósito |
|-----------|--------|-----------|
| **jBCrypt** | 0.4 | Criptografia de senhas |
| **AES/CBC/PKCS5** | - | Criptografia de dados sensíveis (CPF) |

### Estrutura de Arquivos

- **FXML**: Definição de layouts de interface
- **CSS**: Estilização visual das telas
- **XML**: Configuração de persistência e build

---

## 📁 Estrutura do Projeto

```
gestao_notavel-ifsul/
│
├── src/main/
│   ├── java/br/com/gestaonotavel/ifsul/
│   │   ├── App.java                    # Classe principal JavaFX
│   │   ├── Launcher.java               # Ponto de entrada da aplicação
│   │   │
│   │   ├── controller/                 # Controladores de telas
│   │   │   ├── TelaLoginController.java
│   │   │   ├── TelaPrincipalController.java
│   │   │   ├── TelaCadastroPacienteController.java
│   │   │   ├── TelaAgendamentoController.java
│   │   │   ├── TelaCadastroEspecialistaController.java
│   │   │   ├── TelaRelatorioFinanceiroController.java
│   │   │   └── ... (outros controladores)
│   │   │
│   │   ├── model/                      # Entidades JPA
│   │   │   ├── Usuario.java
│   │   │   ├── Paciente.java
│   │   │   ├── Responsavel.java
│   │   │   ├── Especialista.java
│   │   │   ├── Atendimento.java
│   │   │   ├── Atividade.java
│   │   │   ├── MovimentacaoFinanceira.java
│   │   │   ├── ParticipacaoAtividade.java
│   │   │   ├── AuditoriaLog.java
│   │   │   ├── Role.java (enum)
│   │   │   ├── Permission.java (enum)
│   │   │   └── ... (outros modelos)
│   │   │
│   │   ├── service/                    # Lógica de negócio
│   │   │   ├── UsuarioService.java
│   │   │   ├── PacienteService.java
│   │   │   ├── ResponsavelService.java
│   │   │   ├── AtendimentoService.java
│   │   │   ├── RelatorioService.java
│   │   │   ├── factory/
│   │   │   │   └── ServiceFactory.java # Singleton para serviços
│   │   │   └── ... (outros serviços)
│   │   │
│   │   ├── dao/                        # Acesso a dados
│   │   │   ├── UsuarioDAO.java
│   │   │   ├── PacienteDAO.java
│   │   │   ├── AtendimentoDAO.java
│   │   │   └── ... (outros DAOs)
│   │   │
│   │   └── util/                       # Utilitários
│   │       ├── JpaUtil.java            # Gerenciamento EntityManager
│   │       ├── SessionManager.java     # Gestão de sessão do usuário
│   │       ├── EncryptionUtil.java     # Criptografia AES
│   │       ├── ValidationUtil.java     # Validações (CPF, etc.)
│   │       ├── AlertUtil.java          # Diálogos e alertas
│   │       ├── MaskUtil.java           # Máscaras de entrada
│   │       ├── DataInitializer.java    # População inicial do banco
│   │       └── DataChangeManager.java  # Observador de mudanças
│   │
│   └── resources/
│       ├── META-INF/
│       │   └── persistence.xml         # Configuração JPA
│       ├── view/                       # Arquivos FXML
│       │   ├── TelaLogin.fxml
│       │   ├── TelaPrincipal.fxml
│       │   └── ... (outras views)
│       └── styles/                     # Arquivos CSS
│           ├── telalogin.css
│           ├── telaprincipal.css
│           └── ... (outros estilos)
│
├── pom.xml                             # Configuração Maven
├── nbactions.xml                       # Ações NetBeans
└── documentação.md                     # Este arquivo
```

---

## 🗄️ Modelo de Dados

### Entidades Principais

#### 1. Usuario
Representa os usuários do sistema com diferentes níveis de acesso.

**Atributos:**
- `id` (Long): Identificador único
- `nome` (String): Nome completo
- `email` (String): Email único
- `cpf` (String): CPF único (11 caracteres)
- `senha` (String): Senha criptografada com BCrypt
- `telefone` (String): Telefone de contato
- `role` (Role): Perfil de acesso (ADMIN, SECRETARIO)

**Relacionamentos:**
- Não possui relacionamentos diretos com outras entidades

#### 2. Paciente
Representa as crianças atendidas pela instituição.

**Atributos:**
- `idPaciente` (Long): Identificador único
- `nome` (String): Nome completo
- `cpf` (String): CPF criptografado (AES)
- `dataNascimento` (LocalDate): Data de nascimento
- `escolaridade` (String): Nível escolar
- `diagnostico` (String): Diagnóstico clínico
- `condicaoClinica` (String): Condição clínica atual
- `observacoesGerais` (Text): Observações adicionais

**Relacionamentos:**
- `responsaveisLista` (ManyToMany): Lista de responsáveis vinculados
- Vínculo através da tabela `vinculo_responsavel`

#### 3. Responsavel
Representa os responsáveis pelos pacientes e participantes do voluntariado.

**Atributos:**
- `id` (Long): Identificador único
- `nome` (String): Nome completo
- `cpf` (String): CPF criptografado (AES)
- `telefone` (String): Telefone de contato
- `dataNascimento` (LocalDate): Data de nascimento
- `horasVoluntariado` (Double): Total de horas de voluntariado
- `creditos` (Double): Créditos acumulados

**Relacionamentos:**
- `pacientesLista` (ManyToMany): Pacientes vinculados
- `participacoes` (OneToMany): Participações em atividades

#### 4. Especialista
Representa os profissionais que realizam atendimentos.

**Atributos:**
- `idEspecialista` (Integer): Identificador único
- `nome` (String): Nome completo
- `especialidade` (String): Área de especialização
- `valorSessao` (Double): Valor cobrado por sessão
- `duracao` (Integer): Duração padrão das sessões
- `maxPacientes` (Integer): Capacidade máxima de pacientes
- `pacientesAtuais` (Integer): Número atual de pacientes
- `registroProfissional` (String): Registro único do profissional

**Relacionamentos:**
- Atendimentos realizados (OneToMany implícito)
- Movimentações financeiras associadas (OneToMany implícito)

#### 5. Atendimento
Representa os agendamentos e atendimentos realizados.

**Atributos:**
- `idAtendimento` (Integer): Identificador único
- `dataHora` (LocalDateTime): Data e hora do atendimento
- `local` (String): Local de realização
- `statusAtendimento` (StatusAtendimento): Status (AGENDADO, REALIZADO, CANCELADO)
- `observacao` (String): Observações sobre o atendimento

**Relacionamentos:**
- `paciente` (ManyToOne): Paciente atendido
- `especialista` (ManyToOne): Profissional responsável

#### 6. Atividade
Representa atividades e eventos realizados pela instituição.

**Atributos:**
- `idAtividade` (Integer): Identificador único
- `nome` (String): Nome da atividade
- `dataInicio` (LocalDateTime): Data e hora de início
- `dataFim` (LocalDateTime): Data e hora de término
- `local` (String): Local de realização
- `valorArrecadado` (Double): Valor total arrecadado

**Relacionamentos:**
- `tipo` (ManyToOne): Tipo de atividade
- Participações de voluntários (OneToMany implícito)

#### 7. ParticipacaoAtividade
Registra a participação de responsáveis em atividades voluntárias.

**Atributos:**
- `id` (Long): Identificador único
- `dataRegistro` (LocalDateTime): Data de registro
- `horasTrabalhadas` (Double): Horas dedicadas
- `creditosGerados` (Double): Créditos gerados (R$ 10/hora)
- `funcaoDesempenhada` (String): Função exercida

**Relacionamentos:**
- `responsavel` (ManyToOne): Voluntário participante
- `atividade` (ManyToOne): Atividade realizada

**Regra de Negócio:**
- Cálculo automático: `creditosGerados = horasTrabalhadas * 10.00`

#### 8. MovimentacaoFinanceira
Registra entradas e saídas financeiras.

**Atributos:**
- `idMovimentacaoFinanceira` (Integer): Identificador único
- `tipoMovimento` (TipoMovimento): ENTRADA ou SAIDA
- `valorMovimentacao` (Double): Valor da transação
- `dataMovimentacao` (LocalDateTime): Data da movimentação
- `observacao` (String): Descrição da transação
- `formaPagamento` (FormaPagamento): Forma de pagamento

**Relacionamentos:**
- `especialista` (ManyToOne): Especialista relacionado (opcional)
- `atividade` (ManyToOne): Atividade relacionada (opcional)

#### 9. AuditoriaLog
Registra ações realizadas no sistema para auditoria.

**Atributos:**
- `id` (Long): Identificador único
- `timestamp` (LocalDateTime): Data e hora da ação
- `usuarioNome` (String): Nome do usuário que executou a ação
- `acao` (String): Descrição da ação realizada

**Propósito:**
- Rastreabilidade de operações críticas
- Suporte a auditorias e investigações
- Histórico de alterações no sistema

### Enumerações (Enums)

#### Role
Define os perfis de acesso ao sistema.

```java
- SECRETARIO: Acesso operacional
- ADMIN: Acesso administrativo completo
```

#### Permission
Define permissões granulares do sistema.

```java
- VISUALIZAR_PACIENTES
- CADASTRAR_PACIENTE
- EDITAR_PACIENTE
- EXCLUIR_PACIENTE
- CRIAR_AGENDAMENTO
- EDITAR_AGENDAMENTO
- CANCELAR_AGENDAMENTO
- REGISTRAR_VOLUNTARIADO
- VER_VOLUNTARIADO
- VER_FINANCEIRO
- EDITAR_FINANCEIRO
- GERAR_RELATORIOS
- ADMINISTRAR_USUARIOS
```

#### StatusAtendimento
```java
- AGENDADO
- REALIZADO
- CANCELADO
```

#### TipoMovimento
```java
- ENTRADA
- SAIDA
```

#### FormaPagamento
```java
- DINHEIRO
- PIX
- CARTAO_CREDITO
- CARTAO_DEBITO
- TRANSFERENCIA
```

### Diagrama de Relacionamentos

```
Usuario
  (sem relacionamentos diretos)

Paciente ←──────ManyToMany──────→ Responsavel
    ↓                                   ↓
    │                                   │
    └──────→ Atendimento                │
              ↓                         │
         Especialista                   │
              ↓                         │
         MovimentacaoFinanceira         │
                                        │
                                   ParticipacaoAtividade
                                        ↓
                                   Atividade ←── TipoAtividade
                                        ↓
                                   MovimentacaoFinanceira
```

---

## 🔧 Camadas da Aplicação

### 1. Camada de Modelo (Model)

**Responsabilidade:** Representar as entidades de negócio e suas regras básicas.

**Características:**
- Anotações JPA para mapeamento objeto-relacional
- Métodos de ciclo de vida JPA (@PrePersist, @PostLoad)
- Criptografia automática de dados sensíveis
- Implementação de equals() e hashCode()

**Exemplo de Funcionalidade Crítica:**

```java
// Criptografia automática de CPF em Paciente
@PrePersist
@PreUpdate
public void criptografarDados() {
    this.cpf = EncryptionUtil.encrypt(this.cpf);
}

@PostLoad
public void descriptografarDados() {
    this.cpf = EncryptionUtil.decrypt(this.cpf);
}
```

### 2. Camada de Persistência (DAO)

**Responsabilidade:** Gerenciar operações CRUD e consultas ao banco de dados.

**Padrão Implementado:** Data Access Object (DAO)

**Características:**
- Uso de EntityManager para operações JPA
- Gerenciamento de transações
- Tratamento de exceções de persistência
- Queries JPQL para consultas complexas

**Exemplo de Operação:**

```java
public Paciente salvarPaciente(Paciente paciente) {
    EntityManager em = JpaUtil.getEntityManager();
    EntityTransaction tx = em.getTransaction();
    tx.begin();
    try {
        Paciente pacienteSalvo = em.merge(paciente);
        tx.commit();
        return pacienteSalvo;
    } catch (PersistenceException ex) {
        tx.rollback();
        throw ex;
    } finally {
        em.close();
    }
}
```

### 3. Camada de Serviço (Service)

**Responsabilidade:** Implementar regras de negócio e orquestrar operações.

**Características:**
- Validação de dados de entrada
- Aplicação de regras de negócio
- Coordenação entre múltiplos DAOs
- Registro de logs de auditoria
- Lançamento de exceções customizadas (RegraDeNegocioException)

**Exemplo de Regra de Negócio:**

```java
public Usuario autenticarUsuario(String login, String senha) {
    String loginLimpo = login.replaceAll("[^0-9]", "");
    Usuario usuario = usuarioDAO.buscarPorCpf(loginLimpo);
    
    if (usuario != null && BCrypt.checkpw(senha, usuario.getSenha())) {
        return usuario;
    }
    
    throw new RegraDeNegocioException("CPF ou senha inválidos.");
}
```

### 4. Camada de Controle (Controller)

**Responsabilidade:** Gerenciar a lógica de apresentação e eventos da UI.

**Características:**
- Anotações @FXML para binding com elementos da interface
- Inicialização de componentes (Initializable)
- Manipulação de eventos de usuário
- Atualização dinâmica da interface
- Navegação entre telas
- Validação de entrada de dados

**Exemplo de Inicialização:**

```java
@Override
public void initialize(URL url, ResourceBundle rb) {
    configurarColunas();
    carregarDados();
    aplicarFiltros();
    configurarPermissoes();
}
```

### 5. Camada de Apresentação (View)

**Responsabilidade:** Definir a estrutura e aparência das telas.

**Tecnologias:**
- **FXML**: Layout declarativo da interface
- **CSS**: Estilização visual
- **JavaFX Controls**: Componentes interativos

**Componentes Principais:**
- TableView: Listagens de dados
- TextField: Entrada de texto
- DatePicker: Seleção de datas
- ComboBox: Listas suspensas
- Button: Ações do usuário
- Label: Exibição de informações

---

## ⚙️ Funcionalidades

### 1. Autenticação e Controle de Acesso

**Tela de Login**
- Autenticação por CPF e senha
- Senha criptografada com BCrypt
- Validação de CPF
- Máscaras de entrada automáticas
- Mensagens de erro informativas

**Gerenciamento de Sessão**
- SessionManager singleton para controle de usuário logado
- Verificação de permissões em tempo real
- Controle de visibilidade de elementos baseado no perfil

### 2. Gestão de Pacientes

**Cadastro de Pacientes**
- Dados pessoais completos
- Criptografia de CPF
- Vínculo com responsáveis
- Informações clínicas
- Histórico de atendimentos

**Listagem de Pacientes**
- Visualização em tabela
- Filtros por status, responsável e texto
- Cálculo automático de idade
- Ações rápidas (editar, excluir, detalhes)
- Estatísticas em tempo real

### 3. Gestão de Responsáveis

**Cadastro de Responsáveis**
- Dados pessoais
- Criptografia de CPF
- Controle de créditos
- Registro de horas de voluntariado
- Vínculo com múltiplos pacientes

**Sistema de Créditos**
- Geração automática: R$ 10,00 por hora trabalhada
- Acúmulo de créditos para compensação de sessões
- Histórico de participações
- Relatórios de voluntariado

### 4. Gestão de Especialistas

**Cadastro de Especialistas**
- Dados profissionais
- Especialidade
- Valor de sessão
- Duração padrão de atendimento
- Capacidade de atendimento
- Registro profissional único

**Controle de Agenda**
- Visualização de disponibilidade
- Controle de capacidade
- Histórico de atendimentos

### 5. Agendamento de Atendimentos

**Criação de Agendamentos**
- Seleção de paciente
- Seleção de especialista
- Definição de data e hora
- Local de atendimento
- Status inicial: AGENDADO

**Gerenciamento de Atendimentos**
- Alteração de status (REALIZADO, CANCELADO)
- Edição de informações
- Cancelamento com justificativa
- Visualização em calendário mensal

**Calendário Mensal**
- Visualização por mês
- Agrupamento por especialista
- Indicadores visuais de status
- Navegação rápida entre meses

### 6. Gestão de Atividades

**Cadastro de Atividades**
- Nome e descrição
- Tipo de atividade
- Data de início e fim
- Local de realização
- Valor arrecadado

**Tipos de Atividade**
- Categorização personalizável
- Gerenciamento de tipos
- Vinculação com atividades

### 7. Registro de Voluntariado

**Registro de Participação**
- Seleção de responsável
- Seleção de atividade
- Horas trabalhadas
- Função desempenhada
- Cálculo automático de créditos

**Validações**
- Horas trabalhadas > 0
- Atividade válida e existente
- Responsável cadastrado

### 8. Movimentações Financeiras

**Registro de Movimentações**
- Tipo: ENTRADA ou SAIDA
- Valor da transação
- Data da movimentação
- Forma de pagamento
- Observações detalhadas
- Vinculação opcional com especialista ou atividade

**Controle Financeiro**
- Listagem de movimentações
- Filtros por período
- Cálculo de saldo
- Exportação de relatórios

### 9. Relatórios

**Relatório de Atendimentos**
- Filtro por período
- Agrupamento por especialista
- Detalhamento por paciente
- Estatísticas de comparecimento

**Relatório Financeiro**
- Movimentações por período
- Total de entradas e saídas
- Saldo do período
- Detalhamento por forma de pagamento

**Relatório de Voluntariado**
- Lista de responsáveis voluntários
- Horas acumuladas
- Créditos gerados
- Histórico de participações
- Atividades realizadas

### 10. Auditoria e Logs

**Sistema de Auditoria**
- Registro automático de ações críticas
- Timestamp de cada operação
- Identificação do usuário responsável
- Descrição detalhada da ação

**Visualização de Logs**
- Tela dedicada para consulta
- Filtros por usuário e período
- Ordenação por data
- Rastreabilidade completa

---

## 🔐 Segurança e Controle de Acesso

### Criptografia de Dados

#### Senhas de Usuários
- **Algoritmo:** BCrypt
- **Características:**
  - Hash unidirecional
  - Salt automático
  - Resistente a ataques de força bruta
  - Verificação segura sem descriptografia

#### Dados Sensíveis (CPF)
- **Algoritmo:** AES/CBC/PKCS5Padding
- **Chave:** 256 bits (32 bytes)
- **Vetor de Inicialização:** 128 bits (16 bytes)
- **Aplicação:** Automática através de hooks JPA
  - @PrePersist / @PreUpdate: Criptografia
  - @PostLoad: Descriptografia

**Implementação:**

```java
private static final String SECRET_KEY = "12345678901234567890123456789012";
private static final String INIT_VECTOR = "1234567890123456";
```

> **⚠️ IMPORTANTE:** Em ambiente de produção, estas chaves devem ser armazenadas de forma segura em arquivos de configuração externos ou serviços de gerenciamento de segredos.

### Controle de Acesso Baseado em Perfis (RBAC)

#### Perfis Disponíveis

**ADMIN (Administrador)**
- Acesso total ao sistema
- Gerenciamento de usuários
- Todas as permissões disponíveis

**SECRETARIO (Secretário)**
- Acesso operacional
- Sem permissão para excluir pacientes
- Sem acesso à administração de usuários
- Acesso restrito a edições financeiras

#### Matriz de Permissões

| Funcionalidade | SECRETARIO | ADMIN |
|----------------|------------|-------|
| Visualizar Pacientes | ✅ | ✅ |
| Cadastrar Paciente | ✅ | ✅ |
| Editar Paciente | ✅ | ✅ |
| Excluir Paciente | ❌ | ✅ |
| Criar Agendamento | ✅ | ✅ |
| Editar Agendamento | ✅ | ✅ |
| Cancelar Agendamento | ✅ | ✅ |
| Registrar Voluntariado | ✅ | ✅ |
| Ver Voluntariado | ✅ | ✅ |
| Ver Financeiro | ✅ | ✅ |
| Editar Financeiro | ❌ | ✅ |
| Gerar Relatórios | ✅ | ✅ |
| Administrar Usuários | ❌ | ✅ |

#### Implementação de Verificação

```java
// No SessionManager
public boolean hasPermission(Permission permission) {
    if (!isLogado()) return false;
    return this.usuarioLogado.hasPermission(permission);
}

// Nos Controllers
if (SessionManager.getInstance().hasPermission(Permission.EXCLUIR_PACIENTE)) {
    btnExcluir.setVisible(true);
} else {
    btnExcluir.setVisible(false);
}
```

### Validações de Dados

#### Validação de CPF
- Verificação de formato (11 dígitos)
- Validação de dígitos verificadores
- Rejeição de CPFs conhecidos como inválidos
- Remoção automática de máscaras

```java
public static boolean validarCPF(String cpf) {
    String cpfLimpo = cpf.replaceAll("[^0-9]", "");
    if (cpfLimpo.length() != 11) return false;
    if (CPFS_INVALIDOS_CONHECIDOS.contains(cpfLimpo)) return false;
    // Validação de dígitos verificadores...
}
```

#### Validações de Formulários
- Campos obrigatórios
- Formatos de dados (email, telefone, CPF)
- Valores numéricos válidos
- Datas consistentes
- Unicidade de registros (CPF, email, registro profissional)

### Gerenciamento de Sessão

**SessionManager (Singleton)**
- Armazena usuário logado
- Fornece métodos de verificação de permissão
- Garante thread-safety (synchronized)
- Encerramento seguro de sessão

```java
SessionManager.getInstance().iniciarSessao(usuario);
Usuario logado = SessionManager.getInstance().getUsuarioLogado();
SessionManager.getInstance().encerrarSessao();
```

---

## 🚀 Configuração e Execução

### Pré-requisitos

1. **Java Development Kit (JDK) 11 ou superior**
   - Download: [Oracle JDK](https://www.oracle.com/java/technologies/javase-jdk11-downloads.html) ou [OpenJDK](https://adoptopenjdk.net/)

2. **Apache Maven 3.6+**
   - Download: [Maven](https://maven.apache.org/download.cgi)

3. **MySQL Server 8.x**
   - Download: [MySQL](https://dev.mysql.com/downloads/mysql/)

4. **IDE recomendada**
   - NetBeans 12+ (recomendado)
   - IntelliJ IDEA
   - Eclipse

### Configuração do Banco de Dados

#### 1. Criar o Banco de Dados

```sql
CREATE DATABASE gestao_notavel 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;
```

#### 2. Configurar Acesso

Editar o arquivo `src/main/resources/META-INF/persistence.xml`:

```xml
<property name="javax.persistence.jdbc.url" 
          value="jdbc:mysql://localhost:3306/gestao_notavel?..."/>
<property name="javax.persistence.jdbc.user" value="SEU_USUARIO"/>
<property name="javax.persistence.jdbc.password" value="SUA_SENHA"/>
```

#### 3. Criação Automática de Tabelas

O Hibernate está configurado para criar as tabelas automaticamente:

```xml
<property name="javax.persistence.schema-generation.database.action" 
          value="update"/>
```

### Compilação e Execução

#### Via Maven (Linha de Comando)

```bash
# Compilar o projeto
mvn clean compile

# Executar a aplicação
mvn javafx:run

# Gerar pacote JAR
mvn clean package
```

#### Via IDE (NetBeans)

1. Abrir o projeto no NetBeans
2. Clicar com botão direito no projeto
3. Selecionar "Clean and Build"
4. Clicar em "Run" ou pressionar F6

### Primeiro Acesso

#### Usuário Administrador Padrão

Ao executar pela primeira vez, o sistema cria automaticamente um usuário administrador:

- **CPF:** `00637798041`
- **Senha:** `admin`

> **⚠️ IMPORTANTE:** Altere a senha padrão imediatamente após o primeiro acesso em ambiente de produção.

#### População de Dados de Teste

A classe `DataInitializer` pode ser utilizada para popular o banco com dados de teste. Ela é chamada automaticamente no método `main()` da classe `App.java` (comentar/descomentar conforme necessário).

### Estrutura de Configuração

#### persistence.xml

```xml
<persistence-unit name="gestao-notavel-pu" transaction-type="RESOURCE_LOCAL">
    <provider>org.hibernate.jpa.HibernatePersistenceProvider</provider>
    <properties>
        <!-- Conexão -->
        <property name="javax.persistence.jdbc.url" value="..."/>
        <property name="javax.persistence.jdbc.user" value="root"/>
        <property name="javax.persistence.jdbc.driver" value="com.mysql.cj.jdbc.Driver"/>
        <property name="javax.persistence.jdbc.password" value=""/>
        
        <!-- Schema -->
        <property name="javax.persistence.schema-generation.database.action" 
                  value="update"/>
        
        <!-- Hibernate -->
        <property name="hibernate.dialect" value="org.hibernate.dialect.MySQL8Dialect"/>
        <property name="hibernate.show_sql" value="true"/>
        <property name="hibernate.format_sql" value="true"/>
    </properties>
</persistence-unit>
```

#### pom.xml (Dependências Principais)

```xml
<dependencies>
    <!-- JavaFX -->
    <dependency>
        <groupId>org.openjfx</groupId>
        <artifactId>javafx-controls</artifactId>
        <version>19</version>
    </dependency>
    
    <!-- Hibernate -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.6.15.Final</version>
    </dependency>
    
    <!-- MySQL -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version>
    </dependency>
    
    <!-- Criptografia -->
    <dependency>
        <groupId>org.mindrot</groupId>
        <artifactId>jbcrypt</artifactId>
        <version>0.4</version>
    </dependency>
</dependencies>
```

### Resolução de Problemas Comuns

#### Erro de Conexão com o Banco de Dados

**Problema:** `Communications link failure`

**Solução:**
1. Verificar se o MySQL está em execução
2. Confirmar as credenciais em `persistence.xml`
3. Verificar firewall e permissões de rede

#### Erro de Módulo JavaFX

**Problema:** `Error: JavaFX runtime components are missing`

**Solução:**
1. Executar via Maven: `mvn javafx:run`
2. Ou configurar VM options na IDE:
   ```
   --module-path "PATH_TO_JAVAFX_SDK/lib" 
   --add-modules javafx.controls,javafx.fxml
   ```

#### Tabelas Não Criadas Automaticamente

**Problema:** Tabelas não são geradas no banco

**Solução:**
1. Verificar `schema-generation.database.action` no `persistence.xml`
2. Conferir se o usuário do banco tem permissões de CREATE
3. Verificar logs do Hibernate para erros de mapeamento

---

## 🛠️ Manutenção e Suporte

### Padrões de Código

#### Nomenclatura

**Classes:**
- PascalCase
- Substantivos descritivos
- Sufixos: Controller, Service, DAO, Util

**Métodos:**
- camelCase
- Verbos de ação: salvar, buscar, listar, excluir
- Getters/Setters padrão JavaBeans

**Variáveis:**
- camelCase
- Nomes descritivos e significativos
- Prefixos FXML: txt, btn, lbl, cmb, tbl

**Constantes:**
- UPPER_SNAKE_CASE
- Valores imutáveis e configurações

#### Organização de Código

```java
// 1. Declaração de atributos FXML
@FXML private TextField txtNome;
@FXML private Button btnSalvar;

// 2. Atributos da classe
private PacienteService pacienteService;
private ObservableList<Paciente> lista;

// 3. Construtor
public Controller(PacienteService service) {
    this.pacienteService = service;
}

// 4. Método initialize
@Override
public void initialize(URL url, ResourceBundle rb) {
    configurar();
}

// 5. Métodos públicos
public void salvar() { }

// 6. Métodos privados
private void configurar() { }

// 7. Getters/Setters
```

### Tratamento de Exceções

#### RegraDeNegocioException

Exceção customizada para erros de validação:

```java
throw new RegraDeNegocioException("Mensagem amigável ao usuário");
```

#### Padrão de Tratamento

```java
try {
    pacienteService.salvar(paciente);
    AlertUtil.showSuccess("Paciente salvo com sucesso!");
} catch (RegraDeNegocioException e) {
    AlertUtil.showError("Erro de Validação", e.getMessage());
} catch (Exception e) {
    AlertUtil.showError("Erro Inesperado", "Contate o suporte.");
    e.printStackTrace(); // Log para análise
}
```

### Logs e Debugging

#### Hibernate SQL Logging

Configurado em `persistence.xml`:

```xml
<property name="hibernate.show_sql" value="true"/>
<property name="hibernate.format_sql" value="true"/>
```

#### Logs de Aplicação

```java
System.out.println("✅ Operação concluída com sucesso");
System.err.println("❌ Erro ao processar: " + e.getMessage());
e.printStackTrace(); // Stack trace completo
```

### Backup e Segurança

#### Backup do Banco de Dados

```bash
# Backup completo
mysqldump -u root -p gestao_notavel > backup_$(date +%Y%m%d).sql

# Restauração
mysql -u root -p gestao_notavel < backup_20231205.sql
```

#### Recomendações de Produção

1. **Chaves de Criptografia:**
   - Armazenar em arquivos externos
   - Usar variáveis de ambiente
   - Implementar rotação de chaves

2. **Senhas de Banco:**
   - Não versionar credenciais
   - Usar cofres de senhas (Vault, AWS Secrets Manager)

3. **Logs de Auditoria:**
   - Arquivar periodicamente
   - Implementar rotação de logs
   - Backup em storage seguro

4. **Atualizações:**
   - Manter dependências atualizadas
   - Monitorar vulnerabilidades (CVE)
   - Aplicar patches de segurança

### Extensibilidade

#### Adicionar Nova Entidade

1. Criar classe no pacote `model`
2. Adicionar anotações JPA
3. Criar DAO no pacote `dao`
4. Criar Service no pacote `service`
5. Adicionar ao ServiceFactory
6. Criar Controller e View

#### Adicionar Nova Permissão

1. Adicionar constante em `Permission.java`
2. Associar ao Role em `Role.java`
3. Implementar verificação nos Controllers

#### Adicionar Nova Tela

1. Criar arquivo FXML em `resources/view`
2. Criar arquivo CSS em `resources/styles`
3. Criar Controller em `controller`
4. Implementar navegação no menu principal

### Contato e Suporte

**Projeto Acadêmico - IFSul**

Para dúvidas, sugestões ou reportar problemas:

- Documentação completa neste arquivo
- Comentários inline no código-fonte
- Issues no repositório do projeto (se aplicável)

---

## 📝 Notas Finais

### Boas Práticas Implementadas

✅ Arquitetura em camadas bem definida
✅ Separação de responsabilidades (SRP)
✅ Padrão DAO para persistência
✅ Padrão Singleton (ServiceFactory, SessionManager)
✅ Criptografia de dados sensíveis
✅ Controle de acesso granular (RBAC)
✅ Validações robustas de entrada
✅ Auditoria de operações críticas
✅ Interface responsiva e intuitiva
✅ Código documentado e organizado

### Melhorias Futuras Sugeridas

🔧 Implementar sistema de relatórios em PDF
🔧 Adicionar gráficos e dashboards
🔧 Notificações por email/SMS
🔧 API REST para integração externa
🔧 Aplicativo mobile complementar
🔧 Backup automático agendado
🔧 Logs estruturados (Log4j/SLF4J)
🔧 Testes unitários e de integração
🔧 Documentação Javadoc completa
🔧 Deploy containerizado (Docker)

### Tecnologias Complementares Recomendadas

- **Relatórios:** JasperReports, iText
- **Gráficos:** JavaFX Charts, JFreeChart
- **Logging:** SLF4J + Logback
- **Testes:** JUnit 5, Mockito
- **Build:** Maven Wrapper
- **CI/CD:** GitHub Actions, Jenkins
- **Containerização:** Docker, Docker Compose

---

**Versão da Documentação:** 1.0
**Data:** Dezembro de 2025
**Instituição:** IFSul (Instituto Federal Sul-rio-grandense)
**Sistema:** Gestão Notável - Sistema de Gestão de Terapias Infantis
