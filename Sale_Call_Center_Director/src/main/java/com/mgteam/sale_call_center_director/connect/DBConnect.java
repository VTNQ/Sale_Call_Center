/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_director.connect;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author tranp
 */
public class DBConnect {
     public static MongoDatabase getConnection(){
        MongoClient mongoClient=MongoClients.create("mongodb+srv://VTNQ:Quoc%2313052004@cluster3.hqsa1h3.mongodb.net/");
        MongoDatabase database=mongoClient.getDatabase("Sale_Call_Center");
        return database;
    }
}
