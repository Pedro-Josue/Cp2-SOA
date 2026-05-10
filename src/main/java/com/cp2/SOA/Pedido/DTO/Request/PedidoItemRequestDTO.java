package com.cp2.SOA.Pedido.DTO.Request;

import jakarta.validation.constraints.NotNull;
public record PedidoItemRequestDTO(
    @NotNull Long produtoId,
    Integer quantidade
) {
}