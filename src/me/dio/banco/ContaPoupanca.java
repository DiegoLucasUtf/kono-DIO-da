package me.dio.banco;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ContaPoupanca extends Conta {

    public static final BigDecimal TAXA_MENSAL_PADRAO = Moeda.de("0.50");

    public ContaPoupanca(Cliente cliente) {
        super(cliente);
    }

    @Override
    public String getTipo() {
        return "Conta Poupança";
    }

    public BigDecimal aplicarRendimento(BigDecimal taxaPercentual) {
        if (taxaPercentual == null || taxaPercentual.signum() <= 0) {
            throw new ValorInvalidoException("A taxa de rendimento deve ser maior que zero.");
        }
        BigDecimal juros = Moeda.normalizar(saldo.multiply(taxaPercentual).divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_EVEN));
        if (juros.signum() <= 0) {
            return Moeda.ZERO;
        }
        creditar(juros, TipoTransacao.RENDIMENTO, "Taxa de " + taxaPercentual.stripTrailingZeros().toPlainString() + "%");
        return juros;
    }

    @Override
    public void imprimirExtrato() {
        System.out.println("=== Extrato Conta Poupança ===");
        imprimirInfosComuns();
        imprimirHistorico();
    }
}
