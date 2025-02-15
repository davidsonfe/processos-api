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




    public ProcessoDTO salvarProcesso(ProcessoDTO processoDTO, Long id) {
        if (processoDTO.getNpu() == null || processoDTO.getNpu().isEmpty()) {
            throw new IllegalArgumentException("O campo NPU é obrigatório!");
        }


        Processo processoExistente = processoRepository.findByNpu(processoDTO.getNpu()).orElse(null);


        if (processoExistente != null) {
            processoExistente.setDataCadastro(processoDTO.getDataCadastro());
            processoExistente.setUf(processoDTO.getUf());
            processoExistente.setMunicipio(processoDTO.getMunicipio());

            Processo processoAtualizado = processoRepository.save(processoExistente);
            return ProcessoMapper.toDTO(processoAtualizado);
        }

        // 🔹 Se for um novo processo, cria uma nova entidade
        Processo novoProcesso = ProcessoMapper.toEntity(processoDTO);
        Processo processoSalvo = processoRepository.save(novoProcesso);

        return ProcessoMapper.toDTO(processoSalvo);
    }





    public List<ProcessoDTO> listarProcessos() {
        return processoRepository.findAll()
                .stream()
                .map(ProcessoMapper::toDTO)
                .collect(Collectors.toList());
    }


    public Page<ProcessoDTO> listarProcessosPaginados(Pageable pageable) {
        return processoRepository.findAll(pageable)
                .map(ProcessoMapper::toDTO);
    }


    public Optional<ProcessoDTO> buscarProcessoPorId(Long id) {
        return processoRepository.findById(id)
                .map(ProcessoMapper::toDTO);
    }


    public ProcessoDTO atualizarProcesso(Long id, ProcessoDTO processoDTO) {
        // Verifica se o processo existe
        Processo processoExistente = processoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado com o ID: " + id));


        Optional<Processo> processoComMesmoNpu = processoRepository.findByNpu(processoDTO.getNpu());
        if (processoComMesmoNpu.isPresent() && !processoComMesmoNpu.get().getId().equals(id)) {
            throw new IllegalArgumentException("Já existe um processo com este NPU!");
        }


        processoExistente.setNpu(processoDTO.getNpu());
        processoExistente.setDataCadastro(processoDTO.getDataCadastro());
        processoExistente.setMunicipio(processoDTO.getMunicipio());
        processoExistente.setUf(processoDTO.getUf());


        Processo processoAtualizado = processoRepository.save(processoExistente);


        return ProcessoMapper.toDTO(processoAtualizado);
    }


    public void deletarProcessoPorNpu(String npu) {
        Processo processo = processoRepository.findByNpu(npu)
                .orElseThrow(() -> new IllegalArgumentException("Processo não encontrado com NPU: " + npu));
        processoRepository.delete(processo);
    }

    public void uploadDocumento(Long id, MultipartFile file) throws IOException {
        Processo processo = processoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Processo não encontrado com o ID: " + id));

        byte[] bytes = file.getBytes();
        String encodedFile = Base64.getEncoder().encodeToString(bytes); // Codifica o arquivo em Base64
        processo.setDocumento(encodedFile.getBytes());

        processoRepository.save(processo);
    }


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
