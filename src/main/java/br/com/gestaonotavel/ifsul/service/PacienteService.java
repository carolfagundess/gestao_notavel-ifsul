package br.com.gestaonotavel.ifsul.service;


import br.com.gestaonotavel.ifsul.dao.PacienteDAO;
import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import br.com.gestaonotavel.ifsul.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class PacienteService {

    PacienteDAO pacienteDAO = new PacienteDAO();

    public Paciente salvarPaciente(Paciente pacienteSalvando) {

// 1. Validações gerais (exceto CPF, que é opcional)
        validarPacienteCamposObrigatorios(pacienteSalvando); // Renomeado para clareza

        // 2. Validação, limpeza e verificação de duplicidade do CPF (APENAS se fornecido)
        String cpfOriginal = pacienteSalvando.getCpf();
        if (cpfOriginal != null && !cpfOriginal.trim().isEmpty()) { // Adicionado trim() aqui também

            // 2.1 Valida o formato e os dígitos verificadores
            if (!ValidationUtil.validarCPF(cpfOriginal)) { // Verifica o retorno!
                throw new RegraDeNegocioException("CPF inválido."); // Lança exceção se inválido
            }

            // 2.2 Limpa o CPF APÓS a validação
            String cpfLimpo = cpfOriginal.replaceAll("[^0-9]", "");

            // 2.3 Verifica duplicidade (considerando edição)
            Paciente pacienteExistente = pacienteDAO.buscarPorCpf(cpfLimpo);
            if (pacienteExistente != null && !Objects.equals(pacienteExistente.getIdPaciente(), pacienteSalvando.getIdPaciente())) {
                throw new RegraDeNegocioException("CPF já cadastrado para outro Paciente");
            }

            // 2.4 Atualiza o objeto com o CPF limpo antes de salvar
            pacienteSalvando.setCpf(cpfLimpo);
        } else {
            // Garante que, se o CPF for fornecido como espaços em branco, ele seja definido como null
            pacienteSalvando.setCpf(null);
        }

        // 3. Salva o paciente no banco
        Paciente salvo = pacienteDAO.salvarPaciente(pacienteSalvando);

        // 4. Notifica listeners sobre a mudança
        DataChangeManager.getInstance().notificarListeners("Paciente");
        return salvo;
    }

    public void criarEAssociarResponsavel(Responsavel responsavel, Paciente paciente) {
        paciente.adicionarResponsavel(responsavel);
        salvarPaciente(paciente);
    }

    public List<Paciente> listarTodos() {
        return pacienteDAO.listarTodos();
    }

    public void deletarPaciente(Paciente paciente) {
        pacienteDAO.excluir(paciente.getId());
    }

    private void validarPacienteCamposObrigatorios(Paciente pacienteSalvando) {
        if (pacienteSalvando == null) {
            throw new RegraDeNegocioException("Objeto Paciente não pode ser nulo.");
        }

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
