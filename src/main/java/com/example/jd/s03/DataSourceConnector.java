/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s03;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jd.Config;

/**
 * A Hello JDBC by data source
 */
public class DataSourceConnector {
    private static final Logger log = LoggerFactory.getLogger(DataSourceConnector.class);

    /**
     * Connect to the current data source, get and log DB info then terminate
     * 
     * @param args not used
     */
    public static void main(String[] args) {
        log.trace("Connecting ...");
        DataSource ds = Config.getDataSource();

        try (Connection conn = ds.getConnection()) {
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
