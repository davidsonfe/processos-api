package com.example.processos_api.service;

import com.example.processos_api.model.Processo;
import com.example.processos_api.repository.ProcessoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private IbgeService ibgeService;

    // Salvar um processo
    public Processo salvarProcesso(Processo processo) {
        if (!ibgeService.ufValida(processo.getUf())) {
            throw new IllegalArgumentException("UF inválida!");
        }

        if (!ibgeService.municipioValido(processo.getUf(), processo.getMunicipio())) {
            throw new IllegalArgumentException("Município inválido ou não pertence à UF informada!");
        }

        return processoRepository.save(processo);
    }

    // Listar todos os processos
    public List<Processo> listarProcessos() {
        return processoRepository.findAll();
    }

    public Page<Processo> listarProcessosPaginados(Pageable pageable) {
        return processoRepository.findAll(pageable);
    }

    // Buscar um processo por ID
    public Optional<Processo> buscarProcessoPorId(Long id) {
        return processoRepository.findById(id);
    }


    // Atualizar um processo
    public Processo atualizarProcesso(Long id, Processo processoAtualizado) {
        return processoRepository.findById(id)
                .map(processo -> {
                    processo.setNpu(processoAtualizado.getNpu());
                    processo.setDataCadastro(processoAtualizado.getDataCadastro());
                    processo.setMunicipio(processoAtualizado.getMunicipio());
                    processo.setUf(processoAtualizado.getUf());
                    processo.setDocumento(processoAtualizado.getDocumento());
                    return processoRepository.save(processo);
                })
                .orElseThrow(() -> new RuntimeException("Processo não encontrado com o ID: " + id));
    }

    // Deletar um processo
    public void deletarProcesso(Long id) {
        processoRepository.deleteById(id);
    }

    // Upload de documento PDF
    public void uploadDocumento(Long id, MultipartFile file) throws IOException {
        Processo processo = processoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado com o ID: " + id));

        byte[] bytes = file.getBytes();
        String encodedFile = Base64.getEncoder().encodeToString(bytes); // Codifica o arquivo em Base64
        processo.setDocumento(encodedFile.getBytes());

        processoRepository.save(processo);
    }

    public Processo marcarComoVisualizado(Long id) {
        return processoRepository.findById(id)
                .map(processo -> {
                    processo.setDataVisualizacao(new Date());
                    return processoRepository.save(processo);
                })
                .orElseThrow(() -> new RuntimeException("Processo não encontrado com o ID: " + id));
    }

}