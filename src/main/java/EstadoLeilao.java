import java.util.Random;

public enum EstadoLeilao {
    ABERTO,
    FINALIZADO,
    EXPIRADO,
    INATIVO;

    public static EstadoLeilao gerarEstadoAleatorio() {
        Random random = new Random();
        return values()[random.nextInt(values().length)];
    }
}
