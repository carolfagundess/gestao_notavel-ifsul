package br.com.gestaonotavel.ifsul.service;


import br.com.gestaonotavel.ifsul.dao.PacienteDAO;
import br.com.gestaonotavel.ifsul.model.Paciente;

import java.util.Date;

public class PacienteService {

    PacienteDAO pacienteDAO = new PacienteDAO();

    public Paciente salvarPaciente(Paciente pacienteSalvando) {

        Date dataHoje = new Date();

        if (pacienteSalvando.getNome() == null || pacienteSalvando.getNome().isEmpty()) {
            throw new IllegalArgumentException("Preencha o nome do Paciente");
        } else if (pacienteSalvando.getDataNascimento() == null) {
            throw new IllegalArgumentException("Preencha a data de nascimento do paciente");
        } else if(pacienteSalvando.getDiagnostico() == null || pacienteSalvando.getDiagnostico().isEmpty()) {
            throw new IllegalArgumentException("Preencha um diagnóstico para o paciente");
        } else if(pacienteSalvando.getEscolaridade() == null || pacienteSalvando.getEscolaridade().isEmpty()) {
            throw new IllegalArgumentException("Preencha o escolaridade para o paciente");
        } else if (pacienteSalvando.getCondicaoClinica() == null || pacienteSalvando.getCondicaoClinica().isEmpty()) {
            throw new IllegalArgumentException("Preencha o condição clínica  para o paciente");
        } else if (pacienteSalvando.getDataNascimento().after(dataHoje)) {
            throw new IllegalArgumentException("Preencha uma data de nascimento válida");
        }

        //campos opcionais
        if (pacienteSalvando.getCpf() != null && !pacienteSalvando.getCpf().isEmpty()) {
            if (pacienteDAO.buscarPorCpf(pacienteSalvando.getCpf()) != null ){
                throw new IllegalArgumentException("CPF já cadastrado para um Paciente");
            }
        }

        return pacienteDAO.salvar(pacienteSalvando);
    }
}
