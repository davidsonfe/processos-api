package com.example.processos_api.mapper;

import com.example.processos_api.dto.ProcessoDTO;
import com.example.processos_api.model.Processo;

public class ProcessoMapper {

    public static Processo toEntity(ProcessoDTO dto) {
        Processo processo = new Processo();
        processo.setNpu(dto.getNpu());
        processo.setDataCadastro(dto.getDataCadastro());
        processo.setDataVisualizacao(dto.getDataVisualizacao());
        processo.setMunicipio(dto.getMunicipio());
        processo.setUf(dto.getUf());
        processo.setDocumento(dto.getDocumento());
        return processo;
    }

    public static ProcessoDTO toDTO(Processo processo) {
        ProcessoDTO dto = new ProcessoDTO();
        dto.setNpu(processo.getNpu());
        dto.setDataCadastro(processo.getDataCadastro());
        dto.setDataVisualizacao(processo.getDataVisualizacao());
        dto.setMunicipio(processo.getMunicipio());
        dto.setUf(processo.getUf());
        dto.setDocumento(processo.getDocumento());
        return dto;
    }
}
