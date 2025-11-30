package br.com.gestaonotavel.ifsul.service.factory;

import br.com.gestaonotavel.ifsul.service.*;

public class ServiceFactory {
    private static ServiceFactory instance;

    private final UsuarioService usuarioService;
    private final PacienteService pacienteService;
    private final EspecialistaService especialistaService;
    private final AtendimentoService atendimentoService;
    private final ResponsavelService responsavelService;
    private final MovimentacaoFinanceiraService movimentacaoFinanceiraService;
    private final AtividadeService atividadeService;
    private final RelatorioService relatorioService;
    private final AuditoriaLogService auditoriaLogService;
    private final TipoAtividadeService tipoAtividadeService;
    private final ParticipacaoAtividadeService participacaoAtividadeService;

    private ServiceFactory() {
        this.usuarioService = new UsuarioService();
        this.pacienteService = new PacienteService();
        this.especialistaService = new EspecialistaService();
        this.atendimentoService = new AtendimentoService();
        this.responsavelService = new ResponsavelService();
        this.movimentacaoFinanceiraService = new MovimentacaoFinanceiraService();
        this.atividadeService = new AtividadeService();
        this.relatorioService = new RelatorioService();
        this.auditoriaLogService = new AuditoriaLogService();
        this.tipoAtividadeService = new TipoAtividadeService();
        this.participacaoAtividadeService = new ParticipacaoAtividadeService();
    }

    public static synchronized ServiceFactory getInstance() {
        if (instance == null) instance = new ServiceFactory();
        return instance;
    }

    public UsuarioService getUsuarioService() { return usuarioService; }
    public PacienteService getPacienteService() { return pacienteService; }
    public EspecialistaService getEspecialistaService() { return especialistaService; }
    public AtendimentoService getAtendimentoService() { return atendimentoService; }
    public ResponsavelService getResponsavelService() { return responsavelService; }
    public MovimentacaoFinanceiraService getMovimentacaoFinanceiraService() { return movimentacaoFinanceiraService; }
    public AtividadeService getAtividadeService() { return atividadeService; }
    public RelatorioService getRelatorioService() { return relatorioService; }
    public AuditoriaLogService getAuditoriaLogService() { return auditoriaLogService; }
    public TipoAtividadeService getTipoAtividadeService() { return tipoAtividadeService; }
    public ParticipacaoAtividadeService getParticipacaoAtividadeService() { return participacaoAtividadeService; }
}