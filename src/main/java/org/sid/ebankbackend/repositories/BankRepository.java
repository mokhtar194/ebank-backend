package org.sid.ebankbackend.repositories;

import org.sid.ebankbackend.entities.BankAccount;
import org.sid.ebankbackend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankRepository extends JpaRepository<BankAccount,String> {
    List<BankAccount> findByCustomer(Optional<Customer> customer);
}
