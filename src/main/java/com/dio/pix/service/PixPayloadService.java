package com.dio.pix.service;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
@Service
public class PixPayloadService {
    public String gerarPayload(String chave, BigDecimal valor, String nomeRecebedor,
                               String cidadeRecebedor, String txid, String merchantCategoryCode) {
        String nomeSanitizado = sanitizarNome(nomeRecebedor);
        String cidadeSanitizada = sanitizarCidade(cidadeRecebedor);
        String txidSanitizado = sanitizarTxid(txid);
        String mcc = merchantCategoryCode != null ? merchantCategoryCode : "0000";
        StringBuilder payload = new StringBuilder();
        payload.append(formatarCampo("00", "01"));
        String merchantAccountInfo = formatarCampo("00", "BR.GOV.BCB.PIX") + formatarCampo("01", chave);
        payload.append(formatarCampo("26", merchantAccountInfo));
        payload.append(formatarCampo("52", mcc));
        payload.append(formatarCampo("53", "986"));
        payload.append(formatarCampo("54", formatarValor(valor)));
        payload.append(formatarCampo("58", "BR"));
        payload.append(formatarCampo("59", nomeSanitizado));
        payload.append(formatarCampo("60", cidadeSanitizada));
        String additionalData = formatarCampo("05", txidSanitizado);
        payload.append(formatarCampo("62", additionalData));
        String payloadComCrc = payload.toString() + "6304";
        String crc = calcularCRC16(payloadComCrc);
        return payloadComCrc + crc;
    }
    private String formatarCampo(String id, String valor) {
        return id + String.format("%02d", valor.length()) + valor;
    }
    private String formatarValor(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_UP).toString();
    }
    private String removerAcentos(String texto) {
        if (texto == null) return "";
        return Normalizer.normalize(texto, Normalizer.Form.NFD).replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
    }
    private String sanitizarNome(String nome) {
        if (nome == null) return "";
        String sanitizado = removerAcentos(nome).replaceAll("[^a-zA-Z0-9 ]", "").trim();
        return sanitizado.length() > 25 ? sanitizado.substring(0, 25) : sanitizado;
    }
    private String sanitizarCidade(String cidade) {
        if (cidade == null) return "";
        String sanitizado = removerAcentos(cidade).replaceAll("[^a-zA-Z0-9 ]", "").trim();
        return sanitizado.length() > 15 ? sanitizado.substring(0, 15) : sanitizado;
    }
    private String sanitizarTxid(String txid) {
        if (txid == null || txid.isEmpty()) return "***";
        String sanitizado = txid.replaceAll("[^a-zA-Z0-9]", "");
        return sanitizado.length() > 25 ? sanitizado.substring(0, 25) : sanitizado;
    }
    private String calcularCRC16(String payload) {
        int crc = 0xFFFF;
        int polynomial = 0x1021;
        byte[] bytes = payload.getBytes();
        for (byte b : bytes) {
            crc ^= (b & 0xFF) << 8;
            for (int i = 0; i < 8; i++) {
                if ((crc & 0x8000) != 0) {
                    crc = (crc << 1) ^ polynomial;
                } else {
                    crc = crc << 1;
                }
            }
        }
        crc &= 0xFFFF;
        return String.format("%04X", crc);
    }
}
