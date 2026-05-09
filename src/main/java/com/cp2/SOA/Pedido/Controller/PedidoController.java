package com.cp2.SOA.Pedido.Controller;

import com.cp2.SOA.Pedido.DTO.Request.PedidoCreateRequestDTO;
import com.cp2.SOA.Pedido.DTO.Request.PedidoStatusUpdateRequestDTO;
import com.cp2.SOA.Pedido.DTO.Response.PedidoResponseDTO;
import com.cp2.SOA.Pedido.Enum.PedidoStatus;
import com.cp2.SOA.Pedido.Service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criarPedido(@RequestBody @Valid PedidoCreateRequestDTO dto) {
        PedidoResponseDTO pedido = pedidoService.criarPedido(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPedido(@PathVariable Long id) {
        PedidoResponseDTO pedido = pedidoService.buscarPedido(id);
        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<PedidoResponseDTO> atualizarStatus(@PathVariable Long id,
                                                             @RequestBody @Valid PedidoStatusUpdateRequestDTO dto) {

        PedidoStatus status = PedidoStatus.valueOf(dto.status().toUpperCase());

        PedidoResponseDTO pedido = pedidoService.atualizarStatus(id, status);
        return ResponseEntity.ok(pedido);
    }
}
