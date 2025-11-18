// DataInitializer.java - VERSÃO COMPLETA PARA TESTES

package br.com.gestaonotavel.ifsul.util;

import br.com.gestaonotavel.ifsul.model.*;
import br.com.gestaonotavel.ifsul.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DataInitializer {

    public static void popularBancoDeDados() {
        System.out.println("========================================");
        System.out.println("🚀 INICIANDO POPULAÇÃO DO BANCO DE DADOS");
        System.out.println("========================================");

        // --- Serviços ---
        UsuarioService usuarioService = new UsuarioService();
        ResponsavelService responsavelService = new ResponsavelService();
        PacienteService pacienteService = new PacienteService();
        EspecialistaService especialistaService = new EspecialistaService();
        AtendimentoService atendimentoService = new AtendimentoService();

        try {
            // ========================================
            // 1. CRIAR USUÁRIOS
            // ========================================
            System.out.println("\n📝 Criando usuários...");

            if (usuarioService.buscarPorCpf("1234") == null) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setCpf("1234");
                admin.setEmail("admin@gestaonotavel.com");
                admin.setSenha("admin");
                admin.setRole(Role.ADMIN);
                admin.setTelefone("51999999999");
                usuarioService.salvarUsuario(admin);
                System.out.println("✅ Usuário Admin criado - Login: 1234 / Senha: admin");
            }

            if (usuarioService.buscarPorCpf("12345678900") == null) {
                Usuario coordenador = new Usuario();
                coordenador.setNome("Maria Silva");
                coordenador.setCpf("12345678900");
                coordenador.setEmail("maria@gestaonotavel.com");
                coordenador.setSenha("senha123");
                coordenador.setRole(Role.SECRETARIO);
                coordenador.setTelefone("51988887777");
                usuarioService.salvarUsuario(coordenador);
                System.out.println("✅ Usuário Coordenador criado");
            }

            // ========================================
            // 2. CRIAR RESPONSÁVEIS
            // ========================================
            System.out.println("\n👤 Criando responsáveis...");

            Responsavel ana = new Responsavel();
            ana.setNome("Ana Maria Silva");
            ana.setCpf("11122233344");
            ana.setTelefone("51999887766");
            ana.setDataNascimento(LocalDate.of(1984, 12, 5));
            Responsavel anaGravado = responsavelService.salvar(ana);
            System.out.println("✅ Responsável Ana criado");

            Responsavel joao = new Responsavel();
            joao.setNome("João Pedro Santos");
            joao.setCpf("22233344455");
            joao.setTelefone("51988776655");
            joao.setDataNascimento(LocalDate.of(1990, 3, 15));
            Responsavel joaoGravado = responsavelService.salvar(joao);
            System.out.println("✅ Responsável João criado");

            Responsavel carla = new Responsavel();
            carla.setNome("Carla Fernandes");
            carla.setCpf("33344455566");
            carla.setTelefone("51977665544");
            carla.setDataNascimento(LocalDate.of(1988, 7, 20));
            Responsavel carlaGravado = responsavelService.salvar(carla);
            System.out.println("✅ Responsável Carla criado");

            // ========================================
            // 3. CRIAR PACIENTES
            // ========================================
            System.out.println("\n👶 Criando pacientes...");

            // Paciente 1 - Com Responsável
            Paciente carlos = new Paciente();
            carlos.setNome("Carlos Souza");
            carlos.setCpf("44455566677");
            carlos.setDataNascimento(LocalDate.of(2018, 6, 15));
            carlos.setDiagnostico("Atraso de fala");
            carlos.setCondicaoClinica("Leve");
            carlos.setEscolaridade("Creche");
            pacienteService.criarEAssociarResponsavel(anaGravado, carlos);
            System.out.println("✅ Paciente Carlos criado (com responsável Ana)");

            // Paciente 2 - Com Responsável
            Paciente julia = new Paciente();
            julia.setNome("Julia Oliveira");
            julia.setCpf("55566677788");
            julia.setDataNascimento(LocalDate.of(2017, 2, 10));
            julia.setDiagnostico("TEA - Transtorno do Espectro Autista");
            julia.setCondicaoClinica("Moderado");
            julia.setEscolaridade("Pré-escola");
            pacienteService.criarEAssociarResponsavel(joaoGravado, julia);
            System.out.println("✅ Paciente Julia criado (com responsável João)");

            // Paciente 3 - Sem Responsável
            Paciente miguel = new Paciente();
            miguel.setNome("Miguel Santos");
            miguel.setDataNascimento(LocalDate.of(2019, 11, 5));
            miguel.setDiagnostico("Dificuldade de aprendizagem");
            miguel.setCondicaoClinica("Leve");
            miguel.setEscolaridade("Maternal");
            pacienteService.salvarPaciente(miguel);
            System.out.println("✅ Paciente Miguel criado (SEM responsável)");

            // Paciente 4 - Com Responsável
            Paciente lucas = new Paciente();
            lucas.setNome("Lucas Ferreira");
            lucas.setCpf("66677788899");
            lucas.setDataNascimento(LocalDate.of(2016, 8, 25));
            lucas.setDiagnostico("TDAH - Transtorno do Déficit de Atenção");
            lucas.setCondicaoClinica("Moderado");
            lucas.setEscolaridade("Ensino fundamental incompleto");
            pacienteService.criarEAssociarResponsavel(carlaGravado, lucas);
            System.out.println("✅ Paciente Lucas criado (com responsável Carla)");

            // Paciente 5 - Sem CPF (teste de campo opcional)
            Paciente sofia = new Paciente();
            sofia.setNome("Sofia Costa");
            sofia.setDataNascimento(LocalDate.of(2020, 1, 12));
            sofia.setDiagnostico("Atraso motor");
            sofia.setCondicaoClinica("Leve");
            sofia.setEscolaridade("Berçário");
            pacienteService.salvarPaciente(sofia);
            System.out.println("✅ Paciente Sofia criado (sem CPF)");

            // ========================================
            // 4. CRIAR ESPECIALISTAS
            // ========================================
            System.out.println("\n👨‍⚕️ Criando especialistas...");

            Especialista fono = new Especialista();
            fono.setNome("Dra. Patricia Lima");
            fono.setEspecialidade("Fonoaudiologia");
            fono.setValorSessao(150.00);
            fono.setDuracao(50);
            fono.setMaxPacientes(20);
            fono.setPacientesAtuais(0);
            fono.setRegistroProfissional("CRFa-12345");
            Especialista fonoGravado = especialistaService.salvar(fono);
            System.out.println("✅ Especialista Fonoaudióloga criado");

            Especialista psico = new Especialista();
            psico.setNome("Dr. Roberto Alves");
            psico.setEspecialidade("Psicologia Infantil");
            psico.setValorSessao(180.00);
            psico.setDuracao(60);
            psico.setMaxPacientes(15);
            psico.setPacientesAtuais(0);
            psico.setRegistroProfissional("CRP-67890");
            Especialista psicoGravado = especialistaService.salvar(psico);
            System.out.println("✅ Especialista Psicólogo criado");

            Especialista terapeuta = new Especialista();
            terapeuta.setNome("Fernanda Rocha");
            terapeuta.setEspecialidade("Terapia Ocupacional");
            terapeuta.setValorSessao(160.00);
            terapeuta.setDuracao(45);
            terapeuta.setMaxPacientes(18);
            terapeuta.setPacientesAtuais(0);
            terapeuta.setRegistroProfissional("CREFITO-54321");
            Especialista terapeutaGravado = especialistaService.salvar(terapeuta);
            System.out.println("✅ Especialista Terapeuta Ocupacional criado");

            // ========================================
            // 5. CRIAR ATENDIMENTOS
            // ========================================
            System.out.println("\n📅 Criando atendimentos...");

            // Buscar pacientes salvos para criar atendimentos
            java.util.List<Paciente> pacientes = pacienteService.listarTodos();

            if (!pacientes.isEmpty()) {
                // Atendimento 1 - Hoje às 14h
                Atendimento atend1 = new Atendimento();
                atend1.setPaciente(pacientes.get(0));
                atend1.setEspecialista(fonoGravado);
                atend1.setDataHora(LocalDateTime.now().withHour(14).withMinute(0));
                atend1.setLocal("Sala 101");
                atend1.setObservacao("Primeira sessão de avaliação");
                atendimentoService.salvar(atend1);
                System.out.println("✅ Atendimento 1 criado (hoje 14h)");

                // Atendimento 2 - Amanhã às 10h
                Atendimento atend2 = new Atendimento();
                atend2.setPaciente(pacientes.get(1));
                atend2.setEspecialista(psicoGravado);
                atend2.setDataHora(LocalDateTime.now().plusDays(1).withHour(10).withMinute(0));
                atend2.setLocal("Sala 203");
                atend2.setObservacao("Acompanhamento mensal");
                atendimentoService.salvar(atend2);
                System.out.println("✅ Atendimento 2 criado (amanhã 10h)");

                // Atendimento 3 - Semana que vem às 15h30
                if (pacientes.size() > 2) {
                    Atendimento atend3 = new Atendimento();
                    atend3.setPaciente(pacientes.get(2));
                    atend3.setEspecialista(terapeutaGravado);
                    atend3.setDataHora(LocalDateTime.now().plusWeeks(1).withHour(15).withMinute(30));
                    atend3.setLocal("Sala 105");
                    atend3.setObservacao("Sessão de terapia sensorial");
                    atendimentoService.salvar(atend3);
                    System.out.println("✅ Atendimento 3 criado (próxima semana 15h30)");
                }
            }

            // ========================================
            // RESUMO FINAL
            // ========================================
            System.out.println("\n========================================");
            System.out.println("✅ BANCO DE DADOS POPULADO COM SUCESSO!");
            System.out.println("========================================");
            System.out.println("📊 Resumo:");
            System.out.println("   • 2 Usuários (com Roles)"); // Alterado
            System.out.println("   • 3 Responsáveis");
            System.out.println("   • 5 Pacientes (3 com responsável, 2 sem)");
            System.out.println("   • 3 Especialistas");
            System.out.println("   • 3 Atendimentos");
            System.out.println("========================================\n");

        } catch (Exception e) {
            System.err.println("❌ ERRO ao popular banco de dados:");
            e.printStackTrace();
        }
    }
}