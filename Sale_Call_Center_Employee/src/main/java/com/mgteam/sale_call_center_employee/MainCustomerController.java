package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.model.Customer;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import io.github.palexdev.materialfx.controls.MFXPagination;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MainCustomerController extends MainController implements Initializable {

    @FXML
    private TableColumn<?, ?> IdCustomer;

    @FXML
    private TableColumn<?, ?> ListProduct;

    @FXML
    private TableColumn<?, ?> NameCustomer;

    @FXML
    private TableColumn<?, ?> NameEmployee;

    @FXML
    private TableColumn<?, ?> NearBuy;

    @FXML
    private TableColumn<?, ?> StartBuy;

    @FXML
    private MFXPagination pagination;

    @FXML
    private TableView<?> tblOrder;

    @FXML
    private MFXTextField txtSearch;

    @FXML
    private MFXTextField address;

    @FXML
    private MFXDatePicker age;

    @FXML
    private MFXTextField name;

    @FXML
    private MFXTextField phone;

    @FXML
    void Search(ActionEvent event) {

    }

    @FXML
    void addCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/AddCustomer.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(anchorPane, 600, 400));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void add(ActionEvent event) {
        if (!name.getText().isEmpty() && age.getValue() != null && !phone.getText().isEmpty() && !address.getText().isEmpty()) {
            int YearOfNow=LocalDate.now().getYear();
            int YearOfSince=age.getValue().getYear();
            MongoCollection<Document> Customers = DBConnection.getConnection().getCollection("Customer");
            Customers.insertOne(new Document("Name", name.getText()).append("Age",YearOfSince-YearOfSince).append("Phone", phone.getText()).append("Address", address.getText()));
            DialogAlert.DialogSuccess("Add Customer Success");
        }else{
            DialogAlert.DialogError("Please enter all!");
        }
    }
    
    private static List<Customer>listCustomer(){
        List<Customer>ArrayCustomer=new ArrayList<>();
        try{
            MongoCollection<Document>listCustomer=DBConnection.getConnection().getCollection("Customer");
            MongoCollection<Document>listEmployee=DBConnection.getConnection().getCollection("Employee");
            MongoCollection<Document>listOrder=DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document>listProduct=DBConnection.getConnection().getCollection("Product");
            FindIterable<Document>result=listCustomer.find();
            for (Document customer : result) {
                int id_customer=customer.getObjectId("_id").hashCode();
                Document OrderAll=listOrder.find(Filters.eq("id_customer", customer.getObjectId("_id"))).first();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return ArrayCustomer;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }

}
