package com.URsolutions.Billing_System.repository;

import com.URsolutions.Billing_System.model.BillItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BillItemRepository extends JpaRepository<BillItem, Long> {
}