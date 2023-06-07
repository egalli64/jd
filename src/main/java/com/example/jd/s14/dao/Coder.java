/*
 * Java / RDBMS integration by JDBC
 * 
 * https://github.com/egalli64/jd
 */
package com.example.jd.s14.dao;

import java.time.LocalDate;

public class Coder {
    private long id;
    private String firstName;
    private String lastName;
    private int phone;
    private LocalDate hired;
    private double salary;

    public Coder() {
    }

    public Coder(String firstName, String lastName, int phone, double salary) {
        this(firstName, lastName, phone, LocalDate.now(), salary);
    }

    public Coder(String firstName, String lastName, int phone, LocalDate hired, double salary) {
        this(0, firstName, lastName, phone, LocalDate.now(), salary);
    }

    public Coder(long id, String firstName, String lastName, int phone, LocalDate hired, double salary) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.hired = hired;
        this.salary = salary;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getHired() {
        return hired;
    }

    public void setHired(LocalDate hired) {
        this.hired = hired;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Coder [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phone=" + phone
                + ", hired=" + hired + ", salary=" + salary + "]";
    }
}