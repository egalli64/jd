/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s14.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CoderDao implements Dao<Coder> {
    private static final Logger log = LoggerFactory.getLogger(CoderDao.class);

    private static final String GET_BY_PK = """
            SELECT employee_id, first_name, last_name, phone, hired, salary
            FROM employee
            WHERE department_id = 6 AND employee_id = ?""";
    private static final String GET_BY_PHONE = """
            SELECT employee_id, first_name, last_name, phone, hired, salary
            FROM employee
            WHERE department_id = 6 AND phone = ?""";
    private static final String GET_ALL = """
            SELECT employee_id, first_name, last_name, phone, hired, salary
            FROM employee
            WHERE department_id = 6""";
    private static final String INSERT = """
            INSERT INTO employee (first_name, last_name, phone, hired, job_id, salary, manager_id, department_id) VALUES
                (?, ?, ?, ?, 15, ?, 103, 6)""";
    private static final String UPDATE_BY_ID = """
            UPDATE employee
            SET first_name = ?, last_name = ?, phone = ?, hired = ?, salary = ?
            WHERE department_id = 6 AND employee_id = ?""";
    private static final String DELETE = """
            DELETE FROM employee
            WHERE department_id = 6 AND employee_id = ?""";

    @Override
    public List<Coder> getAll() {
        List<Coder> results = new ArrayList<>();

        try (Connection conn = Connector.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(GET_ALL)) {
            while (rs.next()) {
                Coder coder = new Coder(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                        rs.getObject(5, LocalDate.class), rs.getDouble(6));
                results.add(coder);
            }
        } catch (SQLException se) {
            log.error("Can't get all coders", se);
        }

        return results;
    }

    @Override
    public Optional<Coder> get(long id) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(GET_BY_PK)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coder my = new Coder(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getObject(5, LocalDate.class), rs.getDouble(6));
                    return Optional.of(my);
                }
            }
        } catch (SQLException se) {
            log.error("Can't get coder " + id, se);
        }

        return Optional.empty();
    }

    public Optional<Coder> getByPhone(int phone) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(GET_BY_PHONE)) {
            ps.setLong(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Coder my = new Coder(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getObject(5, LocalDate.class), rs.getDouble(6));
                    return Optional.of(my);
                }
            }
        } catch (SQLException se) {
            log.error("Can't get coder with phone" + phone, se);
        }

        return Optional.empty();
    }

    public Coder legacyGet(long id) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(GET_BY_PK)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Coder(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getObject(5, LocalDate.class), rs.getDouble(6));
                }
            }
        } catch (SQLException se) {
            log.error("Can't get coder " + id, se);
        }

        return null;
    }

    public Coder legacyGetByPhone(int phone) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(GET_BY_PHONE)) {
            ps.setLong(1, phone);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Coder(rs.getLong(1), rs.getString(2), rs.getString(3), rs.getInt(4),
                            rs.getObject(5, LocalDate.class), rs.getDouble(6));
                }
            }
        } catch (SQLException se) {
            log.error("Can't get coder with phone" + phone, se);
        }

        return null;
    }

    @Override
    public void save(Coder coder) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(INSERT)) {
            ps.setString(1, coder.getFirstName());
            ps.setString(2, coder.getLastName());
            ps.setInt(3, coder.getPhone());
            ps.setObject(4, coder.getHired());
            ps.setDouble(5, coder.getSalary());
            ps.executeUpdate();
        } catch (SQLException se) {
            log.error("Can't save coder " + coder.getId(), se);
        }
    }

    @Override
    public void update(Coder coder) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(UPDATE_BY_ID)) {
            ps.setString(1, coder.getFirstName());
            ps.setString(2, coder.getLastName());
            ps.setInt(3, coder.getPhone());
            ps.setObject(4, coder.getHired());
            ps.setDouble(5, coder.getSalary());
            ps.setLong(6, coder.getId());
            int count = ps.executeUpdate();
            if (count != 1) {
                log.warn("Updated " + count + " lines for " + coder);
            }
        } catch (SQLException se) {
            log.error("Can't update coder " + coder.getId(), se);
        }
    }

    @Override
    public void delete(long id) {
        try (Connection conn = Connector.getConnection(); //
                PreparedStatement ps = conn.prepareStatement(DELETE)) {
            ps.setLong(1, id);
            int count = ps.executeUpdate();
            if (count != 1) {
                log.warn("Deleted " + count + " lines for " + id);
            }
        } catch (SQLException se) {
            log.error("Can't delete coder " + id, se);
        }
    }
}
