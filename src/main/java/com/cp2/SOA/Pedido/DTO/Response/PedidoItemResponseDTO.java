package com.cp2.SOA.Pedido.DTO.Response;

import java.math.BigDecimal;

public record PedidoItemResponseDTO(Long produtoId,
                                    String nomeProduto,
                                    Integer quantidade,
                                    BigDecimal precoUnitario,
                                    BigDecimal subtotal) {
}
