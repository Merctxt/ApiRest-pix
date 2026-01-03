package com.dio.pix.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para criacao de pagamento PIX")
public class PaymentCreateDTO {
    @NotNull(message = "O valor e obrigatorio")
    @DecimalMin(value = "0.01", message = "O valor minimo e R$ 0,01")
    @DecimalMax(value = "99999999.99", message = "O valor maximo e R$ 99.999.999,99")
    @Schema(description = "Valor do pagamento", example = "100.00")
    private BigDecimal amount;
    @Size(max = 140, message = "A descricao deve ter no maximo 140 caracteres")
    @Schema(description = "Descricao do pagamento", example = "Pagamento de produto")
    private String description;
    @Size(max = 25, message = "O identificador de transacao deve ter no maximo 25 caracteres")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "O identificador deve conter apenas letras e numeros")
    @Schema(description = "Identificador unico da transacao (txid)", example = "PEDIDO123")
    private String txid;
    @Schema(description = "ID do recebedor (opcional, usa o padrao se nao informado)")
    private Long receiverId;
}
