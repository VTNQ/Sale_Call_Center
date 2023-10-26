package com.mgteam.sale_call_center_employee.model;

import org.bson.types.ObjectId;

public class Product {
    private ObjectId _id;
    private String Name;
    private ObjectId id_category;
    private Integer Price;

    public Product() {
    }

    public Product(ObjectId _id, String Name, ObjectId id_category, Integer Price) {
        this._id = _id;
        this.Name = Name;
        this.id_category = id_category;
        this.Price = Price;
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

    public ObjectId getId_category() {
        return id_category;
    }

    public void setId_category(ObjectId id_category) {
        this.id_category = id_category;
    }

    public Integer getPrice() {
        return Price;
    }

    public void setPrice(Integer Price) {
        this.Price = Price;
    }
    
}
