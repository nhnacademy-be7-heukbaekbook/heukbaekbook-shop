package com.nhnacademy.heukbaekbookshop.memberset.customer.repository;

import com.nhnacademy.heukbaekbookshop.memberset.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsCustomerByEmail(String Email);

}
