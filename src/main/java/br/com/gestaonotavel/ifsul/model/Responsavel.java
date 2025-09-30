package br.com.gestaonotavel.ifsul.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "responsavel")
public class Responsavel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 100, nullable = false)
    private String nome;
    @Column(length = 11, nullable = false, unique = true)
    private String cpf;
    @Column(length = 11, nullable = false)
    private String telefone;
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    @Column(name = "horas_voluntariado")
    private Integer horasVoluntariado;
    @Column(name = "creditos")
    private Double creditos;

    public Responsavel() {
    }

    public Responsavel(Long id, String nome, String cpf, String telefone, Date dataNascimento, Integer horasVoluntariado, Double creditos) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.horasVoluntariado = horasVoluntariado;
        this.creditos = creditos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Integer getHorasVoluntariado() {
        return horasVoluntariado;
    }

    public void setHorasVoluntariado(Integer horasVoluntariado) {
        this.horasVoluntariado = horasVoluntariado;
    }

    public Double getCreditos() {
        return creditos;
    }

    public void setCreditos(Double creditos) {
        this.creditos = creditos;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Responsavel that = (Responsavel) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
