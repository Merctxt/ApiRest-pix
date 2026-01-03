package com.dio.pix.repository;
import com.dio.pix.entity.Receiver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface ReceiverRepository extends JpaRepository<Receiver, Long> {
    Optional<Receiver> findByPixKey(String pixKey);
    Optional<Receiver> findByIsDefaultTrue();
    boolean existsByPixKey(String pixKey);
}
