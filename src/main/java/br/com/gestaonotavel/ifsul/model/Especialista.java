package br.com.gestaonotavel.ifsul.model;


import javax.persistence.*;
import java.io.Serializable;

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
}
