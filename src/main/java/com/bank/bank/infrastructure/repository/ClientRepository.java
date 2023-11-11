package com.bank.bank.infrastructure.repository;

import com.bank.bank.domain.model.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepository extends CrudRepository<Client, Long> {
}
