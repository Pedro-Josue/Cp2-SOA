package com.cp2.SOA.Pedido.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record PedidoCreateRequestDTO(@NotNull Long produtoId,
                                     @NotNull @Min(1) Integer quantidade) {
}
