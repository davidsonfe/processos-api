package com.example.processos_api.controller;

import com.example.processos_api.dto.PageResponse;
import com.example.processos_api.dto.ProcessoDTO;
import com.example.processos_api.mapper.ProcessoMapper;
import com.example.processos_api.model.Processo;
import com.example.processos_api.service.ProcessoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

@Validated
@RestController
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;

    // Criar um processo mantendo os parâmetros explícitos
    @PostMapping
    public ResponseEntity<ProcessoDTO> criarProcesso(
            @RequestParam("npu") String npu,
            @RequestParam("dataCadastro") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataCadastro,
            @RequestParam("municipio") String municipio,
            @RequestParam("uf") String uf,
            @RequestParam("documento") MultipartFile documento) {

        try {
            // Criando o DTO diretamente
            ProcessoDTO processoDTO = new ProcessoDTO();
            processoDTO.setNpu(npu);
            processoDTO.setDataCadastro(dataCadastro);
            processoDTO.setMunicipio(municipio);
            processoDTO.setUf(uf);
            processoDTO.setDocumento(documento.getBytes());

            // Passando o DTO diretamente para o serviço, sem converter para a entidade
            ProcessoDTO processoSalvo = processoService.salvarProcesso(processoDTO);

            return ResponseEntity.ok(processoSalvo);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }



    // Listar todos os processos (paginado)
    @GetMapping
    public ResponseEntity<PageResponse<ProcessoDTO>> listarProcessos(Pageable pageable) {
        Page<ProcessoDTO> processos = processoService.listarProcessosPaginados(pageable);

        PageResponse<ProcessoDTO> response = new PageResponse<>(
                processos.getContent(),
                processos.getTotalPages(),
                processos.getTotalElements(),
                processos.getSize(),
                processos.getNumber()
        );

        return ResponseEntity.ok(response);
    }


    // Buscar um processo por ID
    // Buscar um processo por ID
    @GetMapping("/{id}")
    public ResponseEntity<ProcessoDTO> buscarProcessoPorId(@PathVariable Long id) {
        return processoService.buscarProcessoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Atualizar um processo
    @PutMapping("/{id}")
    public ResponseEntity<ProcessoDTO> atualizarProcesso(@PathVariable Long id, @RequestBody ProcessoDTO processoDTO) {
        ProcessoDTO processoAtualizado = processoService.atualizarProcesso(id, processoDTO);
        return ResponseEntity.ok(processoAtualizado);
    }


    // Deletar um processo
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProcesso(@PathVariable Long id) {
        processoService.deletarProcesso(id);
        return ResponseEntity.noContent().build();
    }

    // Upload de documento PDF
    @PostMapping("/{id}/upload")
    public ResponseEntity<Void> uploadDocumento(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            processoService.uploadDocumento(id, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }

    // Marcar como visualizado
    // Marcar como visualizado
    @PatchMapping("/{id}/visualizar")
    public ResponseEntity<ProcessoDTO> marcarComoVisualizado(@PathVariable Long id) {
        return ResponseEntity.ok(processoService.marcarComoVisualizado(id));
    }

}
