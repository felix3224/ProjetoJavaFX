package org.example.projetojavafx;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;
import org.example.projetojavafx.db.DataBase;

import java.io.IOException;
import java.sql.*;

public class Application extends javafx.application.Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {

        System.out.println("1. == TESTE CONNECTION DATABASE == ");
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {

            conn = DataBase.getConnection();

            String sql = " select codigo, nome from turma where codigo = 1";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            boolean find = false;
            while (rs.next()){
                find = true;

                int codigo = rs.getInt("codigo");
                String nome = rs.getString("nome");
                System.out.println(" -> codigo " + codigo + " | " + "nome: " + nome);
            }
            if(!find){
                System.out.println("Foi aberta a connection com o db, mas a tabela está vazia!");
            }

        } catch (SQLException e) {
            throw new RuntimeException("Erro ao consultar o db." + e);
        }finally {
            DataBase.closeResultSet(rs);
            DataBase.closeStatement(st);
            DataBase.closeConnection();
            System.out.println("All close with ssucces!");
        }
        //lauch(args);

    }
}