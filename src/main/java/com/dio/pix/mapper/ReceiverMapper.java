package com.dio.pix.mapper;
import com.dio.pix.dto.ReceiverDTO;
import com.dio.pix.dto.ReceiverResponseDTO;
import com.dio.pix.entity.Receiver;
import org.springframework.stereotype.Component;
@Component
public class ReceiverMapper {
    public ReceiverResponseDTO toResponseDTO(Receiver receiver) {
        return ReceiverResponseDTO.builder()
                .id(receiver.getId())
                .pixKey(receiver.getPixKey())
                .pixKeyType(receiver.getPixKeyType())
                .name(receiver.getName())
                .city(receiver.getCity())
                .merchantCategoryCode(receiver.getMerchantCategoryCode())
                .isDefault(receiver.getIsDefault())
                .createdAt(receiver.getCreatedAt())
                .updatedAt(receiver.getUpdatedAt())
                .build();
    }
    public Receiver toEntity(ReceiverDTO dto) {
        return Receiver.builder()
                .pixKey(dto.getPixKey())
                .pixKeyType(dto.getPixKeyType())
                .name(dto.getName())
                .city(dto.getCity())
                .merchantCategoryCode(dto.getMerchantCategoryCode())
                .isDefault(dto.getIsDefault())
                .build();
    }
    public void updateEntity(Receiver receiver, ReceiverDTO dto) {
        receiver.setPixKey(dto.getPixKey());
        receiver.setPixKeyType(dto.getPixKeyType());
        receiver.setName(dto.getName());
        receiver.setCity(dto.getCity());
        if (dto.getMerchantCategoryCode() != null) {
            receiver.setMerchantCategoryCode(dto.getMerchantCategoryCode());
        }
        if (dto.getIsDefault() != null) {
            receiver.setIsDefault(dto.getIsDefault());
        }
    }
}
