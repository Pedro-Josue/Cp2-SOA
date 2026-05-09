package com.cp2.SOA.Notificacao.Repository;

import com.cp2.SOA.Notificacao.Model.Notificacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificacaoRepository extends JpaRepository<Notificacao, Long> {
}
