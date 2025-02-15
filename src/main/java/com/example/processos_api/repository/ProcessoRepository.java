package com.example.processos_api.repository;

import com.example.processos_api.model.Processo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessoRepository extends JpaRepository<Processo, Long> {
    @Override
    Page<Processo> findAll(Pageable pageable);
    Optional<Processo> findByNpu(String npu);

}