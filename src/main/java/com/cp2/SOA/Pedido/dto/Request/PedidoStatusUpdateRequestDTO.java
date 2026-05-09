package com.cp2.SOA.Pedido.dto.Request;

import jakarta.validation.constraints.NotBlank;

public record PedidoStatusUpdateRequestDTO(@NotBlank String status) {
}
