package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.UsuarioDAO;
import br.com.gestaonotavel.ifsul.model.Usuario;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import br.com.gestaonotavel.ifsul.util.ValidationUtil;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticarUsuario(String login, String senha) {
        // CORREÇÃO CRÍTICA: Limpar a máscara do CPF antes de buscar no banco
        // O banco guarda "12345678900", mas a tela envia "123.456.789-00"
        if (login == null) {
            throw new RegraDeNegocioException("CPF obrigatório.");
        }

        String loginLimpo = login.replaceAll("[^0-9]", "");

        Usuario usuario = usuarioDAO.buscarPorCpf(loginLimpo);

        // O login só é um sucesso se o usuário existir E a senha bater.
        if (usuario != null && BCrypt.checkpw(senha, usuario.getSenha())) {
            return usuario;
        }

        // Se qualquer uma das condições falhar, o resultado é o mesmo.
        throw new RegraDeNegocioException("CPF ou senha inválidos.");
    }

    public Usuario salvarUsuario(Usuario usuarioSalvando) {

        if (usuarioSalvando == null) {
            throw new RegraDeNegocioException("O objeto usuário não pode ser nulo");
        }

        if (usuarioSalvando.getNome() == null || usuarioSalvando.getNome().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o nome do usuário");
        } else if (usuarioSalvando.getCpf() == null || usuarioSalvando.getCpf().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o CPF do usuário");
        } else if(usuarioSalvando.getEmail() == null || usuarioSalvando.getEmail().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o email do usuário");
        } else if (usuarioSalvando.getSenha() == null || usuarioSalvando.getSenha().isEmpty()) {
            throw new RegraDeNegocioException("Preencha a senha do Usuário");
        } else if(usuarioSalvando.getTelefone() == null || usuarioSalvando.getTelefone().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o número de telefone do usuário");
        } else if (usuarioSalvando.getRole() == null) {
            throw new RegraDeNegocioException("Preencha o cargo do usuário");
        }

        // --- Validação, Limpeza e Verificação de Duplicidade do CPF ---
        String cpfOriginal = usuarioSalvando.getCpf();
        if (cpfOriginal == null || cpfOriginal.trim().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o CPF do usuário");
        }

        // 1. Validar formato e dígitos do CPF
        // --- COMENTADO TEMPORARIAMENTE PARA PERMITIR A INICIALIZAÇÃO ---
        /*
        if (!ValidationUtil.validarCPF(cpfOriginal)) {
            throw new RegraDeNegocioException("CPF inválido.");
        }
        */
        // ----------------------------------------------------------------

        // 2. Limpar CPF APÓS validação (para salvar somente números no banco)
        String cpfLimpo = cpfOriginal.replaceAll("[^0-9]", "");

        // 3. Verificar duplicidade de CPF (usando CPF limpo)
        if (usuarioDAO.buscarPorCpf(cpfLimpo) != null) {
            throw new RegraDeNegocioException("Usuário com este CPF já cadastrado no sistema");
        }
        // --- Fim Validação CPF ---

        // 4. Verificar duplicidade de Email
        String email = usuarioSalvando.getEmail().trim();
        if (usuarioDAO.buscarPorEmail(email) != null) {
            throw new RegraDeNegocioException("Usuário com este Email já cadastrado no sistema");
        }

        // Atualiza o objeto com email e CPF limpo
        usuarioSalvando.setEmail(email);
        usuarioSalvando.setCpf(cpfLimpo);

        // 5. Criptografar Senha
        String hashSenha = BCrypt.hashpw(usuarioSalvando.getSenha(), BCrypt.gensalt());
        usuarioSalvando.setSenha(hashSenha);

        // 6. Salvar Usuário
        return usuarioDAO.salvarUsuario(usuarioSalvando);
    }

    public Usuario buscarPorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new RegraDeNegocioException("Informe um número de CPF válido!");
        }
        // Limpa o CPF antes de buscar
        String cpfLimpo = cpf.replaceAll("[^0-9]", "");
        return usuarioDAO.buscarPorCpf(cpfLimpo);
    }
}