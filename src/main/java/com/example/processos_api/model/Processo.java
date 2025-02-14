package com.example.processos_api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.Date;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
@Entity
public class Processo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "O NPU é obrigatório.")
    @Pattern(regexp = "^[0-9]{7}-[0-9]{2}\\.[0-9]{4}\\.[0-9]{1}\\.[0-9]{2}\\.[0-9]{4}$",
            message = "NPU inválido. O formato esperado é 1111111-11.1111.1.11.1111")
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

    // Adicionando o controle de versão
    @Version
    @Column(nullable = false)
    private Integer version;
}
