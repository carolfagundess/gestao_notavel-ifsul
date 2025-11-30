package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.TipoAtividadeDAO;
import br.com.gestaonotavel.ifsul.model.TipoAtividade;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;

import javax.persistence.PersistenceException;
import java.util.List;

public class TipoAtividadeService {

    private final TipoAtividadeDAO tipoAtividadeDAO;

    public TipoAtividadeService() {
        this.tipoAtividadeDAO = new TipoAtividadeDAO();
    }

    public TipoAtividade salvar(String nomeTipo) {
        if (nomeTipo == null || nomeTipo.trim().isEmpty()) {
            throw new RegraDeNegocioException("O nome do tipo de atividade não pode ser vazio.");
        }

        String nomeFormatado = nomeTipo.trim();

        TipoAtividade existente = tipoAtividadeDAO.buscarPorNome(nomeFormatado);
        if (existente != null) {
            throw new RegraDeNegocioException("Este tipo de atividade já está cadastrado.");
        }

        TipoAtividade novoTipo = new TipoAtividade(nomeFormatado);
        return tipoAtividadeDAO.salvar(novoTipo);
    }

    public List<TipoAtividade> listarTodos() {
        return tipoAtividadeDAO.listarTodos();
    }

    public void excluir(TipoAtividade tipoAtividade) {
        if (tipoAtividade == null || tipoAtividade.getId() == null) {
            throw new RegraDeNegocioException("Tipo de atividade inválido para exclusão.");
        }
        try {
            tipoAtividadeDAO.excluir(tipoAtividade.getId());
        } catch (PersistenceException e) {
            throw new RegraDeNegocioException("Não é possível excluir este tipo, pois ele já está sendo usado em atividades cadastradas.");
        }
    }
}