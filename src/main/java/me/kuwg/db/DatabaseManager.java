package me.kuwg.db;

import me.kuwg.config.CrateConfiguration;
import me.kuwg.crate.Crate;
import me.kuwg.util.ItemDataSerializer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.*;

@SuppressWarnings("CallToPrintStackTrace")
public class DatabaseManager {
    private Connection connection;
    private final String host, database, username, password, table;

    private static final String CRATE_TABLE = "crates";


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

    private void createCrateTable() {
        try {
            String query = "CREATE TABLE IF NOT EXISTS " + CRATE_TABLE + " (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "item_name VARCHAR(255)," +
                    "item_amount INT," +
                    "item_data INT" +
                    ")";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
        } catch (SQLException e) {
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










    public void saveCrate(String crateName, Crate crate) {
        try {
            String query = "INSERT INTO " + CRATE_TABLE + " (item_name, item_amount, item_enchantments, item_lore) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            for (final ItemStack reward : crate.getPossibleRewards()) {
                ItemMeta meta = reward.getItemMeta();
                statement.setString(1, reward.getType().name());
                statement.setInt(2, reward.getAmount());
                statement.setString(3, ItemDataSerializer.serializeEnchants(meta.getEnchants()));
                statement.setString(4, ItemDataSerializer.serializeLore(reward.getLore()));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
