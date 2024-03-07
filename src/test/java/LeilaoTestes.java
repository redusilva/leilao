import org.junit.jupiter.api.Test;

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
        lista.adicionarLeilao(new Leilao("Leilão 1", 500.0, new Date()));
        lista.adicionarLeilao(new Leilao("Leilão 2", 750.0, new Date()));
        lista.adicionarLeilao(new Leilao("Leilão 3", 1000.0, new Date()));
        lista.adicionarLeilao(new Leilao("Leilão 4", 1500.0, new Date()));

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
        Leilao leilao = new Leilao("Leilão Teste", 100.0, new Date());

        try {
            // Tentando definir um lance atual no leilão INATIVO
            leilao.setLanceAtual(200.0);
            fail("Deveria ter lançado uma exceção.");
        } catch (Exception e) {
            // Verificando se uma exceção foi lançada
            assertEquals("O leilão não está aberto para receber lances.", e.getMessage());
        }
    }

    @Test
    public void testSetLanceAtualQuandoLeilaoEstaAberto() {
        // Criando um leilão com estado ABERTO
        Leilao leilao = new Leilao("Leilão Teste", 100.0, new Date());
        leilao.abrirLeilao(); // Abrindo o leilão

        try {
            // Definindo um lance atual no leilão ABERTO
            leilao.setLanceAtual(200.0);

            // Verificando se o lance atual foi definido corretamente
            assertEquals(200.0, leilao.getLanceAtual());
        } catch (Exception e) {
            // Se uma exceção for lançada, falha no teste
            fail("Não deveria ter lançado uma exceção: " + e.getMessage());
        }
    }

    // Testes do exercício 4
    @Test
    public void testExpirarLeilao() {
        // Definindo a data atual
        Calendar calendarAtual = Calendar.getInstance();
        Date dataAtual = calendarAtual.getTime();

        // Definindo uma data de expiração no passado
        Calendar calendarExpiracaoPassada = Calendar.getInstance();
        calendarExpiracaoPassada.add(Calendar.DAY_OF_MONTH, -1); // Uma dia atrás
        Date dataExpiracaoPassada = calendarExpiracaoPassada.getTime();

        // Criando um leilão com estado ABERTO e data de expiração no passado
        Leilao leilao = new Leilao("Leilão Teste", 100.0, dataExpiracaoPassada);
        leilao.abrirLeilao(); // Abrindo o leilão

        // Verificando se o leilão está inicialmente ABERTO
        assertEquals(EstadoLeilao.ABERTO, leilao.getEstado());

        // Avançando a data atual para a data de expiração
        calendarAtual.setTime(dataExpiracaoPassada);
        dataAtual = calendarAtual.getTime();

        // Atualizando o estado do leilão
        leilao.expirarLeilao();

        // Verificando se o estado do leilão foi atualizado corretamente para EXPIRADO
        assertEquals(EstadoLeilao.EXPIRADO, leilao.getEstado());
    }

    @Test
    public void testNaoExpirarLeilaoSeDataExpiracaoNaoAlcancada() {
        // Definindo a data atual
        Calendar calendarAtual = Calendar.getInstance();
        Date dataAtual = calendarAtual.getTime();

        // Definindo uma data de expiração no futuro
        Calendar calendarExpiracaoFutura = Calendar.getInstance();
        calendarExpiracaoFutura.add(Calendar.DAY_OF_MONTH, 1); // Um dia no futuro
        Date dataExpiracaoFutura = calendarExpiracaoFutura.getTime();

        // Criando um leilão com estado ABERTO e data de expiração no futuro
        Leilao leilao = new Leilao("Leilão Teste", 100.0, dataExpiracaoFutura);
        leilao.abrirLeilao(); // Abrindo o leilão

        // Verificando se o leilão está inicialmente ABERTO
        assertEquals(EstadoLeilao.ABERTO, leilao.getEstado());

        // Avançando a data atual para um dia antes da data de expiração
        calendarAtual.add(Calendar.DAY_OF_MONTH, 1);
        dataAtual = calendarAtual.getTime();

        // Atualizando o estado do leilão
        leilao.expirarLeilao();

        // Verificando se o estado do leilão ainda é ABERTO
        assertEquals(EstadoLeilao.ABERTO, leilao.getEstado());
    }

}
