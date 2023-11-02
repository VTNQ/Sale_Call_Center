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
public class Import {

    private String Employee;
    private String Date;
    private boolean productpopup;
    private ObjectId IdWarehouse;
    private String NameProduct;
    private String NameCategory;
    private String price;
    private ObjectId idEmployee;
    private int Quality;
    private ObjectId idincoming;
    private String supply;

    public String getSupply() {
        return supply;
    }

    public void setSupply(String supply) {
        this.supply = supply;
    }

    public ObjectId getIdincoming() {
        return idincoming;
    }

    public void setIdincoming(ObjectId idincoming) {
        this.idincoming = idincoming;
    }
    public ObjectId getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(ObjectId idEmployee) {
        this.idEmployee = idEmployee;
    }

    public int getQuality() {
        return Quality;
    }

    public void setQuality(int Quality) {
        this.Quality = Quality;
    }

    public Import(String NameProduct, String NameCategory, String price,int Quality) {
        this.NameProduct = NameProduct;
        this.NameCategory = NameCategory;
        this.price = price;
        this.Quality=Quality;
    }

    public String getNameProduct() {
        return NameProduct;
    }

    public void setNameProduct(String NameProduct) {
        this.NameProduct = NameProduct;
    }

    public String getNameCategory() {
        return NameCategory;
    }

    public void setNameCategory(String NameCategory) {
        this.NameCategory = NameCategory;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ObjectId getIdWarehouse() {
        return IdWarehouse;
    }

    public void setIdWarehouse(ObjectId IdWarehouse) {
        this.IdWarehouse = IdWarehouse;
    }
    private int status;

    public String getEmployee() {
        return Employee;
    }

    public void setEmployee(String Employee) {
        this.Employee = Employee;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public boolean isProductpopup() {
        return productpopup;
    }

    public void setProductpopup(boolean productpopup) {
        this.productpopup = productpopup;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Import(String Employee, String Date, int status, ObjectId idWarehouse,ObjectId idEmployee,ObjectId idincoming,String supply) {
        this.Employee = Employee;
        this.Date = Date;
        this.status = status;
        this.productpopup = true;
        this.IdWarehouse = idWarehouse;
        this.idEmployee=idEmployee;
        this.idincoming=idincoming;
        this.supply=supply;
    }
}
