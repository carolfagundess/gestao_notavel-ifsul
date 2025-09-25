
package br.com.gestaonotavel.gestao_notavel.ifsul.dao;

import br.com.gestaonotavel.gestao_notavel.ifsul.model.Usuario;
import br.com.gestaonotavel.gestao_notavel.ifsul.util.JpaUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import java.util.List;

/**
 *
 * @author carol
 */
public class UsuarioDAO {

    public Usuario salvarUsuario(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            Usuario usuarioSalvo = em.merge(usuario);
            tx.commit();
            return usuarioSalvo;
        } catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorId(Long id) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public List<Usuario> listarTodos() {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u", Usuario.class)
                    .getResultList();
        } finally {
            em.close();
        }
    }

    public void remover(Usuario usuario) {
        EntityManager em = JpaUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            em.remove(em.merge(usuario));
            tx.commit();
        }catch (Exception ex) {
            tx.rollback();
            throw ex;
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorCpf(String cpfBuscado) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery(
                            "SELECT u FROM Usuario u WHERE u.cpf = :cpf", Usuario.class)
                    .setParameter("cpf", cpfBuscado)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null; //deixar para o service tratar
        } finally {
            em.close();
        }
    }

    public Usuario buscarPorEmail(String emailBuscado) {
        EntityManager em = JpaUtil.getEntityManager();
        try {
            return em.createQuery("SELECT u FROM Usuario u WHERE u.email = :email", Usuario.class)
                    .setParameter("email", emailBuscado)
                    .getSingleResult();
        }catch (NoResultException e) {
            return null; //deixar para o service tratar
        }finally {
            em.close();
        }
    }
}
