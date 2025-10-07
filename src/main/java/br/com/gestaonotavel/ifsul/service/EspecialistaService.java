package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.EspecialistaDAO;
import br.com.gestaonotavel.ifsul.model.Especialista;

public class EspecialistaService {

    private EspecialistaDAO especialistaDAO = new EspecialistaDAO();

    public Especialista salvar(Especialista especialistaSalvando) {

        if (especialistaSalvando.getNome() == null || especialistaSalvando.getNome().isEmpty()) {
            throw new IllegalArgumentException("Preencha o nome do Especialista");
        } else if (especialistaSalvando.getEspecialidade() == null || especialistaSalvando.getEspecialidade().isEmpty()) {
            throw new IllegalArgumentException("Preencha uma especialidade para o Especialista");
        } else if (especialistaSalvando.getValorSessao() == null || especialistaSalvando.getValorSessao() < 0) {
            throw new IllegalArgumentException("Preencha o valor da sessão para o Especialista");
        } else if (especialistaSalvando.getRegistroProfissional() == null || especialistaSalvando.getRegistroProfissional().isEmpty()) {
            throw new IllegalArgumentException("Preencha o registro profissional do Especialista");
        }

        if (especialistaDAO.buscarPorRegistroProfissional(especialistaSalvando.getRegistroProfissional()) != null) {
            throw new IllegalArgumentException("Registro Profissional já cadastrado no sistema");
        }

        return especialistaDAO.salvar(especialistaSalvando);
    }
}
