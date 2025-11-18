package br.com.gestaonotavel.ifsul.dao;

import br.com.gestaonotavel.ifsul.model.AuditoriaLog;
import br.com.gestaonotavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

public class AuditoriaLogDAO {

    /**
     * Salva um novo registro de log no banco de dados.
     * @param log O objeto AuditoriaLog a ser salvo.
     */
    public void salvar(AuditoriaLog log) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.persist(log); // Prezando pela semântica, foi usado persist para uma nova entidade
            tx.commit();
        } catch (PersistenceException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            System.err.println("Erro ao salvar log de auditoria: " + e.getMessage());
            // Ao usar logs, não é comum relançar exceções, para que o fluxo principal não seja interrompido
        } finally {
            em.close();
        }
    }
}
