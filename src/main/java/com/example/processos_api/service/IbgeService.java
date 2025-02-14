package com.example.processos_api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class IbgeService {

    private static final String URL_UFS = "https://servicodados.ibge.gov.br/api/v1/localidades/estados";
    private static final String URL_MUNICIPIOS = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/%s/municipios";

    private final RestTemplate restTemplate;

    @Autowired  // 🔹 Permite a injeção automática do RestTemplate
    public IbgeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<String> listarUfs() {
        try {
            EstadoIbge[] estados = restTemplate.getForObject(URL_UFS, EstadoIbge[].class);
            return estados != null ? Arrays.stream(estados).map(EstadoIbge::getSigla).toList() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            System.err.println("Erro ao buscar UFs: " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public List<String> listarMunicipiosPorUf(String uf) {
        try {
            MunicipioIbge[] municipios = restTemplate.getForObject(String.format(URL_MUNICIPIOS, uf), MunicipioIbge[].class);
            return municipios != null ? Arrays.stream(municipios).map(MunicipioIbge::getNome).toList() : Collections.emptyList();
        } catch (HttpClientErrorException e) {
            System.err.println("Erro ao buscar municípios da UF " + uf + ": " + e.getMessage());
            return Collections.emptyList();
        }
    }

    public boolean ufValida(String uf) {
        return listarUfs().contains(uf);
    }

    public boolean municipioValido(String uf, String municipio) {
        return listarMunicipiosPorUf(uf).contains(municipio);
    }

    private static class EstadoIbge {
        private String sigla;
        public String getSigla() { return sigla; }
    }

    private static class MunicipioIbge {
        private String nome;
        public String getNome() { return nome; }
    }
}
