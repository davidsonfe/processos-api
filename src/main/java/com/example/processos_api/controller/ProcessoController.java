package com.example.processos_api.controller;

import com.example.processos_api.dto.PageResponse;
import com.example.processos_api.dto.ProcessoDTO;
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


@Validated
@RestController
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;


    @PostMapping
    public ResponseEntity<ProcessoDTO> criarProcesso(
            @RequestParam("npu") String npu,
            @RequestParam("dataCadastro") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataCadastro,
            @RequestParam("municipio") String municipio,
            @RequestParam("uf") String uf,
            @RequestParam("documento") MultipartFile documento) {

        try {

            ProcessoDTO processoDTO = new ProcessoDTO();
            processoDTO.setNpu(npu);
            processoDTO.setDataCadastro(dataCadastro);
            processoDTO.setMunicipio(municipio);
            processoDTO.setUf(uf);
            processoDTO.setDocumento(documento.getBytes());


            ProcessoDTO processoSalvo = processoService.salvarProcesso(processoDTO, null);

            return ResponseEntity.ok(processoSalvo);

        } catch (IOException e) {
            return ResponseEntity.status(500).body(null);
        }
    }




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



    @GetMapping("/{id}")
    public ResponseEntity<ProcessoDTO> buscarProcessoPorId(@PathVariable Long id) {
        return processoService.buscarProcessoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }



    @PutMapping("/{id}")
    public ResponseEntity<ProcessoDTO> atualizarProcesso(@PathVariable Long id, @RequestBody ProcessoDTO processoDTO) {
        ProcessoDTO processoAtualizado = processoService.atualizarProcesso(id, processoDTO);
        return ResponseEntity.ok(processoAtualizado);
    }



    @DeleteMapping("/npu/{npu}")
    public ResponseEntity<Void> deletarProcessoPorNpu(@PathVariable String npu) {
        processoService.deletarProcessoPorNpu(npu);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/upload")
    public ResponseEntity<Void> uploadDocumento(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            processoService.uploadDocumento(id, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            return ResponseEntity.status(500).build();
        }
    }



    @PatchMapping("/{id}/visualizar")
    public ResponseEntity<ProcessoDTO> marcarComoVisualizado(@PathVariable Long id) {
        return ResponseEntity.ok(processoService.marcarComoVisualizado(id));
    }

}
