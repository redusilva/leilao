import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class LeilaoTestes {

    // Teste do exercício 1
    @Test
    public void testFiltrarPorEstado() {
        ListaLeiloes lista = new ListaLeiloes();

        // Gera uma lista aleatória de leilões
        lista.gerarListaAleatoria(20);

        // Verifica se todos os leilões têm o estado correto
        for (EstadoLeilao estado : EstadoLeilao.values()) {
            List<Leilao> leiloesFiltrados = lista.filtrarPorEstado(estado);
            for (Leilao leilao : leiloesFiltrados) {
                assertEquals(estado, leilao.getEstado());
            }
        }
    }

    // Teste do exercício 2
    @Test
    public void testLeilaoInativoAoCadastrar() {
        ListaLeiloes lista = new ListaLeiloes();

        // Gera uma lista de leilões
        lista.adicionarLeilao(new Leilao("Leilão 1", 500.0, new CustomDateTime().getCurrentDate()));
        lista.adicionarLeilao(new Leilao("Leilão 2", 750.0, new CustomDateTime().getCurrentDate()));
        lista.adicionarLeilao(new Leilao("Leilão 3", 1000.0, new CustomDateTime().getCurrentDate()));
        lista.adicionarLeilao(new Leilao("Leilão 4", 1500.0, new CustomDateTime().getCurrentDate()));

        // Obtém a lista de leilões cadastrados
        List<Leilao> leiloes = lista.getLeiloes();

        // Verifica se todos os leilões gerados estão inativos
        for (Leilao leilao : leiloes) {
            assertTrue(leilao.leilaoEstaInativo());
        }
    }

    // Testes do exercício 3
    @Test
    public void testSetLanceAtualQuandoLeilaoNaoEstaAberto() {
        // Criando um leilão com estado INATIVO
        Leilao leilao = new Leilao("Leilão Teste", 100.0, new CustomDateTime().getCurrentDate());

        try {
            leilao.setLanceAtual(200.0);
            fail("Deveria ter lançado uma exceção.");
        } catch (Exception e) {
            assertEquals("O leilão não está aberto para receber lances.", e.getMessage());
        }
    }

    @Test
    public void testSetLanceAtualQuandoLeilaoEstaAberto() {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, new CustomDateTime().getCurrentDate());
        leilao.abrirLeilao();

        try {
            leilao.setLanceAtual(200.0);
            assertEquals(200.0, leilao.getLanceAtual());
        } catch (Exception e) {
            fail("Não deveria ter lançado uma exceção: " + e.getMessage());
        }
    }

    // Testes do exercício 4
    @Test
    public void testExpirarLeilao() {

        Leilao leilao = new Leilao("Leilão Teste", 100.0, new CustomDateTime().parseDate("1998-08-19"));
        leilao.abrirLeilao();

        assertEquals(EstadoLeilao.ABERTO, leilao.getEstado());

        // Atualizando o estado do leilão
        leilao.expirarLeilao();

        // Verificando se o estado do leilão foi atualizado corretamente para EXPIRADO
        assertEquals(EstadoLeilao.EXPIRADO, leilao.getEstado());
    }

    @Test
    public void testNaoExpirarLeilaoSeDataExpiracaoNaoAlcancada() {
        // Criando um leilão com estado ABERTO e data de expiração no futuro
        Leilao leilao = new Leilao("Leilão Teste", 100.0, new CustomDateTime().getTomorrowDate());
        leilao.abrirLeilao();

        // Verificando se o leilão está inicialmente ABERTO
        assertEquals(EstadoLeilao.ABERTO, leilao.getEstado());

        leilao.expirarLeilao();

        // Verificando se o estado do leilão ainda é ABERTO
        assertEquals(EstadoLeilao.ABERTO, leilao.getEstado());
    }

    // Testes do exercício 5
    @Test
    public void testFinalizaLeilaoExpirado() throws ParseException {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, new CustomDateTime().parseDate("1991-12-13"));
        leilao.expirarLeilao();
        leilao.finalizarLeilao();
        assertEquals(leilao.getEstado(), EstadoLeilao.FINALIZADO);
    }

}
