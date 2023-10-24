package com.mgteam.sale_call_center_employee.model;

import org.bson.types.ObjectId;

public class Order {
    private ObjectId _id;
    private ObjectId id_Customer; 
    private ObjectId id_Employee;
    private String Order_date;
    private String Ship_date;

    public Order() {
    }

    public Order(ObjectId _id, ObjectId id_Customer, ObjectId id_Employee, String Order_date, String Ship_date, Integer status, Object DetailOrder) {
        this._id = _id;
        this.id_Customer = id_Customer;
        this.id_Employee = id_Employee;
        this.Order_date = Order_date;
        this.Ship_date = Ship_date;
        this.status = status;
        this.DetailOrder = DetailOrder;
    }
    
    

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public ObjectId getId_Customer() {
        return id_Customer;
    }

    public void setId_Customer(ObjectId id_Customer) {
        this.id_Customer = id_Customer;
    }

    public ObjectId getId_Employee() {
        return id_Employee;
    }

    public void setId_Employee(ObjectId id_Employee) {
        this.id_Employee = id_Employee;
    }

    public String getOrder_date() {
        return Order_date;
    }

    public void setOrder_date(String Order_date) {
        this.Order_date = Order_date;
    }

    public String getShip_date() {
        return Ship_date;
    }

    public void setShip_date(String Ship_date) {
        this.Ship_date = Ship_date;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Object getDetailOrder() {
        return DetailOrder;
    }

    public void setDetailOrder(Object DetailOrder) {
        this.DetailOrder = DetailOrder;
    }
    private Integer status;
    private Object DetailOrder;
}
