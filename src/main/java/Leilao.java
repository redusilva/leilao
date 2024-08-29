import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class Leilao {
    private String descricao;
    private double lanceMinimo;
    private Date dataExpiracao;
    private EstadoLeilao estado;
    private Lance lanceAtual;
    private List<Participante> todosParticipantes;

    public Leilao(String descricao, double lanceMinimo, Date dataExpiracao) {
        this.descricao = descricao;
        this.lanceMinimo = lanceMinimo;
        this.dataExpiracao = dataExpiracao;
        this.estado = EstadoLeilao.INATIVO;
        this.lanceAtual = null;
        this.todosParticipantes = new ArrayList<Participante>();
    }

    public void abrirLeilao() {
        this.estado = EstadoLeilao.ABERTO;
    }

    public void finalizarLeilao() throws Exception {
        if(this.estado == EstadoLeilao.EXPIRADO || this.estado == EstadoLeilao.ABERTO){
            this.estado = EstadoLeilao.FINALIZADO;
        } else if(this.estado != EstadoLeilao.FINALIZADO) {
            throw new Exception("O leilão deve estar ativo ou expirado para ser finalizado!");
        }
    }

    public String avisaVencedorTorneio() throws Exception {

        if(this.lanceAtual.getParticipante() != null && this.estado == EstadoLeilao.FINALIZADO){
            return this.lanceAtual.getParticipante().enviarEmail("Parabéns por vencer o leilão!");
        }

        throw new Exception("Não é possível buscar o vencedor do leilão!");
    }

    public boolean leilaoEstaInativo(){
        return this.estado == EstadoLeilao.INATIVO;
    }

    public boolean leilaoEstaExpirado(){
        return new Date().after(this.dataExpiracao);
    }

    public void expirarLeilao() throws Exception {
        // Verifica se a data atual é posterior à data de expiração
        if (this.leilaoEstaExpirado()) {
            this.estado = EstadoLeilao.EXPIRADO;
        } else {
            throw new Exception("Não é possível expirar um leilão com uma data de expiração no futuro!");
        }
    }

    public void setLanceAtual(Lance novoLance) throws Exception {
        if (this.estado != EstadoLeilao.ABERTO) {
            throw new Exception("O leilão não está aberto para receber lances!");
        }

        if(!this.todosParticipantes.contains(novoLance.getParticipante())){
            throw new Exception("O participante deve ser cadastrado no leilão antes de poder fazer um lance!");
        }

        if(this.lanceMinimo > novoLance.getValor()){
            throw new Exception("O novo lance deve ser maior ou igual ao lance mínimo!");
        }

        if (this.lanceAtual != null) {
            if (novoLance.getValor() <= this.lanceAtual.getValor()) {
                throw new Exception("O novo lance deve ser maior que o lance atual!");
            }

            if (this.lanceAtual.getParticipante() == novoLance.getParticipante()) {
                throw new Exception("O mesmo participante não pode realizar 2 lances seguidos!");
            }
        }

        this.lanceAtual = novoLance;
    }

    public void cadastraParticipante(Participante participante) {
        if(!this.todosParticipantes.contains(participante)){
            this.todosParticipantes.add(participante);
        }
    }

    public List<Lance> buscaLancesLeilao(){
        return new ArrayList<Lance>();
    }

    public Lance buscaMaiorLance(){
        return null;
    }

    public Lance buscaMenorLance(){
        return  null;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getLanceMinimo() {
        return lanceMinimo;
    }

    public void setLanceMinimo(double lanceMinimo) {
        this.lanceMinimo = lanceMinimo;
    }

    public Date getDataExpiracao() {
        return dataExpiracao;
    }

    public void setDataExpiracao(Date dataExpiracao) {
        this.dataExpiracao = dataExpiracao;
    }

    public EstadoLeilao getEstado() {
        return estado;
    }

    public Lance getLanceAtual() {
        return lanceAtual;
    }

    public Participante getUltimoParticipante() {
        return lanceAtual.getParticipante();
    }
}
