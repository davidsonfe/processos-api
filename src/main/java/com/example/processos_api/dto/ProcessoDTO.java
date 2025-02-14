package com.example.processos_api.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.Date;

@Data
public class ProcessoDTO {

    @NotNull(message = "O NPU é obrigatório.")
    @Pattern(
            regexp = "^[0-9]{7}-[0-9]{2}\\.[0-9]{4}\\.[0-9]{1}\\.[0-9]{2}\\.[0-9]{4}$",
            message = "NPU inválido. O formato esperado é 1111111-11.1111.1.11.1111"
    )
    private String npu;

    @NotNull(message = "A data de cadastro é obrigatória.")
    private Date dataCadastro;

    private Date dataVisualizacao; // Pode ser null inicialmente

    @NotNull(message = "O município é obrigatório.")
    private String municipio;

    @NotNull(message = "A UF é obrigatória.")
    @Pattern(
            regexp = "^[A-Z]{2}$",
            message = "UF inválida. Deve conter exatamente duas letras maiúsculas (ex: SP, RJ, MG)."
    )
    private String uf;

    @NotNull(message = "O documento é obrigatório.")
    private byte[] documento;
}
