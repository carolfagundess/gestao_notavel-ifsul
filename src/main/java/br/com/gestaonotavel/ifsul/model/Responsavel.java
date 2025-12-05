package br.com.gestaonotavel.ifsul.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import br.com.gestaonotavel.ifsul.util.EncryptionUtil; // Import para criptografia

@Entity
@Table(name = "responsavel")
public class Responsavel implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    private String nome;

    // AUMENTADO PARA 255 (Para suportar criptografia ou máscaras)
    @Column(length = 255, nullable = false, unique = true)
    private String cpf;

    // AUMENTADO PARA 255 (Para evitar erro se salvar com máscara)
    @Column(length = 255, nullable = false)
    private String telefone;

    @Column(nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "horas_voluntariado")
    private Double horasVoluntariado = 0.0;

    @Column(name = "creditos")
    private Double creditos = 0.0;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "responsaveisLista")
    private List<Paciente> pacientesLista =  new ArrayList<>();

    @OneToMany(mappedBy = "responsavel", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<ParticipacaoAtividade> participacoes = new ArrayList<>();

    public Responsavel() {
    }

    public Responsavel(Long id, String nome, String cpf, String telefone, LocalDate dataNascimento, Double horasVoluntariado, Double creditos) {
        this.id = id;
        this.nome = nome;
        this.cpf = cpf;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.horasVoluntariado = horasVoluntariado;
        this.creditos = creditos;
    }

    // --- HOOKS DE CRIPTOGRAFIA (SPRINT 1) ---
    @PrePersist
    @PreUpdate
    public void criptografarDados() {
        this.cpf = EncryptionUtil.encrypt(this.cpf);
        // Opcional: Criptografar telefone também se desejar
        // this.telefone = EncryptionUtil.encrypt(this.telefone);
    }

    @PostLoad
    public void descriptografarDados() {
        this.cpf = EncryptionUtil.decrypt(this.cpf);
        // this.telefone = EncryptionUtil.decrypt(this.telefone);
    }
    // ---------------------------------------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public Double getHorasVoluntariado() { return horasVoluntariado; }
    public void setHorasVoluntariado(Double horasVoluntariado) { this.horasVoluntariado = horasVoluntariado; }
    public Double getCreditos() { return creditos; }
    public void setCreditos(Double creditos) { this.creditos = creditos; }
    public List<Paciente> getPacientesLista() { return pacientesLista; }
    public void setPacientesLista(List<Paciente> pacientesLista) { this.pacientesLista = pacientesLista; }
    public List<ParticipacaoAtividade> getParticipacoes() { return participacoes; }
    public void setParticipacoes(List<ParticipacaoAtividade> participacoes) { this.participacoes = participacoes; }

    public void adicionarResponsavel(Paciente paciente){
        this.pacientesLista.add(paciente);
        paciente.getResponsaveisLista().add(this);
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