/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s14;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jd.s14.dao.Coder;
import com.example.jd.s14.dao.CoderDao;

/**
 * Sample application using DAO
 */
public class Main {
    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        CoderDao dao = new CoderDao();

        // create a new coder ...
        Coder tom = new Coder("Tom", "Jones", 99_999, 2_000);
        dao.save(tom);

        Optional<Coder> fiveNinesCoder = dao.getByPhone(tom.getPhone());

        fiveNinesCoder.ifPresentOrElse(coder -> {
            System.out.println("Get: " + coder);

            // update coder last name and salary
            coder.setLastName("Hollz");
            coder.setSalary(coder.getSalary() * 2);
            dao.update(coder);
            System.out.println("Updating coder: " + coder);

            System.out.print("Coder get: ");
            dao.get(coder.getId()).ifPresent(System.out::println);

            long id = coder.getId();
            dao.delete(id);

            // ensure the coder is actually removed from the database
            dao.get(id).ifPresentOrElse(c -> log.error("Unexpected! Coder is still alive: " + c),
                    () -> System.out.printf("Coder %d correctly removed%n", id));
        }, () -> {
            log.error("Unexpected! Can't get coder by phone: ", tom.getPhone());
            System.out.println("No coder to work with!");
        });

        System.out.println("All coders");
        dao.getAll().forEach(System.out::println);
    }
}
