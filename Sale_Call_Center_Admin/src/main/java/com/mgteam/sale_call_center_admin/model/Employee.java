/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_admin.model;

/**
 *
 * @author tranp
 */
public class Employee {
private String name;
private String Email;
private String since;
private String position;
private String isReset;

    public String isIsReset() {
        return isReset;
    }

    public void setIsReset(String isReset) {
        this.isReset = isReset;
    }
    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
private String phone;
private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public Employee(String name, String Email, String since, String phone,String username,String position,String isReset) {
        this.name = name;
        this.Email = Email;
        this.since = since;
        this.phone = phone;
        this.username=username;
        this.position=position;
       this.isReset=isReset;
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
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
