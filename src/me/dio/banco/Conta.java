package me.dio.banco;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Conta implements IConta {

    private static final int AGENCIA_PADRAO = 1;
    private static int sequencial = 1;

    protected final int agencia;
    protected final int numero;
    protected final Cliente cliente;
    protected BigDecimal saldo;

    private final List<Transacao> historico = new ArrayList<>();

    protected Conta(Cliente cliente) {
        if (cliente == null) {
            throw new IllegalArgumentException("Uma conta precisa de um cliente titular.");
        }
        this.agencia = AGENCIA_PADRAO;
        this.numero = sequencial++;
        this.cliente = cliente;
        this.saldo = Moeda.ZERO;
    }

    public abstract String getTipo();

    @Override
    public void sacar(BigDecimal valor) {
        debitar(Moeda.validarPositivo(valor), TipoTransacao.SAQUE, "Saque em espécie");
    }

    @Override
    public void depositar(BigDecimal valor) {
        creditar(Moeda.validarPositivo(valor), TipoTransacao.DEPOSITO, "Depósito em espécie");
    }

    @Override
    public void transferir(BigDecimal valor, IConta contaDestino) {
        if (contaDestino == null) {
            throw new IllegalArgumentException("A conta de destino não foi informada.");
        }
        if (contaDestino == this) {
            throw new IllegalArgumentException("Não é possível transferir para a própria conta.");
        }
        BigDecimal montante = Moeda.validarPositivo(valor);
        debitar(montante, TipoTransacao.TRANSFERENCIA_ENVIADA, "Para " + contaDestino.identificacao());
        contaDestino.receberTransferencia(montante, this);
    }

    @Override
    public void receberTransferencia(BigDecimal valor, IConta contaOrigem) {
        creditar(Moeda.validarPositivo(valor), TipoTransacao.TRANSFERENCIA_RECEBIDA, "De " + contaOrigem.identificacao());
    }

    @Override
    public String identificacao() {
        return String.format("%s ag. %d / conta %d", getTipo(), agencia, numero);
    }

    protected void debitar(BigDecimal valor, TipoTransacao tipo, String descricao) {
        BigDecimal disponivel = getSaldoDisponivel();
        if (valor.compareTo(disponivel) > 0) {
            throw new SaldoInsuficienteException(identificacao(), valor, disponivel);
        }
        saldo = Moeda.normalizar(saldo.subtract(valor));
        registrar(tipo, valor, descricao);
    }

    protected void creditar(BigDecimal valor, TipoTransacao tipo, String descricao) {
        saldo = Moeda.normalizar(saldo.add(valor));
        registrar(tipo, valor, descricao);
    }

    private void registrar(TipoTransacao tipo, BigDecimal valor, String descricao) {
        historico.add(new Transacao(tipo, valor, saldo, LocalDateTime.now(), descricao));
    }

    public BigDecimal getSaldoDisponivel() {
        return saldo;
    }

    public int getAgencia() {
        return agencia;
    }

    public int getNumero() {
        return numero;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public List<Transacao> getHistorico() {
        return Collections.unmodifiableList(historico);
    }

    protected void imprimirInfosComuns() {
        System.out.printf("Titular:  %s%n", cliente.getNome());
        System.out.printf("CPF:      %s%n", cliente.getCpfFormatado());
        System.out.printf("Agência:  %d%n", agencia);
        System.out.printf("Número:   %d%n", numero);
        System.out.printf("Saldo:    %s%n", Moeda.formatar(saldo));
    }

    protected void imprimirHistorico() {
        System.out.println("-- Histórico --");
        if (historico.isEmpty()) {
            System.out.println("Nenhuma movimentação registrada.");
            return;
        }
        historico.forEach(transacao -> System.out.println("  " + transacao));
    }

    @Override
    public String toString() {
        return String.format("%s | %s | saldo %s", identificacao(), cliente, Moeda.formatar(saldo));
    }
}
