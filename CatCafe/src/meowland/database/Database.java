package meowland.database;

import javafx.scene.control.Alert;

import java.io.Closeable;
import java.sql.*;
import java.util.ArrayList;

// Class for working with database MySQL, not absolutely universal.

public class Database implements Closeable
{
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String table;
    private int countColumns;

    public Database(String url, String username, String password)
    {
        try
        {
            connection = DriverManager.getConnection(url, username, password);
            statement = connection.createStatement();
        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Невозможно установить соединение с базой данных");
            alert.show();
        }
    }

    public void setTable(String table)
    {
        try
        {
            this.table = table;
            statement.execute("CREATE TABLE IF NOT EXISTS `" + table + "` (" +
                    "  `Id` INT NOT NULL AUTO_INCREMENT, `StartTime` VARCHAR(5) NOT NULL, `EndTime` VARCHAR(5) NOT NULL," +
                    "  `Number` INT(3) NULL DEFAULT 1,  `Sale` INT(2) NULL DEFAULT NULL,  `Price` INT(5) NOT NULL," +
                    "  UNIQUE INDEX `Id_UNIQUE` (`Id` ASC) VISIBLE,  PRIMARY KEY (`Id`));");
            resultSet = statement.executeQuery("SELECT * FROM `" + table + "`;");
            countColumns = resultSet.getMetaData().getColumnCount();
        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных");
            alert.show();
        }
    }

    public String toString()
    {
        try
        {
            resultSet = statement.executeQuery("SELECT * FROM " + table);
            StringBuilder sb = new StringBuilder();
            while (resultSet.next())
            {
                for (int i = 1; i <= countColumns; i++)
                {
                    String data = resultSet.getString(i);
                    sb.append(data).append(" ");
                }
                sb.append(";");
            }
            return sb.toString();
        } catch (SQLException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных");
            alert.show();
            return null;
        }
    }

    public boolean isThereTable(String table)
    {
        try
        {
            resultSet = statement.executeQuery("SELECT * FROM `" + table + "`;");
            resultSet.next();
            return resultSet != null;
        } catch (SQLException e)
        {
            return false;
        }
    }

    public String getLastRowCell(String table, String columnKey)
    {
        try
        {
            resultSet = statement.executeQuery("SELECT " + columnKey + " FROM `" + table + "`;");
            while (resultSet.next() && resultSet != null)
            {
                if (resultSet.isLast())
                    return resultSet.getString(columnKey);
            }
            return null;
        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных lastcell");
            alert.show();
            return null;
        }
    }

    public int getCountOfRows(String table)
    {
        try
        {
            resultSet = statement.executeQuery("SELECT COUNT(*) FROM `" + table + "`;");
            resultSet.next();
            return resultSet.getInt(0);
        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных");
            alert.show();
            return 0;
        }
    }

    public ArrayList<String> getAll(String columnKey)
    {
        try
        {
            ArrayList<String> list = new ArrayList<>();
            resultSet = statement.executeQuery("SELECT `" + columnKey + "` FROM `" + table + "`;");
            while (resultSet.next())
            {
                System.out.println(resultSet.getString(columnKey));
                list.add(resultSet.getString(columnKey));
            }

            if (list.size() == 0)
                return null;

            return list;
        } catch (SQLException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных getall");
            alert.show();
            return null;
        }
    }

    public String get(String knownColumnKey, String key, String columnKey)
    {
        try
        {
            resultSet = statement.executeQuery("SELECT " + columnKey + " FROM " + table + " WHERE " + knownColumnKey + " ='" + key + "';");
            resultSet.next();
            return resultSet.getString(columnKey);
        } catch (SQLException ex)
        {
            return null;
        }
    }

    public ArrayList<String> get(String knownColumnKey, String key, String[] columnKeys)
    {
        try
        {
            ArrayList<String> list = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT ");
            for (String columnKey : columnKeys)
            {
                sb.append(columnKey).append(", ");
            }
            sb.delete(sb.lastIndexOf(","), sb.lastIndexOf(",") + 2);
            sb.append(" FROM ").append(table).append(" WHERE ").append(knownColumnKey).append(" ='").append(key).append("';");
            resultSet = statement.executeQuery(sb.toString());
            while (resultSet.next())
            {
                for (String columnKey : columnKeys)
                {
                    list.add(resultSet.getString(columnKey));
                }
            }
            if (list.size() == 0)
                return null;

            return list;
        } catch (SQLException ex)
        {
            return null;
        }
    }

    public void insert(String[] values)
    {
        try
        {
            if (values.length == countColumns - 1)
            {
                resultSet = statement.executeQuery("SELECT * FROM `" + table + "`;");
                ResultSetMetaData resultSetMD = resultSet.getMetaData();
                StringBuilder sb = new StringBuilder();
                int i;
                sb.append("INSERT INTO `").append(table).append("` (`");
                for (i = 2; i < countColumns; i++)
                    sb.append(resultSetMD.getColumnLabel(i)).append("`, `");
                sb.append(resultSetMD.getColumnLabel(i)).append("`) VALUES (");
                for (i = 0; i < values.length - 1; i++)
                    sb.append("'").append(values[i]).append("',");
                sb.append("'").append(values[i]).append("');");

                System.out.println(sb.toString());
                statement.executeUpdate(sb.toString());
            } else
            {
                throw new Exception("Неверное количество данных");
            }
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных");
            alert.show();
        }
    }

    public void insert(String[] values, int n)
    {
        try
        {
            if (values.length == countColumns - n)
            {
                resultSet = statement.executeQuery("SELECT * FROM `" + table + "`;");
                ResultSetMetaData resultSetMD = resultSet.getMetaData();
                StringBuilder sb = new StringBuilder();
                int i;
                sb.append("INSERT INTO `").append(table).append("` (`");
                for (i = n + 1; i < countColumns; i++)
                    sb.append(resultSetMD.getColumnLabel(i)).append("`, `");
                sb.append(resultSetMD.getColumnLabel(i)).append("`) VALUES (");
                for (i = 0; i < values.length - 1; i++)
                    sb.append("'").append(values[i]).append("',");
                sb.append("'").append(values[i]).append("');");

                System.out.println(sb.toString());
                statement.executeUpdate(sb.toString());
            } else
            {
                throw new Exception("Неверное количество данных");
            }
        } catch (Exception ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных");
            alert.show();
        }
    }

    public void set(String columnName, String value, String columnKey, String key)
    {
        try
        {
            statement.executeUpdate("UPDATE " + table + " SET `" + columnName + "` = '" + value + "' WHERE (`" + columnKey + "` = '" + key + "');");
        } catch (SQLException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных");
            alert.show();
        }
    }

    public void delete(String columnKey, String key)
    {
        try
        {
            statement.executeUpdate("DELETE FROM " + table + " WHERE `" + columnKey + "` = '" + key + "';");
        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных");
            alert.show();
        }
    }

    public void func(String query)
    {
        try (Statement statement = connection.createStatement())
        {
            statement.executeUpdate(query);
        } catch (SQLException ex)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных");
            alert.show();
        }
    }

    public void close()
    {
        try
        {
            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Ошибка базы данных");
            alert.show();
        }
    }
}