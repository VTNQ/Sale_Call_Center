/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_admin.connect;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 *
 * @author tranp
 */
public class DBConnect {

    public static MongoDatabase getdatabase() {

        MongoClient mongoclient = MongoClients.create(new ConnectionString("mongodb+srv://Phong:Phong%2322072003@cluster3.hqsa1h3.mongodb.net/"));
        MongoDatabase database = mongoclient.getDatabase("Sale_Call_Center");

        return database;
    }
}
