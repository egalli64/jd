/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s06;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jd.Config;

/**
 * CallableStatement example
 */
public class StoredProcedure {
    private static final Logger log = LoggerFactory.getLogger(StoredProcedure.class);

    private static final String GET_EMPLOYEE_SALARY = "{call get_employee_salary(?, ?)}";
    private static final String GET_SALARY_FUNCTION = "{? = call get_salary(?)}";

    private DataSource ds;

    public StoredProcedure() {
        this.ds = Config.getDataSource();
    }

    /**
     * Employee salary
     * 
     * @param id the employee id
     * @return employee salary, 0 if the specified employee id is not available
     * @throws SQLException in case of problems
     */
    public double getEmployeeSalary(int id) throws SQLException {
        try (Connection conn = ds.getConnection(); //
                CallableStatement cs = conn.prepareCall(GET_EMPLOYEE_SALARY)) {
            cs.setInt(1, id);
            cs.registerOutParameter(2, Types.DECIMAL);

            log.debug("Before execute: {}", cs);
            cs.executeUpdate();
            log.debug("After execute: {}", cs);

            double result = cs.getDouble(2);
            log.debug("Result is: {}", cs);

            return result;
        }
    }

    /**
     * Employee salary
     *
     * @param id the employee id
     * @return employee salary, 0 if the specified employee id is not available
     * @throws SQLException in case of problems
     */
    public double getSalaryByFunction(int id) throws SQLException {
        try (Connection conn = ds.getConnection(); //
                CallableStatement cs = conn.prepareCall(GET_SALARY_FUNCTION)) {
            log.debug("Prepared: " + cs.toString());

            cs.registerOutParameter(1, Types.DECIMAL);
            cs.setInt(2, id);

            log.debug("Before execute: {}", cs);
            cs.executeUpdate();
            log.debug("After execute: {}", cs);

            double result = cs.getBigDecimal(1).doubleValue();
            log.debug("Result is: {}", result);

            return result;
        }
    }
}
