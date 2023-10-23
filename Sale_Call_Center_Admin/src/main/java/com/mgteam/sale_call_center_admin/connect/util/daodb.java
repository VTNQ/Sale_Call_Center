/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_admin.connect.util;

import com.mgteam.sale_call_center_admin.connect.DBConnect;
import com.mgteam.sale_call_center_admin.model.Employee;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import static com.mongodb.client.model.Filters.eq;
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
    public static List<Employee>getAcccountwithKey(String key){
          List<Employee>emp=new ArrayList<>();
        try {
          
            MongoCollection<Document>collection=DBConnect.getdatabase().getCollection("Admin");
            MongoCollection<Document> requestCollection = DBConnect.getdatabase().getCollection("Request");
            Document query=new Document("Name",new Document("$regex",".*"+key+".*"));
            FindIterable<Document>cursor=collection.find(query);
            for (Document document : cursor) {
                int usertype = document.getInteger("Usertype");
                

            if ((usertype == 2) || (usertype == 3 )) {
                String name = document.getString("Name");
                String phone = document.getString("Phone");
                String sinceString = document.getString("Since");
                LocalDate sinceDate = LocalDate.parse(sinceString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String username = document.getString("Username");
                String email = document.getString("Email");
                Document requestDocument = requestCollection.find(eq("EmailEmployee", email)).first();
                int  status = (requestDocument != null) ? requestDocument.getInteger("status") : 1;
                String value=(requestDocument!=null)?String.valueOf(status):"";
                String position = (usertype == 2) ? "Manager" : "Director";
                emp.add(new Employee(name, email, formattedSince, phone, username, position,value));
            }
            }
              
        } catch (Exception e) {
            e.printStackTrace();
            
        }
      return emp;
    }
   public static List<Employee> getAccountEmployee() {
    List<Employee> employees = new ArrayList<>();
    MongoCollection<Document> collection = DBConnect.getdatabase().getCollection("Admin");
    MongoCollection<Document> requestCollection = DBConnect.getdatabase().getCollection("Request");
    MongoCursor<Document> cursor = collection.find().iterator();
    
    try {
        while (cursor.hasNext()) {
            Document document = cursor.next();

            int usertype = document.getInteger("Usertype");
           

            if ((usertype == 2) || (usertype == 3 )) {
                String name = document.getString("Name");
                String phone = document.getString("Phone");
                String sinceString = document.getString("Since");
                LocalDate sinceDate = LocalDate.parse(sinceString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                String username = document.getString("Username");
                String email = document.getString("Email");
                
               
                Document requestDocument = requestCollection.find(eq("EmailEmployee", email)).first();
                int  status = (requestDocument != null) ? requestDocument.getInteger("status") : 1;
                String value=(requestDocument!=null)?String.valueOf(status):"";
                String position = (usertype == 2) ? "Manager" : "Director";
                employees.add(new Employee(name, email, formattedSince, phone, username, position,value));
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return employees;
}
}
