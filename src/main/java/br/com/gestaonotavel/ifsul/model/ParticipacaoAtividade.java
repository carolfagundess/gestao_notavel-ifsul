package br.com.gestaonotavel.ifsul.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "participacao_atividade")
public class ParticipacaoAtividade implements Serializable {

    private static final double VALOR_HORA_CREDITO = 10.00;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "responsavel_id", nullable = false)
    private Responsavel responsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "atividade_id", nullable = false)
    private Atividade atividade;

    @Column(nullable = false)
    private LocalDateTime dataRegistro;

    @Column(nullable = false)
    private Double horasTrabalhadas;

    @Column(nullable = false)
    private Double creditosGerados;

    @Column(length = 255)
    private String funcaoDesempenhada;

    public ParticipacaoAtividade() {
    }

    public ParticipacaoAtividade(Responsavel responsavel, Atividade atividade, Double horasTrabalhadas, String funcaoDesempenhada) {
        this.responsavel = responsavel;
        this.atividade = atividade;
        this.horasTrabalhadas = horasTrabalhadas;
        this.funcaoDesempenhada = funcaoDesempenhada;
        this.dataRegistro = LocalDateTime.now();
    }

    @PrePersist
    public void calcularCreditos() {
        if (this.horasTrabalhadas != null) {
            this.creditosGerados = this.horasTrabalhadas * VALOR_HORA_CREDITO;
        } else {
            this.creditosGerados = 0.0;
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Responsavel getResponsavel() { return responsavel; }
    public void setResponsavel(Responsavel responsavel) { this.responsavel = responsavel; }
    public Atividade getAtividade() { return atividade; }
    public void setAtividade(Atividade atividade) { this.atividade = atividade; }
    public LocalDateTime getDataRegistro() { return dataRegistro; }
    public void setDataRegistro(LocalDateTime dataRegistro) { this.dataRegistro = dataRegistro; }
    public Double getHorasTrabalhadas() { return horasTrabalhadas; }
    public void setHorasTrabalhadas(Double horasTrabalhadas) { this.horasTrabalhadas = horasTrabalhadas; }
    public Double getCreditosGerados() { return creditosGerados; }
    public void setCreditosGerados(Double creditosGerados) { this.creditosGerados = creditosGerados; }
    public String getFuncaoDesempenhada() { return funcaoDesempenhada; }
    public void setFuncaoDesempenhada(String funcaoDesempenhada) { this.funcaoDesempenhada = funcaoDesempenhada; }
    public static double getValorHoraCredito() { return VALOR_HORA_CREDITO; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ParticipacaoAtividade that = (ParticipacaoAtividade) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}