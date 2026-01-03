package com.dio.pix.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para atualizacao de pagamento PIX")
public class PaymentUpdateDTO {
    @DecimalMin(value = "0.01", message = "O valor minimo e R$ 0,01")
    @DecimalMax(value = "99999999.99", message = "O valor maximo e R$ 99.999.999,99")
    @Schema(description = "Valor do pagamento", example = "150.00")
    private BigDecimal amount;
    @Size(max = 140, message = "A descricao deve ter no maximo 140 caracteres")
    @Schema(description = "Descricao do pagamento", example = "Pagamento atualizado")
    private String description;
}
