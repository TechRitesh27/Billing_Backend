package com.URsolutions.Billing_System.repository;

import com.URsolutions.Billing_System.model.Bill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillRepository extends JpaRepository<Bill, Long> {
}