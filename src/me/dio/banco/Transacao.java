package me.dio.banco;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Transacao(TipoTransacao tipo, BigDecimal valor, BigDecimal saldoResultante, LocalDateTime momento, String descricao) {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

    @Override
    public String toString() {
        return String.format("%s  %-22s %s%-14s saldo: %s  %s",
                momento.format(FORMATO_DATA),
                tipo.getRotulo(),
                tipo.getSinal(),
                Moeda.formatar(valor),
                Moeda.formatar(saldoResultante),
                descricao);
    }
}
