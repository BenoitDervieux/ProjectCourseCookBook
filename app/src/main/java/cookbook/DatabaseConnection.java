package cookbook;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {

  public Connection databaseLink;

  public Connection getDBConnection() {

    String databaseName = "chanuka1";
    //String hostName = "34.88.207.9";
    String databaseUser = "root";
    String databasePassword = "Kevin_mathwiz!23";
    String url = "jdbc:mysql://34.88.207.9/" + databaseName;


    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
      databaseLink = DriverManager.getConnection(url, databaseUser, databasePassword);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return databaseLink;
  }
}
