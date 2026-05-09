package com.cp2.SOA.Produto.Repository;

import com.cp2.SOA.Produto.Model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {
}