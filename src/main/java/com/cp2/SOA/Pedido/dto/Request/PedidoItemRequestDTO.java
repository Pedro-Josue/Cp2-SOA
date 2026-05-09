package com.cp2.SOA.Pedido.dto.Request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PedidoItemRequestDTO(@NotNull Long clienteId,
                                   @NotEmpty @Valid List<PedidoItemRequestDTO> itens) {
}
