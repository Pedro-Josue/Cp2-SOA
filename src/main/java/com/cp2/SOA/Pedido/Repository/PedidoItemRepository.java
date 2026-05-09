package com.cp2.SOA.Pedido.Repository;

import com.cp2.SOA.Pedido.Model.PedidoItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PedidoItemRepository extends JpaRepository<PedidoItem, Long> {
}
