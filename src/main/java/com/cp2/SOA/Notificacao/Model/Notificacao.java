package com.cp2.SOA.Notificacao.Model;

import com.cp2.SOA.Notificacao.Enum.NotificacaoTipo;
import com.cp2.SOA.Pedido.Model.Pedido;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificacao")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Pedido pedido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NotificacaoTipo tipo;

    @Column(nullable = false, length = 255)
    private String mensagem;

    @Column(name = "data_envio")
    private LocalDateTime dataEnvio;
}
