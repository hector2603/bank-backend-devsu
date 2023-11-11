package com.bank.bank.infrastructure.repository;

import com.bank.bank.domain.model.Account;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<Account, Long> {
    Optional<Account> findByAccountNumber(Long accountNumber);
    List<Account> findAll();
    List<Account> findByClientId(Long clientId);

}
