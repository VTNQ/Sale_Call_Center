/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee.model;

import org.bson.types.ObjectId;



/**
 *
 * @author tranp
 */
public class Warehouse {
    private ObjectId idwarehouse;
    private ObjectId idProduct;
    private ObjectId idsupply;
    
    public ObjectId getIdsupply() {
        return idsupply;
    }

    public void setIdsupply(ObjectId idsupply) {
        this.idsupply = idsupply;
    }
    public ObjectId getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(ObjectId idProduct) {
        this.idProduct = idProduct;
    }

    public ObjectId getIdwarehouse() {
        return idwarehouse;
    }

    public void setIdwarehouse(ObjectId idwarehouse) {
        this.idwarehouse = idwarehouse;
    }
    private int id;
    private String Date;
    private boolean product;
    private String status;
    private String suppliers;
    private String nameProduct;
    private int Quality;
    private String price;
    private String category;
    public Warehouse(){
        
    }
    public Warehouse(ObjectId id,String nameProduct, int Quality, String price, String category,ObjectId idproduct) {
        this.nameProduct = nameProduct;
        this.Quality = Quality;
        this.price = price;
        this.category = category;
        this.idProduct=idproduct;
        this.idwarehouse=id;
    }


    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }

    public int getQuality() {
        return Quality;
    }

    public void setQuality(int Quality) {
        this.Quality = Quality;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Warehouse(int id, String Date, String status, String suppliers,ObjectId idwarehouse) {
        this.id = id;
        this.Date = Date;
        this.product = true;
        this.status = status;
        this.suppliers = suppliers;
        this.idwarehouse=idwarehouse;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public boolean isProduct() {
        return product;
    }

    public void setProduct(boolean product) {
        this.product = product;
    }

   

    public String getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(String suppliers) {
        this.suppliers = suppliers;
    }
}
