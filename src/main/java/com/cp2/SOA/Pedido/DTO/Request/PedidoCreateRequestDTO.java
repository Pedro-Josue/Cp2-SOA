package com.cp2.SOA.Pedido.DTO.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoCreateRequestDTO(@NotNull Long clienteId,
                                     @NotEmpty List<PedidoItemRequestDTO> itens) {
}
