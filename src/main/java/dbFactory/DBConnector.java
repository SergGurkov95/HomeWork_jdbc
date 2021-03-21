package dbFactory;

import java.sql.*;

import static dbFactory.ConnectionConstants.*;

public class DBConnector {
    private final Connection connection;

    public DBConnector() {
        try {
            connection = DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public PreparedStatement getPrepareStatement(String sql){
        try {
            return connection.prepareStatement(sql);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public ResultSet executeQuery(String sql){
        try {
            return connection.createStatement().executeQuery(sql);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public void execute(String sql){
        try {
            connection.createStatement().execute(sql);
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public void close(){
        try {
            connection.close();
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }
}
