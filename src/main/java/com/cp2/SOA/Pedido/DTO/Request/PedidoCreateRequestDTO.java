package com.cp2.SOA.Pedido.DTO.Request;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoCreateRequestDTO(
    @NotNull(message = "Cliente é obrigatório")
    Long clienteId,

    @NotEmpty(message = "Pedido deve possuir itens")
    List<@Valid PedidoItemRequestDTO> itens
) {
}