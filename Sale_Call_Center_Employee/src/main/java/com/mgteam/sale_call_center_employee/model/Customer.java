package com.mgteam.sale_call_center_employee.model;

import org.bson.types.ObjectId;

public class Customer {
    private ObjectId _id;
    private String Name;
    private String Age;
    private String Phone;
    private String Address;
    private int id_customer;

    public int getId_customer() {
        return id_customer;
    }

    public void setId_customer(int id_customer) {
        this.id_customer = id_customer;
    }
    
    public Customer() {
    }

    public Customer(ObjectId _id, String Name, String Age, String Phone,String Address,int id_customer) {
        this._id = _id;
        this.Name = Name;
        this.Age = Age;
        this.Phone = Phone;
        this.Address=Address;
        this.id_customer=id_customer;
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

    public String getAge() {
        return Age;
    }

    public void setAge(String Age) {
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
