package com.mgteam.sale_call_center_employee.model;

import org.bson.types.ObjectId;

public class Order {
    private ObjectId _id;
    private boolean Product;
    private int id_order;

    public int getId_order() {
        return id_order;
    }

    public void setId_order(int id_order) {
        this.id_order = id_order;
    }

    public boolean isProduct() {
        return Product;
    }

    public void setProduct(boolean Product) {
        this.Product = Product;
    }
    private ObjectId id_Customer; 
    private ObjectId id_Employee;
    private String Order_date;
    private String NameCustomer;
    private String NameEmployee;

    public String getNameCustomer() {
        return NameCustomer;
    }

    public void setNameCustomer(String NameCustomer) {
        this.NameCustomer = NameCustomer;
    }

    public String getNameEmployee() {
        return NameEmployee;
    }

    public void setNameEmployee(String NameEmployee) {
        this.NameEmployee = NameEmployee;
    }
    private String Ship_date;

    public Order() {
    }

    public Order(ObjectId _id, ObjectId id_Customer, ObjectId id_Employee, String Order_date, String Ship_date, String Status, Object DetailOrder,String NameCustomer,String NameEmployee,int id_order) {
        this._id = _id;
        this.id_Customer = id_Customer;
        this.id_Employee = id_Employee;
        this.Order_date = Order_date;
        this.Ship_date = Ship_date;
        this.Status = Status;
        this.DetailOrder = DetailOrder;
        this.NameCustomer=NameCustomer;
        this.NameEmployee=NameEmployee;
        this.id_order=id_order;
        this.Product=true;
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

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public Object getDetailOrder() {
        return DetailOrder;
    }

    public void setDetailOrder(Object DetailOrder) {
        this.DetailOrder = DetailOrder;
    }
    private String Status;
    private Object DetailOrder;
}
