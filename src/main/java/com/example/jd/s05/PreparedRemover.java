/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s05;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jd.Config;

/**
 * PreparedStatement example
 */
public class PreparedRemover {
    private static final Logger log = LoggerFactory.getLogger(PreparedRemover.class);
    private static final String DELETE_SERVICE_BY_NAME = """
            DELETE FROM service
            WHERE name = ? and location_id = ?""";

    /**
     * Remove the service whose name and location is passed by the user
     * 
     * @param args the required info
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Pass me a service name and its location!");
            return;
        }

        // TODO: check for "strange" characters against SQL injection attack
        String name = args[0];
        // TODO: robustness, try for NumberFormatException
        int locationId = Integer.valueOf(args[1]);

        log.info("Deleting service named <{}>, location <{}>, if exists", name, locationId);

        DataSource ds = Config.getDataSource();
        try (Connection conn = ds.getConnection();
                PreparedStatement stmt = conn.prepareStatement(DELETE_SERVICE_BY_NAME)) {
            stmt.setString(1, name);
            stmt.setInt(2, locationId);
            int lines = stmt.executeUpdate();
            System.out.printf("Delete executed, %d lines affected%n", lines);
        } catch (SQLException se) {
            log.error("Can't remove", se);
        }
    }
}
