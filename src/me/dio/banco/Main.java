package me.dio.banco;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {

    private static final Banco BANCO = new Banco("Kono DIO da");
    private static final Scanner ENTRADA = new Scanner(System.in);

    public static void main(String[] args) {
        if (args.length > 0 && "--demo".equals(args[0])) {
            executarDemonstracao();
            return;
        }
        System.out.printf("Bem-vindo ao %s!%n", BANCO.getNome());
        boolean executando = true;
        while (executando) {
            imprimirMenu();
            try {
                executando = processar(lerTexto("Opção").trim());
            } catch (RuntimeException erro) {
                System.out.println("[erro] " + erro.getMessage());
            }
        }
        System.out.println("Até logo!");
    }

    private static void imprimirMenu() {
        System.out.println();
        System.out.println("=============================");
        System.out.println(" 1 - Abrir conta corrente");
        System.out.println(" 2 - Abrir conta poupança");
        System.out.println(" 3 - Depositar");
        System.out.println(" 4 - Sacar");
        System.out.println(" 5 - Transferir");
        System.out.println(" 6 - Imprimir extrato");
        System.out.println(" 7 - Listar contas do banco");
        System.out.println(" 8 - Aplicar rendimento na poupança");
        System.out.println(" 9 - Carregar dados de demonstração");
        System.out.println(" 0 - Sair");
        System.out.println("=============================");
    }

    private static boolean processar(String opcao) {
        switch (opcao) {
            case "1" -> abrirConta(false);
            case "2" -> abrirConta(true);
            case "3" -> {
                Conta conta = lerConta("Conta para depósito");
                conta.depositar(lerValor("Valor do depósito"));
                System.out.printf("Depósito concluído. Novo saldo: %s%n", Moeda.formatar(conta.getSaldo()));
            }
            case "4" -> {
                Conta conta = lerConta("Conta para saque");
                conta.sacar(lerValor("Valor do saque"));
                System.out.printf("Saque concluído. Novo saldo: %s%n", Moeda.formatar(conta.getSaldo()));
            }
            case "5" -> {
                Conta origem = lerConta("Conta de origem");
                Conta destino = lerConta("Conta de destino");
                origem.transferir(lerValor("Valor da transferência"), destino);
                System.out.printf("Transferência concluída. Saldo da origem: %s%n", Moeda.formatar(origem.getSaldo()));
            }
            case "6" -> lerConta("Conta do extrato").imprimirExtrato();
            case "7" -> BANCO.imprimirContas();
            case "8" -> {
                Conta conta = lerConta("Conta poupança");
                if (!(conta instanceof ContaPoupanca poupanca)) {
                    throw new IllegalArgumentException("A conta " + conta.getNumero() + " não é uma conta poupança.");
                }
                BigDecimal juros = poupanca.aplicarRendimento(ContaPoupanca.TAXA_MENSAL_PADRAO);
                System.out.printf("Rendimento creditado: %s. Novo saldo: %s%n", Moeda.formatar(juros), Moeda.formatar(poupanca.getSaldo()));
            }
            case "9" -> carregarDemonstracao();
            case "0" -> {
                return false;
            }
            default -> System.out.println("Opção inválida.");
        }
        return true;
    }

    private static void abrirConta(boolean poupanca) {
        String cpf = lerTexto("CPF do titular");
        Cliente cliente = BANCO.buscarClientePorCpf(cpf).orElseGet(() -> new Cliente(lerTexto("Nome do titular"), cpf));
        Conta conta = poupanca ? new ContaPoupanca(cliente) : new ContaCorrente(cliente);
        BANCO.adicionarConta(conta);
        System.out.printf("Conta aberta: %s%n", conta);
    }

    private static Conta lerConta(String rotulo) {
        String numero = lerTexto(rotulo + " (número)");
        int identificador;
        try {
            identificador = Integer.parseInt(numero.trim());
        } catch (NumberFormatException erro) {
            throw new IllegalArgumentException("Número de conta inválido: " + numero);
        }
        Optional<Conta> conta = BANCO.buscarPorNumero(identificador);
        return conta.orElseThrow(() -> new IllegalArgumentException("Conta " + identificador + " não encontrada."));
    }

    private static BigDecimal lerValor(String rotulo) {
        String bruto = lerTexto(rotulo).trim().replace(',', '.');
        try {
            return Moeda.de(bruto);
        } catch (NumberFormatException erro) {
            throw new ValorInvalidoException("Valor monetário inválido: " + bruto);
        }
    }

    private static String lerTexto(String rotulo) {
        System.out.print(rotulo + ": ");
        if (!ENTRADA.hasNextLine()) {
            throw new IllegalStateException("Entrada encerrada.");
        }
        return ENTRADA.nextLine();
    }

    private static void carregarDemonstracao() {
        Cliente venilton = new Cliente("Venilton", "123.456.789-09");
        ContaCorrente corrente = new ContaCorrente(venilton);
        ContaPoupanca poupanca = new ContaPoupanca(venilton);
        BANCO.adicionarConta(corrente);
        BANCO.adicionarConta(poupanca);
        corrente.depositar(Moeda.de("1000.00"));
        corrente.transferir(Moeda.de("400.00"), poupanca);
        System.out.println("Dados de demonstração carregados.");
        BANCO.imprimirContas();
    }

    private static void executarDemonstracao() {
        Cliente venilton = new Cliente("Venilton", "123.456.789-09");
        Cliente maria = new Cliente("Maria", "987.654.321-00");

        ContaCorrente corrente = new ContaCorrente(venilton);
        ContaPoupanca poupanca = new ContaPoupanca(venilton);
        ContaCorrente contaMaria = new ContaCorrente(maria);
        List.of(corrente, poupanca, contaMaria).forEach(BANCO::adicionarConta);

        corrente.depositar(Moeda.de("1000.00"));
        corrente.transferir(Moeda.de("400.00"), poupanca);
        poupanca.aplicarRendimento(ContaPoupanca.TAXA_MENSAL_PADRAO);
        corrente.transferir(Moeda.de("250.50"), contaMaria);
        corrente.sacar(Moeda.de("800.00"));

        corrente.imprimirExtrato();
        System.out.println();
        poupanca.imprimirExtrato();
        System.out.println();
        contaMaria.imprimirExtrato();
        System.out.println();
        BANCO.imprimirContas();

        System.out.println();
        System.out.println("=== Regras de negócio ===");
        try {
            poupanca.sacar(Moeda.de("999999.00"));
        } catch (SaldoInsuficienteException erro) {
            System.out.println("[esperado] " + erro.getMessage());
        }
        try {
            corrente.depositar(Moeda.de("-10.00"));
        } catch (ValorInvalidoException erro) {
            System.out.println("[esperado] " + erro.getMessage());
        }
        try {
            corrente.transferir(Moeda.de("10.00"), corrente);
        } catch (IllegalArgumentException erro) {
            System.out.println("[esperado] " + erro.getMessage());
        }
    }
}
