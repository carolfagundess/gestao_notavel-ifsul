package br.com.gestaonotavel.ifsul.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "movimentacao_financeira")
public class MovimentacaoFinanceira implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMovimentacaoFinanceira;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimento tipoMovimento;
    @Column(nullable = false, precision = 19, scale = 2)
    private Double valorMovimentacao;
    @Column(nullable = false)
    private LocalDateTime dataMovimentacao;
    @Column(nullable = false, length = 255)
    private String observacao;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private FormaPagamento formaPagamento;

    @ManyToOne
    @JoinColumn(name = "especialista_id")
    private Especialista especialista;

    @ManyToOne
    @JoinColumn(name = "atividade_id")
    private Atividade atividade;

    public MovimentacaoFinanceira() {
    }

    public MovimentacaoFinanceira(Integer idMovimentacaoFinanceira, TipoMovimento tipoMovimento, Double valorMovimentacao, LocalDateTime dataMovimentacao, String observacao, FormaPagamento formaPagamento) {
        this.idMovimentacaoFinanceira = idMovimentacaoFinanceira;
        this.tipoMovimento = tipoMovimento;
        this.valorMovimentacao = valorMovimentacao;
        this.dataMovimentacao = dataMovimentacao;
        this.observacao = observacao;
        this.formaPagamento = formaPagamento;
    }

    public Integer getIdMovimentacaoFinanceira() {
        return idMovimentacaoFinanceira;
    }

    public void setIdMovimentacaoFinanceira(Integer idMovimentacaoFinanceira) {
        this.idMovimentacaoFinanceira = idMovimentacaoFinanceira;
    }

    public TipoMovimento getTipoMovimento() {
        return tipoMovimento;
    }

    public void setTipoMovimento(TipoMovimento tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    public Double getValorMovimentacao() {
        return valorMovimentacao;
    }

    public void setValorMovimentacao(Double valorMovimentacao) {
        this.valorMovimentacao = valorMovimentacao;
    }

    public LocalDateTime getDataMovimentacao() {
        return dataMovimentacao;
    }

    public void setDataMovimentacao(LocalDateTime dataMovimentacao) {
        this.dataMovimentacao = dataMovimentacao;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public FormaPagamento getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(FormaPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }
}
