package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.PacienteDAO;
import br.com.gestaonotavel.ifsul.dao.ResponsavelDAO;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import br.com.gestaonotavel.ifsul.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

public class ResponsavelService {

    ResponsavelDAO responsavelDAO = new ResponsavelDAO();
    PacienteDAO pacienteDAO = new PacienteDAO();

    public Responsavel salvar(Responsavel responsavelSalvando) {
        LocalDate hoje = LocalDate.now();

        if (responsavelSalvando == null) throw new IllegalArgumentException("Preencha as informações");
        if (responsavelSalvando.getNome() == null || responsavelSalvando.getNome().isEmpty()) throw new IllegalArgumentException("Preencha o nome do Responsável");
        if (responsavelSalvando.getTelefone() == null || responsavelSalvando.getTelefone().isEmpty()) throw new IllegalArgumentException("Preencha o número de telefone do Responsável");
        if (responsavelSalvando.getDataNascimento() == null) throw new IllegalArgumentException("Preencha a data de nascimento do Responsável");
        if (responsavelSalvando.getDataNascimento().isAfter(hoje)) throw new IllegalArgumentException("Preencha uma data de nascimento do Responsável válida");

        String cpfOriginal = responsavelSalvando.getCpf();
        if (cpfOriginal == null || cpfOriginal.trim().isEmpty()) throw new RegraDeNegocioException("Preencha o CPF do Responsável");

        String cpfLimpo = cpfOriginal.replaceAll("[^0-9]", "");
        if (!ValidationUtil.validarCPF(cpfOriginal)) throw new RegraDeNegocioException("CPF inválido");

        Responsavel existente = responsavelDAO.buscarPorCpf(cpfLimpo);
        if (existente != null && !existente.getId().equals(responsavelSalvando.getId())) {
            throw new RegraDeNegocioException("Este CPF já está cadastrado! Tente cadastrar um documento válido");
        }

        responsavelSalvando.setCpf(cpfLimpo);
        return responsavelDAO.salvarResponsavel(responsavelSalvando);
    }

    public List<Responsavel> buscarTodos() {
        return responsavelDAO.buscarTodos();
    }
}