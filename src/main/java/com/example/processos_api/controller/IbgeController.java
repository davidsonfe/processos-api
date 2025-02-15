package com.example.processos_api.controller;

import com.example.processos_api.service.IbgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class IbgeController {

    @Autowired
    private IbgeService ibgeService;


    @GetMapping("/ufs")
    public List<String> listarUfs() {
        return ibgeService.listarUfs();
    }


    @GetMapping("/municipios/{uf}")
    public List<String> listarMunicipios(@PathVariable String uf) {
        return ibgeService.listarMunicipiosPorUf(uf);
    }
}
