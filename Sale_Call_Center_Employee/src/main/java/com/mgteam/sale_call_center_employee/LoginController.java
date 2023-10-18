package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.MD5;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

public class LoginController {
    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField UsernameField;

    @FXML
    void login(ActionEvent event) {
        if(UsernameField.getText().isEmpty()&&PasswordField.getText().isEmpty()){
            UsernameField.getStyleClass().add("text_field_error");
            UsernameField.applyCss();
            PasswordField.getStyleClass().add("text_field_error");
            PasswordField.applyCss();
            DialogAlert.DialogError("Username and Password is require");
        }else if(UsernameField.getText().isEmpty()){
            UsernameField.getStyleClass().add("text_field_error");
            UsernameField.applyCss();
            DialogAlert.DialogError("Username is require");
        }else if(PasswordField.getText().isEmpty()){
            PasswordField.getStyleClass().add("text_field_error");
            PasswordField.applyCss();
            DialogAlert.DialogError("Password is require");
        }else if(!UsernameField.getText().isEmpty()&&!PasswordField.getText().isEmpty()){
            MongoCollection<Document>empCollection =DBConnection.getConnection().getCollection("Employee");
            Bson filter = Filters.and(Filters.eq("Username", MD5.Md5(UsernameField.getText())),Filters.eq("Password",MD5.Md5(PasswordField.getText())));
            MongoIterable<Document> results=empCollection.find(filter);
            boolean isFound=false;
            for (Document result : results) {
                isFound=true;
            }
            if(isFound==true){
                try {
                    DialogAlert.DialogSuccess("Login Success");
                    App.setRoot("Main");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }else{
                DialogAlert.DialogError("Account not exist");
            }
        }
    }
    @FXML
    void forgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader=new FXMLLoader(App.class.getResource("view/ForgetPassword.fxml"));
            AnchorPane anchorPane=loader.load();
            Stage stage =new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(anchorPane,400,300));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
