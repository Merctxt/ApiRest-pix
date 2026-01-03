package com.dio.pix.controller;
import com.dio.pix.dto.ReceiverDTO;
import com.dio.pix.dto.ReceiverResponseDTO;
import com.dio.pix.service.ReceiverService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/receivers")
@RequiredArgsConstructor
@Tag(name = "Recebedores PIX", description = "Endpoints para configuracao de recebedores PIX")
public class ReceiverController {
    private final ReceiverService receiverService;
    @PostMapping
    @Operation(summary = "Criar recebedor")
    public ResponseEntity<ReceiverResponseDTO> createReceiver(@Valid @RequestBody ReceiverDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(receiverService.createReceiver(dto));
    }
    @GetMapping("/{id}")
    @Operation(summary = "Buscar recebedor por ID")
    public ResponseEntity<ReceiverResponseDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(receiverService.findById(id));
    }
    @GetMapping("/default")
    @Operation(summary = "Buscar recebedor padrao")
    public ResponseEntity<ReceiverResponseDTO> findDefault() {
        return ResponseEntity.ok(receiverService.findDefault());
    }
    @GetMapping
    @Operation(summary = "Listar recebedores")
    public ResponseEntity<List<ReceiverResponseDTO>> findAll() {
        return ResponseEntity.ok(receiverService.findAll());
    }
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar recebedor")
    public ResponseEntity<ReceiverResponseDTO> updateReceiver(@PathVariable Long id, @Valid @RequestBody ReceiverDTO dto) {
        return ResponseEntity.ok(receiverService.updateReceiver(id, dto));
    }
    @PatchMapping("/{id}/set-default")
    @Operation(summary = "Definir como padrao")
    public ResponseEntity<ReceiverResponseDTO> setAsDefault(@PathVariable Long id) {
        return ResponseEntity.ok(receiverService.setAsDefault(id));
    }
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir recebedor")
    public ResponseEntity<Void> deleteReceiver(@PathVariable Long id) {
        receiverService.deleteReceiver(id);
        return ResponseEntity.noContent().build();
    }
}
