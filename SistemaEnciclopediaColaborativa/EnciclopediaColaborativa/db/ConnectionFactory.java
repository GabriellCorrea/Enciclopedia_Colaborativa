package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

  public Connection recuperaConexao() {
    try {
      String sgbd = "mysql";
      String endereco = "localhost";
      String bd = "enciclopedia";
      String usuario = "root";
      String senha = "Estev@o21032007";

      Connection connection = DriverManager.getConnection(
          "jdbc:" + sgbd + "://" + endereco + "/" + bd, usuario, senha);

      return connection;
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

}