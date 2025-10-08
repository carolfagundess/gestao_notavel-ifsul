package br.com.gestaonotavel.ifsul.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "atendimento")
public class Atendimento implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idAtendimento;
    @Column(nullable = false)
    private LocalDateTime dataHora;
    @Column(nullable = false, length = 60)
    private String local;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 60)
    private StatusAtendimento statusAtendimento;
    @Column(nullable = false, length = 255)
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "paciente_id", nullable = false)
    private Paciente paciente;

    @ManyToOne
    @JoinColumn(name = "especialista_id", nullable = false)
    private Especialista especialista;

    public Atendimento() {
    }

    public Atendimento(Integer idAtendimento, LocalDateTime dataHora, String local, StatusAtendimento statusAtendimento, String observacao, Paciente paciente, Especialista especialista) {
        this.idAtendimento = idAtendimento;
        this.dataHora = dataHora;
        this.local = local;
        this.statusAtendimento = statusAtendimento;
        this.observacao = observacao;
        this.paciente = paciente;
        this.especialista = especialista;
    }

    public Integer getIdAtendimento() {
        return idAtendimento;
    }

    public void setIdAtendimento(Integer idAtendimento) {
        this.idAtendimento = idAtendimento;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public StatusAtendimento getStatusAtendimento() {
        return statusAtendimento;
    }

    public void setStatusAtendimento(StatusAtendimento statusAtendimento) {
        this.statusAtendimento = statusAtendimento;
    }

    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Especialista getEspecialista() {
        return especialista;
    }

    public void setEspecialista(Especialista especialista) {
        this.especialista = especialista;
    }
}



