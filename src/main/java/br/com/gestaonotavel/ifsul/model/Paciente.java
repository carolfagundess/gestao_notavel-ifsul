package br.com.gestaonotavel.ifsul.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.*;

@Entity
@Table(name = "paciente")
public class Paciente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idPaciente")
    private Long idPaciente;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 11)
    private String cpf;

    @Column(nullable = false)
    private Date dataNascimento;

    @Column(nullable = false, length = 50)
    private String escolaridade; //

    //Substitui deficiencia
    @Column(length = 255)
    private String diagnostico;

    //Para fins de aprofundamento da condição
    @Column(nullable = false, length = 100)
    private String condicaoClinica;

    @Column(columnDefinition = "TEXT")
    private String observacoesGerais;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "vinculo_responsavel", joinColumns = @JoinColumn(name = "paciente_id"),
    inverseJoinColumns = @JoinColumn(name = "responsavel_id"))
    private List<Responsavel> responsaveisLista;

    public Paciente() {
    }

    public Paciente(String nome, Date dataNascimento, String condicaoClinica, String escolaridade, String diagnostico, String observacoesGerais) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.condicaoClinica = condicaoClinica;
        this.escolaridade = escolaridade;
        this.diagnostico = diagnostico;
        this.observacoesGerais = observacoesGerais;
    }

    public Long getId() {
        return idPaciente;
    }

    public void setId(Long id) {
        this.idPaciente = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEscolaridade() {
        return escolaridade;
    }

    public void setEscolaridade(String escolaridade) {
        this.escolaridade = escolaridade;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public void setDiagnostico(String diagnostico) {
        this.diagnostico = diagnostico;
    }

    public String getCondicaoClinica() {
        return condicaoClinica;
    }

    public void setCondicaoClinica(String condicaoClinica) {
        this.condicaoClinica = condicaoClinica;
    }

    public String getObservacoesGerais() {
        return observacoesGerais;
    }

    public void setObservacoesGerais(String observacoesGerais) {
        this.observacoesGerais = observacoesGerais;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Objects.hashCode(this.idPaciente);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Paciente other = (Paciente) obj;
        return Objects.equals(this.idPaciente, other.idPaciente);
    }
}
