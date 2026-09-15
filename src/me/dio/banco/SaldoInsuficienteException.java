package me.dio.banco;

import java.math.BigDecimal;

public class SaldoInsuficienteException extends RuntimeException {

    private final BigDecimal valorSolicitado;
    private final BigDecimal valorDisponivel;

    public SaldoInsuficienteException(String identificacaoConta, BigDecimal valorSolicitado, BigDecimal valorDisponivel) {
        super(String.format("%s não possui saldo suficiente: solicitado %s, disponível %s.",
                identificacaoConta, Moeda.formatar(valorSolicitado), Moeda.formatar(valorDisponivel)));
        this.valorSolicitado = valorSolicitado;
        this.valorDisponivel = valorDisponivel;
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public BigDecimal getValorDisponivel() {
        return valorDisponivel;
    }
}
