package s09;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import static jd.Config.*;

public class Selector {
    private static final Logger log = LogManager.getLogger(Selector.class);
    private static final String SELECT_NAMES = "SELECT first_name FROM coders ORDER BY 1";
    private static final String SELECT_CODERS = "SELECT first_name, last_name, salary FROM coders ORDER BY 1";
    private static final String SELECT_CODERS_BY_SALARY_DOUBLE = "SELECT first_name, last_name, salary FROM coders WHERE salary >= %d ORDER BY 3 DESC";
    private static final String SELECT_CODERS_BY_SALARY_STRING = "SELECT first_name, last_name, salary FROM coders WHERE salary >= %s ORDER BY 3 DESC";

    public List<String> getCoderNames() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SELECT_NAMES)) {
            List<String> results = new ArrayList<>();
            while (rs.next()) {
                String current = rs.getString(1);
                results.add(current);
            }

            return results;
        }
    }

    public List<Coder> getCoders() throws SQLException {
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(SELECT_CODERS)) {
            List<Coder> results = new ArrayList<>();
            while (rs.next()) {
                Coder coder = new Coder(rs.getString(1), rs.getString(2), rs.getInt(3));
                results.add(coder);
            }

            return results;
        }
    }

    public List<Coder> getCodersBySalary(int lower) throws SQLException {
        String query = String.format(SELECT_CODERS_BY_SALARY_DOUBLE, lower);
        log.debug(query);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            List<Coder> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new Coder(rs.getString(1), rs.getString(2), rs.getInt(3)));
            }

            return results;
        }
    }

    /*
     * DANGER! SQL Injection!
     */
    public List<Coder> getCodersBySalary(String lower) throws SQLException {
        String query = String.format(SELECT_CODERS_BY_SALARY_STRING, lower);
        log.debug(query);

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {
            List<Coder> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new Coder(rs.getString(1), rs.getString(2), rs.getInt(3)));
            }

            return results;
        }
    }

    public static List<Coder> getCodersHiredBefore(LocalDate limit) throws SQLException {
        final String query = "SELECT first_name, last_name, salary FROM coders WHERE hire_date < ? ORDER BY hire_date";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                PreparedStatement prepStmt = conn.prepareStatement(query)) {
            prepStmt.setDate(1, Date.valueOf(limit));

            // sadly, PreparedStatement.toString() not implemented in Oracle JDBC
            log.debug(prepStmt.toString());
            List<Coder> results = new ArrayList<>();

            try (ResultSet rs = prepStmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new Coder(rs.getString(1), rs.getString(2), rs.getInt(3)));
                }
            }

            return results;
        }
    }

    public List<Coder2> getCodersWithLetterIn(char letter) throws SQLException {
        final String sql = "SELECT first_name, last_name, salary " + //
                "FROM coders " + //
                "WHERE first_name LIKE '%%%c%%' or last_name LIKE '%%%c%%'";
        String query = String.format(sql, letter, letter);
        log.debug(query.toString());

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query)) {

            List<Coder2> results = new ArrayList<>();
            while (rs.next()) {
                results.add(new Coder2(rs.getString(1), rs.getString(2), rs.getDouble(3)));
            }

            return results;
        }
    }
}