package com.dio.pix.dto;
import com.dio.pix.entity.PixKeyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO para criacao/atualizacao de recebedor PIX")
public class ReceiverDTO {
    @NotBlank(message = "A chave PIX e obrigatoria")
    @Size(max = 77, message = "A chave PIX deve ter no maximo 77 caracteres")
    @Schema(description = "Chave PIX do recebedor", example = "email@exemplo.com")
    private String pixKey;
    @NotNull(message = "O tipo de chave PIX e obrigatorio")
    @Schema(description = "Tipo da chave PIX")
    private PixKeyType pixKeyType;
    @NotBlank(message = "O nome e obrigatorio")
    @Size(max = 25, message = "O nome deve ter no maximo 25 caracteres")
    @Schema(description = "Nome do recebedor", example = "Venus Store")
    private String name;
    @NotBlank(message = "A cidade e obrigatoria")
    @Size(max = 15, message = "A cidade deve ter no maximo 15 caracteres")
    @Schema(description = "Cidade do recebedor", example = "SAO PAULO")
    private String city;
    @Size(max = 4, message = "O codigo de categoria deve ter no maximo 4 caracteres")
    @Pattern(regexp = "^[0-9]{4}$", message = "O codigo de categoria deve ter 4 digitos")
    @Schema(description = "Codigo de categoria do comerciante (MCC)", example = "0000")
    private String merchantCategoryCode;
    @Schema(description = "Define como recebedor padrao", example = "true")
    private Boolean isDefault;
}
