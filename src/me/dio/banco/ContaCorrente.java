package me.dio.banco;

import java.math.BigDecimal;

public class ContaCorrente extends Conta {

    public static final BigDecimal LIMITE_PADRAO = Moeda.de("500.00");

    private BigDecimal limiteChequeEspecial;

    public ContaCorrente(Cliente cliente) {
        this(cliente, LIMITE_PADRAO);
    }

    public ContaCorrente(Cliente cliente, BigDecimal limiteChequeEspecial) {
        super(cliente);
        setLimiteChequeEspecial(limiteChequeEspecial);
    }

    @Override
    public String getTipo() {
        return "Conta Corrente";
    }

    @Override
    public BigDecimal getSaldoDisponivel() {
        return Moeda.normalizar(saldo.add(limiteChequeEspecial));
    }

    public BigDecimal getLimiteChequeEspecial() {
        return limiteChequeEspecial;
    }

    public void setLimiteChequeEspecial(BigDecimal limiteChequeEspecial) {
        if (limiteChequeEspecial == null || limiteChequeEspecial.signum() < 0) {
            throw new ValorInvalidoException("O limite do cheque especial não pode ser negativo.");
        }
        this.limiteChequeEspecial = Moeda.normalizar(limiteChequeEspecial);
    }

    @Override
    public void imprimirExtrato() {
        System.out.println("=== Extrato Conta Corrente ===");
        imprimirInfosComuns();
        System.out.printf("Limite:   %s%n", Moeda.formatar(limiteChequeEspecial));
        System.out.printf("Disponível: %s%n", Moeda.formatar(getSaldoDisponivel()));
        imprimirHistorico();
    }
}
