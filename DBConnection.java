package doan;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class DBConnection {

    public static Connection getConnection() {
        try {
            Properties props = new Properties();

            FileInputStream fis = new FileInputStream(
                "C:/Users/LENOVO/eclipse-workspace/oop/db.properties"
            );
            props.load(fis);

            return DriverManager.getConnection(
                props.getProperty("db.url"),
                props.getProperty("db.username"),
                props.getProperty("db.password")
            );

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}