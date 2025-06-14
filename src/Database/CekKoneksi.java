package Database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CekKoneksi {
    public static void main(String[] args) {
        // Ganti sesuai database kamu
        String URL = "jdbc:mysql://localhost:3306/perpustakaan"; // nama DB
        String USER = "root"; // user phpMyAdmin
        String PASSWORD = ""; // password MySQL (kosong kalau default XAMPP)

        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Koneksi berhasil ke database!");
            conn.close();
        } catch (SQLException e) {
            System.out.println("❌ Gagal koneksi: " + e.getMessage());
        }
    }
}
