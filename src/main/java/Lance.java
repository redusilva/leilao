public class Lance {
    private Participante participante;
    private double valor = 0;

    public Lance(Participante participante, double valor){
        this.participante = participante;
        this.valor = valor;
    }

    public Participante getParticipante() {
        return participante;
    }

    public double getValor() {
        return valor;
    }
}
