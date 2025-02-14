package com.example.processos_api.service;

import com.example.processos_api.dto.ProcessoDTO;
import com.example.processos_api.mapper.ProcessoMapper;
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
import java.util.stream.Collectors;

@Service
public class ProcessoService {

    @Autowired
    private ProcessoRepository processoRepository;

    @Autowired
    private IbgeService ibgeService;

    // Salvar um processo usando DTO
    public ProcessoDTO salvarProcesso(ProcessoDTO processoDTO) {
        // Validar UF e município
        if (!ibgeService.ufValida(processoDTO.getUf())) {
            throw new IllegalArgumentException("UF inválida!");
        }
        if (!ibgeService.municipioValido(processoDTO.getUf(), processoDTO.getMunicipio())) {
            throw new IllegalArgumentException("Município inválido ou não pertence à UF informada!");
        }

        // Converter DTO para entidade e salvar
        Processo processo = ProcessoMapper.toEntity(processoDTO);
        Processo processoSalvo = processoRepository.save(processo);

        // Retornar DTO correspondente
        return ProcessoMapper.toDTO(processoSalvo);
    }


    // Listar todos os processos (não paginado)
    public List<ProcessoDTO> listarProcessos() {
        return processoRepository.findAll()
                .stream()
                .map(ProcessoMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Listar processos paginados
    public Page<ProcessoDTO> listarProcessosPaginados(Pageable pageable) {
        return processoRepository.findAll(pageable)
                .map(ProcessoMapper::toDTO);
    }

    // Buscar um processo por ID
    public Optional<ProcessoDTO> buscarProcessoPorId(Long id) {
        return processoRepository.findById(id)
                .map(ProcessoMapper::toDTO);
    }

    // Atualizar um processo
    public ProcessoDTO atualizarProcesso(Long id, ProcessoDTO processoDTO) {
        return processoRepository.findById(id)
                .map(processo -> {
                    processo.setNpu(processoDTO.getNpu());
                    processo.setDataCadastro(processoDTO.getDataCadastro());
                    processo.setMunicipio(processoDTO.getMunicipio());
                    processo.setUf(processoDTO.getUf());
                    processo.setDocumento(processoDTO.getDocumento());
                    Processo atualizado = processoRepository.save(processo);
                    return ProcessoMapper.toDTO(atualizado);
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

    // Marcar como visualizado
    public ProcessoDTO marcarComoVisualizado(Long id) {
        return processoRepository.findById(id)
                .map(processo -> {
                    processo.setDataVisualizacao(new Date());
                    Processo atualizado = processoRepository.save(processo);
                    return ProcessoMapper.toDTO(atualizado);
                })
                .orElseThrow(() -> new RuntimeException("Processo não encontrado com o ID: " + id));
    }
}
