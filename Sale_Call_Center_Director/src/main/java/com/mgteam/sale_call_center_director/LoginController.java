package com.mgteam.sale_call_center_director;

import com.mgteam.sale_call_center_director.connect.DBConnect;
import com.mgteam.sale_call_center_director.util.Dialog;
import com.mgteam.sale_call_center_director.util.MD5;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class LoginController {

    @FXML
    private PasswordField Password;

    @FXML
    private TextField Username;
     public static ObjectId id_employee;
    public static String username;
    public static String name;
    public static String email;
    public static String phone;
    public static String since;
     @FXML
  void Login(ActionEvent event) {
        if (Username.getText().isEmpty() && Password.getText().isEmpty()) {
          
            Dialog.Dialogerror("Username and Password is require");
        } else if (Username.getText().isEmpty()) {
             Dialog.Dialogerror("Username is require");
        } else if (Password.getText().isEmpty()) {
             Dialog.Dialogerror("Password is require");
        } else if (!Username.getText().isEmpty() && !Password.getText().isEmpty()) {
            MongoCollection<Document> empCollection = DBConnect.getConnection().getCollection("Employee");
            Bson filter = Filters.and(Filters.eq("Username", MD5.Md5(Username.getText())), Filters.eq("Password", MD5.Md5(Password.getText())));
            MongoIterable<Document> results = empCollection.find(filter);
            boolean isFound = false;
            for (Document result : results) {
                isFound = true;
                try {
                    if (result.getInteger("usertype") == 3) {
                        Dialog.DialogSuccess("Login Success");
                        id_employee = result.getObjectId("_id");
                        username = Username.getText();
                        name = result.getString("Name");
                        email = result.getString("Email");
                        phone = result.getString("Phone");
                        since = result.getString("Since");
                        App.setRoot("secondary");
                    }
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (isFound == false) {
                Dialog.Dialogerror("Account not exist Or Username/Password Incorrect");
            }
        }
    }
}
