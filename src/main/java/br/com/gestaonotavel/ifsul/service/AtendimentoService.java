package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.AtendimentoDAO;
import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.StatusAtendimento;

import java.util.ArrayList;
import java.util.List;

public class AtendimentoService {

    AtendimentoDAO atendimentoDAO = new AtendimentoDAO();

    public Atendimento salvar(Atendimento atendimento){
        if(atendimento.getPaciente()==null){
            throw new IllegalArgumentException("Selecione um Paciente");
        }else if(atendimento.getEspecialista()==null){
            throw new IllegalArgumentException("Selecione um Especialista");
        }else if(atendimento.getDataHora()==null){
            throw new IllegalArgumentException("Selecione um Data e Horário");
        }
        List<Atendimento> testeAtendimentos = new ArrayList<>();
        testeAtendimentos = atendimentoDAO.buscarPorEspecialistaEDataHora(atendimento.getEspecialista().getIdEspecialista(),atendimento.getDataHora());

        if (!testeAtendimentos.isEmpty()){
            throw new IllegalArgumentException("Existe um atendimento listado nesta data para o especialista");
        }

        atendimento.setStatusAtendimento(StatusAtendimento.AGENDADO);

        return atendimentoDAO.salvar(atendimento);
    }
}
