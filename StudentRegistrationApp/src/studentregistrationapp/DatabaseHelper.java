package studentregistrationapp;
import java.sql.*;
import java.time.LocalDate;
/**
 *
 * @author anita
 */
public class DatabaseHelper {
    
private Connection conn;

    public DatabaseHelper() {

        try {
            conn = DriverManager.getConnection("jdbc:sqlite:students.db");

            Statement st = conn.createStatement();
            st.execute("CREATE TABLE IF NOT EXISTS students(" +
                    "id TEXT, first TEXT, last TEXT, gender TEXT," +
                    "dept TEXT, dob TEXT, email TEXT)");
            
            try {
    Class.forName("org.sqlite.JDBC");
} catch (ClassNotFoundException e) {
    System.out.println("SQLite JDBC driver not found: " + e.getMessage());
}

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void insertStudent(Student s) {

        try {
            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO students VALUES(?,?,?,?,?,?,?)");

            ps.setString(1, s.getId());
            ps.setString(2, s.getFirst());
            ps.setString(3, s.getLast());
            ps.setString(4, s.getGender());
            ps.setString(5, s.getDept());
            ps.setString(6, s.getDob());
            ps.setString(7, s.getEmail());
            ps.executeUpdate();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public String generateID() {

        int year = LocalDate.now().getYear();
        int count = 1;

        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT COUNT(*) FROM students WHERE id LIKE '"+year+"-%'");

            if(rs.next())
                count = rs.getInt(1) + 1;

        } catch(Exception e){}

        return year + "-" + String.format("%05d", count);
    }
}
