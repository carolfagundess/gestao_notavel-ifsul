package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.AtendimentoDAO;
import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.StatusAtendimento;
import java.util.List;

public class AtendimentoService {

    AtendimentoDAO atendimentoDAO = new AtendimentoDAO();

    public Atendimento salvar(Atendimento atendimento){
        if(atendimento.getPaciente()==null) throw new IllegalArgumentException("Selecione um Paciente");
        if(atendimento.getEspecialista()==null) throw new IllegalArgumentException("Selecione um Especialista");
        if(atendimento.getDataHora()==null) throw new IllegalArgumentException("Data obrigatória");

        List<Atendimento> conflitos = atendimentoDAO.buscarPorEspecialistaEDataHora(atendimento.getEspecialista().getIdEspecialista(), atendimento.getDataHora());
        if (conflitos != null && !conflitos.isEmpty()){
            throw new IllegalArgumentException("Horário indisponível para este especialista");
        }

        atendimento.setStatusAtendimento(StatusAtendimento.AGENDADO);
        return atendimentoDAO.salvar(atendimento);
    }

    // Método novo necessário para o Calendário
    public List<Atendimento> listarTodos(Integer idEspecialista) {
        return atendimentoDAO.buscarPorEspecialista(idEspecialista);
    }
}