/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager.model;

/**
 *
 * @author tranp
 */
public class Customer {

    private String Name;
    private String startDate;
    private String EndDate;
    private String Employee;
    private String Demand;
    private int id_order;

    public Customer(String Employee, String Demand, int id_order) {
        this.Employee = Employee;
        this.Demand = Demand;
        this.id_order = id_order;
    }

    public String getEmployee() {
        return Employee;
    }

    public void setEmployee(String Employee) {
        this.Employee = Employee;
    }

    public String getDemand() {
        return Demand;
    }

    public void setDemand(String Demand) {
        this.Demand = Demand;
    }

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }
    private boolean order;

    public boolean isOrder() {
        return order;
    }

    public void setOrder(boolean order) {
        this.order = order;
    }

    public Customer(String Name, String startDate, String EndDate) {
        this.Name = Name;
        this.startDate = startDate;
        this.EndDate = EndDate;
        this.order = true;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String EndDate) {
        this.EndDate = EndDate;
    }
}
