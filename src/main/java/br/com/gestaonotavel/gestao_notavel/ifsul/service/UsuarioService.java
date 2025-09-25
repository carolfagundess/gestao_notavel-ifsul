package br.com.gestaonotavel.gestao_notavel.ifsul.service;

import br.com.gestaonotavel.gestao_notavel.ifsul.dao.UsuarioDAO;
import br.com.gestaonotavel.gestao_notavel.ifsul.model.Usuario;
import br.com.gestaonotavel.gestao_notavel.ifsul.util.JpaUtil;
import org.mindrot.jbcrypt.BCrypt;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

public class UsuarioService {

    private UsuarioDAO usuarioDAO = new UsuarioDAO();

    public Usuario autenticarUsuario(String login, String senha) {
        Usuario usuarioAutenticando = usuarioDAO.buscarPorCpf(login);

        if (usuarioAutenticando == null) {
            throw new IllegalArgumentException("Usuário com login " + login + " não encontrado");
        }

        if (BCrypt.checkpw(senha, usuarioAutenticando.getSenha())) {
            return usuarioAutenticando;
        } else {
            throw new IllegalArgumentException("Senha invalida! Tente novamente. ");
        }
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