package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.AtividadeDAO;
import br.com.gestaonotavel.ifsul.model.Atividade;
import br.com.gestaonotavel.ifsul.util.DataChangeManager;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;

import java.util.List;

public class AtividadeService {

    private AtividadeDAO atividadeDAO = new AtividadeDAO();

    public Atividade salvar(Atividade atividade) {
        if (atividade.getNome() == null || atividade.getNome().trim().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o nome da atividade");
        }
        if (atividade.getTipo() == null) {
            throw new RegraDeNegocioException("Selecione o tipo da atividade");
        }
        if (atividade.getDataInicio() == null) {
            throw new RegraDeNegocioException("Preencha uma data de início");
        }
        if (atividade.getDataFim() == null) {
            throw new RegraDeNegocioException("Preencha uma data de fim");
        }
        if (atividade.getLocal() == null || atividade.getLocal().trim().isEmpty()) {
            throw new RegraDeNegocioException("Preencha o local da atividade");
        }
        if (atividade.getDataInicio().isAfter(atividade.getDataFim())) {
            throw new RegraDeNegocioException("A data de início não pode ser posterior à data de fim");
        }

        atividade.setNome(atividade.getNome().trim());
        atividade.setLocal(atividade.getLocal().trim());

        if (atividade.getValorArrecadado() == null) {
            atividade.setValorArrecadado(0.0);
        }

        Atividade atividadeSalva = atividadeDAO.salvar(atividade);
        DataChangeManager.getInstance().notificarListeners("Atividade");
        return atividadeSalva;
    }

    public List<Atividade> listarTodos() {
        return atividadeDAO.listarTodos();
    }
}