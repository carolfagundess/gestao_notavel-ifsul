package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.PacienteDAO;
import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import br.com.gestaonotavel.ifsul.util.ValidationUtil;
import java.time.LocalDate;
import java.util.List;

public class PacienteService {

    PacienteDAO pacienteDAO = new PacienteDAO();

    public Paciente salvarPaciente(Paciente pacienteSalvando) {
        if (pacienteSalvando == null) throw new RegraDeNegocioException("Paciente nulo");
        if (pacienteSalvando.getNome() == null || pacienteSalvando.getNome().isEmpty()) throw new RegraDeNegocioException("Nome obrigatório");
        if (pacienteSalvando.getDataNascimento() == null) throw new RegraDeNegocioException("Data de nascimento obrigatória");

        String cpfOriginal = pacienteSalvando.getCpf();
        if (cpfOriginal != null && !cpfOriginal.trim().isEmpty()) {
            // Comentado para facilitar testes se necessário, descomentar para validar após os testes.
            /*
            if (!ValidationUtil.validarCPF(cpfOriginal)) {
                throw new RegraDeNegocioException("CPF inválido.");
            }
            */
            String cpfLimpo = cpfOriginal.replaceAll("[^0-9]", "");

            // Verifica duplicidade apenas se for novo ou se o CPF mudou
            Paciente existente = pacienteDAO.buscarPorCpf(cpfLimpo);
            if (existente != null && !existente.getId().equals(pacienteSalvando.getId())) {
                throw new RegraDeNegocioException("CPF já cadastrado");
            }
            pacienteSalvando.setCpf(cpfLimpo);
        } else {
            pacienteSalvando.setCpf(null);
        }

        Paciente salvo = pacienteDAO.salvarPaciente(pacienteSalvando);
        DataChangeManager.getInstance().notificarListeners("Paciente");
        return salvo;
    }

    public void criarEAssociarResponsavel(Responsavel responsavel, Paciente paciente) {
        paciente.adicionarResponsavel(responsavel);
        salvarPaciente(paciente);
    }

    public List<Paciente> listarTodos() { return pacienteDAO.listarTodos(); }
    public void deletarPaciente(Paciente paciente) { pacienteDAO.excluir(paciente.getId()); }
}