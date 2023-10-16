package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.MD5;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.bson.Document;
import org.bson.conversions.Bson;

public class LoginController {
    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField UsernameField;

    @FXML
    void login(ActionEvent event) {
        if(!UsernameField.getText().isEmpty()&&!PasswordField.getText().isEmpty()){
            MongoCollection<Document>empCollection =DBConnection.getConnection().getCollection("Employee");
            Bson filter = Filters.and(Filters.eq("Username", MD5.Md5(UsernameField.getText())),Filters.eq("Password",MD5.Md5(PasswordField.getText())));
            empCollection.find(filter);
        }
    }
}
