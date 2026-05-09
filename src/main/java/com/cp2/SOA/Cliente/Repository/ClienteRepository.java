package com.cp2.SOA.Cliente.Repository;

import com.cp2.SOA.Cliente.Entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}