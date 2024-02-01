package me.kuwg.db;

import me.kuwg.config.CrateConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("CallToPrintStackTrace")
public class DatabaseManager {
    private Connection connection;
    private final String host, database, username, password, table;

    public DatabaseManager(String host, String database, String username, String password, String table) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        this.table = table;
        connectToDatabase();
    }
    public static DatabaseManager fromConfig(CrateConfiguration configuration){
        return new DatabaseManager(
                configuration.getSQLSetting("host"),
                configuration.getSQLSetting("database"),
                configuration.getSQLSetting("username"),
                configuration.getSQLSetting("password"),
                configuration.getSQLSetting("table")
        );
    }
    private void connectToDatabase() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
