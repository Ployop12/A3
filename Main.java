import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            RegistrationFrame frame = new RegistrationFrame();
            frame.setVisible(true);
        });
    }
}

class ValidationException extends Exception {
    public ValidationException(String mensagem) {
        super(mensagem);
    }
}

class AlunoValidator {
    public static void validar(
            String nome,
            String email,
            String senha,
            String idade
    ) throws ValidationException {
        if (
                nome.isEmpty() ||
                email.isEmpty() ||
                senha.isEmpty() ||
                idade.isEmpty()
        ) {
            throw new ValidationException(
                    "Por favor, preencha todos os campos!"
            );
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new ValidationException(
                    "Por favor, insira um e-mail válido!"
            );
        }
        int idadeConvertida;
        try {
            idadeConvertida = Integer.parseInt(idade);
        } catch (NumberFormatException e) {
            throw new ValidationException(
                    "A idade deve ser um número válido!"
                );
        }
        if (idadeConvertida < 16 || idadeConvertida > 100) {
            throw new ValidationException(
                    "Por favor, insira uma idade entre 16 e 100 anos!"
            );
        }
        if (senha.length() < 6) {
            throw new ValidationException(
                    "A senha deve ter pelo menos 6 caracteres!"
            );
        }
    }
}

class AlunoService {
    private final Academia academia = new Academia();

    public void registrar(Aluno aluno) {
        academia.adicionarAluno(aluno);
        System.out.println("LOG: Aluno '" + aluno.getNome() + "' registrado!");
        System.out.println("Plano: " + aluno.getPlano());
        System.out.println("Total de alunos: " + academia.getQuantidadeAlunos());
    }

    public String listarAlunos() {
        if (academia.getAlunos().isEmpty()) {
            return "Nenhum aluno cadastrado.";
        }
        StringBuilder sb = new StringBuilder();
        for (Aluno aluno : academia.getAlunos()) {
            sb.append("Nome: ").append(aluno.getNome()).append("\n");
            sb.append("Email: ").append(aluno.getEmail()).append("\n");
            sb.append("Plano: ").append(aluno.getPlano()).append("\n\n");
        }
        return sb.toString();
    }

    public Aluno buscarPorEmail(String email) {
        for (Aluno aluno : academia.getAlunos()) {
            if (aluno.getEmail().equalsIgnoreCase(email)) {
                return aluno;
            }
        }
        return null;
    }

    public int getQuantidadeAlunos() {
        return academia.getQuantidadeAlunos();
    }
}

class RegistrationFrame extends JFrame {
    private JTextField nameField;
    private JTextField emailField;
    private JTextField ageField;
    private JPasswordField passwordField;
    private JComboBox<String> membershipCombo;
    private final AlunoService service = new AlunoService();

    public RegistrationFrame() {
        setTitle("Academia - Registro");
        setSize(450, 550); // Aumentado levemente para acomodar os botões novos
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        initialize(); // Chama a inicialização da tela
    }

    private void initialize() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets = new Insets(10, 10, 10, 10);
        g.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Registro de Novo Aluno", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        g.gridx = 0;
        g.gridy = 0;
        g.gridwidth = 2;
        panel.add(title, g);
        g.gridwidth = 1;

        /* =========================
           CAMPOS
        ========================= */
        nameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);
        ageField = new JTextField(20);

        String[] labels = { "Nome Completo:", "E-mail:", "Senha:", "Idade:" };
        JComponent[] fields = { nameField, emailField, passwordField, ageField };

        for (int i = 0; i < fields.length; i++) {
            g.gridx = 0;
            g.gridy = i + 1;
            panel.add(new JLabel(labels[i]), g);
            g.gridx = 1;
            panel.add(fields[i], g);
        }

        /* =========================
           COMBOBOX
        ========================= */
        g.gridx = 0;
        g.gridy = 5;
        panel.add(new JLabel("Tipo de Plano:"), g);

        g.gridx = 1;
        membershipCombo = new JComboBox<>(new String[]{ "Básico", "Standard", "Premium", "Familiar" });
        panel.add(membershipCombo, g);

        /* =========================
           BOTÕES
        ========================= */
        JButton registerButton = new JButton("Registrar");
        JButton buscarButton = new JButton("Buscar Aluno");
        JButton listarButton = new JButton("Listar Alunos");

        // Posicionando os botões na tela
        g.gridx = 0;
        g.gridy = 6;
        g.gridwidth = 2;
        panel.add(registerButton, g);

        g.gridx = 0;
        g.gridy = 7;
        panel.add(buscarButton, g);

        g.gridx = 0;
        g.gridy = 8;
        panel.add(listarButton, g);

        /* =========================
           AÇÃO DO BOTÃO REGISTRAR
        ========================= */
        registerButton.addActionListener(e -> {
            String nome = nameField.getText().trim();
            String email = emailField.getText().trim();
            String senha = new String(passwordField.getPassword()).trim();
            String idadeStr = ageField.getText().trim();
            
            try {
                // Valida os dados antes de prosseguir
                AlunoValidator.validar(nome, email, senha, idadeStr);
                
                int idade = Integer.parseInt(idadeStr);
                
                // Mapeia a seleção do ComboBox para o Enum Plano correspondente
                String planoSelecionado = (String) membershipCombo.getSelectedItem();
                Plano plano = Plano.BASICO;
                if ("Standard".equals(planoSelecionado)) plano = Plano.STANDARD;
                else if ("Premium".equals(planoSelecionado)) plano = Plano.PREMIUM;
                else if ("Familiar".equals(planoSelecionado)) plano = Plano.FAMILIAR;

                // Cria e registra o aluno
                Aluno novoAluno = new Aluno(nome, email, senha, idade, plano);
                service.registrar(novoAluno);

                JOptionPane.showMessageDialog(this, "Aluno cadastrado com sucesso!");
                
                // Limpa os campos após o cadastro bem-sucedido
                nameField.setText("");
                emailField.setText("");
                passwordField.setText("");
                ageField.setText("");
                membershipCombo.setSelectedIndex(0);

            } catch (ValidationException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            }
        });

        /* =========================
           AÇÃO DO BOTÃO BUSCAR
        ========================= */
        buscarButton.addActionListener(e -> {
            String emailBusca = JOptionPane.showInputDialog(this, "Digite o e-mail:");
            if (emailBusca == null || emailBusca.isBlank()) {
                return;
            }
            Aluno aluno = service.buscarPorEmail(emailBusca.trim());
            if (aluno != null) {
                JOptionPane.showMessageDialog(this,
                        "Nome: " + aluno.getNome() +
                        "\nEmail: " + aluno.getEmail() +
                        "\nIdade: " + aluno.getIdade() +
                        "\nPlano: " + aluno.getPlano()
                );
            } else {
                JOptionPane.showMessageDialog(this, "Aluno não encontrado.");
            }
        });

        /* =========================
           AÇÃO DO BOTÃO LISTAR
        ========================= */
        listarButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, service.listarAlunos(), "Alunos Cadastrados", JOptionPane.INFORMATION_MESSAGE);
        });

        // Adiciona o painel principal ao JFrame
        add(panel);
    }
}