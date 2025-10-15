package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.UsuarioDAO;
import br.com.gestaonotavel.ifsul.model.Usuario;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticarUsuario(String login, String senha) {
        Usuario usuario = usuarioDAO.buscarPorCpf(login);

        // O login só é um sucesso se o usuário existir E a senha bater.
        if (usuario != null && BCrypt.checkpw(senha, usuario.getSenha())) {
            return usuario;
        }

        // Se qualquer uma das condições falhar, o resultado é o mesmo.
        throw new IllegalArgumentException("CPF ou senha inválidos.");
    }

    public Usuario salvarUsuario(Usuario usuarioSalvando) {

        if (usuarioSalvando.getNome() == null || usuarioSalvando.getNome().isEmpty()) {
            throw new IllegalArgumentException("Preencha o nome do usuário");
        } else if (usuarioSalvando.getCpf() == null || usuarioSalvando.getCpf().isEmpty()) {
            throw new IllegalArgumentException("Preencha o CPF do usuário");
        }else if(usuarioSalvando.getEmail() == null || usuarioSalvando.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Preencha o email do usuário");
        }else if (usuarioSalvando.getSenha() == null || usuarioSalvando.getSenha().isEmpty()) {
            throw new IllegalArgumentException("Preencha a senha do Usuário");
        }else if(usuarioSalvando.getTelefone() == null || usuarioSalvando.getTelefone().isEmpty()) {
            throw new IllegalArgumentException("Preencha o número de telefone do usuário");
        }else if (usuarioSalvando.getCargo() == null || usuarioSalvando.getCargo().isEmpty()) {
            throw new IllegalArgumentException("Preecha o cargo do usuário");
        }

        if(usuarioDAO.buscarPorCpf(usuarioSalvando.getCpf()) != null) {
            throw new IllegalArgumentException("Usuário com login já cadastrado no sistema");
        }

        if (usuarioDAO.buscarPorEmail(usuarioSalvando.getEmail()) != null) {
            throw new IllegalArgumentException("Usuário com Email já cadastrado no sistema");
        }

        String result = BCrypt.hashpw(usuarioSalvando.getSenha(), BCrypt.gensalt());
        usuarioSalvando.setSenha(result);
        return usuarioDAO.salvarUsuario(usuarioSalvando);
    }
}