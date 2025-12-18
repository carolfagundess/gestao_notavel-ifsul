package br.com.gestaonotavel.ifsul.service;

import br.com.gestaonotavel.ifsul.dao.AtendimentoDAO;
import br.com.gestaonotavel.ifsul.model.Atendimento;
import br.com.gestaonotavel.ifsul.model.StatusAtendimento;
import br.com.gestaonotavel.ifsul.service.factory.ServiceFactory;
import br.com.gestaonotavel.ifsul.util.RegraDeNegocioException;

import java.time.LocalDateTime;
import java.util.List;

public class AtendimentoService {

    private final AtendimentoDAO atendimentoDAO = new AtendimentoDAO();

    public Atendimento salvar(Atendimento atendimento){
        if(atendimento.getPaciente()==null) throw new RegraDeNegocioException("Selecione um Paciente");
        if(atendimento.getEspecialista()==null) throw new RegraDeNegocioException("Selecione um Especialista");
        if(atendimento.getDataHora()==null) throw new RegraDeNegocioException("Data obrigatória");

        // Validação de Conflito de Horário
        validarConflitoHorario(atendimento);

        // Define o status como AGENDADO apenas para novos atendimentos
        if (atendimento.getIdAtendimento() == null) {
            atendimento.setStatusAtendimento(StatusAtendimento.AGENDADO);
        }
        
        return atendimentoDAO.salvar(atendimento);
    }

    private void validarConflitoHorario(Atendimento novoAtendimento) {
        Integer duracaoMinutos = novoAtendimento.getEspecialista().getDuracao();
        if (duracaoMinutos == null || duracaoMinutos <= 0) {
            duracaoMinutos = 50; // Valor padrão caso não definido
        }

        LocalDateTime inicioNovo = novoAtendimento.getDataHora();
        LocalDateTime fimNovo = inicioNovo.plusMinutes(duracaoMinutos);

        // Busca todos os atendimentos do dia para o especialista (exceto cancelados)
        List<Atendimento> atendimentosDoDia = atendimentoDAO.buscarConflitosDeHorario(
                novoAtendimento.getEspecialista().getIdEspecialista(),
                inicioNovo,
                fimNovo
        );

        for (Atendimento existente : atendimentosDoDia) {
            // Se for edição, ignora o próprio atendimento
            if (novoAtendimento.getIdAtendimento() != null && 
                novoAtendimento.getIdAtendimento().equals(existente.getIdAtendimento())) {
                continue;
            }

            Integer duracaoExistente = existente.getEspecialista().getDuracao();
            if (duracaoExistente == null || duracaoExistente <= 0) duracaoExistente = 50;

            LocalDateTime inicioExistente = existente.getDataHora();
            LocalDateTime fimExistente = inicioExistente.plusMinutes(duracaoExistente);

            // Lógica de sobreposição: (StartA < EndB) && (EndA > StartB)
            if (inicioNovo.isBefore(fimExistente) && fimNovo.isAfter(inicioExistente)) {
                throw new RegraDeNegocioException("Conflito de horário! Já existe um atendimento das " +
                        inicioExistente.toLocalTime() + " às " + fimExistente.toLocalTime());
            }
        }
    }

    public void atualizarStatus(Integer atendimentoId, StatusAtendimento novoStatus) {
        Atendimento atendimento = atendimentoDAO.buscarPorId(atendimentoId);
        if (atendimento == null) {
            throw new RegraDeNegocioException("Atendimento não encontrado.");
        }
        atendimento.setStatusAtendimento(novoStatus);
        atendimentoDAO.salvar(atendimento);
    }

    public List<Atendimento> listarTodos(Integer idEspecialista) {
        return atendimentoDAO.buscarPorEspecialista(idEspecialista);
    }
}
