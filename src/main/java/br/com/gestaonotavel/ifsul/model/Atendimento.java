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
    private LocalDateTime dataAtendimento;
    @Column(nullable = false, length = 60)
    private String local;
    @Column(nullable = false, length = 60)
    private Enum statusAtendimento;
    @Column(nullable = false, length = 60)
    private String observacao;
}



