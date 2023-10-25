package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.bson.Document;


public class MainController implements Initializable{

    @FXML
    private TextField username;
    
    @FXML
    private AnchorPane MainDisplay;
    
    @FXML
    private Label TotalCustomer;
    
    @FXML
    private Label TotalOrder;
    


    @FXML
    void changePassword(ActionEvent event) {

    }

    @FXML
    void customer(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/MainCustomer"));
        try {
            AnchorPane HomeCustomer=loader.load();
            MainDisplay.getChildren().clear();
            MainDisplay.getChildren().setAll(HomeCustomer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void home(ActionEvent event) {
        try {
            App.setRoot("SalePerson");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent event) {
        
    }

    @FXML
    void order(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("MainOrder.fxml"));
        try {
            AnchorPane HomeOrder=loader.load();
            MainDisplay.getChildren().clear();
            MainDisplay.getChildren().setAll(HomeOrder);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void report(ActionEvent event) {

    }

    private int TotalCustomer(){
        int count=0;
        MongoCollection<Document>totalCustomer=DBConnection.getConnection().getCollection("Customer");
        MongoIterable<Document>result=totalCustomer.find();
        for (Document document : result) {
            count++;
        }
        return count;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TotalCustomer.setText(String.valueOf(TotalCustomer()));
    }
    
}
