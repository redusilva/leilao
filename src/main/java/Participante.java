import java.util.Date;

public class Participante {
    private String nome;
    private Date nascimento;
    private String email;

    public Participante (String nome, String email, Date nascimento){
        this.nome = nome;
        this.email = email;
        this.nascimento = nascimento;
    }

    public String enviarEmail(String mensagem) {
        return mensagem;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getNascimento() {
        return nascimento;
    }

    public void setNascimento(Date nascimento) {
        this.nascimento = nascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
