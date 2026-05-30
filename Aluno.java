public class Aluno extends Pessoa {
    private final String senha;
    private final int idade;
    private final Plano plano;
    public Aluno(
            String nome,
            String email,
            String senha,
            int idade,
            Plano plano
    ) {
        super(nome, email);
        this.senha = senha;
        this.idade = idade;
        this.plano = plano;
    }
    public String getSenha() {
        return senha;
    }
    public int getIdade() {
        return idade;
    }
    public Plano getPlano() {
        return plano;
    }
}