package me.dio.banco;

import java.util.Objects;

public class Cliente {

    private String nome;
    private final String cpf;

    public Cliente(String nome, String cpf) {
        this.cpf = normalizarCpf(cpf);
        setNome(nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do cliente não pode ser vazio.");
        }
        this.nome = nome.trim();
    }

    public String getCpf() {
        return cpf;
    }

    public String getCpfFormatado() {
        return String.format("%s.%s.%s-%s", cpf.substring(0, 3), cpf.substring(3, 6), cpf.substring(6, 9), cpf.substring(9));
    }

    private static String normalizarCpf(String cpf) {
        if (cpf == null) {
            throw new IllegalArgumentException("O CPF do cliente não pode ser nulo.");
        }
        String apenasDigitos = cpf.replaceAll("\\D", "");
        if (apenasDigitos.length() != 11) {
            throw new IllegalArgumentException("O CPF deve conter 11 dígitos, mas foram informados " + apenasDigitos.length() + ".");
        }
        return apenasDigitos;
    }

    @Override
    public boolean equals(Object outro) {
        if (this == outro) {
            return true;
        }
        return outro instanceof Cliente cliente && cpf.equals(cliente.cpf);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cpf);
    }

    @Override
    public String toString() {
        return nome + " (" + getCpfFormatado() + ")";
    }
}
