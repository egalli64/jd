/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s10;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.time.LocalDate;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jd.Config;

/**
 * Commit, rollback, savepoint
 */
public class Transactor {
    private static final Logger log = LoggerFactory.getLogger(Transactor.class);

    private static final String GET_ALL_CODERS = """
            SELECT employee_id, first_name, last_name, hired
            FROM employee e JOIN department d USING (department_id)
            WHERE d.name = 'IT'""";
    private static final String INSERT_CODER = """
            INSERT INTO employee (first_name, last_name, phone, hired, job_id, salary, manager_id, department_id) VALUES
                (?, ?, ?, ?, 15, ?, 103, 6)""";
    private static final String UPDATE_EMPLOYEE_NAME = """
            UPDATE employee
            SET first_name = ?
            WHERE first_name = ? AND last_name = ?""";

    private DataSource ds;

    public Transactor() {
        this.ds = Config.getDataSource();
    }

    public static void main(String[] args) {
        Transactor transactor = new Transactor();

        transactor.insertCoder("Jim", "Steward", 9999, 7000.0);
        transactor.insertUpdateCoder("John", "Watson", 9998, 6500.0);
        transactor.sloppyUpdate("Valli", "Pataballa");

        try (Connection connection = transactor.ds.getConnection()) {
            transactor.logAllCoders(connection);
        } catch (Exception se) {
            log.error("DBMS error", se);
        }

    }

    public void logAllCoders(Connection conn) throws SQLException {
        StringBuilder sb = new StringBuilder("[");

        try (Statement stmt = conn.createStatement(); //
                ResultSet rs = stmt.executeQuery(GET_ALL_CODERS)) {
            while (rs.next()) {
                sb.append("(").append(rs.getInt("employee_id")).append(": "); // 1
                sb.append(rs.getString("first_name")).append(" "); // 2
                sb.append(rs.getString("last_name")).append(" "); // 3
                sb.append(rs.getDate("hired")).append(")"); // 4
            }
        } finally {
            sb.append("]");
            log.debug(sb.toString());
        }
    }

    public void insertCoder(String first, String last, int phone, double salary) {
        try (Connection conn = ds.getConnection()) {
            conn.setAutoCommit(false);

            try {
                System.out.println("Inserting new coder ...");
                doInsert(conn, first, last, phone, salary);

                doSomethingDangerous();
                conn.commit();
                System.out.println("New coder inserted");
            } catch (Exception ex) {
                log.error("Can't insert new coder", ex);
                System.out.println("Rollback");
                conn.rollback();
                logAllCoders(conn);
                System.out.println("No coder inserted");
            }
        } catch (Exception se) {
            log.error("DBMS error", se);
        }
    }

    public void insertUpdateCoder(String first, String last, int phone, double salary) {
        try (Connection connection = ds.getConnection()) {
            connection.setAutoCommit(false);

            System.out.println("Inserting new coder ...");
            doInsert(connection, first, last, phone, salary);

            System.out.println("Setting savepoint");
            Savepoint savepoint = connection.setSavepoint();

            System.out.println("Updating coder ...");
            doUpdate(connection, first, last);

            try {
                doSomethingDangerous();
                connection.commit();
                System.out.println("Insert plus update committed");
            } catch (Exception ex) {
                log.error("Can't update coder", ex);
                System.out.println("Rollback update");
                connection.rollback(savepoint);
                System.out.println("Update rolled back");
                logAllCoders(connection);
            }

            System.out.println("Final rollback");
            connection.rollback();
            logAllCoders(connection);
        } catch (Exception se) {
            log.error("DBMS error", se);
        }
    }

    public void sloppyUpdate(String first, String last) {
        try (Connection connection = ds.getConnection()) {
            connection.setAutoCommit(false);

            System.out.println("Updating coder ...");
            doUpdate(connection, first, last);

            System.out.println("VERY BAD: no commit, implicit rollback!");
        } catch (Exception se) {
            log.error("DBMS error", se);
        }
    }

    private void doInsert(Connection connection, String first, String last, int phone, double salary) {
        try (PreparedStatement ps = connection.prepareStatement(INSERT_CODER)) {
            ps.setString(1, first);
            ps.setString(2, last);
            ps.setInt(3, phone);
            ps.setObject(4, LocalDate.now());
            ps.setDouble(5, salary);
            ps.executeUpdate();
            logAllCoders(connection);
        } catch (SQLException se) {
            throw new IllegalStateException(se);
        }
    }

    private void doUpdate(Connection connection, String first, String last) {
        try (PreparedStatement ps = connection.prepareStatement(UPDATE_EMPLOYEE_NAME)) {
            ps.setString(1, first + "ny");
            ps.setString(2, first);
            ps.setString(3, last);
            ps.executeUpdate();
            logAllCoders(connection);
        } catch (SQLException se) {
            throw new IllegalStateException(se);
        }
    }

    private void doSomethingDangerous() {
        throw new IllegalStateException("Something bad happened");
    }
}
