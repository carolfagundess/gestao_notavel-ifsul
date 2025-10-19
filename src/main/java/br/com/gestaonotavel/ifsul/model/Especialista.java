package br.com.gestaonotavel.ifsul.model;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "especialista")
public class Especialista implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEspecialista;
    @Column(nullable = false, length = 50)
    private String nome;
    @Column(nullable = false, length = 50)
    private String especialidade;
    @Column(nullable = false, name = "valor_sessao", precision = 10, scale = 2)
    private Double valorSessao;
    private Integer duracao;
    private Integer maxPacientes;
    private Integer pacientesAtuais;
    @Column(nullable = false, unique = true, length = 50, name = "registro_profissional")
    private String registroProfissional;

    public Especialista(Integer idEspecialista) {}

    public Especialista(Integer idEspecialista, String nome, String especialidade, Double valorSessao, Integer duracao, Integer maxPacientes, Integer pacientesAtuais, String registroProfissional) {
        this.idEspecialista = idEspecialista;
        this.nome = nome;
        this.especialidade = especialidade;
        this.valorSessao = valorSessao;
        this.duracao = duracao;
        this.maxPacientes = maxPacientes;
        this.pacientesAtuais = pacientesAtuais;
        this.registroProfissional = registroProfissional;
    }

    public Integer getIdEspecialista() {
        return idEspecialista;
    }

    public void setIdEspecialista(Integer idEspecialista) {
        this.idEspecialista = idEspecialista;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    public Double getValorSessao() {
        return valorSessao;
    }

    public void setValorSessao(Double valorSessao) {
        this.valorSessao = valorSessao;
    }

    public Integer getDuracao() {
        return duracao;
    }

    public void setDuracao(Integer duracao) {
        this.duracao = duracao;
    }

    public Integer getMaxPacientes() {
        return maxPacientes;
    }

    public void setMaxPacientes(Integer maxPacientes) {
        this.maxPacientes = maxPacientes;
    }

    public Integer getPacientesAtuais() {
        return pacientesAtuais;
    }

    public void setPacientesAtuais(Integer pacientesAtuais) {
        this.pacientesAtuais = pacientesAtuais;
    }

    public String getRegistroProfissional() {
        return registroProfissional;
    }

    public void setRegistroProfissional(String registroProfissional) {
        this.registroProfissional = registroProfissional;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Especialista that = (Especialista) o;
        return Objects.equals(idEspecialista, that.idEspecialista);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idEspecialista);
    }
}
