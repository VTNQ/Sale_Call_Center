package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;

public class ProfileController implements Initializable {

    @FXML
    private TextField email=new TextField();

    @FXML
    private TextField name=new TextField();

    @FXML
    private TextField phone=new TextField();

    @FXML
    private TextField since=new TextField();

    @FXML
    private TextField username=new TextField();
    
    @FXML
    private DatePicker since_edit=new DatePicker();

    @FXML
    void Edit(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/EditProfile.fxml"));
        AnchorPane newpopup;
        try {
            newpopup = loader.load();
            Stage popupStage = new Stage();
            ProfileController profile=loader.getController();
            profile.since_edit.setValue(LocalDate.parse(since.getText(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(newpopup));
            popupStage.setResizable(false);
            popupStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
    
    @FXML
    void Update(ActionEvent event) {
        if(name.getText().isEmpty()&&email.getText().isEmpty()&&phone.getText().isEmpty()&&since_edit.getValue()==null){
            DialogAlert.DialogError("Name is require");
        }else if(email.getText().isEmpty()&&phone.getText().isEmpty()){
            DialogAlert.DialogError("Email and Phone is require");
        }else if(email.getText().isEmpty()&&name.getText().isEmpty()){
            DialogAlert.DialogError("Email and Name is require");
        }else if(email.getText().isEmpty()&&since_edit.getValue()==null){
            DialogAlert.DialogError("Email and Since is require");
        }else if(phone.getText().isEmpty()&&name.getText().isEmpty()){
            DialogAlert.DialogError("Phone and Name is require");
        }else if(phone.getText().isEmpty()&&since_edit.getValue()==null){
            DialogAlert.DialogError("Phone and Since is require");
        }else if(name.getText().isEmpty()&&since_edit.getValue()==null){
            DialogAlert.DialogError("Name and Since is require");
        }else{
            MongoCollection<Document> EmployeeCollection = DBConnection.getConnection().getCollection("Employee");
            Bson Find=Filters.eq("_id",LoginController.id_employee);
            Bson UpdateName=Updates.set("Name", name.getText());
            Bson UpdateEmail=Updates.set("Email", email.getText());
            Bson UpdatePhone=Updates.set("Phone", phone.getText());
            Bson updateSince=Updates.set("Since", since_edit.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            UpdateResult updateResult1=EmployeeCollection.updateOne(Find,UpdateName);
            UpdateResult updateResult2=EmployeeCollection.updateOne(Find, UpdateEmail);
            UpdateResult updateResult3=EmployeeCollection.updateOne(Find, UpdatePhone);
            UpdateResult updateResult4=EmployeeCollection.updateOne(Find, updateSince);
            FindIterable<Document>listEmployee=EmployeeCollection.find(Find);
            for (Document Employee : listEmployee) {
                 LoginController.name=Employee.get("Name").toString();
                 LoginController.email=Employee.get("Email").toString();
                 LoginController.phone=Employee.get("Phone").toString();
                 LoginController.since=Employee.get("Since").toString();
            }
            DialogAlert.DialogSuccess("Update Success");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setText(LoginController.username);
        name.setText(LoginController.name);
        email.setText(LoginController.email);
        phone.setText(LoginController.phone);
        since.setText(LoginController.since);
    }

}
