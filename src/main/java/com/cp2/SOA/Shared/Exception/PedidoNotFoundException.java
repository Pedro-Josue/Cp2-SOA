package com.cp2.SOA.Shared.Exception;

public class PedidoNotFoundException extends RuntimeException {
    public PedidoNotFoundException(Long id) {
        super("Pedido não encontrado. ID: " + id);
    }
}
