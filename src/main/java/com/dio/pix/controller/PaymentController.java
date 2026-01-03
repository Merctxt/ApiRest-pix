package com.dio.pix.controller;
import com.dio.pix.dto.PaymentCreateDTO;
import com.dio.pix.dto.PaymentResponseDTO;
import com.dio.pix.dto.PaymentUpdateDTO;
import com.dio.pix.entity.PaymentStatus;
import com.dio.pix.exception.ErrorResponse;
import com.dio.pix.service.PaymentService;
import com.dio.pix.service.QrCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Tag(name = "Pagamentos PIX", description = "Endpoints para gerenciamento de pagamentos PIX")
public class PaymentController {
    private final PaymentService paymentService;
    private final QrCodeService qrCodeService;
    @PostMapping
    @Operation(summary = "Criar pagamento PIX")
    public ResponseEntity<PaymentResponseDTO> createPayment(@Valid @RequestBody PaymentCreateDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(dto));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Buscar pagamento por ID")
    public ResponseEntity<PaymentResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }
    @GetMapping("/txid/{txid}")
    @Operation(summary = "Buscar pagamento por TXID")
    public ResponseEntity<PaymentResponseDTO> findByTxid(@PathVariable String txid) {
        return ResponseEntity.ok(paymentService.findByTxid(txid));
    }
    @GetMapping
    @Operation(summary = "Listar pagamentos")
    public ResponseEntity<List<PaymentResponseDTO>> findAll() {
        return ResponseEntity.ok(paymentService.findAll());
    }
    @GetMapping("/status/{status}")
    @Operation(summary = "Listar pagamentos por status")
    public ResponseEntity<List<PaymentResponseDTO>> findByStatus(@PathVariable PaymentStatus status) {
        return ResponseEntity.ok(paymentService.findByStatus(status));
    }
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar pagamento")
    public ResponseEntity<PaymentResponseDTO> updatePayment(@PathVariable Long id, @Valid @RequestBody PaymentUpdateDTO dto) {
        return ResponseEntity.ok(paymentService.updatePayment(id, dto));
    }
    @PatchMapping("/{id}/approve")
    @Operation(summary = "Aprovar pagamento")
    public ResponseEntity<PaymentResponseDTO> approvePayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.approvePayment(id));
    }
    @PatchMapping("/{id}/cancel")
    @Operation(summary = "Cancelar pagamento")
    public ResponseEntity<PaymentResponseDTO> cancelPayment(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.cancelPayment(id));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir pagamento")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/payload")
    @Operation(summary = "Obter payload PIX")
    public ResponseEntity<String> getPayload(@PathVariable Long id) {
        return ResponseEntity.ok().contentType(MediaType.TEXT_PLAIN).body(paymentService.getPayload(id));
    }
    @GetMapping("/{id}/qrcode")
    @Operation(summary = "Gerar QR Code")
    public ResponseEntity<byte[]> getQrCode(@PathVariable Long id,
                                            @RequestParam(defaultValue = "300") int width,
                                            @RequestParam(defaultValue = "300") int height) {
        String payload = paymentService.getPayload(id);
        byte[] qrCodeImage = qrCodeService.gerarQrCode(payload, width, height);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        headers.setContentLength(qrCodeImage.length);
        return new ResponseEntity<>(qrCodeImage, headers, HttpStatus.OK);
    }
}
