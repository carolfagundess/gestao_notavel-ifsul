package br.com.gestaonotavel.ifsul.util;

import br.com.gestaonotavel.ifsul.model.Permission;
import br.com.gestaonotavel.ifsul.model.Usuario;

/**
 * Gerenciador de Sessão (Singleton) para manter o usuário logado.
 */
public final class SessionManager {

    // Instância única
    private static SessionManager instance;

    // Usuário logado
    private Usuario usuarioLogado;

    // Construtor privado para impedir instanciação externa
    private SessionManager() {}

    /**
     * Obtém a instância única do SessionManager.
     * @return A instância.
     */
    public static SessionManager getInstance() {
        if (instance == null) {
            // Thread-safe initialization
            synchronized (SessionManager.class) {
                if (instance == null) {
                    instance = new SessionManager();
                }
            }
        }
        return instance;
    }

    /**
     * Inicia uma nova sessão para um usuário.
     * @param usuario O usuário que está logando.
     */
    public void iniciarSessao(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário não pode ser nulo para iniciar a sessão.");
        }
        this.usuarioLogado = usuario;
        System.out.println("Sessão iniciada para: " + usuario.getNome());
    }

    /**
     * Encerra a sessão atual, deslogando o usuário.
     */
    public void encerrarSessao() {
        if (this.usuarioLogado != null) {
            System.out.println("Encerrando sessão de: " + this.usuarioLogado.getNome());
        }
        this.usuarioLogado = null;
    }

    /**
     * Obtém o usuário atualmente logado.
     * @return O objeto Usuario, ou null se ninguém estiver logado.
     */
    public Usuario getUsuarioLogado() {
        return usuarioLogado;
    }

    /**
     * Verifica se existe um usuário logado.
     * @return true se houver um usuário na sessão, false caso contrário.
     */
    public boolean isLogado() {
        return this.usuarioLogado != null;
    }

    /**
     * Atalho para verificar se o usuário logado possui uma permissão.
     * @param permission A permissão a ser verificada.
     * @return true se o usuário estiver logado E tiver a permissão, false caso contrário.
     */
    public boolean hasPermission(Permission permission) {
        if (!isLogado()) {
            return false;
        }
        return this.usuarioLogado.hasPermission(permission);
    }
}