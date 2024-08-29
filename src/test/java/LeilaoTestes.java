import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;


public class LeilaoTestes {
    CustomDateTime customDate = new CustomDateTime();

    @Test
    public void testFiltrarPorEstado() throws Exception {
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

    @Test
    public void testLeilaoInativoAoCadastrar() {
        ListaLeiloes lista = new ListaLeiloes();

        // Gera uma lista de leilões
        lista.adicionarLeilao(new Leilao("Leilão 1", 500.0, this.customDate.getCurrentDate()));
        lista.adicionarLeilao(new Leilao("Leilão 2", 750.0, this.customDate.getCurrentDate()));
        lista.adicionarLeilao(new Leilao("Leilão 3", 1000.0, this.customDate.getCurrentDate()));
        lista.adicionarLeilao(new Leilao("Leilão 4", 1500.0, this.customDate.getCurrentDate()));

        // Obtém a lista de leilões cadastrados
        List<Leilao> leiloes = lista.getLeiloes();

        // Verifica se todos os leilões gerados estão inativos
        for (Leilao leilao : leiloes) {
            assertTrue(leilao.leilaoEstaInativo());
        }
    }

    @Test
    public void testSetLanceAtualQuandoLeilaoNaoEstaAberto() {
        // Criando um leilão com estado INATIVO
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getCurrentDate());
        Participante participante = new Participante("Rodrigo", "rodrigo@gmail.com", this.customDate.parseDate("2002-10-02"));

        try {
            Lance novoLance = new Lance(participante, 200.0);
            leilao.setLanceAtual(novoLance);
            fail("Deveria ter lançado uma exceção.");
        } catch (Exception e) {
            assertEquals("O leilão não está aberto para receber lances!", e.getMessage());
        }
    }

    @Test
    public void testSetLanceAtualQuandoLeilaoEstaAberto() {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getCurrentDate());
        leilao.abrirLeilao();

        Participante participante = new Participante("Rodrigo", "rodrigo@gmail.com", this.customDate.parseDate("2002-10-02"));
        leilao.cadastraParticipante(participante);

        try {
            Lance novoLance = new Lance(participante, 200.0);
            leilao.setLanceAtual(novoLance);
            assertEquals(200.0, leilao.getLanceAtual().getValor());
        } catch (Exception e) {
            fail("Não deveria ter lançado uma exceção: " + e.getMessage());
        }
    }

    @Test
    public void testExpirarLeilao() throws Exception {

        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.parseDate("1998-08-19"));
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
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getTomorrowDate());
        leilao.abrirLeilao();

        // Verificando se o leilão está inicialmente ABERTO
        assertEquals(EstadoLeilao.ABERTO, leilao.getEstado());

        try{
            leilao.expirarLeilao();
        } catch (Exception e){
            // Verificando se o estado do leilão ainda é ABERTO
            assertEquals(EstadoLeilao.ABERTO, leilao.getEstado());
            assertEquals("Não é possível expirar um leilão com uma data de expiração no futuro!", e.getMessage());
        }
    }

    @Test
    public void testFinalizaLeilaoExpirado() throws Exception {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.parseDate("1991-12-13"));
        leilao.expirarLeilao();
        leilao.finalizarLeilao();
        assertEquals(leilao.getEstado(), EstadoLeilao.FINALIZADO);
    }

    @Test
    public void testTentaFinalizarLeilaoAberto() throws Exception {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getTomorrowDate());
        leilao.abrirLeilao();
        leilao.finalizarLeilao();
        assertEquals(leilao.getEstado(), EstadoLeilao.FINALIZADO);
    }

    @Test
    public void testTentaFinalizarLeilaoInativo() throws Exception {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, new CustomDateTime().getTomorrowDate());

        try {
            leilao.finalizarLeilao();
            fail("Deveria ser lançado um exceção pois leilões não podem ser finalizados a partir do estado INATIVO!");
        } catch (Exception e){
            assertEquals(e.getMessage(), "O leilão deve estar ativo ou expirado para ser finalizado!");
        }
    }

    @Test
    public void testTentaFinalizarLeilaoJaFinalizado() throws Exception {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getTomorrowDate());
        leilao.abrirLeilao();
        leilao.finalizarLeilao();
        assertEquals(leilao.getEstado(), EstadoLeilao.FINALIZADO);

        leilao.finalizarLeilao();
        assertEquals(leilao.getEstado(), EstadoLeilao.FINALIZADO);
    }

    @Test
    public void testNaoDeveAceitarNovosLancesMenoresQueOLanceMinimo() {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getCurrentDate());
        leilao.abrirLeilao();

        Participante participante = new Participante("Rodrigo", "rodrigo@gmail.com", this.customDate.parseDate("2002-10-02"));
        leilao.cadastraParticipante(participante);

        try {
            Lance novoLance = new Lance(participante, 50);
            leilao.setLanceAtual(novoLance);
            fail("Não deveria ter aceito um lance menor que o lance mínimo!");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "O novo lance deve ser maior ou igual ao lance mínimo!");
        }
    }

    @Test
    public void testNaoDeveAceitarNovosLancesComValorIgualAoAnterior() throws Exception {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getCurrentDate());
        leilao.abrirLeilao();

        Participante participante = new Participante("Rodrigo", "rodrigo@gmail.com", this.customDate.parseDate("2002-10-02"));
        leilao.cadastraParticipante(participante);
        leilao.setLanceAtual(new Lance(participante, 150));

        try {
            leilao.setLanceAtual(new Lance(participante, 150));
            fail("Não deveria ter aceito um novo lance com valor igual ao anterior!");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "O novo lance deve ser maior que o lance atual!");
        }
    }

    @Test
    public void testNaoDeveAceitarNovosLancesComValorMenorAoAnterior() throws Exception {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getCurrentDate());
        leilao.abrirLeilao();

        Participante participante = new Participante("Rodrigo", "rodrigo@gmail.com", this.customDate.parseDate("2002-10-02"));
        leilao.cadastraParticipante(participante);
        leilao.setLanceAtual(new Lance(participante, 150.0));

        try {
            leilao.setLanceAtual(new Lance(participante, 120.0));
            fail("Não deveria ter aceito um novo lance com valor menor ao anterior!");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "O novo lance deve ser maior que o lance atual!");
        }
    }

    @Test
    public void testNaoDeveAceitarDoisLancesSeguidosDoMesmoParticipante() {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getCurrentDate());
        leilao.abrirLeilao();

        Participante participante = new Participante("Rodrigo", "rodrigo@gmail.com", this.customDate.parseDate("2002-10-02"));
        leilao.cadastraParticipante(participante);

        try {
            leilao.setLanceAtual(new Lance(participante, 120.0));
            leilao.setLanceAtual(new Lance(participante, 180.0));
            fail("Não deveria ter aceito dois lances seguidos do mesmo participante!");
        } catch (Exception e) {
            assertEquals(e.getMessage(), "O mesmo participante não pode realizar 2 lances seguidos!");
        }
    }

    @Test
    public void testDeveAceitarDoisLancesDoMesmoParticipanteDesdeQueNaoSejaEmSequencia() {
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getCurrentDate());
        leilao.abrirLeilao();

        Participante participante1 = new Participante("Rodrigo", "rodrigo@gmail.com", this.customDate.parseDate("2002-10-02"));
        Participante participante2 = new Participante("Rodrigo", "rodrigo@gmail.com", this.customDate.parseDate("2002-10-02"));
        leilao.cadastraParticipante(participante1);
        leilao.cadastraParticipante(participante2);

        try {
            leilao.setLanceAtual(new Lance(participante1, 120.0));
            leilao.setLanceAtual(new Lance(participante2, 180.0));
            leilao.setLanceAtual(new Lance(participante1, 200.0));
            leilao.setLanceAtual(new Lance(participante2, 250.0));
        } catch (Exception e) {
            fail("Os participantes deveriam realizar quantos lances quiserem desde que não seja em sequência!");
        }
    }

    @Test
    public void testVencedorLeilaoDeveReceberEmailParabenizandoEle() throws Exception {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);

        // Criar o mock do Participante
        Participante participante = mock(Participante.class);

        when(participante.getNome()).thenReturn("Rodrigo");
        String mensagemEsperadaMock = "Parabéns " +  participante.getNome() + " por ganhar o leilão!";

        // Configurar o mock para retornar o que você espera
        when(participante.enviarEmail("Parabéns por vencer o leilão!")).thenReturn(mensagemEsperadaMock);

        // Criar o leilao e abrir
        Leilao leilao = new Leilao("Leilão Teste", 100.0, this.customDate.getTomorrowDate()); // Data para amanhã
        leilao.abrirLeilao();
        leilao.cadastraParticipante(participante);

        // Configura o lance e finaliza o leilão
        leilao.setLanceAtual(new Lance(participante, 120));
        leilao.finalizarLeilao();

        // Verifica se o leilão foi finalizado
        assertEquals(EstadoLeilao.FINALIZADO, leilao.getEstado());
        assertEquals(leilao.getUltimoParticipante(), participante);

        // Chama o método que avisa o vencedor e captura a mensagem
        String enviaMensagem = leilao.avisaVencedorTorneio();

        // Verifica se o mock de enviarEmail foi chamado com a mensagem correta e retorna "testeee"
        assertEquals(mensagemEsperadaMock, enviaMensagem);
    }

    @Test
    public void testDeveCadastrarListaDeUsuarios(){
        Leilao leilao = new Leilao("teste", 100, this.customDate.getTomorrowDate());
        Participante participante1 = new Participante("nome", "email", this.customDate.parseDate("2001-10-02"));
        leilao.cadastraParticipante(participante1);
    }

    @Test
    public void testDeveBuscarTodosOsLancesDoLeilao(){
        List<Lance> lancesSimulados = new ArrayList<Lance>();
        Participante participante1 = new Participante("participante1", "participante1@gmail.com", this.customDate.getCurrentDate());
        Participante participante2 = new Participante("participante2", "participante2@gmail.com", this.customDate.getCurrentDate());
        Participante participante3 = new Participante("participante3", "participante3@gmail.com", this.customDate.getCurrentDate());
        Participante participante4 = new Participante("participante4", "participante4@gmail.com", this.customDate.getCurrentDate());
        Participante participante5 = new Participante("participante5", "participante5@gmail.com", this.customDate.getCurrentDate());

        lancesSimulados.add(new Lance(participante1, 100));
        lancesSimulados.add(new Lance(participante2, 200));
        lancesSimulados.add(new Lance(participante3, 300));
        lancesSimulados.add(new Lance(participante4, 400));
        lancesSimulados.add(new Lance(participante5, 500));

        Leilao leilao = mock(Leilao.class);
        leilao.abrirLeilao();
        when(leilao.buscaLancesLeilao()).thenReturn(lancesSimulados);
        List<Lance> lancesLeilao = leilao.buscaLancesLeilao();

        assertEquals(lancesLeilao, lancesSimulados);
    }
    @Test
    public void testDeveBuscarMaiorLanceDoLeilao(){
        List<Lance> lancesSimulados = new ArrayList<Lance>();
        Participante participante1 = new Participante("participante1", "participante1@gmail.com", this.customDate.getCurrentDate());
        Participante participante2 = new Participante("participante2", "participante2@gmail.com", this.customDate.getCurrentDate());
        Participante participante3 = new Participante("participante3", "participante3@gmail.com", this.customDate.getCurrentDate());
        Participante participante4 = new Participante("participante4", "participante4@gmail.com", this.customDate.getCurrentDate());
        Participante participante5 = new Participante("participante5", "participante5@gmail.com", this.customDate.getCurrentDate());

        lancesSimulados.add(new Lance(participante1, 100));
        lancesSimulados.add(new Lance(participante2, 200));
        lancesSimulados.add(new Lance(participante3, 300));
        lancesSimulados.add(new Lance(participante4, 400));
        lancesSimulados.add(new Lance(participante5, 500));

        Leilao leilao = mock(Leilao.class);
        leilao.abrirLeilao();
        when(leilao.buscaMaiorLance()).thenReturn(lancesSimulados.getLast());
        Lance maiorLance = leilao.buscaMaiorLance();

        assertEquals(maiorLance, lancesSimulados.getLast());
    }

    @Test
    public void testDeveBuscarMenorLanceDoLeilao(){
        List<Lance> lancesSimulados = new ArrayList<Lance>();
        Participante participante1 = new Participante("participante1", "participante1@gmail.com", this.customDate.getCurrentDate());
        Participante participante2 = new Participante("participante2", "participante2@gmail.com", this.customDate.getCurrentDate());
        Participante participante3 = new Participante("participante3", "participante3@gmail.com", this.customDate.getCurrentDate());
        Participante participante4 = new Participante("participante4", "participante4@gmail.com", this.customDate.getCurrentDate());
        Participante participante5 = new Participante("participante5", "participante5@gmail.com", this.customDate.getCurrentDate());

        lancesSimulados.add(new Lance(participante1, 100));
        lancesSimulados.add(new Lance(participante2, 200));
        lancesSimulados.add(new Lance(participante3, 300));
        lancesSimulados.add(new Lance(participante4, 400));
        lancesSimulados.add(new Lance(participante5, 500));

        Leilao leilao = mock(Leilao.class);
        leilao.abrirLeilao();
        when(leilao.buscaMenorLance()).thenReturn(lancesSimulados.getFirst());
        Lance maiorLance = leilao.buscaMenorLance();

        assertEquals(maiorLance, lancesSimulados.getFirst());
    }
}
