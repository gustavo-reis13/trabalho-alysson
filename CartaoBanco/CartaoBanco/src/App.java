import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.chrono.IsoChronology;
import java.util.Scanner;

import java.sql.PreparedStatement;

import model.entity.Cartao;
import model.entity.ConectaBD;
import model.entity.Usuario;
import model.entity.entidades_DAO.CartaoDAO;
import model.entity.entidades_DAO.UsuarioDAO;

public class App {

    public static int menu(boolean is_primeiro_usuario) {
        if (is_primeiro_usuario) {
            Scanner teclado = new Scanner(System.in);
            System.out.println("-----MENU-----");
            System.out.println("1- Cadastre um usuário: ");
            return teclado.nextInt();
			//Se for o primeiro usuário, cadastrar o mesmo em antes de fazer qualquer coisa
        } else {
            Scanner teclado = new Scanner(System.in);
            System.out.println("-----MENU-----");
            System.out.println("1- Cadastre um usuário ");
            System.out.println("2- Gerar um cartão para usuário ");
            System.out.println("3- Realizar alguma transação");
            System.out.println("4- Efetuar pagamento ");
            System.out.println("5- Consultar saldo ");
            System.out.println("6- Listar cartões de usuário ");
            System.out.println("7- Verificar o Log de Transações");
            System.out.println("8- Definir Usuário (Escolher) ");
            System.out.println("9- Sair");
            System.out.print("Selecione: ");
            return teclado.nextInt();
			// Escolha de opção caso não seja novo usuário
        }

    }

    public static int metodoSelecionarUsuario() {
        Scanner teclado = new Scanner(System.in);
        ConectaBD con = new ConectaBD();
        con.getConexao();
        int i = 1;
        int id_usuario = 0;
		
        String sql = "SELECT id, nome FROM usuario";

        try {
            PreparedStatement pst = con.getConexao().prepareStatement(sql);
            ResultSet res = pst.executeQuery();
            System.out.println("Usuários cadastrados no Sistema: ");

            while (res.next()) {
                System.out.println(i + " - " + res.getString("nome"));
                i++;
            }

            try {
                System.out.println("\nSelecione o usuário através do número: ");
                while (!teclado.hasNextInt()) {
                    System.out.println("O valor informado não foi um número. Tente novamente: ");
                    teclado.next();
                }
                int index_usuario = teclado.nextInt();

                while (index_usuario <= 0 || index_usuario > i - 1) {
                    System.out.println("Usuário inexistente. Tente novamente: ");
                    while (!teclado.hasNextInt()) {
                        System.out.println("O valor informado não foi um número. Tente novamente: ");
                        teclado.next();
                    }
                    index_usuario = teclado.nextInt();
                }
                ResultSet res2 = pst.executeQuery();
                int j = 0;

                while (res2.next()) {
                    if (index_usuario - 1 == j) {
                        id_usuario = res2.getInt("id");
                    }
                    j++;
                }

                sql = "SELECT id from usuario where id = ?;";
                PreparedStatement pst2 = con.getConexao().prepareStatement(sql);

                return id_usuario;

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Ocorreu algum problema ao buscar o número do usuário. ");
                return -1; 
				// Retornar uma mensagem para informar um erro
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Falha ao conectar no Banco de Dados. Tente novamente!");
            return -1; 
			// Retornar uma mensagem para informar um erro
        }
    }

    public static int getNumeroUsuarios() {
        ConectaBD con = new ConectaBD();
        con.getConexao();
        int i = 0;

        String sql = "SELECT nome FROM usuario";

        try {
            PreparedStatement pst = con.getConexao().prepareStatement(sql);
            ResultSet res = pst.executeQuery();
            while (res.next()) {
                i++;
            }

        } catch (Exception e) {
            System.out.println("Falha ao conectar no Banco de Dados. Tente novamente!");
            return -1;
			// Retornar uma mensagem para informar um erro 
        }
        return i;
    }

    public static void metodoCadastrarUsuario(int user_id) {
        Scanner teclado = new Scanner(System.in);
        UsuarioDAO uDAO = new UsuarioDAO();

        System.out.println("Digite o nome do novo usuário: ");
        String nome = teclado.next();
        System.out.println("Digite o documento do novo usuário: ");
        String documento = teclado.next();
        System.out.println("Digite o sexo do novo usuário: ");
        String genero = teclado.next();
        System.out.println("Digite o telefone do novo usuário: ");
        String telefone = teclado.next();
        System.out.println("Digite a data de nascimento do novo usuário: ");
        String data_nascimento = teclado.next();

        try {
            Usuario u = new Usuario(nome, documento, data_nascimento, genero, telefone);
            uDAO.cadastrasUsuario(u, user_id);
        } catch (Exception e) {
			
        }

    }

    public static void metodoEscolherUsuario() {
        Scanner teclado = new Scanner(System.in);

    }

    public static void metodoGerarCartao(int user_id) {
        Scanner teclado = new Scanner(System.in);
        CartaoDAO cDAO = new CartaoDAO();

        System.out.println("Limite do cartão: ");
        Double limite_credito = teclado.nextDouble();
        boolean criado = cDAO.gerarCartaoParaUsuario(user_id, limite_credito);

        if (criado) {
            System.out.println("O cartão foi gerado com sucesso!");
        }

    }

    public static void metodoRealizarTransacao(int user_id) {
        Scanner teclado = new Scanner(System.in);
        CartaoDAO cDAO = new CartaoDAO();

        System.out.println("Quantidade que deseja transferir?");
        double valorTransacao = teclado.nextDouble();

        System.out.println("Pessoa que você deseja transferir?");
        String recebedor = teclado.next();

        cDAO.realizarTransacao(user_id, valorTransacao, recebedor);

    }

    private static void metodoConsultarSaldo(int id_usuario){
        CartaoDAO cDAO = new CartaoDAO();
        cDAO.consultarSaldo(id_usuario);
    }

    private static void metodoListarCartoes(int id_usuario){
        UsuarioDAO uDAO = new UsuarioDAO();
        uDAO.listarCartoes(id_usuario);

    }

    private static void metodoEfetuarPagamento(int id_usuario){
        Scanner teclado = new Scanner(System.in);
        CartaoDAO cDAO = new CartaoDAO();

        System.out.println("Quantidade que deseja transferir?");
        double valorTransacao = teclado.nextDouble();

        System.out.println("Pessoa que você deseja transferir?");
        String recebedor = teclado.next();

        cDAO.realizarTransacao(id_usuario, valorTransacao, recebedor);

    }

    public static void main(String[] args) throws Exception {
        boolean is_primeiro_usuario = getNumeroUsuarios() > 0 ? false : true;
        int user_id = 0;
        int op;
        System.out.println("\nSistema de Cartão\n");
        do {
            is_primeiro_usuario = getNumeroUsuarios() > 0 ? false : true;

            if (is_primeiro_usuario == false && user_id == 0) {
                user_id = metodoSelecionarUsuario();

            }

            op = menu(is_primeiro_usuario);

            switch (op) {
                case 1: {
                    // Cadastro de usuario
                    metodoCadastrarUsuario(user_id);
                    break;
                }

                case 2: {
					// Gerar cartão 
                    System.out.println(user_id);
                    metodoGerarCartao(user_id);
                    break;
                }
                case 3: {
                    // Realizar transação
                    metodoRealizarTransacao(user_id);
                    break;
                }

                case 4: {
                    // Efetuar pagamento
                    metodoEfetuarPagamento(user_id);
                    break;

                }
                case 5: {
                    // Consultar o saldo
                    metodoConsultarSaldo(user_id);
                    break;

                }

                case 6: {
                    // Listar os cartoes
                    metodoListarCartoes(user_id);
                    break;
                }
                case 7: {
                    // Listar log de transacoes
                    break;
                }
                case 8: {
                    // Definir usuario
                    user_id = 0;
                    break;
                }

                case 9: {
                    System.out.println("Exit");
                    break;
                }

                default:
                    System.out.println("Opção incorreta! ");
                    break;
            }
        } while (op != 9);

    }
}
