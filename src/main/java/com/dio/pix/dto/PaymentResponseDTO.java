package com.dio.pix.dto;
import com.dio.pix.entity.PaymentStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de resposta de pagamento PIX")
public class PaymentResponseDTO {
    @Schema(description = "ID do pagamento")
    private Long id;
    @Schema(description = "Identificador unico da transacao")
    private String txid;
    @Schema(description = "Valor do pagamento")
    private BigDecimal amount;
    @Schema(description = "Descricao do pagamento")
    private String description;
    @Schema(description = "Status do pagamento")
    private PaymentStatus status;
    @Schema(description = "Payload PIX no padrao EMV")
    private String payload;
    @Schema(description = "Chave PIX do recebedor")
    private String pixKey;
    @Schema(description = "Nome do recebedor")
    private String receiverName;
    @Schema(description = "Cidade do recebedor")
    private String receiverCity;
    @Schema(description = "Data de criacao")
    private LocalDateTime createdAt;
    @Schema(description = "Data de atualizacao")
    private LocalDateTime updatedAt;
    @Schema(description = "Data de aprovacao")
    private LocalDateTime approvedAt;
}
