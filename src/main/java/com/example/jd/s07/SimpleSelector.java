/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s07;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jd.Config;

/**
 * ResultSet example
 */
public class SimpleSelector {
    private static final Logger log = LoggerFactory.getLogger(SimpleSelector.class);

    private static final String GET_CODERS = """
            SELECT e.employee_id, e.first_name, e.last_name
            FROM employee e JOIN department d
            USING (department_id)
            WHERE d.name = 'IT'""";

    /**
     * Run the query, print the data in the received result set
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        DataSource ds = Config.getDataSource();

        try (Connection conn = ds.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(GET_CODERS)) {
            log.debug("Looping on the result set");
            System.out.printf("%4s %20s %20s\n", "id", "first", "last");

            while (rs.next()) {
                int id = rs.getInt(1);
                String first = rs.getString(2);
                String last = rs.getString(3);

                System.out.printf("%4d %20s %20s\n", id, first, last);
            }
            log.debug("Done");
        } catch (SQLException se) {
            log.error("Can't get coders", se);
        }
    }
}
