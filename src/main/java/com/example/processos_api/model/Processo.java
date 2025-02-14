package com.example.processos_api.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String npu;

    @Column(nullable = false)
    private Date dataCadastro;

    @Column
    private Date dataVisualizacao;

    @Column(nullable = false)
    private String municipio;

    @Column(nullable = false)
    private String uf;

    @Lob
    @Column(nullable = false)
    private byte[] documento;

    @Version
    @Column(nullable = false)
    private Integer version;
}
