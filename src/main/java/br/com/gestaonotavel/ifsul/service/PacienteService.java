package br.com.gestaonotavel.ifsul.service;


import br.com.gestaonotavel.ifsul.dao.PacienteDAO;
import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PacienteService {

    PacienteDAO pacienteDAO = new PacienteDAO();

    public Paciente salvarPaciente(Paciente pacienteSalvando) {

        validarPaciente(pacienteSalvando);

        //campos opcionais
        if (pacienteSalvando.getCpf() != null && !pacienteSalvando.getCpf().isEmpty()) {
            String cpfLimpo =  pacienteSalvando.getCpf().replaceAll("[^0-9]", "");
            Paciente pacienteExistente = pacienteDAO.buscarPorCpf(cpfLimpo);
            if (pacienteExistente != null && pacienteExistente.getIdPaciente() != pacienteSalvando.getIdPaciente()) {
                throw new RegraDeNegocioException("CPF já cadastrado para um Paciente");
            }
            pacienteSalvando.setCpf(cpfLimpo);
        }

        Paciente salvo =  pacienteDAO.salvarPaciente(pacienteSalvando);
        //service notificando que os dados foram atualizados
        DataChangeManager.getInstance().notificarListeners("Paciente");
        return salvo;
    }

    public Paciente criarEAssociarResponsavel(Responsavel responsavel, Paciente paciente) {
        paciente.adicionarResponsavel(responsavel);
        return salvarPaciente(paciente);
    }

    public List<Paciente> listarTodos() {
        return pacienteDAO.listarTodos();
    }

    public void deletarPaciente(Paciente paciente) {
        pacienteDAO.excluir(paciente.getId());
    }

    private void validarPaciente(Paciente pacienteSalvando) {

        LocalDate hoje = LocalDate.now();

        if (pacienteSalvando.getNome() == null || pacienteSalvando.getNome().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o nome do Paciente");
        } else if (pacienteSalvando.getDataNascimento() == null) {
            throw new RegraDeNegocioException("Preencha a data de nascimento do paciente");
        } else if(pacienteSalvando.getDiagnostico() == null || pacienteSalvando.getDiagnostico().isEmpty()) {
            throw new RegraDeNegocioException("Preencha um diagnóstico para o paciente");
        } else if(pacienteSalvando.getEscolaridade() == null || pacienteSalvando.getEscolaridade().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o escolaridade para o paciente");
        } else if (pacienteSalvando.getCondicaoClinica() == null || pacienteSalvando.getCondicaoClinica().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o condição clínica  para o paciente");
        } else if (pacienteSalvando.getDataNascimento().isAfter(hoje)) {
            throw new RegraDeNegocioException("Preencha uma data de nascimento válida");
        }
    }
}
