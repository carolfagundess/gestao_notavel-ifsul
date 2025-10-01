package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.ResponsavelDAO;
import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;

import java.util.Date;

public class ResponsavelService {

    ResponsavelDAO responsavelDAO = new ResponsavelDAO();

    public Responsavel salvar(Responsavel responsavelSalvando) {

        Date dataHoje = new Date();

        if (responsavelSalvando == null) {
            throw new IllegalArgumentException("Preencha as informações");
        }
        if (responsavelSalvando.getNome() == null || responsavelSalvando.getNome().isEmpty()) {
            throw new IllegalArgumentException("Preencha o nome do Responsavél");
        } else if (responsavelSalvando.getCpf() == null || responsavelSalvando.getCpf().isEmpty()) {
            throw new IllegalArgumentException("Preencha o CPF do Responsavél");
        } else if (responsavelSalvando.getTelefone() == null || responsavelSalvando.getTelefone().isEmpty()) {
            throw new IllegalArgumentException("Preencha o número de telefone do Responsavél");
        } else if (responsavelSalvando.getDataNascimento().after(dataHoje)) {
            throw new IllegalArgumentException("Preencha uma data de nascimento do Responsavel válida");
        }

        if (responsavelDAO.buscarPorCpf(responsavelSalvando.getCpf()) != null) {
            throw new IllegalArgumentException("Este CPF já está cadastrado! Tente cadastrar um documento válido");
        }

        return responsavelDAO.salvarResponsavel(responsavelSalvando);
    }

    public Responsavel criarEAssociarPaciente(Responsavel responsavel, Paciente paciente) {
        // A lógica viria aqui...
        return null;
    }
}
