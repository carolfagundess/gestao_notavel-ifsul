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
        // Nota: Se seus serviços precisarem de DAOs, você os instanciaria aqui também.
        // Ex: this.pacienteService = new PacienteService(new PacienteDAO());
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
}
