package com.cp2.SOA.Notificacao.Notificacao;

import com.cp2.SOA.Notificacao.Enum.NotificacaoTipo;
import com.cp2.SOA.Notificacao.Model.Notificacao;
import com.cp2.SOA.Notificacao.Repository.NotificacaoRepository;
import com.cp2.SOA.Pedido.Model.Pedido;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificacaoService {
    private final NotificacaoRepository notificacaoRepository;

    public NotificacaoService(NotificacaoRepository notificacaoRepository) {
        this.notificacaoRepository = notificacaoRepository;
    }

    public void registrar(Pedido pedido, NotificacaoTipo tipo, String mensagem) {
        Notificacao notificacao = Notificacao.builder()
                .pedido(pedido)
                .tipo(tipo)
                .mensagem(mensagem)
                .dataEnvio(LocalDateTime.now())
                .build();

        notificacaoRepository.save(notificacao);

        System.out.println("[NOTIFICACAO] " + mensagem);
    }
}
