package com.example.processos_api.controller;

import com.example.processos_api.dto.PageResponse;
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
import java.util.List;

@Validated
@RestController
@RequestMapping("/processos")
public class ProcessoController {

    @Autowired
    private ProcessoService processoService;

    // Criar um processo
    @PostMapping
    public ResponseEntity<Processo> criarProcesso(
            @RequestParam("npu") String npu,
            @RequestParam("dataCadastro") @DateTimeFormat(pattern = "yyyy-MM-dd") Date dataCadastro,
            @RequestParam("municipio") String municipio,
            @RequestParam("uf") String uf,
            @RequestParam("documento") MultipartFile documento) {

        try {
            Processo processo = new Processo();
            processo.setNpu(npu);
            processo.setDataCadastro(dataCadastro);
            processo.setMunicipio(municipio);
            processo.setUf(uf);
            processo.setDocumento(documento.getBytes()); // Converte o arquivo para byte[]

            Processo processoSalvo = processoService.salvarProcesso(processo);
            return ResponseEntity.ok(processoSalvo);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(null); // Retorna erro 500 se houver problema no upload
        }
    }


    // Listar todos os processos (paginado)
    @GetMapping
    public ResponseEntity<PageResponse<Processo>> listarProcessos(Pageable pageable) {
        Page<Processo> processos = processoService.listarProcessosPaginados(pageable);

        // Cria um objeto PageResponse com os dados da página
        PageResponse<Processo> response = new PageResponse<>(
                processos.getContent(),
                processos.getTotalPages(),
                processos.getTotalElements(),
                processos.getSize(),
                processos.getNumber()
        );

        return ResponseEntity.ok(response);
    }

    // Buscar um processo por ID
    @GetMapping("/{id}")
    public ResponseEntity<Processo> buscarProcessoPorId(@PathVariable Long id) {
        return processoService.buscarProcessoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Atualizar um processo
    @PutMapping("/{id}")
    public ResponseEntity<Processo> atualizarProcesso(@PathVariable Long id, @RequestBody Processo processoAtualizado) {
        return ResponseEntity.ok(processoService.atualizarProcesso(id, processoAtualizado));
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
            System.out.println("Recebendo upload do documento: " + file.getOriginalFilename());
            System.out.println("Tamanho: " + file.getSize() + " bytes");

            processoService.uploadDocumento(id, file);
            return ResponseEntity.ok().build();
        } catch (IOException e) {
            System.err.println("Erro ao salvar documento: " + e.getMessage());
            return ResponseEntity.status(500).build(); // Retorna erro 500 em caso de falha no upload
        }
    }


    @PatchMapping("/{id}/visualizar")
    public ResponseEntity<Processo> marcarComoVisualizado(@PathVariable Long id) {
        return ResponseEntity.ok(processoService.marcarComoVisualizado(id));
    }

}