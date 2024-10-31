package com.pilzbros.autorun.io;

import com.pilzbros.autorun.Autorun;
import com.pilzbros.autorun.component.ACommand;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

public class InputOutput {
    public static YamlConfiguration global;
    private static Connection connection;

    public InputOutput() {
        if (!Autorun.instance.getDataFolder().exists()) {
            try {
                Autorun.instance.getDataFolder().mkdir();
            } catch (Exception var2) {
                var2.printStackTrace();
            }
        }

        global = new YamlConfiguration();
    }

    public void LoadSettings() {
        try {
            if (!(new File(Autorun.instance.getDataFolder(), "global.yml")).exists()) {
                global.save(new File(Autorun.instance.getDataFolder(), "global.yml"));
            }

            global.load(new File(Autorun.instance.getDataFolder(), "global.yml"));
            Setting[] var4;
            int var3 = (var4 = Setting.values()).length;

            for(int var2 = 0; var2 < var3; ++var2) {
                Setting s = var4[var2];
                if (global.get(s.getString()) == null) {
                    global.set(s.getString(), s.getDefault());
                }
            }

            global.save(new File(Autorun.instance.getDataFolder(), "global.yml"));
        } catch (FileNotFoundException var5) {
            var5.printStackTrace();
        } catch (IOException var6) {
            var6.printStackTrace();
        } catch (InvalidConfigurationException var7) {
            var7.printStackTrace();
        }

    }

    public static synchronized Connection getConnection() {
        if (connection == null) {
            connection = createConnection();
        }

        try {
            if (connection.isClosed()) {
                connection = createConnection();
            }
        } catch (SQLException var1) {
            var1.printStackTrace();
        }

        return connection;
    }

    private static Connection createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection ret = DriverManager.getConnection("jdbc:sqlite:" + (new File(Autorun.instance.getDataFolder().getPath(), "db.sqlite")).getPath());
            ret.setAutoCommit(false);
            return ret;
        } catch (ClassNotFoundException var1) {
            Autorun.log.log(Level.SEVERE, "[Autorun] Fatal database connection error (Class)");
            var1.printStackTrace();
            return null;
        } catch (SQLException var2) {
            Autorun.log.log(Level.SEVERE, "[Autorun] Fatal database connection error (SQL)");
            var2.printStackTrace();
            return null;
        }
    }

    public static synchronized void freeConnection() {
        Connection conn = getConnection();
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException var2) {
                var2.printStackTrace();
            }
        }

    }

    public void prepareDB() {
        Connection conn = getConnection();
        Statement st = null;

        try {
            st = conn.createStatement();
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"autorun_usercommands\" (\"UDID\" VARCHAR PRIMARY KEY  NOT NULL , \"Command\" VARCHAR, \"Level\" VARCHAR, \"Times\" INT, \"By\" VARCHAR)");
            st.executeUpdate("CREATE TABLE IF NOT EXISTS \"autorun_allcommands\" (\"Command\" VARCHAR PRIMARY KEY  NOT NULL , \"Times\" INT, \"Level\" VARCHAR, \"By\" VARCHAR)");
            conn.commit();
            st.close();
        } catch (SQLException var4) {
            Autorun.log.log(Level.SEVERE, "[Autorun] Error preparing database! (SQL)");
            var4.printStackTrace();
        } catch (Exception var5) {
            Autorun.log.log(Level.SEVERE, "[Autorun] Error preparing database! (Unknown)");
        }

    }

    public void updateDB() {
    }

    public void Update(String check, String sql) {
        this.Update(check, sql, sql);
    }

    public void Update(String check, String sqlite, String mysql) {
        try {
            Statement statement = getConnection().createStatement();
            statement.executeQuery(check);
            statement.close();
        } catch (SQLException var13) {
            try {
                String[] query = sqlite.split(";");
                Connection conn = getConnection();
                Statement st = conn.createStatement();
                String[] var11 = query;
                int var10 = query.length;

                for(int var9 = 0; var9 < var10; ++var9) {
                    String q = var11[var9];
                    st.executeUpdate(q);
                }

                conn.commit();
                st.close();
                Autorun.log.log(Level.INFO, "[Autorun] Database upgraded");
            } catch (SQLException var12) {
                Autorun.log.log(Level.SEVERE, "[Autorun] Error while upgrading database to new version");
                var12.printStackTrace();
            }
        }

    }

    public void loadCommands() {
        try {
            PreparedStatement ps = null;
            ResultSet result = null;
            Connection conn = getConnection();
            ps = conn.prepareStatement("SELECT `UDID`, `Command`, `Level`, `Times`, `By` FROM `autorun_usercommands`");
            result = ps.executeQuery();
            int allPlayers = 0;
            int justPlayers = 0;

            while(result.next()) {
                if (result.getString("UDID").equalsIgnoreCase("666")) {
                    Autorun.commandManager.addCommand("666", result.getString("By"), result.getString("Command"), result.getString("Level"), result.getInt("Times"));
                    ++allPlayers;
                } else {
                    Autorun.commandManager.addCommand(result.getString("UDID"), result.getString("By"), result.getString("Command"), result.getString("Level"), result.getInt("Times"));
                    ++justPlayers;
                }
            }

            Autorun.log.log(Level.INFO, "[Autorun] " + justPlayers + " user command(s) & " + allPlayers + " all-player command(s) loaded");
            conn.commit();
            ps.close();
        } catch (SQLException var6) {
            Autorun.log.log(Level.WARNING, "[Autorun] Encountered an issue loading user commands");
        }

    }

    public void loadAllUserCommands() {
    }

    public void removeCommand(ACommand command) {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM autorun_usercommands WHERE UDID = ? AND Command = ?");
            ps.setString(1, command.getAPlayer().getUUID());
            ps.setString(2, command.getCommand());
            ps.executeUpdate();
            conn.commit();
            ps.close();
        } catch (SQLException var4) {
            Autorun.log.log(Level.WARNING, "[Autorun] Error while removing command from database - " + var4.getMessage());
            var4.printStackTrace();
        }

    }

    public void saveCommand(ACommand command) {
        try {
            Connection conn = getConnection();
            String sql = "INSERT INTO autorun_usercommands (`UDID`, `Command`, `Level`, `Times`, `By`) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, command.getAPlayer().getUUID());
            preparedStatement.setString(2, command.getCommand());
            preparedStatement.setString(3, command.getLevel());
            preparedStatement.setInt(4, command.getTimes());
            preparedStatement.setString(5, command.getByUUID());
            preparedStatement.executeUpdate();
            conn.commit();
            Autorun.log.log(Level.INFO, "[Autorun] Command stored to DB");
        } catch (SQLException var5) {
            Autorun.log.log(Level.WARNING, "[Autorun] Unexpected error while storing sign in database! ---" + var5.getMessage());
        }

    }

    public void updateCommand(ACommand command) {
        try {
            Connection conn = getConnection();
            String sql = "UPDATE `autorun_usercommands` SET `Times` = ? WHERE `UDID` = ? AND `Command` = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, command.getTimes());
            preparedStatement.setString(2, command.getAPlayer().getUUID());
            preparedStatement.setString(3, command.getCommand());
            preparedStatement.executeUpdate();
            connection.commit();
            conn.commit();
        } catch (SQLException var5) {
            Autorun.log.log(Level.WARNING, "[Autorun] Error while updating command - " + var5.getMessage());
        }

    }

    public void purgeData() {
        try {
            Connection conn = getConnection();
            PreparedStatement ps = conn.prepareStatement("DELETE FROM autorun_usercommands");
            ps.executeUpdate();
            conn.commit();
            ps.close();
        } catch (SQLException var3) {
            Autorun.log.log(Level.WARNING, "[Autorun] Error while purging Autorun database data - " + var3.getMessage());
            var3.printStackTrace();
        }

    }
}