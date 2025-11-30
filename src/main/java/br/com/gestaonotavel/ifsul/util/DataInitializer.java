package br.com.gestaonotavel.ifsul.util;

import br.com.gestaonotavel.ifsul.model.*;
import br.com.gestaonotavel.ifsul.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List; // Import necessário para listas

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
            // 1. CRIAR USUÁRIOS (Correção: Usando Role Enum)
            // ========================================
            System.out.println("\n📝 Criando usuários...");

            // Admin - CPF Válido para teste: 00637798041
            String cpfAdmin = "00637798041";
            if (usuarioService.buscarPorCpf(cpfAdmin) == null) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setCpf(cpfAdmin);
                admin.setEmail("admin@gestaonotavel.com");
                admin.setSenha("admin");
                admin.setRole(Role.ADMIN); // <-- CORREÇÃO AQUI (Era setCargo)
                admin.setTelefone("51999999999");
                usuarioService.salvarUsuario(admin);
                System.out.println("✅ Usuário Admin criado - Login: " + cpfAdmin + " / Senha: admin");
            }

            // Coordenadora - CPF Válido para teste: 44063018060
            String cpfMaria = "44063018060";
            if (usuarioService.buscarPorCpf(cpfMaria) == null) {
                Usuario coordenador = new Usuario();
                coordenador.setNome("Maria Silva");
                coordenador.setCpf(cpfMaria);
                coordenador.setEmail("maria@gestaonotavel.com");
                coordenador.setSenha("senha123");
                coordenador.setRole(Role.SECRETARIO); // <-- CORREÇÃO AQUI (Era setCargo)
                coordenador.setTelefone("51988887777");
                usuarioService.salvarUsuario(coordenador);
                System.out.println("✅ Usuário Coordenador criado");
            }

            // ========================================
            // 2. CRIAR RESPONSÁVEIS
            // ========================================
            System.out.println("\n👤 Criando responsáveis...");

            Responsavel anaGravado = null;
            Responsavel joaoGravado = null;
            Responsavel carlaGravado = null;

            List<Responsavel> listaResp = responsavelService.buscarTodos();

            if (listaResp.isEmpty()) {
                Responsavel ana = new Responsavel();
                ana.setNome("Ana Maria Silva");
                ana.setCpf("36205569021");
                ana.setTelefone("51999887766");
                ana.setDataNascimento(LocalDate.of(1984, 12, 5));
                anaGravado = responsavelService.salvar(ana);
                System.out.println("✅ Responsável Ana criado");

                Responsavel joao = new Responsavel();
                joao.setNome("João Pedro Santos");
                joao.setCpf("63376760059");
                joao.setTelefone("51988776655");
                joao.setDataNascimento(LocalDate.of(1990, 3, 15));
                joaoGravado = responsavelService.salvar(joao);
                System.out.println("✅ Responsável João criado");

                Responsavel carla = new Responsavel();
                carla.setNome("Carla Fernandes");
                carla.setCpf("02643869027");
                carla.setTelefone("51977665544");
                carla.setDataNascimento(LocalDate.of(1988, 7, 20));
                carlaGravado = responsavelService.salvar(carla);
                System.out.println("✅ Responsável Carla criado");
            } else {
                anaGravado = listaResp.get(0);
                if(listaResp.size() > 1) joaoGravado = listaResp.get(1);
                if(listaResp.size() > 2) carlaGravado = listaResp.get(2);
                System.out.println("ℹ️ Responsáveis já existentes.");
            } // Dados para popular banco.

            // ========================================
            // 3. CRIAR PACIENTES
            // ========================================
            System.out.println("\n👶 Criando pacientes...");

            if (pacienteService.listarTodos().isEmpty() && anaGravado != null) {
                Paciente carlos = new Paciente();
                carlos.setNome("Carlos Souza");
                carlos.setCpf("92036444068"); // CPF Válido
                carlos.setDataNascimento(LocalDate.of(2018, 6, 15));
                carlos.setDiagnostico("Atraso de fala");
                carlos.setCondicaoClinica("Leve");
                carlos.setEscolaridade("Creche");
                pacienteService.criarEAssociarResponsavel(anaGravado, carlos);
                System.out.println("✅ Paciente Carlos criado");

                Paciente julia = new Paciente();
                julia.setNome("Julia Oliveira");
                julia.setCpf(null);
                julia.setDataNascimento(LocalDate.of(2017, 2, 10));
                julia.setDiagnostico("TEA");
                julia.setCondicaoClinica("Moderado");
                julia.setEscolaridade("Pré-escola");
                if(joaoGravado != null) pacienteService.criarEAssociarResponsavel(joaoGravado, julia);
                System.out.println("✅ Paciente Julia criado");

                    // Dados para popular banco.
            } else {
                System.out.println("ℹ️ Pacientes já existentes.");
            }

            // ========================================
            // 4. CRIAR ESPECIALISTAS
            // ========================================
            System.out.println("\n👨‍⚕️ Criando especialistas...");

            Especialista fonoGravado = null;

            if (especialistaService.listarTodos().isEmpty()) {
                Especialista fono = new Especialista();
                fono.setNome("Dra. Patricia Lima");
                fono.setEspecialidade("Fonoaudiologia");
                fono.setValorSessao(150.00);
                fono.setDuracao(50);
                fono.setMaxPacientes(20);
                fono.setPacientesAtuais(0);
                fono.setRegistroProfissional("CRFa-12345");
                fonoGravado = especialistaService.salvar(fono);
                System.out.println("✅ Especialista Fonoaudióloga criado");

                // Dados para popular banco.
            } else {
                System.out.println("ℹ️ Especialistas já existentes.");
                fonoGravado = especialistaService.listarTodos().get(0);
            }

            // ========================================
            // 5. CRIAR ATENDIMENTOS
            // ========================================
            if (atendimentoService.listarTodos(null).isEmpty() && fonoGravado != null && !pacienteService.listarTodos().isEmpty()) {
                try {
                    Atendimento atend1 = new Atendimento();
                    atend1.setPaciente(pacienteService.listarTodos().get(0));
                    atend1.setEspecialista(fonoGravado);
                    atend1.setDataHora(LocalDateTime.now().withHour(14).withMinute(0));
                    atend1.setLocal("Sala 101");
                    atend1.setObservacao("Sessão Inicial");
                    atendimentoService.salvar(atend1);
                    System.out.println("✅ Atendimento criado");
                } catch (Exception e) {
                    System.out.println("ℹ️ Erro ao criar atendimento (conflito): " + e.getMessage());
                }
            }

            System.out.println("\n========================================");
            System.out.println("✅ BANCO DE DADOS POPULADO COM SUCESSO!");
            System.out.println("========================================");

        } catch (Exception e) {
            System.err.println("❌ ERRO ao popular banco de dados:");
            e.printStackTrace();
        }
    }
}