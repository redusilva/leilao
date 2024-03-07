import java.util.Date;

class Leilao {
    private String descricao;
    private double lanceMinimo;
    private Date dataExpiracao;
    private EstadoLeilao estado;
    private  Double lanceAtual;

    public Leilao(String descricao, double lanceMinimo, Date dataExpiracao) {
        this.descricao = descricao;
        this.lanceMinimo = lanceMinimo;
        this.dataExpiracao = dataExpiracao;
        this.estado = EstadoLeilao.INATIVO;
        this.lanceAtual = 0.0;
    }

    public void abrirLeilao() {
        this.estado = EstadoLeilao.ABERTO;
    }

    public void finalizarLeilao() {
        this.estado = EstadoLeilao.FINALIZADO;
    }

    public boolean leilaoEstaInativo(){
        return this.estado == EstadoLeilao.INATIVO;
    }

    public boolean leilaoEstaExpirado(){
        return new Date().after(this.dataExpiracao);
    }

    public void expirarLeilao() {
        // Verifica se a data atual é posterior à data de expiração
        if (this.leilaoEstaExpirado()) {
            this.estado = EstadoLeilao.EXPIRADO;
        }
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

    public Double getLanceAtual() {
        return lanceAtual;
    }

    public void setLanceAtual(Double lanceAtual) throws Exception {
        if (this.estado != EstadoLeilao.ABERTO) {
            throw new Exception("O leilão não está aberto para receber lances.");
        }

        this.lanceAtual = lanceAtual;
    }


}
