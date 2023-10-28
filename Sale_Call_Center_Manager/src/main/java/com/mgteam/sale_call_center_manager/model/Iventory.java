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
    private int product;
    private int Qualiry;
    private String Date;
    private String name_product;

    public String getName_product() {
        return name_product;
    }

    public void setName_product(String name_product) {
        this.name_product = name_product;
    }
    private String price;

    public int getId_Eventory() {
        return id_Eventory;
    }

    public void setId_Eventory(int id_Eventory) {
        this.id_Eventory = id_Eventory;
    }

    public int getProduct() {
        return product;
    }

    public void setProduct(int product) {
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

    public Iventory(int id_Eventory, int product, int Qualiry, String Date, String price,String name_product) {
        this.id_Eventory = id_Eventory;
        this.product = product;
        this.Qualiry = Qualiry;
        this.Date = Date;
        this.price = price;
        this.name_product=name_product;
    }
    
}
