package org.example;

import java.sql.Date;
//import com.sun.net.httpserver.HttpServer;

public class Employee {
    private int empNo;
    private String firstName;
    private String lastName;
    private String gender;
    private Date hireDate;




    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getEmpNo() {
        return empNo;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmpNo(int empNo) {
        this.empNo = empNo;
    }

    public Employee() {
    }
}
