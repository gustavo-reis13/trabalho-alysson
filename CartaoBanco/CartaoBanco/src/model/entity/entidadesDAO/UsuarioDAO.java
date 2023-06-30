package model.entity.entidades_DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import model.entity.ConectaBD;
import model.entity.Usuario;

public class UsuarioDAO {

    public static String getDataAtual() {
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return dataAtual.format(formato);
    }

    public void cadastrasUsuario(Usuario u, int user_id){
        ConectaBD con = new ConectaBD();
        con.getConexao();

        String sql = "INSERT INTO usuario (nome, documento, genero, telefone, dataNascimento) VALUES (?,?,?,?,?)";
        try {
            PreparedStatement pst = con.getConexao().prepareStatement(sql);
            pst.setString(1, u.getNome());
            pst.setString(2, u.getDocumento());
            pst.setString(3, u.getGenero());
            pst.setString(4, u.getTelefone());
            pst.setString(5, u.getDataNascimento());

            pst.execute();
            System.out.println("O usuário foi cadastrado! ");
            
            String log = "Insert into log (id_usuario, log) Values (" + user_id + ", '" + getDataAtual() + " - ID: " + user_id + " | Cadastro de usuario - sucesso');";
            pst = con.getConexao().prepareStatement(log);
            pst.execute();

        } catch (SQLException e) {
            String log = "Insert into log (id_usuario, log) Values (" + user_id + ", '" + getDataAtual() + " - ID: " + user_id + " | Cadastro de usuario - fracasso');";
            
            try {
                PreparedStatement pst  = con.getConexao().prepareStatement(log);
                pst = con.getConexao().prepareStatement(log);
                pst.execute();
            } catch (SQLException e1) {
                
                e1.printStackTrace();
            }
            
            
            e.printStackTrace();
            
        }
    }

    public int listarCartoes(int id_usuario){
        ConectaBD con = new ConectaBD();
        
        String sql = "SELECT num_cartao FROM cartao WHERE id_usuario = ?";

        try {
            PreparedStatement pst = con.getConexao().prepareStatement(sql);
            pst.setInt(1, id_usuario);

            ResultSet res = pst.executeQuery();

            if(!res.isBeforeFirst()){
                System.out.println("O usuário informado não possui nenhum cartão.");
                return -1;
            } else {
                int i = 1;
                System.out.println("Cartões deste usuário: \n");
                while(res.next()){
                    System.out.println(i + "- " + res.getString("num_cartao"));
                    
                }
                String log = "Insert into log (id_usuario, log) Values (" + id_usuario + ", '" + getDataAtual() + " - ID: " + id_usuario + " | Solicitação de listagem de cartoes');";
                System.out.println(log);
                pst = con.getConexao().prepareStatement(log);
                pst.execute();
                return 0;
            }
            
        } catch (SQLException e) {
            
            e.printStackTrace();
            return -1;
        }


    }
    
}
