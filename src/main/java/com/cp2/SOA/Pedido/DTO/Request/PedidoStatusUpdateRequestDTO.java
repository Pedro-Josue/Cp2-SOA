package com.cp2.SOA.Pedido.DTO.Request;

import jakarta.validation.constraints.NotBlank;

public record PedidoStatusUpdateRequestDTO(@NotBlank String status) {
}
