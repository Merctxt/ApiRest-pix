package com.dio.pix.service;
import com.dio.pix.dto.ReceiverDTO;
import com.dio.pix.dto.ReceiverResponseDTO;
import com.dio.pix.entity.Receiver;
import com.dio.pix.exception.BusinessException;
import com.dio.pix.exception.ResourceNotFoundException;
import com.dio.pix.mapper.ReceiverMapper;
import com.dio.pix.repository.ReceiverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
@Slf4j
public class ReceiverService {
    private final ReceiverRepository receiverRepository;
    private final ReceiverMapper receiverMapper;
    @Transactional
    public ReceiverResponseDTO createReceiver(ReceiverDTO dto) {
        log.info("Criando novo recebedor com chave PIX: {}", dto.getPixKey());
        if (receiverRepository.existsByPixKey(dto.getPixKey())) {
            throw new BusinessException("Ja existe um recebedor com a chave PIX: " + dto.getPixKey());
        }
        Receiver receiver = receiverMapper.toEntity(dto);
        if (Boolean.TRUE.equals(dto.getIsDefault())) {
            removeDefaultFromAll();
        }
        receiver = receiverRepository.save(receiver);
        log.info("Recebedor criado com sucesso. ID: {}", receiver.getId());
        return receiverMapper.toResponseDTO(receiver);
    }
    public ReceiverResponseDTO findById(Long id) {
        Receiver receiver = receiverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recebedor", id));
        return receiverMapper.toResponseDTO(receiver);
    }
    public ReceiverResponseDTO findDefault() {
        Receiver receiver = receiverRepository.findByIsDefaultTrue()
                .orElseThrow(() -> new ResourceNotFoundException("Recebedor padrao nao configurado"));
        return receiverMapper.toResponseDTO(receiver);
    }
    public List<ReceiverResponseDTO> findAll() {
        return receiverRepository.findAll().stream()
                .map(receiverMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    @Transactional
    public ReceiverResponseDTO updateReceiver(Long id, ReceiverDTO dto) {
        log.info("Atualizando recebedor ID: {}", id);
        Receiver receiver = receiverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recebedor", id));
        if (!receiver.getPixKey().equals(dto.getPixKey()) && receiverRepository.existsByPixKey(dto.getPixKey())) {
            throw new BusinessException("Ja existe um recebedor com a chave PIX: " + dto.getPixKey());
        }
        if (Boolean.TRUE.equals(dto.getIsDefault()) && !Boolean.TRUE.equals(receiver.getIsDefault())) {
            removeDefaultFromAll();
        }
        receiverMapper.updateEntity(receiver, dto);
        receiver = receiverRepository.save(receiver);
        log.info("Recebedor atualizado com sucesso. ID: {}", receiver.getId());
        return receiverMapper.toResponseDTO(receiver);
    }
    @Transactional
    public ReceiverResponseDTO setAsDefault(Long id) {
        log.info("Definindo recebedor ID: {} como padrao", id);
        Receiver receiver = receiverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recebedor", id));
        removeDefaultFromAll();
        receiver.setIsDefault(true);
        receiver = receiverRepository.save(receiver);
        log.info("Recebedor definido como padrao. ID: {}", receiver.getId());
        return receiverMapper.toResponseDTO(receiver);
    }
    @Transactional
    public void deleteReceiver(Long id) {
        log.info("Excluindo recebedor ID: {}", id);
        Receiver receiver = receiverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recebedor", id));
        receiverRepository.delete(receiver);
        log.info("Recebedor excluido com sucesso. ID: {}", id);
    }
    private void removeDefaultFromAll() {
        receiverRepository.findByIsDefaultTrue().ifPresent(receiver -> {
            receiver.setIsDefault(false);
            receiverRepository.save(receiver);
        });
    }
}
