package doan;
import java.sql.Connection;
import java.sql.DriverManager;

public class test {

	public static void main(String[] args) {
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String url = "jdbc:sqlserver://localhost\\SQLEXPRESS:1433;databaseName=SQL;encrypt=true;trustServerCertificate=true";
			String userName ="sa";
			String password ="123456789";
			Connection connection = DriverManager.getConnection(url, userName, password);
			System.out.println("kết nối thành công");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

}
