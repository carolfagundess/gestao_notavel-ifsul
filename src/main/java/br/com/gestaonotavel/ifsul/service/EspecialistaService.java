package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.EspecialistaDAO;
import br.com.gestaonotavel.ifsul.model.Especialista;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import java.util.List;

public class EspecialistaService {

    private EspecialistaDAO especialistaDAO = new EspecialistaDAO();

    public Especialista salvar(Especialista especialista) {
        if (especialista.getNome() == null || especialista.getNome().isEmpty()) throw new IllegalArgumentException("Nome obrigatório");
        if (especialista.getRegistroProfissional() == null) throw new IllegalArgumentException("Registro obrigatório");

        Especialista existente = especialistaDAO.buscarPorRegistroProfissional(especialista.getRegistroProfissional());
        if (existente != null && !existente.getIdEspecialista().equals(especialista.getIdEspecialista())) {
            throw new IllegalArgumentException("Registro Profissional já cadastrado");
        }
        return especialistaDAO.salvar(especialista);
    }

    public List<Especialista> listarTodos() { return especialistaDAO.listarTodos(); }
}