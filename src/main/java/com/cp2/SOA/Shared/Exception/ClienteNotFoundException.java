package com.cp2.SOA.Shared.Exception;

public class ClienteNotFoundException extends RuntimeException {
    public ClienteNotFoundException(Long id) {
        super("Cliente não encontrado. ID: " + id);
    }
}