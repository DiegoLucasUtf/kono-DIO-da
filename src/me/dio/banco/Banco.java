package me.dio.banco;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class Banco {

    private String nome;
    private final List<Conta> contas = new ArrayList<>();

    public Banco(String nome) {
        setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do banco não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public void adicionarConta(Conta conta) {
        if (conta == null) {
            throw new IllegalArgumentException("Conta não informada.");
        }
        contas.add(conta);
    }

    public List<Conta> getContas() {
        return Collections.unmodifiableList(contas);
    }

    public Optional<Conta> buscarPorNumero(int numero) {
        return contas.stream().filter(conta -> conta.getNumero() == numero).findFirst();
    }

    public List<Conta> buscarPorCpf(String cpf) {
        String apenasDigitos = cpf == null ? "" : cpf.replaceAll("\\D", "");
        return contas.stream().filter(conta -> conta.getCliente().getCpf().equals(apenasDigitos)).toList();
    }

    public Optional<Cliente> buscarClientePorCpf(String cpf) {
        return buscarPorCpf(cpf).stream().map(Conta::getCliente).findFirst();
    }

    public BigDecimal getSaldoTotal() {
        return contas.stream().map(Conta::getSaldo).reduce(Moeda.ZERO, BigDecimal::add);
    }

    public void imprimirContas() {
        System.out.printf("=== Contas do %s ===%n", nome);
        if (contas.isEmpty()) {
            System.out.println("Nenhuma conta cadastrada.");
            return;
        }
        contas.forEach(conta -> System.out.println("  " + conta));
        System.out.printf("Saldo total sob custódia: %s%n", Moeda.formatar(getSaldoTotal()));
    }
}
