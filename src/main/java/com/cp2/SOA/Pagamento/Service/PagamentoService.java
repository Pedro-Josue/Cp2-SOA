package com.cp2.SOA.Pagamento.Service;

import com.cp2.SOA.Pagamento.Enum.PagamentoStatus;
import com.cp2.SOA.Pagamento.Model.Pagamento;
import com.cp2.SOA.Pagamento.Repository.PagamentoRepository;
import com.cp2.SOA.Pedido.Model.Pedido;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PagamentoService {
    private final PagamentoRepository pagamentoRepository;

    public PagamentoService(PagamentoRepository pagamentoRepository) {
        this.pagamentoRepository = pagamentoRepository;
    }

    public Pagamento processarPagamento(Pedido pedido) {

        boolean aprovado = pedido.getValorTotal().doubleValue() <= 150.0;

        Pagamento pagamento = Pagamento.builder()
                .pedido(pedido)
                .valor(pedido.getValorTotal())
                .formaPagamento("CARTAO")
                .dataPagamento(LocalDateTime.now())
                .status(aprovado ? PagamentoStatus.APROVADO : PagamentoStatus.RECUSADO)
                .build();

        return pagamentoRepository.save(pagamento);
    }
}
