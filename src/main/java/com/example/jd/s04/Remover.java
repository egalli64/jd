/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s04;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jd.Config;

/**
 * A raw hron service remover
 */
public class Remover {
    private static final Logger log = LoggerFactory.getLogger(Remover.class);
    private static final String DELETE_SERVICE_BY_NAME = """
            DELETE FROM service
            WHERE name = '%s'""";

    /**
     * Remove the service whose name is passed by the user
     * 
     * @param args the service to be removed should be the first passed argument
     */
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Pass me a service name!");
            return;
        }

        log.info("Deleting service named {}, if exists", args[0]);

        DataSource ds = Config.getDataSource();
        try (Connection conn = ds.getConnection(); //
                Statement stmt = conn.createStatement()) {
            // !!! DANGER - POSSIBLE SQL INJECTION ATTACK !!!
            // ex: arg -> "Tom' OR name like 'A%"
            String sql = String.format(DELETE_SERVICE_BY_NAME, args[0]);
            int lines = stmt.executeUpdate(sql);
            System.out.printf("Delete executed, %d lines affected%n", lines);
        } catch (SQLException se) {
            log.error("Can't remove", se);
        }
    }
}
