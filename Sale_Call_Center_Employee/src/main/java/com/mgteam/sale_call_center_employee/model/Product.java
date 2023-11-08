package com.mgteam.sale_call_center_employee.model;

import org.bson.types.ObjectId;

public class Product {
    private ObjectId _id;
    private String Name;
    private ObjectId id_category;
    private Integer Price;
    private String customer;

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
    private int Quality;
    private int Id_product;
    private String priceformat;
    private boolean isactive;

    public boolean isIsactive() {
        return isactive;
    }

    public void setIsactive(boolean isactive) {
        this.isactive = isactive;
    }
    public String getPriceformat() {
        return priceformat;
    }

    public void setPriceformat(String priceformat) {
        this.priceformat = priceformat;
    }
    private String category;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Product(ObjectId id,String Name, String priceformat, String category) {
        this._id=id;
        this.Name = Name;
        this.priceformat = priceformat;
        this.category = category;
        this.isactive=true;
    }
    

    public Product(String Name, int Quality, int Id_product,int Price) {
        this.Name = Name;
        this.Quality = Quality;
        this.Id_product = Id_product;
        this.Price=Price;
    }

    public int getQuality() {
        return Quality;
    }

    public void setQuality(int Quality) {
        this.Quality = Quality;
    }

    public int getId_product() {
        return Id_product;
    }

    public void setId_product(int Id_product) {
        this.Id_product = Id_product;
    }

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
