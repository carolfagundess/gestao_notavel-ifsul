package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.PacienteDAO;
import br.com.gestaonotavel.ifsul.dao.ResponsavelDAO;
import br.com.gestaonotavel.ifsul.model.Paciente;
import br.com.gestaonotavel.ifsul.model.Responsavel;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;
import br.com.gestaonotavel.ifsul.util.ValidationUtil;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ResponsavelService {

    ResponsavelDAO responsavelDAO = new ResponsavelDAO();
    PacienteDAO pacienteDAO = new PacienteDAO();

    public Responsavel salvar(Responsavel responsavelSalvando) {

        LocalDate hoje = LocalDate.now();

        // --- Validações Iniciais ---
        if (responsavelSalvando == null) {
            throw new IllegalArgumentException("Preencha as informações");
        }
        if (responsavelSalvando.getNome() == null || responsavelSalvando.getNome().isEmpty()) {
            throw new IllegalArgumentException("Preencha o nome do Responsável");
        }
        // A validação de CPF nulo/vazio foi movida para baixo
        if (responsavelSalvando.getTelefone() == null || responsavelSalvando.getTelefone().isEmpty()) {
            throw new IllegalArgumentException("Preencha o número de telefone do Responsável");
        }
        if (responsavelSalvando.getDataNascimento() == null) { // Adicionada verificação de nulidade da data
            throw new IllegalArgumentException("Preencha a data de nascimento do Responsável");
        }
        if (responsavelSalvando.getDataNascimento().isAfter(hoje)) {
            throw new IllegalArgumentException("Preencha uma data de nascimento do Responsável válida (não pode ser futura)");
        }
        // --- Fim das Validações Iniciais ---
        // --- Validação e Limpeza do CPF ---
        String cpfOriginal = responsavelSalvando.getCpf();
        if (cpfOriginal == null || cpfOriginal.trim().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o CPF do Responsável");
        }
        String cpfLimpo = cpfOriginal.replaceAll("[^0-9]", "");

        if (!ValidationUtil.validarCPF(cpfOriginal)) {
            throw new RegraDeNegocioException("CPF inválido");
        }
        // Busca por duplicidade usando o CPF limpo
        if (responsavelDAO.buscarPorCpf(cpfLimpo) != null) {
            // Usar RegraDeNegocioException para erros de negócio
            throw new RegraDeNegocioException("Este CPF já está cadastrado! Tente cadastrar um documento válido");
        }
        // Define o CPF limpo no objeto que será salvo
        responsavelSalvando.setCpf(cpfLimpo);
        // --- Fim da Validação e Limpeza do CPF ---
        // Salva o responsável com o CPF já limpo
        return responsavelDAO.salvarResponsavel(responsavelSalvando);
    }
}
