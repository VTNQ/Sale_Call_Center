package com.mgteam.sale_call_center_admin;

import com.mgteam.sale_call_center_admin.connect.DBConnect;
import com.mgteam.sale_call_center_admin.connect.MD5;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;
import org.bson.conversions.Bson;

public class LoginController {

    @FXML
    private PasswordField field_pass;
    
    @FXML
    private TextField field_user;

    @FXML
    void submit(ActionEvent event) {
        if (!field_user.getText().isEmpty() && !field_pass.getText().isEmpty()) {
            MongoCollection<Document> collection = DBConnect.getdatabase().getCollection("Admin");
            Bson filter = Filters.and(
                    Filters.eq("Username", MD5.encryPassword(field_user.getText())),
                    Filters.eq("Password", MD5.encryPassword(field_pass.getText()))
            );
            
            Document result = collection.find(filter).first();
            if (result != null) {
                Alert.DialogSuccess("Login successfully");
                try {
                    App.setRoot("Main");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert.Dialogerror("Login failed");
            }            
        }else if(!field_user.getText().isEmpty() && field_pass.getText().isEmpty()){
            Alert.Dialogerror("Password is required");
        }else if(field_user.getText().isEmpty() && !field_pass.getText().isEmpty()){
            Alert.Dialogerror("Username is required");
        }else if(field_user.getText().isEmpty() && field_pass.getText().isEmpty()){
            Alert.Dialogerror("Username and Password is required");
        }
        
    }    
}
