package org.prgrms.kdt.voucher.repository;

import org.prgrms.kdt.voucher.domain.Customer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepository {

    Customer insert(Customer customer);

    Customer update(Customer customer);

    List<Customer> findAll();

    Optional<Customer> findById(UUID customerId);

    Optional<Customer> findByName(String customerId);

    Optional<Customer> findByEmail(String customerId);

    void deleteAll();

}
