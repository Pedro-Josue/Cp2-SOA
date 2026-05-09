package com.cp2.SOA.Pagamento.Model;

import com.cp2.SOA.Pagamento.Enum.PagamentoStatus;
import com.cp2.SOA.Pedido.Model.Pedido;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pedido pedido;

    @Column(nullable = false)
    private BigDecimal valor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PagamentoStatus status;

    @Column(name = "forma_pagamento")
    private String formaPagamento;

    @Column(name = "data_pagamento")
    private LocalDateTime dataPagamento;
}
