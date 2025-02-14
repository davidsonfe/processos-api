package com.example.processos_api.dto;

import java.util.List;

public class PageResponse<T> {
    private List<T> content; // Lista de itens da página
    private int totalPages; // Número total de páginas
    private long totalElements; // Número total de elementos
    private int size; // Tamanho da página
    private int number; // Número da página atual

    // Construtor vazio (necessário para serialização JSON)
    public PageResponse() {}

    // Construtor com parâmetros
    public PageResponse(List<T> content, int totalPages, long totalElements, int size, int number) {
        this.content = content;
        this.totalPages = totalPages;
        this.totalElements = totalElements;
        this.size = size;
        this.number = number;
    }

    // Getters e Setters
    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}