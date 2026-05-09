package com.cp2.SOA.Shared.Exception;

import com.cp2.SOA.Shared.DTO.ErrorResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PedidoNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handlePedidoNotFound(PedidoNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(LocalDateTime.now(), 404, ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(ProdutoNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleProdutoNotFound(ProdutoNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(LocalDateTime.now(), 404, ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleClienteNotFound(ClienteNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponseDTO(LocalDateTime.now(), 404, ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<ErrorResponseDTO> handleEstoqueInsuficiente(EstoqueInsuficienteException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(LocalDateTime.now(), 400, ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(PagamentoRecusadoException.class)
    public ResponseEntity<ErrorResponseDTO> handlePagamentoRecusado(PagamentoRecusadoException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponseDTO(LocalDateTime.now(), 400, ex.getMessage(), req.getRequestURI()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponseDTO(LocalDateTime.now(), 500, "Erro interno: " + ex.getMessage(), req.getRequestURI()));
    }
}
