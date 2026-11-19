package org.example.projetojavafx.db;

import java.sql.*;

public class DataBase {

    private static Connection connection;

    public static Connection getConnection(){
        if(connection==null){

            String url = "jdbc:mysql://localhost:3306/projetojavafx";

            // Lê as variáveis configuradas no IntelliJ
            String user = System.getenv("DB_USER");
            String password = System.getenv("DB_PASSWORD");

            try{
                 connection = DriverManager.getConnection(url,user,password);
            } catch (SQLException e) {
                throw new RuntimeException("Erro na conexão com o database" + e );
            }
        }
        return connection;
    }

    public static void closeConnection(){
        if(connection!=null){
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void closeResultSet(ResultSet rs){
        if(rs!=null){
            try {
                rs.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    }

    public static void closeStatement(Statement st){
        if(st!=null){
            try {
                st.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}


