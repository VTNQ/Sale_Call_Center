/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager.model;

/**
 *
 * @author tranp
 */
public class Iventory {
    private int id_Eventory;
    private String product;
    private int Qualiry;
    private String Date;
    private String price;

    public int getId_Eventory() {
        return id_Eventory;
    }

    public void setId_Eventory(int id_Eventory) {
        this.id_Eventory = id_Eventory;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getQualiry() {
        return Qualiry;
    }

    public void setQualiry(int Qualiry) {
        this.Qualiry = Qualiry;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String Date) {
        this.Date = Date;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Iventory(int id_Eventory, String product, int Qualiry, String Date, String price) {
        this.id_Eventory = id_Eventory;
        this.product = product;
        this.Qualiry = Qualiry;
        this.Date = Date;
        this.price = price;
    }
    
}
