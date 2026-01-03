package com.dio.pix.mapper;
import com.dio.pix.dto.PaymentResponseDTO;
import com.dio.pix.entity.Payment;
import org.springframework.stereotype.Component;
@Component
public class PaymentMapper {
    public PaymentResponseDTO toResponseDTO(Payment payment) {
        return PaymentResponseDTO.builder()
                .id(payment.getId())
                .txid(payment.getTxid())
                .amount(payment.getAmount())
                .description(payment.getDescription())
                .status(payment.getStatus())
                .payload(payment.getPayload())
                .pixKey(payment.getPixKey())
                .receiverName(payment.getReceiverName())
                .receiverCity(payment.getReceiverCity())
                .createdAt(payment.getCreatedAt())
                .updatedAt(payment.getUpdatedAt())
                .approvedAt(payment.getApprovedAt())
                .build();
    }
}
