package com.nhnacademy.heukbaekbookshop.order.repository;

import com.nhnacademy.heukbaekbookshop.order.domain.Refund;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefundRepository extends JpaRepository<Refund, Long>{

}