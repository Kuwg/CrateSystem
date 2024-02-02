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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CallToPrintStackTrace")
public class DatabaseManager {
    private Connection connection;
    private final String host;
    private final String database;
    private final String username;
    private final String password;

    private static final String CRATE_TABLE = "crates";

    public DatabaseManager(String host, String database, String username, String password) {
        this.host = host;
        this.database = database;
        this.username = username;
        this.password = password;
        connectToDatabase();
        createCrateTable();
    }

    public static DatabaseManager fromConfig(CrateConfiguration configuration) {
        return new DatabaseManager(
                configuration.getSQLSetting("host"),
                configuration.getSQLSetting("database"),
                configuration.getSQLSetting("username"),
                configuration.getSQLSetting("password")
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
                    "crate_name VARCHAR(255)," +
                    "item_name VARCHAR(255)," +
                    "item_amount INT," +
                    "item_enchantments VARCHAR(255)," +
                    "item_lore VARCHAR(255)," +
                    "world VARCHAR(255)," +
                    "x DOUBLE," +
                    "y DOUBLE," +
                    "z DOUBLE" +
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

    public void saveCrate(Crate crate) {
        try {
            String query = "INSERT INTO " + CRATE_TABLE + " (crate_name, item_name, item_amount, item_enchantments, item_lore, world, x, y, z) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);

            for (final ItemStack reward : crate.getPossibleRewards()) {
                ItemMeta meta = reward.getItemMeta();
                statement.setString(1, crate.getName());
                statement.setString(2, reward.getType().name());
                statement.setInt(3, reward.getAmount());
                statement.setString(4, ItemDataSerializer.serializeEnchants(meta.getEnchants()));
                statement.setString(5, ItemDataSerializer.serializeLore(reward.getLore()));
                statement.setString(6, crate.getLocation().getWorld().getName());
                statement.setDouble(7, crate.getLocation().getX());
                statement.setDouble(8, crate.getLocation().getY());
                statement.setDouble(9, crate.getLocation().getZ());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Crate loadCrate(String crateName) {
        try {
            String query = "SELECT item_name, item_amount, item_enchantments, item_lore, world, x, y, z FROM " + CRATE_TABLE + " WHERE crate_name = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, crateName);
            ResultSet resultSet = statement.executeQuery();

            Crate crate = new Crate(
                    crateName,
                    Bukkit.getWorld(resultSet.getString("world")),
                    resultSet.getDouble("x"),
                    resultSet.getDouble("y"),
                    resultSet.getDouble("z"));

            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int itemAmount = resultSet.getInt("item_amount");
                String enchantments = resultSet.getString("item_enchantments");
                String lore = resultSet.getString("item_lore");
                ItemStack reward = new ItemStack(Material.getMaterial(itemName), itemAmount);
                ItemMeta meta = reward.getItemMeta();
                Map<Enchantment, Integer> deserialized = ItemDataSerializer.deserializeEnchants(enchantments);
                meta.setLore(ItemDataSerializer.deserializeLore(lore));
                for (Enchantment enchantment : deserialized.keySet()) {
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
    public List<Crate> loadAllCrates() {
        List<Crate> crates = new ArrayList<>();
        try {
            String query = "SELECT DISTINCT(crate_name) FROM " + CRATE_TABLE;
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String crateName = resultSet.getString("crate_name");
                Crate crate = loadCrate(crateName);
                if (crate != null) {
                    crates.add(crate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return crates;
    }
}
