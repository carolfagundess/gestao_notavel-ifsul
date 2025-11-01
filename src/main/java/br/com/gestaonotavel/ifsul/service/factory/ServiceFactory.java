package br.com.gestaonotavel.ifsul.service.factory;

// Importe nossa fábrica
// Importe o controller


import br.com.gestaonotavel.ifsul.service.*;

public class ServiceFactory {

    // Instância única (Singleton)
    private static ServiceFactory instance;

    // Instâncias únicas de cada serviço
    private final UsuarioService usuarioService;
    private final PacienteService pacienteService;
    private final EspecialistaService especialistaService;
    private final AtendimentoService atendimentoService;
    private final ResponsavelService responsavelService;
    private final MovimentacaoFinanceiraService movimentacaoFinanceiraService;
    private final AtividadeService atividadeService;
    private final RelatorioService relatorioService;
    // Adicione outros serviços aqui conforme eles forem criados...

    /**
     * Construtor privado: cria todas as instâncias de serviço uma única vez.
     */
    private ServiceFactory() {
        this.usuarioService = new UsuarioService();
        this.pacienteService = new PacienteService();
        this.especialistaService = new EspecialistaService();
        this.atendimentoService = new AtendimentoService();
        this.responsavelService = new ResponsavelService();
        this.movimentacaoFinanceiraService = new MovimentacaoFinanceiraService();
        this.atividadeService = new AtividadeService();
        this.relatorioService = new RelatorioService();
    }

    /**
     * Método público estático para obter a instância única da fábrica.
     */
    public static synchronized ServiceFactory getInstance() {
        if (instance == null) {
            instance = new ServiceFactory();
        }
        return instance;
    }

    // Getters públicos para cada serviço
    public UsuarioService getUsuarioService() {
        return usuarioService;
    }

    public PacienteService getPacienteService() {
        return pacienteService;
    }

    public EspecialistaService getEspecialistaService() {
        return especialistaService;
    }

    public AtendimentoService getAtendimentoService() {
        return atendimentoService;
    }

    public ResponsavelService getResponsavelService() {
        return responsavelService;
    }

    public MovimentacaoFinanceiraService getMovimentacaoFinanceiraService() {return movimentacaoFinanceiraService;}

    public AtividadeService getAtividadeService() {return atividadeService;}

    public RelatorioService getRelatorioService() {return relatorioService;}
}
