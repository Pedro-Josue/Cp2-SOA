package com.cp2.SOA.Pagamento.Repository;

import com.cp2.SOA.Pagamento.Model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
}
