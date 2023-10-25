/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager.model;

/**
 *
 * @author tranp
 */
public class Order {

    private String name;
    private String Day;
    private String Employee;
    private String Demand;
    private String status;
    private int IdOrder;

    public int getIdOrder() {
        return IdOrder;
    }

    public void setIdOrder(int IdOrder) {
        this.IdOrder = IdOrder;
    }
    private String Email;
    private Integer Quality;
    private String Price;

    public Order(String name, Integer Quality, String Price) {
        this.name = name;
        this.Quality = Quality;
        this.Price = Price;
    }

    public Integer getQuality() {
        return Quality;
    }

    public void setQuality(Integer Quality) {
        this.Quality = Quality;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String Price) {
        this.Price = Price;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    private boolean Detail;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDay() {
        return Day;
    }

    public void setDay(String Day) {
        this.Day = Day;
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

    public boolean isDetail() {
        return Detail;
    }

    public void setDetail(boolean Detail) {
        this.Detail = Detail;
    }

    public Order(String name, String Day, String Employee, String status,String Email,int id_order) {
        this.name = name;
        this.Day = Day;
        this.Employee = Employee;
        this.status = status;
        this.Detail=true;
        this.Email=Email;
        this.IdOrder=id_order;
    }

}
