package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.AtividadeDAO;
import br.com.gestaonotavel.ifsul.model.Atividade;

public class AtividadeService {

    AtividadeDAO atividadeDAO = new AtividadeDAO();

    public Atividade salvar(Atividade atividade) {
        if (atividade.getNome() == null || atividade.getNome().isEmpty()) {
            throw new IllegalArgumentException("Preencha o nome da atividade");
        } else if (atividade.getTipo() == null) {
            throw new IllegalArgumentException("Selecione o tipo da atividade");
        } else if (atividade.getDataInicio() == null) {
            throw new IllegalArgumentException("Preencha uma data de início");
        } else if (atividade.getDataFim() == null) {
            throw new IllegalArgumentException("Preencha uma data de fim");
        }

        if (atividade.getDataInicio().isAfter(atividade.getDataFim())) {
            throw new IllegalArgumentException("A data do início  não pode ser posterior a data de fim");
        }
        return atividadeDAO.salvar(atividade);
    }

}
