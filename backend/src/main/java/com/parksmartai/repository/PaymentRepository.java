package com.parksmartai.repository;

import com.parksmartai.entity.Payment;
import com.parksmartai.enums.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPaymentStatus(PaymentStatus status);

    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.paymentStatus = com.parksmartai.enums.PaymentStatus.PAID")
    BigDecimal totalRevenue();
}
