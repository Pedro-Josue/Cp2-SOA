package com.cp2.SOA.Pedido.Repository;

import com.cp2.SOA.Pedido.Model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {
}