/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s14;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jd.s14.dao.Coder;
import com.example.jd.s14.dao.CoderDao;

/**
 * Sample application using DAO
 */
public class ClassicMain {
    private static final Logger log = LoggerFactory.getLogger(ClassicMain.class);

    public static void main(String[] args) {
        CoderDao cd = new CoderDao();

        // create a new coder ...
        int phone = 99_999;
        Coder tom = new Coder("Tom", "Jones", phone, 2_000);
        cd.save(tom);

        // ... then get it
        Coder fiveNineCoder = cd.legacyGetByPhone(phone);
        if (fiveNineCoder == null) {
            log.error("Unexpected! Can't get the coder with phone " + phone);
            System.out.println("Coder has not been saved correctly!");
            return;
        } else {
            System.out.println("Get coder by phone: " + fiveNineCoder);
        }

        // update coder salary
        fiveNineCoder.setSalary(fiveNineCoder.getSalary() * 2);
        cd.update(fiveNineCoder);
        System.out.println("Update salary: " + fiveNineCoder);

        // rename the coder
        fiveNineCoder.setLastName("Hollz");
        cd.update(fiveNineCoder);
        System.out.println("Update renamed: " + fiveNineCoder);

        // delete the coder
        long id = fiveNineCoder.getId();
        cd.delete(id);

        // ensure the coder is actually removed from the database
        fiveNineCoder = cd.legacyGet(id);
        if (fiveNineCoder == null) {
            System.out.printf("Coder %d correctly removed%n", id);
        } else {
            System.out.println("Unexpected! Coder is still alive: " + fiveNineCoder);
        }

        System.out.println("All coders");
        List<Coder> coders = cd.getAll();
        for (Coder coder : coders) {
            System.out.println(coder);
        }
    }
}
