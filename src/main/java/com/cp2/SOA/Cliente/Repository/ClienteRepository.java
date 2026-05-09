package com.cp2.SOA.Cliente.Repository;

import com.cp2.SOA.Cliente.Model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}