package com.dio.pix.repository;
import com.dio.pix.entity.Payment;
import com.dio.pix.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByTxid(String txid);
    List<Payment> findByStatus(PaymentStatus status);
    boolean existsByTxid(String txid);
}
