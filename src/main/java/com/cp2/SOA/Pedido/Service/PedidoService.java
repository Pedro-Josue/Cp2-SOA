package com.cp2.SOA.Pedido.Service;

import com.cp2.SOA.Cliente.Model.Cliente;
import com.cp2.SOA.Cliente.Repository.ClienteRepository;
import com.cp2.SOA.Estoque.EstoqueService;
import com.cp2.SOA.Notificacao.Enum.NotificacaoTipo;
import com.cp2.SOA.Notificacao.Notificacao.NotificacaoService;
import com.cp2.SOA.Pagamento.Enum.PagamentoStatus;
import com.cp2.SOA.Pagamento.Model.Pagamento;
import com.cp2.SOA.Pagamento.Service.PagamentoService;
import com.cp2.SOA.Pedido.DTO.Request.PedidoCreateRequestDTO;
import com.cp2.SOA.Pedido.DTO.Request.PedidoItemRequestDTO;
import com.cp2.SOA.Pedido.DTO.Response.PedidoItemResponseDTO;
import com.cp2.SOA.Pedido.DTO.Response.PedidoResponseDTO;
import com.cp2.SOA.Pedido.Enum.PedidoStatus;
import com.cp2.SOA.Pedido.Model.Pedido;
import com.cp2.SOA.Pedido.Model.PedidoItem;
import com.cp2.SOA.Pedido.Repository.PedidoRepository;
import com.cp2.SOA.Produto.Model.Produto;
import com.cp2.SOA.Produto.Service.ProdutoService;
import com.cp2.SOA.Shared.Exception.ClienteNotFoundException;
import com.cp2.SOA.Shared.Exception.PagamentoRecusadoException;
import com.cp2.SOA.Shared.Exception.PedidoNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteRepository clienteRepository;
    private final ProdutoService produtoService;
    private final EstoqueService estoqueService;
    private final PagamentoService pagamentoService;
    private final NotificacaoService notificacaoService;

    public PedidoService(PedidoRepository pedidoRepository,
                         ClienteRepository clienteRepository,
                         ProdutoService produtoService,
                         EstoqueService estoqueService,
                         PagamentoService pagamentoService,
                         NotificacaoService notificacaoService) {
        this.pedidoRepository = pedidoRepository;
        this.clienteRepository = clienteRepository;
        this.produtoService = produtoService;
        this.estoqueService = estoqueService;
        this.pagamentoService = pagamentoService;
        this.notificacaoService = notificacaoService;
    }

    public PedidoResponseDTO criarPedido(PedidoCreateRequestDTO dto) {

        if (dto.itens() == null || dto.itens().isEmpty()) {
            throw new IllegalArgumentException("Pedido não pode ser criado sem itens.");
        }

        Cliente cliente = clienteRepository.findById(dto.clienteId())
                .orElseThrow(() -> new ClienteNotFoundException(dto.clienteId()));

        // Validar estoque antes
        for (PedidoItemRequestDTO item : dto.itens()) {
            estoqueService.validarEstoque(item.produtoId(), item.quantidade());
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setStatus(PedidoStatus.CRIADO);
        pedido.setDataCriacao(LocalDateTime.now());
        pedido.setValorTotal(BigDecimal.ZERO);

        List<PedidoItem> itens = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;

        for (PedidoItemRequestDTO itemDTO : dto.itens()) {

            Produto produto = produtoService.buscarPorId(itemDTO.produtoId());

            BigDecimal subtotal = produto.getPreco()
                    .multiply(BigDecimal.valueOf(itemDTO.quantidade()));

            PedidoItem item = new PedidoItem();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setQuantidade(itemDTO.quantidade());
            item.setPrecoUnitario(produto.getPreco());
            item.setSubtotal(subtotal);

            itens.add(item);
            total = total.add(subtotal);
        }

        pedido.setItens(itens);
        pedido.setValorTotal(total);

        // salva pedido
        pedido.setStatus(PedidoStatus.AGUARDANDO_PAGAMENTO);
        pedido = pedidoRepository.save(pedido);

        // processar pagamento
        Pagamento pagamento = pagamentoService.processarPagamento(pedido);

        if (pagamento.getStatus() == PagamentoStatus.RECUSADO) {
            pedido.setStatus(PedidoStatus.CANCELADO);
            pedidoRepository.save(pedido);

            notificacaoService.registrar(pedido, NotificacaoTipo.PEDIDO_CANCELADO,
                    "Pedido " + pedido.getId() + " cancelado: pagamento recusado.");

            throw new PagamentoRecusadoException();
        }

        // pagamento aprovado
        pedido.setStatus(PedidoStatus.PAGO);
        pedidoRepository.save(pedido);

        notificacaoService.registrar(pedido, NotificacaoTipo.PAGAMENTO_APROVADO,
                "Pagamento aprovado para pedido " + pedido.getId());

        // baixar estoque
        for (PedidoItem item : pedido.getItens()) {
            estoqueService.baixarEstoque(item.getProduto().getId(), item.getQuantidade());
        }

        // finalizar pedido
        pedido.setStatus(PedidoStatus.FINALIZADO);
        pedidoRepository.save(pedido);

        notificacaoService.registrar(pedido, NotificacaoTipo.PEDIDO_FINALIZADO,
                "Pedido " + pedido.getId() + " finalizado com sucesso.");

        return toResponseDTO(pedido);
    }

    public PedidoResponseDTO buscarPedido(Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));

        return toResponseDTO(pedido);
    }

    public PedidoResponseDTO atualizarStatus(Long id, PedidoStatus novoStatus) {

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new PedidoNotFoundException(id));

        if (pedido.getStatus() == PedidoStatus.FINALIZADO) {
            throw new IllegalStateException("Pedido já finalizado não pode mudar status.");
        }

        pedido.setStatus(novoStatus);
        pedidoRepository.save(pedido);

        if (novoStatus == PedidoStatus.CANCELADO) {
            notificacaoService.registrar(pedido, NotificacaoTipo.PEDIDO_CANCELADO,
                    "Pedido " + pedido.getId() + " cancelado manualmente.");
        }

        return toResponseDTO(pedido);
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {

        List<PedidoItemResponseDTO> itensDTO = pedido.getItens().stream()
                .map(item -> new PedidoItemResponseDTO(
                        item.getProduto().getId(),
                        item.getProduto().getNome(),
                        item.getQuantidade(),
                        item.getPrecoUnitario(),
                        item.getSubtotal()
                ))
                .toList();

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getStatus(),
                pedido.getValorTotal(),
                pedido.getDataCriacao(),
                itensDTO
        );
    }
}
