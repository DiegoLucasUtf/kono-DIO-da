# Banco Digital com Java e Orientação a Objetos

Solução do desafio de projeto da [DIO](https://www.dio.me/) "Criando um Banco Digital com Java e Orientação a Objetos". O cenário proposto é o seguinte:

> "Um banco oferece aos seus clientes dois tipos de contas (corrente e poupança), as quais possuem as funcionalidades de depósito, saque e transferência (entre contas da própria instituição)."

Além de reproduzir o projeto de referência, esta versão traz evoluções próprias descritas mais abaixo.

## Como executar

Requer apenas um JDK 17 ou superior — não há dependências externas nem Maven/Gradle.

```powershell
# compila para a pasta out/
.\build.ps1

# roteiro de demonstração (não interativo)
.\run.ps1 --demo

# menu interativo
.\run.ps1
```

Se preferir chamar o compilador diretamente:

```powershell
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src).FullName
java -Dfile.encoding=UTF-8 -cp out me.dio.banco.Main --demo
```

## Os quatro pilares no código

| Pilar | Onde aparece |
| --- | --- |
| **Abstração** | `IConta` define o contrato de uma conta bancária sem dizer como ele é cumprido. `Conta` abstrai o que toda conta tem em comum (agência, número, saldo, titular, histórico). |
| **Encapsulamento** | O saldo só muda através de `debitar` e `creditar`, que validam a operação e registram a transação. Não há setter público de saldo, e `getHistorico()` devolve uma lista imutável. |
| **Herança** | `ContaCorrente` e `ContaPoupanca` estendem `Conta`, reaproveitando construtor, movimentações e impressão de dados comuns. |
| **Polimorfismo** | `transferir(BigDecimal, IConta)` opera sobre o contrato, não sobre o tipo concreto — uma corrente transfere para uma poupança sem saber disso. Cada subclasse dá sua própria versão de `imprimirExtrato()` e `getTipo()`. |

## Estrutura

```
src/me/dio/banco/
├── IConta.java                      contrato das operações bancárias
├── Conta.java                       classe abstrata com o comportamento comum
├── ContaCorrente.java               conta com cheque especial
├── ContaPoupanca.java               conta com rendimento
├── Cliente.java                     titular identificado por CPF
├── Banco.java                       agrega e consulta as contas
├── Transacao.java                   registro imutável de uma movimentação
├── TipoTransacao.java               depósito, saque, transferências, rendimento
├── Moeda.java                       normalização e formatação monetária
├── SaldoInsuficienteException.java
├── ValorInvalidoException.java
└── Main.java                        menu interativo e modo demonstração
```

## Evoluções em relação ao projeto de referência

- **`BigDecimal` no lugar de `double`.** Valores monetários usam escala fixa de duas casas e arredondamento `HALF_EVEN`, evitando os erros de ponto flutuante que aparecem ao somar centavos com `double`.
- **Regras de negócio validadas.** Saque e transferência falham com `SaldoInsuficienteException` quando excedem o disponível; valores nulos, zerados ou negativos disparam `ValorInvalidoException`; transferir para a própria conta é rejeitado.
- **Cheque especial na conta corrente.** O limite (R$ 500,00 por padrão) entra no saldo disponível para saque, então a conta pode ficar negativa até o teto configurado.
- **Rendimento na poupança.** `aplicarRendimento(taxa)` credita os juros como uma transação de tipo próprio.
- **Histórico de transações.** Cada conta acumula seus lançamentos com tipo, valor, saldo resultante, data/hora e descrição, e o extrato passa a mostrar essa lista. Transferências são registradas como envio e recebimento, identificando a conta do outro lado.
- **Cliente com CPF.** O CPF é normalizado para 11 dígitos, valida o tamanho e serve como identidade do cliente (`equals`/`hashCode`), permitindo que o mesmo titular tenha várias contas.
- **`Banco` com comportamento.** Deixou de ser apenas um par de getters e setters: cadastra contas, busca por número ou CPF, soma o saldo sob custódia e imprime a carteira.
- **`Main` interativo.** Menu de console para abrir contas, movimentar, transferir, aplicar rendimento e imprimir extratos, além do modo `--demo`, que executa um roteiro completo incluindo os casos de erro esperados.

## Projeto de referência

O projeto original do expert está em [digitalinnovationone/lab-banco-digital-oo](https://github.com/digitalinnovationone/lab-banco-digital-oo).
