package com.mgteam.sale_call_center_admin;

import com.mgteam.sale_call_center_admin.connect.DBConnect;
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
            MongoCollection<Document>collection=DBConnect.getdatabase().getCollection("admin");
            String username=encryPassword(field_user.getText());
            String password=encryPassword(field_pass.getText());
        Bson filter=Filters.and(Filters.eq("Username",username),Filters.eq(""));
    }
    public static String encryPassword(String input){
        try {
            String base64Encode=Base64.getEncoder().encodeToString(input.getBytes());
            MessageDigest md=MessageDigest.getInstance("MD5");
            byte[]md5Byte=md.digest(base64Encode.getBytes());
            StringBuilder sb=new StringBuilder();
            for (byte b : md5Byte) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
