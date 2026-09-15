package me.dio.banco;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class Moeda {

    private static final Locale BRASIL = new Locale("pt", "BR");
    private static final String PADRAO = "#,##0.00";

    public static final BigDecimal ZERO = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_EVEN);

    private Moeda() {
    }

    public static BigDecimal normalizar(BigDecimal valor) {
        return valor.setScale(2, RoundingMode.HALF_EVEN);
    }

    public static BigDecimal de(String valor) {
        return normalizar(new BigDecimal(valor));
    }

    public static BigDecimal validarPositivo(BigDecimal valor) {
        if (valor == null) {
            throw new ValorInvalidoException("Valor não informado.");
        }
        BigDecimal normalizado = normalizar(valor);
        if (normalizado.compareTo(ZERO) <= 0) {
            throw new ValorInvalidoException("O valor deve ser maior que zero, mas foi informado " + formatar(normalizado) + ".");
        }
        return normalizado;
    }

    public static String formatar(BigDecimal valor) {
        return "R$ " + new DecimalFormat(PADRAO, DecimalFormatSymbols.getInstance(BRASIL)).format(valor);
    }
}
