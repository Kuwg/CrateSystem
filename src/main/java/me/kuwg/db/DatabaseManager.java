package me.kuwg.db;

import me.kuwg.config.CrateConfiguration;
import me.kuwg.crate.Crate;
import me.kuwg.util.ItemDataSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

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
            String query = "INSERT INTO " + CRATE_TABLE + " (item_name, item_amount, item_enchantments, item_lore, world, x, y, z) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            for (final ItemStack reward : crate.getPossibleRewards()) {
                ItemMeta meta = reward.getItemMeta();
                statement.setString(1, reward.getType().name());
                statement.setInt(2, reward.getAmount());
                statement.setString(3, ItemDataSerializer.serializeEnchants(meta.getEnchants()));
                statement.setString(4, ItemDataSerializer.serializeLore(reward.getLore()));
                statement.setString(5, crate.getLocation().getWorld().getName());
                statement.setDouble(5, crate.getLocation().getX());
                statement.setDouble(6, crate.getLocation().getX());
                statement.setDouble(7, crate.getLocation().getZ());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Crate loadCrate(String crateName) {
        try{
            String query = "SELECT item_name, item_amount, item_enchantments, item_lore, world, x, y, z FROM " + CRATE_TABLE;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            Crate crate = new Crate(
                    Bukkit.getWorld(resultSet.getString("world")),
                    resultSet.getDouble("x"),
                    resultSet.getDouble("y"),
                    resultSet.getDouble("z")
            );

            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int itemAmount = resultSet.getInt("item_amount");
                String enchantments = resultSet.getString("item_enchantments");
                String lore = resultSet.getString("item_lore");
                ItemStack reward = new ItemStack(Material.getMaterial(itemName), itemAmount);
                ItemMeta meta = reward.getItemMeta();
                Map<Enchantment, Integer> deserialized = ItemDataSerializer.deserializeEnchants(enchantments);
                meta.setLore(ItemDataSerializer.deserializeLore(lore));
                for(Enchantment enchantment : deserialized.keySet()){
                    meta.addEnchant(enchantment, deserialized.get(enchantment), true /* allow unsafe */);
                }
                crate.addReward(reward);
            }

            return crate;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
