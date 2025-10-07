package br.com.gestaonotavel.ifsul.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "atividade")
public class Atividade implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAtividade;
    @Column(nullable = false, length = 100)
    private String nome;
    @Column(nullable = false)
    private LocalDateTime dataInicio;
    @Column(nullable = false)
    private LocalDateTime dataFim;
    private String local;
    @Column(precision = 12, scale = 2)
    private Double valorArrecadado;

    //criar uma classe tipo atividade
    @ManyToOne
    @JoinColumn(name = "tipo_atividade_id", nullable = false)
    private TipoAtividade tipo;

    public Atividade() {
    }

    public Atividade(Integer idAtividade, String nome, LocalDateTime dataInicio, LocalDateTime dataFim, String local, Double valorArrecadado, TipoAtividade tipo) {
        this.idAtividade = idAtividade;
        this.nome = nome;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.local = local;
        this.valorArrecadado = valorArrecadado;
        this.tipo = tipo;
    }

    public Integer getIdAtividade() {
        return idAtividade;
    }

    public void setIdAtividade(Integer idAtividade) {
        this.idAtividade = idAtividade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Double getValorArrecadado() {
        return valorArrecadado;
    }

    public void setValorArrecadado(Double valorArrecadado) {
        this.valorArrecadado = valorArrecadado;
    }

    public TipoAtividade getTipo() {
        return tipo;
    }

    public void setTipo(TipoAtividade tipo) {
        this.tipo = tipo;
    }
}
