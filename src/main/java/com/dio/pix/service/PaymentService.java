package com.dio.pix.service;
import com.dio.pix.dto.PaymentCreateDTO;
import com.dio.pix.dto.PaymentResponseDTO;
import com.dio.pix.dto.PaymentUpdateDTO;
import com.dio.pix.entity.Payment;
import com.dio.pix.entity.PaymentStatus;
import com.dio.pix.entity.Receiver;
import com.dio.pix.exception.BusinessException;
import com.dio.pix.exception.ResourceNotFoundException;
import com.dio.pix.mapper.PaymentMapper;
import com.dio.pix.repository.PaymentRepository;
import com.dio.pix.repository.ReceiverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final ReceiverRepository receiverRepository;
    private final PixPayloadService pixPayloadService;
    private final PaymentMapper paymentMapper;
    @Value("${pix.receiver.key}")
    private String defaultPixKey;
    @Value("${pix.receiver.name}")
    private String defaultReceiverName;
    @Value("${pix.receiver.city}")
    private String defaultReceiverCity;
    @Transactional
    public PaymentResponseDTO createPayment(PaymentCreateDTO dto) {
        log.info("Criando novo pagamento PIX com valor: {}", dto.getAmount());
        String txid = dto.getTxid();
        if (txid == null || txid.isEmpty()) {
            txid = generateTxid();
        }
        if (paymentRepository.existsByTxid(txid)) {
            throw new BusinessException("Já existe um pagamento com o txid: " + txid);
        }
        String pixKey;
        String receiverName;
        String receiverCity;
        String merchantCategoryCode = "0000";
        if (dto.getReceiverId() != null) {
            Receiver receiver = receiverRepository.findById(dto.getReceiverId())
                    .orElseThrow(() -> new ResourceNotFoundException("Recebedor", dto.getReceiverId()));
            pixKey = receiver.getPixKey();
            receiverName = receiver.getName();
            receiverCity = receiver.getCity();
            merchantCategoryCode = receiver.getMerchantCategoryCode();
        } else {
            Receiver defaultReceiver = receiverRepository.findByIsDefaultTrue().orElse(null);
            if (defaultReceiver != null) {
                pixKey = defaultReceiver.getPixKey();
                receiverName = defaultReceiver.getName();
                receiverCity = defaultReceiver.getCity();
                merchantCategoryCode = defaultReceiver.getMerchantCategoryCode();
            } else {
                pixKey = defaultPixKey;
                receiverName = defaultReceiverName;
                receiverCity = defaultReceiverCity;
            }
        }
        String payload = pixPayloadService.gerarPayload(pixKey, dto.getAmount(), receiverName, receiverCity, txid, merchantCategoryCode);
        Payment payment = Payment.builder()
                .txid(txid)
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .status(PaymentStatus.PENDING)
                .payload(payload)
                .pixKey(pixKey)
                .receiverName(receiverName)
                .receiverCity(receiverCity)
                .build();
        payment = paymentRepository.save(payment);
        log.info("Pagamento criado com sucesso. ID: {}, TXID: {}", payment.getId(), payment.getTxid());
        return paymentMapper.toResponseDTO(payment);
    }
    public PaymentResponseDTO findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", id));
        return paymentMapper.toResponseDTO(payment);
    }
    public PaymentResponseDTO findByTxid(String txid) {
        Payment payment = paymentRepository.findByTxid(txid)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", "txid", txid));
        return paymentMapper.toResponseDTO(payment);
    }
    public List<PaymentResponseDTO> findAll() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    public List<PaymentResponseDTO> findByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status).stream()
                .map(paymentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public PaymentResponseDTO updatePayment(Long id, PaymentUpdateDTO dto) {
        log.info("Atualizando pagamento ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", id));
        if (payment.getStatus() == PaymentStatus.APPROVED) {
            throw new BusinessException("Não é possível alterar um pagamento já aprovado");
        }
        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            throw new BusinessException("Não é possível alterar um pagamento cancelado");
        }
        boolean needsNewPayload = false;
        if (dto.getAmount() != null) {
            payment.setAmount(dto.getAmount());
            needsNewPayload = true;
        }
        if (dto.getDescription() != null) {
            payment.setDescription(dto.getDescription());
        }
        if (needsNewPayload) {
            String newPayload = pixPayloadService.gerarPayload(payment.getPixKey(), payment.getAmount(),
                    payment.getReceiverName(), payment.getReceiverCity(), payment.getTxid(), "0000");
            payment.setPayload(newPayload);
        }
        payment = paymentRepository.save(payment);
        log.info("Pagamento atualizado com sucesso. ID: {}", payment.getId());
        return paymentMapper.toResponseDTO(payment);
    }
    @Transactional
    public PaymentResponseDTO approvePayment(Long id) {
        log.info("Aprovando pagamento ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", id));
        if (payment.getStatus() == PaymentStatus.APPROVED) {
            throw new BusinessException("Pagamento já foi aprovado");
        }
        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            throw new BusinessException("Não é possível aprovar um pagamento cancelado");
        }
        payment.setStatus(PaymentStatus.APPROVED);
        payment.setApprovedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);
        log.info("Pagamento aprovado com sucesso. ID: {}", payment.getId());
        return paymentMapper.toResponseDTO(payment);
    }
    @Transactional
    public PaymentResponseDTO cancelPayment(Long id) {
        log.info("Cancelando pagamento ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", id));
        if (payment.getStatus() == PaymentStatus.APPROVED) {
            throw new BusinessException("Não é possível cancelar um pagamento já aprovado");
        }
        if (payment.getStatus() == PaymentStatus.CANCELLED) {
            throw new BusinessException("Pagamento já foi cancelado");
        }
        payment.setStatus(PaymentStatus.CANCELLED);
        payment = paymentRepository.save(payment);
        log.info("Pagamento cancelado com sucesso. ID: {}", payment.getId());
        return paymentMapper.toResponseDTO(payment);
    }
    @Transactional
    public void deletePayment(Long id) {
        log.info("Excluindo pagamento ID: {}", id);
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", id));
        if (payment.getStatus() == PaymentStatus.APPROVED) {
            throw new BusinessException("Não é possível excluir um pagamento já aprovado");
        }
        paymentRepository.delete(payment);
        log.info("Pagamento excluído com sucesso. ID: {}", id);
    }
    public String getPayload(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pagamento", id));
        return payment.getPayload();
    }
    private String generateTxid() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 25);
    }
}
