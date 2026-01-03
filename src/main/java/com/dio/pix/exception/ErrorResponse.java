package com.dio.pix.exception;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Resposta de erro da API")
public class ErrorResponse {
    @Schema(description = "Timestamp do erro")
    private LocalDateTime timestamp;
    @Schema(description = "Codigo de status HTTP")
    private int status;
    @Schema(description = "Mensagem de erro")
    private String error;
    @Schema(description = "Mensagem detalhada")
    private String message;
    @Schema(description = "Caminho da requisicao")
    private String path;
    @Schema(description = "Lista de erros de validacao")
    private List<FieldError> fieldErrors;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class FieldError {
        @Schema(description = "Nome do campo")
        private String field;
        @Schema(description = "Mensagem de erro do campo")
        private String message;
    }
}
