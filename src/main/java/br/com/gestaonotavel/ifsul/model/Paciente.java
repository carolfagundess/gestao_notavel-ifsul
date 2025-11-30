package br.com.gestaonotavel.ifsul.model;

import br.com.gestaonotavel.ifsul.util.EncryptionUtil;
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "paciente")
public class Paciente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPaciente;

    @Column(nullable = false, length = 100)
    private String nome;

    @Column(length = 255) // Aumentado para suportar criptografia
    private String cpf;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(nullable = false, length = 50)
    private String escolaridade;

    @Column(length = 255)
    private String diagnostico;

    @Column(length = 100)
    private String condicaoClinica;

    @Column(columnDefinition = "TEXT")
    private String observacoesGerais;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "vinculo_responsavel",
            joinColumns = @JoinColumn(name = "paciente_id"),
            inverseJoinColumns = @JoinColumn(name = "responsavel_id"))
    private List<Responsavel> responsaveisLista = new ArrayList<>();

    public Paciente() {
    }

    public Paciente(String nome, LocalDate dataNascimento, String condicaoClinica, String escolaridade, String diagnostico, String observacoesGerais) {
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.condicaoClinica = condicaoClinica;
        this.escolaridade = escolaridade;
        this.diagnostico = diagnostico;
        this.observacoesGerais = observacoesGerais;
    }

    // --- CRIPTOGRAFIA ---
    @PrePersist
    @PreUpdate
    public void criptografarDados() {
        this.cpf = EncryptionUtil.encrypt(this.cpf);
    }

    @PostLoad
    public void descriptografarDados() {
        this.cpf = EncryptionUtil.decrypt(this.cpf);
    }
    // --------------------

    public Long getId() { return idPaciente; }
    public void setId(Long id) { this.idPaciente = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getEscolaridade() { return escolaridade; }
    public void setEscolaridade(String escolaridade) { this.escolaridade = escolaridade; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getCondicaoClinica() { return condicaoClinica; }
    public void setCondicaoClinica(String condicaoClinica) { this.condicaoClinica = condicaoClinica; }
    public String getObservacoesGerais() { return observacoesGerais; }
    public void setObservacoesGerais(String observacoesGerais) { this.observacoesGerais = observacoesGerais; }
    public Long getIdPaciente() { return idPaciente; }
    public void setIdPaciente(Long idPaciente) { this.idPaciente = idPaciente; }
    public List<Responsavel> getResponsaveisLista() { return responsaveisLista; }
    public void setResponsaveisLista(List<Responsavel> responsaveisLista) { this.responsaveisLista = responsaveisLista; }

    public void adicionarResponsavel(Responsavel responsavel){
        this.responsaveisLista.add(responsavel);
        responsavel.getPacientesLista().add(this);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPaciente);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Paciente other = (Paciente) obj;
        return Objects.equals(this.idPaciente, other.idPaciente);
    }
}