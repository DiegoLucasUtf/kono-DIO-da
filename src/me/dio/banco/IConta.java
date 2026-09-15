package me.dio.banco;

import java.math.BigDecimal;

public interface IConta {

    void sacar(BigDecimal valor);

    void depositar(BigDecimal valor);

    void transferir(BigDecimal valor, IConta contaDestino);

    void receberTransferencia(BigDecimal valor, IConta contaOrigem);

    String identificacao();

    void imprimirExtrato();
}
