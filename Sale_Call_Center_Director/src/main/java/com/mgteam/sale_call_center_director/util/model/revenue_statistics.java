/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_director.util.model;

/**
 *
 * @author tranp
 */
public class revenue_statistics {

    private String product;
    private String category;
    private int Quality;
    private String price;
    private String Employee;

    public revenue_statistics(int Quality, String price, String Employee) {
        this.Quality = Quality;
        this.price = price;
        this.Employee = Employee;
    }

    public String getEmployee() {
        return Employee;
    }

    public void setEmployee(String Employee) {
        this.Employee = Employee;
    }

    public revenue_statistics( String product, String category, int Quality, String price) {
        this.product = product;
        this.category = category;
        this.Quality = Quality;
        this.price = price;
    }

    
    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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
}
