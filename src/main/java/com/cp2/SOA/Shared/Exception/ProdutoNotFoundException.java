package com.cp2.SOA.Shared.Exception;

public class ProdutoNotFoundException extends RuntimeException {
    public ProdutoNotFoundException(Long id) {
        super("Produto não encontrado. ID: " + id);
    }
}
