package br.com.gestaonotavel.ifsul.service;


import br.com.gestaonotavel.ifsul.dao.PacienteDAO;
import br.com.gestaonotavel.ifsul.model.Paciente;

public class PacienteService {

    PacienteDAO pacienteDAO = new PacienteDAO();

    public Paciente salvarPaciente(Paciente pacienteSalvando) {
        if (pacienteSalvando.getNome() == null || pacienteSalvando.getNome().isEmpty()) {
            throw new IllegalArgumentException("Preencha o nome do Paciente");
        } else if (pacienteSalvando.getCpf() == null || pacienteSalvando.getCpf().isEmpty()) {
            throw new IllegalArgumentException("Preencha o CPF do Paciente");
        } else if (pacienteSalvando.getDataNascimento() == null) {
            throw new IllegalArgumentException("Preencha a data de nascimento do usuário");
        }

        if(pacienteDAO.buscarPorCpf(pacienteSalvando.getCpf()) != null) {
            throw new IllegalArgumentException("Usuário com login já cadastrado no sistema");
        }
        return pacienteDAO.salvar(pacienteSalvando);
    }
}
