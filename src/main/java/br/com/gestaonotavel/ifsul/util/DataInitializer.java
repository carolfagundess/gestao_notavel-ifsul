package br.com.gestaonotavel.ifsul.util;

import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.model.Usuario;
import br.com.gestaonotavel.ifsul.service.PacienteService;
import br.com.gestaonotavel.ifsul.service.ResponsavelService;
import br.com.gestaonotavel.ifsul.service.UsuarioService;

import java.time.LocalDate;
import java.util.Date;

public class DataInitializer {

    public static void popularBancoDeDados(){
        // --- Serviços ---
        UsuarioService usuarioService = new UsuarioService();
        ResponsavelService responsavelService = new ResponsavelService();
        PacienteService pacienteService = new PacienteService();

        // --- 1. Criar um Usuário ---
        // Verificamos se o usuário já não existe para não dar erro
        if (usuarioService.buscarPorCpf("1234") == null) {
            Usuario admin = new Usuario();
            admin.setNome("Administrador");
            admin.setCpf("1234");
            admin.setEmail("admin@gestaonotavel.com");
            admin.setSenha("admin"); // O service vai criptografar
            admin.setCargo("Admin");
            admin.setTelefone("51999999999");
            usuarioService.salvarUsuario(admin);
        }

        // --- 2. Criar um Responsável ---
        Responsavel ana = new Responsavel();
        ana.setNome("Ana Maria Silva");
        ana.setCpf("11122233344");
        ana.setTelefone("51999887766");
        ana.setDataNascimento(LocalDate.of(1984, 12, 5)); // Ano - 1900, Mês (0-11), Dia
        Responsavel responsavelSalvo = responsavelService.salvar(ana);

        // --- 3. Criar um Paciente e Associar ---
        Paciente carlos = new Paciente();
        carlos.setNome("Carlos Souza");
        carlos.setDataNascimento(LocalDate.of(2010, 6, 15)); // Ano 2019
        carlos.setDiagnostico("Atraso de fala");
        carlos.setCondicaoClinica("Leve");
        carlos.setEscolaridade("Creche");

        // Usamos o método do service para associar e salvar
        pacienteService.criarEAssociarResponsavel(responsavelSalvo, carlos);

    }
}
