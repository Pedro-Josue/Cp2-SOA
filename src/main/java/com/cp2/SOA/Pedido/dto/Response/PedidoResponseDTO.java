package com.cp2.SOA.Pedido.dto.Response;

import com.cp2.SOA.Pedido.Enum.PedidoStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponseDTO(Long id,
                                Long clienteId,
                                String clienteNome,
                                PedidoStatus status,
                                BigDecimal valorTotal,
                                LocalDateTime dataCriacao,
                                List<PedidoItemResponseDTO> itens) {
}
