/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_director.util.model;

import org.bson.types.ObjectId;

/**
 *
 * @author tranp
 */
public class product {
private String product;
private String date;
private int order;
private String customer;
private String status;

    public product(String date, int order, String customer, String status) {
        this.date = date;
        this.order = order;
        this.customer = customer;
        this.status = status;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
private String Category;
private int totalproduct;
private int ordercount;
private boolean istatic;
private ObjectId idproduct;

    public ObjectId getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(ObjectId idproduct) {
        this.idproduct = idproduct;
    }
    public boolean isIstatic() {
        return istatic;
    }

    public void setIstatic(boolean istatic) {
        this.istatic = istatic;
    }

    public int getOrdercount() {
        return ordercount;
    }

    public void setOrdercount(int ordercount) {
        this.ordercount = ordercount;
    }
    public product(String product, String Category, int totalproduct,int ordercount,ObjectId idproduct) {
        this.product = product;
        this.Category = Category;
        this.totalproduct = totalproduct;
        this.ordercount=ordercount;
        this.istatic=true;
        this.idproduct=idproduct;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public int getTotalproduct() {
        return totalproduct;
    }

    public void setTotalproduct(int totalproduct) {
        this.totalproduct = totalproduct;
    }
}
