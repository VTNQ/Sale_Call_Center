package com.mgteam.sale_call_center_employee.model;

import org.bson.types.ObjectId;

public class Customer {
    private ObjectId _id;
    private String Name;
    private int Age;
    private String Phone;
    private String Address;
    private int id_customer;
    private String nameEmployee;
    private boolean Product;
    private String startDay;
    private String nearDay;

    public String getStartDay() {
        return startDay;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public String getNearDay() {
        return nearDay;
    }

    public void setNearDay(String nearDay) {
        this.nearDay = nearDay;
    }

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }

    public String getNameEmployee() {
        return nameEmployee;
    }

    public void setNameEmployee(String nameEmployee) {
        this.nameEmployee = nameEmployee;
    }

    public boolean isProduct() {
        return Product;
    }

    public void setProduct(boolean Product) {
        this.Product = Product;
    }

    public Customer() {
    }

    public Customer(ObjectId _id, String Name, int Age, String Phone, String Address,String nameEmployee,String startDay,String nearDay,int id_customer) {
        this._id = _id;
        this.Name = Name;
        this.Age = Age;
        this.Phone = Phone;
        this.Address = Address;
        this.nameEmployee=nameEmployee;
        this.id_customer=id_customer;
        this.startDay=startDay;
        this.nearDay=nearDay;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String Name) {
        this.Name = Name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int Age) {
        this.Age = Age;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String Phone) {
        this.Phone = Phone;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }
}
