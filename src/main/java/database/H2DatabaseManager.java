package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class H2DatabaseManager {
    private static final String PROJECT_FOLDER = "dz";
    private static final String URL = "jdbc:h2:file:C:\\Users\\mibar\\IdeaProjects\\dz";
    private static final String USER = "sa";
    private static final String PASSWORD = "password";

    static {
        try {
            Class.forName("org.h2.Driver");

            try (Connection connection = getConnection();
                 Statement statement = connection.createStatement()) {
                statement.executeUpdate("CREATE SCHEMA IF NOT EXISTS fff");
                statement.executeUpdate("DROP TABLE IF EXISTS `fff`.`comment`");
                statement.executeUpdate("DROP TABLE IF EXISTS `fff`.`post`");
                statement.executeUpdate("DROP TABLE IF EXISTS `fff`.`user`");
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS `fff`.`user` (\n" +
                        " id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  `username` VARCHAR(45) NULL,\n" +
                        "  `name` VARCHAR(45) NULL);");
                System.out.println(statement.executeUpdate("INSERT INTO `fff`.`user` (username, name) VALUES ('jon', 'John' )"));


                System.out.println(statement.executeQuery("SELECT * FROM `fff`.`user` where id=1"));
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS `fff`.`post` (\n" +
                        "  id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  `text` VARCHAR(450) DEFAULT NULL,\n" +
                        "  `user` INT DEFAULT NULL,\n" +

                        "  CONSTRAINT `fk_post_user` FOREIGN KEY (`user`) REFERENCES `user` (`id`) ON DELETE CASCADE\n" +
                        ");");
                System.out.println(statement.executeUpdate("INSERT INTO `fff`.`post` (text, user) VALUES ('это  первый пост ', 1 )"));
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS `fff`.`comment` (\n" +
                        "  id INT AUTO_INCREMENT PRIMARY KEY,\n" +
                        "  `text` VARCHAR(450) NULL,\n" +
                        "  `user` INT NULL,\n" +
                        "  `post` INT NULL,\n" +

                        "  CONSTRAINT `fk_comment_post`\n" +
                        "    FOREIGN KEY (`post`)\n" +
                        "    REFERENCES `fff`.`post` (`id`)\n" +
                        "    ON DELETE CASCADE\n" +
                        "    ON UPDATE NO ACTION,\n" +
                        "  CONSTRAINT `fk_comment_user`\n" +
                        "    FOREIGN KEY (`user`)\n" +
                        "    REFERENCES `fff`.`user` (`id`)\n" +
                        "    ON DELETE CASCADE\n" +
                        "    ON UPDATE NO ACTION);\n");

                connection.commit();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load H2 driver.");
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}
