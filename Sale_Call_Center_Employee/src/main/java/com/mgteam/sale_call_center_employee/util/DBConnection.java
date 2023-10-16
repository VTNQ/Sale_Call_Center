package com.mgteam.sale_call_center_employee.util;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class DBConnection {
    public static MongoDatabase getConnection(){
        MongoClient mongoClient=MongoClients.create("mongodb+srv://VTNQ:Quoc%2313052004@cluster3.hqsa1h3.mongodb.net/");
        MongoDatabase database=mongoClient.getDatabase("Sale_Call_Center");
        return database;
    }
}
