/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_admin.connect.util;

import com.mgteam.sale_call_center_admin.connect.DBConnect;
import com.mgteam.sale_call_center_admin.model.Employee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.bson.Document;

/**
 *
 * @author tranp
 */
public class daodb {
    public static List<Employee> getAccoutEmployee(){
        List<Employee>employees=new ArrayList<>();
        MongoCollection<Document> collection=DBConnect.getdatabase().getCollection("Employee");
        MongoCursor<Document>cursor=collection.find().iterator();
        try {
            while(cursor.hasNext()){
                Document document=cursor.next();
                String name=document.getString("name");
                String phone=document.getString("Phone");
               String sinceString=document.getString("Since");
               LocalDate sinceDate=LocalDate.parse(sinceString,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
               String formattedSince=sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
               String username=document.getString("Username");
               String email=document.getString("Email");
               employees.add(new Employee(name, email, formattedSince, phone,username));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return employees;
    }
}
