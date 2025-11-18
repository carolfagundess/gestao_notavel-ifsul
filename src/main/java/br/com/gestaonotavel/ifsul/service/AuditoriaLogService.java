package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.AuditoriaLogDAO;
import br.com.gestaonotavel.ifsul.model.AuditoriaLog;
import br.com.gestaonotavel.ifsul.model.Usuario;
import br.com.gestaonotavel.ifsul.util.SessionManager;

public class AuditoriaLogService {

    private final AuditoriaLogDAO auditoriaLogDAO;

    public AuditoriaLogService() {
        this.auditoriaLogDAO = new AuditoriaLogDAO();
    }

    /**
     * Registra uma nova ação de auditoria no sistema.
     * Pega automaticamente o usuário da sessão.
     *
     * @param acao A descrição da ação realizada (ex: "Excluiu Paciente ID: 1 - Nome: João").
     */
    public void registrarAcao(String acao) {
        try {
            Usuario usuario = SessionManager.getInstance().getUsuarioLogado();
            String nomeUsuario = (usuario != null) ? usuario.getNome() : "Sistema"; // Fallback

            AuditoriaLog log = new AuditoriaLog(nomeUsuario, acao);
            auditoriaLogDAO.salvar(log);

        } catch (Exception e) {
            // Não pode travar o main, no caso do log falhar
            System.err.println("Falha crítica ao registrar auditoria: " + e.getMessage());
        }
    }
}
