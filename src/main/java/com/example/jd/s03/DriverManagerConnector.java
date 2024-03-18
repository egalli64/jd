/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s03;

import static com.example.jd.Config.PASSWORD;
import static com.example.jd.Config.URL;
import static com.example.jd.Config.USER;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Hello JDBC by driver manager
 */
public class DriverManagerConnector {
    private static final Logger log = LoggerFactory.getLogger(DriverManagerConnector.class);

    /**
     * Connect by driver manager, get and log DB info then terminate
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Connecting ... (legacy)");
        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD)) {
            DatabaseMetaData dmd = conn.getMetaData();

            String db = dmd.getDatabaseProductName();
            String version = dmd.getDatabaseProductVersion();

            String catalog = conn.getCatalog();
            String schema = conn.getSchema();

            System.out.printf("Connected to %s version %s, catalog %s, schema %s\n", db, version, catalog, schema);
        } catch (SQLException e) {
            log.error("Can't get database info", e);
            System.out.println("Can't get database info");
        }
        System.out.println("... done");
    }
}
