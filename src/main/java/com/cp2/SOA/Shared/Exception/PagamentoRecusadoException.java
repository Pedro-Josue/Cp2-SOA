package com.cp2.SOA.Shared.Exception;

public class PagamentoRecusadoException extends RuntimeException {
    public PagamentoRecusadoException() {
        super("Pagamento recusado. Pedido foi cancelado.");
    }
}
