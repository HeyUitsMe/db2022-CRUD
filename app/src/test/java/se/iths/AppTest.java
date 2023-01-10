package se.iths;

import org.junit.jupiter.api.*;

import java.sql.*;

class AppTest {

    private static final String JDBC_CONNECTION = "jdbc:mysql://localhost:3306/iths";
    private static final String JDBC_USER = "iths";
    private static final String JDBC_PASSWORD = "iths";

    public static Connection con = null; //static connection

    @BeforeAll //
    public static void setUp() throws Exception {
        con = DriverManager.getConnection(JDBC_CONNECTION, JDBC_USER, JDBC_PASSWORD);
        con.createStatement().execute("DROP TABLE IF EXISTS User");
        con.createStatement().execute("CREATE TABLE User (ID INT NOT NULL AUTO_INCREMENT, NAME VARCHAR(255), ROLE VARCHAR(255), PRIMARY KEY (ID))");
    }

    @AfterAll
    public static void tearDown() throws Exception {
        con.close();
    }

    @Order(1)
    @Test
    void shouldCreateRowInDatabase() throws Exception {
        PreparedStatement stmt = con.prepareStatement("INSERT INTO User (NAME, ROLE) VALUES (?, ?)");
        stmt.setString(1, "Adam Adamsson");
        stmt.setString(2, "MASTER");
    }

    @Order(2)
    @Test
    void shouldFindRowInDatabase() throws Exception {
        con.createStatement().execute("DELETE FROM User");
        PreparedStatement stmt = con.prepareStatement("INSERT INTO User (NAME, ROLE) VALUES (?, ?)");
        stmt.setString(1, "Adam Adamsson");
        stmt.setString(2, "MASTER");
        stmt.execute();
        stmt.close();

        stmt = con.prepareStatement("SELECT * FROM User");
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getLong(1));
            System.out.println(rs.getString(2));
        }
    }

    @Order(3)
    @Test
    void shouldUpdateRowInDatabase() throws Exception {
        con.createStatement().execute("DELETE FROM User");
        PreparedStatement stmt = con.prepareStatement("INSERT INTO User (NAME, ROLE) VALUES (?, ?)");
        stmt.setString(1, "Adam Adamsson");
        stmt.setString(2, "MASTER");
        stmt.execute();
        stmt.close();

        stmt = con.prepareStatement("SELECT * FROM User");
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            long id = rs.getLong(1);
            System.out.println("ID ÄR " + id);
            stmt = con.prepareStatement("UPDATE User Set Name = ? WHERE ID = ?");
            stmt.setString(1, "Adam Adamsson");
            stmt.setLong(2, id);
            stmt.execute();
        }
        stmt = con.prepareStatement("SELECT * FROM User");
        rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println(rs.getLong(1));
            System.out.println(rs.getString(2));
        }
    }

    @Order(4)
    @Test
    void shouldDeleteRowInDatabase() throws Exception {
        con.createStatement().execute("DELETE FROM User");
        PreparedStatement stmt = con.prepareStatement("INSERT INTO User (NAME, ROLE) VALUES (?, ?)");
        stmt.setString(1, "To Be Deleted");
        stmt.setString(2, "MASTER");
        stmt.execute();
        stmt.close();

        stmt = con.prepareStatement("SELECT * FROM User");
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            long id = rs.getLong(1);
            System.out.println("ID ÄR " + id);
            stmt = con.prepareStatement("DELETE FROM User WHERE ID = ?");
            stmt.setLong(1, id);
        }
    }
}