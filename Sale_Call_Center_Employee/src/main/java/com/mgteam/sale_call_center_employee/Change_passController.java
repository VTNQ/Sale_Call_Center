/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.MD5;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author tranp
 */
public class Change_passController {
      @FXML
    private PasswordField newpass;

    @FXML
    private PasswordField oldpass;

    @FXML
    private PasswordField renewpass;

    @FXML
    void change(ActionEvent event) {
        if (!oldpass.getText().isEmpty() && !newpass.getText().isEmpty() && !renewpass.getText().isEmpty()) {
            MongoCollection<Document> collection = DBConnection.getConnection().getCollection("Employee");
            Bson filter = Filters.and(Filters.eq("Password", MD5.Md5(oldpass.getText())), Filters.eq("Username", LoginController.user));
            Document result = collection.find(filter).first();
            if (result != null) {
                if (newpass.getText().equals(renewpass.getText()) && !oldpass.getText().equals(newpass.getText())) {
                    if (newpass.getText().length() >= 8 && renewpass.getText().length() >= 8) {
                        Bson ft = Filters.eq("Username", LoginController.user);
                        Bson Update = Updates.set("Password", MD5.Md5(renewpass.getText()));
                        UpdateResult updates = collection.updateOne(ft, Update);

                        if (updates.getModifiedCount() > 0) {
                            DialogAlert.DialogSuccess("Change Password Successfully");
                            oldpass.setText("");
                            newpass.setText("");
                            renewpass.setText("");
                        }
                    } else {
                        DialogAlert.DialogError("Password must be above 8 characters");
                    }

                } else if (!newpass.getText().equals(renewpass.getText())) {
                     DialogAlert.DialogError("New password and renew password is match");
                } else if (oldpass.getText().equals(newpass.getText())) {
                    DialogAlert.DialogError("Old password and  password is different");
                }

            } else {
                 DialogAlert.DialogError("Old is not correct");
            }

        } else if (oldpass.getText().isEmpty() && !newpass.getText().isEmpty() && !renewpass.getText().isEmpty()) {
             DialogAlert.DialogError("Old password is required");
        } else if (!oldpass.getText().isEmpty() && newpass.getText().isEmpty() && !renewpass.getText().isEmpty()) {
             DialogAlert.DialogError("new Password is required");
        } else if (!oldpass.getText().isEmpty() && !newpass.getText().isEmpty() && renewpass.getText().isEmpty()) {
             DialogAlert.DialogError("Re_new Password is required");
        } else if (!oldpass.getText().isEmpty() && newpass.getText().isEmpty() && renewpass.getText().isEmpty()) {
             DialogAlert.DialogError("New password And re_new Password is required");
        } else if (oldpass.getText().isEmpty() && !newpass.getText().isEmpty() && renewpass.getText().isEmpty()) {
           DialogAlert.DialogError("Old password And Re_new Password is required");
        } else if (oldpass.getText().isEmpty() && newpass.getText().isEmpty() && !renewpass.getText().isEmpty()) {
             DialogAlert.DialogError("old password And New Password is required");
        } else if (oldpass.getText().isEmpty() && newpass.getText().isEmpty() && renewpass.getText().isEmpty()) {
             DialogAlert.DialogError("Old password and new Password  and RE_new Password is required");
        }

    }
}
