/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager.model;

/**
 *
 * @author tranp
 */
public class Manager {
    private String name;
    private String Email;
    private String Since;
    private String phone;
    private String status;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getSince() {
        return Since;
    }

    public void setSince(String Since) {
        this.Since = Since;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Manager(String name, String Email, String Since, String phone, String status,String username) {
        this.name = name;
        this.Email = Email;
        this.Since = Since;
        this.phone = phone;
        this.status = status;
        this.username=username;
    }
    
}
