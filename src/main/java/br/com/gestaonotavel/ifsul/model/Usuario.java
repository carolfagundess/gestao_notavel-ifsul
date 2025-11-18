package br.com.gestaonotavel.ifsul.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
/**
 *
 * @author carol
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nome;

    @Column(nullable = false, length = 100, unique = true)
    private String email;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(nullable = false, length = 255)
    private String senha;

    @Column(nullable = false, length = 20)
    private String telefone;

    // CAMPO ALTERADO: Trocamos String cargo por Role role
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private Role role; // Antigamente 'cargo'
    
    public Usuario() {
    }

    public Usuario(String nome, String email, String cpf, String senha, String telefone, Role role) {
        this.nome = nome;
        this.email = email;
        this.cpf = cpf;
        this.senha = senha;
        this.telefone = telefone;
        this.role = role;
    }

    // --- NOVO MÉTODO ---
    /**
     * Verifica se o usuário tem uma permissão específica, baseado em seu Role.
     * @param permission A permissão a ser verificada.
     * @return true se o usuário tiver a permissão, false caso contrário.
     */
    public boolean hasPermission(Permission permission) {
        if (this.role == null) {
            return false;
        }
        return this.role.hasPermission(permission);
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

    public String getCpf() { return cpf; }

    public void setCpf(String cpf) { this.cpf = cpf; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    // GETTER/SETTER ALTERADO
    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
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
        final Usuario other = (Usuario) obj;
        return Objects.equals(this.id, other.id);
    }
}
