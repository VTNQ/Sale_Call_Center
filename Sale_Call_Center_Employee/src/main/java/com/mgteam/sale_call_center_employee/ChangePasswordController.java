package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.MD5;
import com.mgteam.sale_call_center_employee.util.Regax;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import io.github.palexdev.materialfx.controls.MFXPasswordField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ChangePasswordController {

    @FXML
    private MFXPasswordField newPassword;

    @FXML
    private MFXPasswordField oldPassword;

    @FXML
    private MFXPasswordField rePassword;

    @FXML
    void changePassword(ActionEvent event) {
        MongoCollection<Document> EmployeeCollection = DBConnection.getConnection().getCollection("Employee");
        FindIterable<Document> result = EmployeeCollection.find(Filters.eq("_id", LoginController.id_employee));
        for (Document employee : result) {
            if (oldPassword.getText().isEmpty() && newPassword.getText().isEmpty() && rePassword.getText().isEmpty()) {
                DialogAlert.DialogError("Old Password, New Password, Re-ented Password is require");
            } else if (oldPassword.getText().isEmpty() && newPassword.getText().isEmpty()) {
                DialogAlert.DialogError("Old Password and New Password is require");
            } else if (oldPassword.getText().isEmpty() && rePassword.getText().isEmpty()) {
                DialogAlert.DialogError("Old Password and Re-ented Password is require");
            } else if (newPassword.getText().isEmpty() && rePassword.getText().isEmpty()) {
                DialogAlert.DialogError("New Password and Re-ented Password is require");
            } else if (oldPassword.getText().isEmpty()) {
                DialogAlert.DialogError("Old Password is require");
            } else if (newPassword.getText().isEmpty()) {
                DialogAlert.DialogError("New Password is require");
            }else if(rePassword.getText().isEmpty()){
                DialogAlert.DialogError("Re-ented Password is require");
            } else if (!MD5.Md5(oldPassword.getText()).equals(employee.getString("Password")) && newPassword.getText().isEmpty() && rePassword.getText().isEmpty()) {
                DialogAlert.DialogError("Old  Password is Incorrect and New Password, Re-ented Password is require");
            } else if (!MD5.Md5(oldPassword.getText()).equals(employee.getString("Password")) && newPassword.getText().isEmpty()) {
                DialogAlert.DialogError("Old  Password is Incorrect and New Password is require");
            } else if (!MD5.Md5(oldPassword.getText()).equals(employee.getString("Password")) && rePassword.getText().isEmpty()) {
                DialogAlert.DialogError("Old  Password is Incorrect and Re-ented Password is require");
            } else if(!MD5.Md5(oldPassword.getText()).equals(employee.getString("Password")) && !newPassword.getText().equals(rePassword.getText())){
                DialogAlert.DialogError("old Password is Incorrect and New Password not match with Re-ented Password");
            }else if(!MD5.Md5(oldPassword.getText()).equals(employee.getString("Password"))){
                DialogAlert.DialogError("Old Password is Incorrect");
            }else if(!newPassword.getText().equals(rePassword.getText())){
                DialogAlert.DialogError("New Password and Re-ented Password not match");
            }else{
                if(Regax.isValidPassword(newPassword.getText())==false&&Regax.isValidPassword(rePassword.getText())==false&&newPassword.getText().length()>=8&&rePassword.getText().length()>=8){
                    DialogAlert.DialogError("PPassword must be over 8 characters and have at least 1 lowercase, uppercase and number");
                }else{
                    Bson Find=Filters.eq("_id",LoginController.id_employee);
                    Bson Update=Updates.set("Password", MD5.Md5(newPassword.getText()));
                    UpdateResult updateResult=EmployeeCollection.updateOne(Find, Update);
                    if(updateResult.getModifiedCount()>0){
                        DialogAlert.DialogSuccess("Change Password is Correct");
                        oldPassword.setText("");
                        newPassword.setText("");
                        rePassword.setText("");
                    }
                }
            }
        }

    }

}
