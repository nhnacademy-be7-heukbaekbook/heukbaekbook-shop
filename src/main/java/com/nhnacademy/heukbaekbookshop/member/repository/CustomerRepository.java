package com.nhnacademy.heukbaekbookshop.member.repository;

import com.nhnacademy.heukbaekbookshop.member.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer,Long> {
    boolean existsCustomerByEmail(String Email);
}
