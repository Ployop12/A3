import java.util.ArrayList;
import java.util.List;

public class Academia {
    private static final List<Aluno> alunos =
            new ArrayList<>();
    public void adicionarAluno(Aluno aluno) {
        alunos.add(aluno);
        System.out.println(
                "DEBUG -> lista possui " +
                alunos.size() +
                " alunos."
        );
    }
    public int getQuantidadeAlunos() {
        return alunos.size();
    }
    public List<Aluno> getAlunos() {
        return alunos;
    }
}