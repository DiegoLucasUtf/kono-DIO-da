package me.dio.banco;

public enum TipoTransacao {

    DEPOSITO("Depósito", true),
    SAQUE("Saque", false),
    TRANSFERENCIA_ENVIADA("Transferência enviada", false),
    TRANSFERENCIA_RECEBIDA("Transferência recebida", true),
    RENDIMENTO("Rendimento", true);

    private final String rotulo;
    private final boolean credito;

    TipoTransacao(String rotulo, boolean credito) {
        this.rotulo = rotulo;
        this.credito = credito;
    }

    public String getRotulo() {
        return rotulo;
    }

    public boolean isCredito() {
        return credito;
    }

    public String getSinal() {
        return credito ? "+" : "-";
    }
}
