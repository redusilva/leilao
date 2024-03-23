import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class ListaLeiloes {
    private List<Leilao> leiloes;

    public ListaLeiloes() {
        this.leiloes = new ArrayList<>();
    }

    public void adicionarLeilao(Leilao leilao) {
        leiloes.add(leilao);
    }

    public void removerLeilao(Leilao leilao) {
        leiloes.remove(leilao);
    }

    public List<Leilao> getLeiloes() {
        return leiloes;
    }

    public void gerarListaAleatoria(int tamanho) {
        Random random = new Random();

        for (int i = 0; i < tamanho; i++) {
            String nome = "Leilão " + (i + 1);
            double lanceInicial = random.nextDouble() * 10000; // Valor aleatório até 10000
            Date data = new Date(System.currentTimeMillis() + random.nextInt(100000000)); // Data aleatória

            // Escolhendo aleatoriamente um estado
            EstadoLeilao estado = EstadoLeilao.gerarEstadoAleatorio();

            Leilao leilao = new Leilao(nome, lanceInicial, data);

            if (estado == EstadoLeilao.ABERTO) {
                leilao.abrirLeilao();
            } else if (estado == EstadoLeilao.FINALIZADO) {
                leilao.abrirLeilao();
                leilao.finalizarLeilao();
            } else if(estado == EstadoLeilao.EXPIRADO){
                leilao.expirarLeilao();
            }

            leiloes.add(leilao);
        }
    }

    public List<Leilao> filtrarPorEstado(EstadoLeilao estadoDesejado) {
        List<Leilao> leiloesFiltrados = new ArrayList<>();

        for (Leilao leilao : this.leiloes) {
            if (leilao.getEstado() == estadoDesejado) {
                leiloesFiltrados.add(leilao);
            }
        }

        return leiloesFiltrados;
    }

}
