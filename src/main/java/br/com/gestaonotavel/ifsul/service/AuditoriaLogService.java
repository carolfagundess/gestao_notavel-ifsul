package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.AuditoriaLogDAO;
import br.com.gestaonotavel.ifsul.model.AuditoriaLog;
import br.com.gestaonotavel.ifsul.model.Usuario;
import br.com.gestaonotavel.ifsul.util.SessionManager;

import java.util.List;

public class AuditoriaLogService {

    private final AuditoriaLogDAO auditoriaLogDAO;

    public AuditoriaLogService() {
        this.auditoriaLogDAO = new AuditoriaLogDAO();
    }

    public void registrarAcao(String acao) {
        try {
            Usuario usuario = SessionManager.getInstance().getUsuarioLogado();
            String nomeUsuario = (usuario != null) ? usuario.getNome() : "Sistema";

            AuditoriaLog log = new AuditoriaLog(nomeUsuario, acao);
            auditoriaLogDAO.salvar(log);

        } catch (Exception e) {
            System.err.println("Falha crítica ao registrar auditoria: " + e.getMessage());
        }
    }

    public List<AuditoriaLog> listarLogs() {
        return auditoriaLogDAO.listarTodos();
    }
}