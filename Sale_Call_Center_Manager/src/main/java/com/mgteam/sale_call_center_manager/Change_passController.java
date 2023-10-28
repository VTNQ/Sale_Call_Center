/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.connect.DBconnect;
import com.mgteam.sale_call_center_manager.connect.MD5;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 * FXML Controller class
 *
 * @author tranp
 */
public class Change_passController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private PasswordField newpass;

    @FXML
    private PasswordField oldpass;

    @FXML
    private PasswordField renewpass;

    @FXML
    void change(ActionEvent event) {
        if (!oldpass.getText().isEmpty() && !newpass.getText().isEmpty() && !renewpass.getText().isEmpty()) {
            if (newpass.getText().equals(renewpass.getText()) && !oldpass.getText().equals(newpass.getText())) {
                if (newpass.getText().length() >= 8 && renewpass.getText().length() >= 8) {
                        MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Admin");
                        Bson filter = Filters.and(Filters.eq("Password", MD5.encryPassword(oldpass.getText())), Filters.eq("Username", LoginController.userName));
                        Document result = collection.find(filter).first();  
                        if (result != null) {

                            Bson ft = Filters.eq("Username", LoginController.userName);
                            Bson Update = Updates.set("Password", MD5.encryPassword(renewpass.getText()));
                            UpdateResult updates = collection.updateOne(ft, Update);
                            if (updates.getModifiedCount() > 0) {
                                Alert.DialogSuccess("Change Password Successfully");
                            }

                        } else {
                            Alert.Dialogerror("Old is not correct");
                        }
                  
                }else{
                    Alert.Dialogerror("Password must be above 8 characters");
                }

            } else if(!newpass.getText().equals(renewpass.getText())) {
                Alert.Dialogerror("New password and renew password is match");
            }else if(oldpass.getText().equals(newpass.getText())){
                Alert.Dialogerror("Old password and new password is different");
            }

        } else if (oldpass.getText().isEmpty() && !newpass.getText().isEmpty() && !renewpass.getText().isEmpty()) {
            Alert.Dialogerror("Old password is required");
        } else if (!oldpass.getText().isEmpty() && newpass.getText().isEmpty() && !renewpass.getText().isEmpty()) {
            Alert.Dialogerror("new Password is required");
        } else if (!oldpass.getText().isEmpty() && !newpass.getText().isEmpty() && renewpass.getText().isEmpty()) {
            Alert.Dialogerror("Re_new Password is required");
        } else if (!oldpass.getText().isEmpty() && newpass.getText().isEmpty() && renewpass.getText().isEmpty()) {
            Alert.Dialogerror("New password And re_new Password is required");
        } else if (oldpass.getText().isEmpty() && !newpass.getText().isEmpty() && renewpass.getText().isEmpty()) {
            Alert.Dialogerror("Old password And Re_new Password is required");
        } else if (oldpass.getText().isEmpty() && newpass.getText().isEmpty() && !renewpass.getText().isEmpty()) {
            Alert.Dialogerror("old password And New Password is required");
        } else if (oldpass.getText().isEmpty() && newpass.getText().isEmpty() && renewpass.getText().isEmpty()) {
            Alert.Dialogerror("Old password and new Password  and RE_new Password");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

}
