package br.com.gestaonotavel.ifsul.util;

import br.com.gestaonotavel.ifsul.model.Permission;
import br.com.gestaonotavel.ifsul.model.Usuario;

public final class SessionManager {

    private static SessionManager instance;
    private Usuario usuarioLogado;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    public void iniciarSessao(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo para iniciar a sessão.");
        }
        this.usuarioLogado = usuario;
        System.out.println("Sessão iniciada para: " + usuario.getNome());
    }

    public void encerrarSessao() {
        if (this.usuarioLogado != null) {
            System.out.println("Encerrando sessão de: " + this.usuarioLogado.getNome());
        }
        this.usuarioLogado = null;
    }

    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    public boolean isLogado() {
        return this.usuarioLogado != null;
    }

    public boolean hasPermission(Permission permission) {
        if (!isLogado()) {
            return false;
        }
        return this.usuarioLogado.hasPermission(permission);
    }
}