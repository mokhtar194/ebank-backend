package org.sid.ebankbackend.repositories;

import org.sid.ebankbackend.entities.AccountOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface BankOpperationRepository extends JpaRepository<AccountOperation,Long> {
    List<AccountOperation> findByBankAccountId(String account);
    Page<AccountOperation> findByBankAccountIdOrderByOperationDateDesc(String account, Pageable pageable);
}
