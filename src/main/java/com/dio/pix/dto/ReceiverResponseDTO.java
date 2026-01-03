package com.dio.pix.dto;
import com.dio.pix.entity.PixKeyType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO de resposta de recebedor PIX")
public class ReceiverResponseDTO {
    @Schema(description = "ID do recebedor")
    private Long id;
    @Schema(description = "Chave PIX do recebedor")
    private String pixKey;
    @Schema(description = "Tipo da chave PIX")
    private PixKeyType pixKeyType;
    @Schema(description = "Nome do recebedor")
    private String name;
    @Schema(description = "Cidade do recebedor")
    private String city;
    @Schema(description = "Codigo de categoria do comerciante")
    private String merchantCategoryCode;
    @Schema(description = "Indica se e o recebedor padrao")
    private Boolean isDefault;
    @Schema(description = "Data de criacao")
    private LocalDateTime createdAt;
    @Schema(description = "Data de atualizacao")
    private LocalDateTime updatedAt;
}
