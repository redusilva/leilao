import java.util.Date;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        ListaLeiloes lista = new ListaLeiloes();
        lista.gerarListaAleatoria(20);

        // Filtra os leilões pelo estado desejado
        List<Leilao> leiloesFiltrados = lista.filtrarPorEstado(EstadoLeilao.ABERTO);

        // Itera sobre os leilões filtrados e imprime suas informações
        System.out.println("Leilões com estado ABERTO:");
        for (Leilao leilao : leiloesFiltrados) {
            System.out.println("\n\n\n");
            System.out.println("Descrição: " + leilao.getDescricao());
            System.out.println("Lance Mínimo: " + leilao.getLanceMinimo());
            System.out.println("Data de Expiração: " + leilao.getDataExpiracao());
            System.out.println("Estado: " + leilao.getEstado());
            System.out.println();
        }

    }
}
