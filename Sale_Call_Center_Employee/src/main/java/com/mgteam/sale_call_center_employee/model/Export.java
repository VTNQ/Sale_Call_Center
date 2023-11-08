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
public class Export {
private int order;
private String Customer;
private String Employee;
private boolean Product;
private String NameCategory;
private int Quality;
private String price;
private ObjectId idEmployee;
private ObjectId idOrder;
private ObjectId idWarehouse;
private int status;
private ObjectId idProduct;

    public ObjectId getIdProduct() {
        return idProduct;
    }

    public void setIdProduct(ObjectId idProduct) {
        this.idProduct = idProduct;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public ObjectId getIdWarehouse() {
        return idWarehouse;
    }

    public void setIdWarehouse(ObjectId idWarehouse) {
        this.idWarehouse = idWarehouse;
    }

    public ObjectId getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(ObjectId idOrder) {
        this.idOrder = idOrder;
    }

    public ObjectId getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(ObjectId idEmployee) {
        this.idEmployee = idEmployee;
    }
private String NameProduct;

    public Export(String NameCategory, int Quality, String price, String NameProduct) {
        this.NameCategory = NameCategory;
        this.Quality = Quality;
        this.price = price;
        this.NameProduct = NameProduct;
    }

    public String getNameCategory() {
        return NameCategory;
    }

    public void setNameCategory(String NameCategory) {
        this.NameCategory = NameCategory;
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

    public String getNameProduct() {
        return NameProduct;
    }

    public void setNameProduct(String NameProduct) {
        this.NameProduct = NameProduct;
    }

    public Export(int order, String Customer, String Employee,ObjectId idEmployee,ObjectId idorder,int status) {
        this.order = order;
        this.Customer = Customer;
        this.Employee = Employee;
        this.Product=true;
        this.idEmployee=idEmployee;
        this.idOrder=idorder;
  
        this.status=status;
       

    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCustomer() {
        return Customer;
    }

    public void setCustomer(String Customer) {
        this.Customer = Customer;
    }

    public String getEmployee() {
        return Employee;
    }

    public void setEmployee(String Employee) {
        this.Employee = Employee;
    }

    public boolean isProduct() {
        return Product;
    }

    public void setProduct(boolean Product) {
        this.Product = Product;
    }

}
