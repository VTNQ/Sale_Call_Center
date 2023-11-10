package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.model.Customer;
import com.mgteam.sale_call_center_employee.model.Order;
import com.mgteam.sale_call_center_employee.model.Warehouse;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import io.github.palexdev.materialfx.controls.MFXPagination;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

public class MainCustomerController extends MainController implements Initializable {
    @FXML
    private TableColumn<Customer, Boolean> colupdate=new TableColumn<>();
    @FXML
    private TableColumn<?, ?> AddressCustomer=new TableColumn<>();
    private ObjectId idcustomer=new ObjectId();
    @FXML
    private TableColumn<?, ?> AgeCustomer=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> IdCustomer=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> NameCustomer=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> PhoneCustomer=new TableColumn<>();

    @FXML
    private MFXPagination pagination=new MFXPagination();

    @FXML
    private TableView<Customer> tblCustomer=new TableView<>();

    @FXML
    private MFXTextField txtSearch=new MFXTextField();

    @FXML
    private MFXTextField address=new MFXTextField();

    @FXML
    private MFXDatePicker age=new MFXDatePicker();

    @FXML
    private TextField UpdateAddrees=new TextField();
    @FXML
    private MFXTextField name=new MFXTextField();

    @FXML
    private MFXTextField phone=new MFXTextField();

    @FXML
    void Search(ActionEvent event) {
        if (txtSearch.getText().isEmpty()) {
            ListCustomerView();
        } else {
            ListCustomerViewWithKey();
        }
    }
    
    @FXML
    void addCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/AddCustomer.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(anchorPane, 400, 300));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void add(ActionEvent event) {
        if (!name.getText().isEmpty() && age.getValue() != null && !phone.getText().isEmpty() && !address.getText().isEmpty()) {
            boolean isFound = false;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String YearOfSince = age.getValue().format(formatter);
            MongoCollection<Document> ListCustomer = DBConnection.getConnection().getCollection("Customer");
            MongoIterable<Document> iterable = ListCustomer.find(Filters.and(Filters.eq("Name", name.getText()), Filters.eq("Phone", phone.getText())));
            for (Document customer : iterable) {
                isFound = true;
                DialogAlert.DialogError("Customer Is Exist");
            }
            if (isFound == false) {
                MongoCollection<Document> Customers = DBConnection.getConnection().getCollection("Customer");
                Customers.insertOne(new Document("Name", name.getText()).append("Age", YearOfSince).append("Phone", phone.getText()).append("Address", address.getText()));
                DialogAlert.DialogSuccess("Add Customer Success");
            }
            ListCustomerView();
        } else {
            DialogAlert.DialogError("Please enter all!");
        }
    }

    private static List<Customer> listCustomer() {
        List<Customer> ArrayCustomer = new ArrayList<>();
        try {
            MongoCollection<Document> listCustomer = DBConnection.getConnection().getCollection("Customer");
            FindIterable<Document> result1 = listCustomer.find();
            for (Document customer : result1) {
                int id_customer = customer.getObjectId("_id").hashCode();
                String nameCustomer = customer.getString("Name");
                String ageCustomer = customer.getString("Age");
                String phoneCustomer = customer.getString("Phone");
                String addressCustomer = customer.getString("Address");
                int id_filter = Math.abs(id_customer);
                ArrayCustomer.add(new Customer(customer.getObjectId("_id"), nameCustomer, ageCustomer, phoneCustomer, addressCustomer, id_filter));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ArrayCustomer;
    }

    private static List<Customer> listCustomerWithKey(String Key) {
        List<Customer> ArrayCustomer = new ArrayList<>();
        try {
            MongoCollection<Document> listCustomer = DBConnection.getConnection().getCollection("Customer");
            FindIterable<Document> result1 = listCustomer.find();
            for (Document customer : result1) {
                int id_customer = customer.getObjectId("_id").hashCode();
                String nameCustomer = customer.getString("Name");
                String ageCustomer = customer.getString("Age");
                String phoneCustomer = customer.getString("Phone");
                String addressCustomer = customer.getString("Address");
                int id_filter = Math.abs(id_customer);
                Pattern regexPattern = Pattern.compile(".*" + Key + ".*", Pattern.CASE_INSENSITIVE);
                boolean isSimilar1 = regexPattern.matcher(String.valueOf(id_filter)).matches();
                boolean isSimilar2 = regexPattern.matcher(nameCustomer).matches();
                if (String.valueOf(id_customer).matches(Key) || nameCustomer.matches(Key) || isSimilar1 || isSimilar2) {
                        ArrayCustomer.add(new Customer(customer.getObjectId("_id"), nameCustomer, ageCustomer, phoneCustomer, addressCustomer, id_filter));
                    }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ArrayCustomer;
    }
  @FXML
    void update(ActionEvent event) {
        if(!UpdateAddrees.getText().isEmpty()){
            Document filter=new Document("_id",idcustomer);
            Document update = new Document("$set", new Document("Address", UpdateAddrees.getText()));
            MongoCollection<Document>Employee=DBConnection.getConnection().getCollection("Customer");
            UpdateResult updateresult=Employee.updateOne(filter, update);
            DialogAlert.DialogSuccess("Update success");
        }else{
            DialogAlert.DialogError("Address is required");
        }
    }
    private void ListCustomerView() {
        List<Customer> customers = listCustomer();
        ObservableList<Customer> observableList = FXCollections.observableArrayList(customers);
        tblCustomer.setItems(observableList);
        IdCustomer.setCellValueFactory(new PropertyValueFactory<>("id_customer"));
        NameCustomer.setCellValueFactory(new PropertyValueFactory<>("Name"));
        AgeCustomer.setCellValueFactory(new PropertyValueFactory<>("Age"));
        PhoneCustomer.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        AddressCustomer.setCellValueFactory(new PropertyValueFactory<>("Address"));
     colupdate.setCellFactory(column -> new TableCell<Customer, Boolean>() {
            private Button button = new Button("Update");

            {
                button.setOnAction(event -> {
                    Customer ware = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/updatecustomer.fxml"));
                        AnchorPane newpopup;
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        MainCustomerController list = loader.getController();
                        list.UpdateAddrees.setText(ware.getAddress());
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                         popupStage.setOnCloseRequest(closeEvent -> {
                ListCustomerView();
            });
                       list.idcustomer=ware.getId();
                       
                        popupStage.setResizable(false);
                        popupStage.show();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("btn-design");
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
        pagination.setCurrentPage(0);
        pagination.setMaxPage(customers.size());
    }
    
    private void ListCustomerViewWithKey() {
        List<Customer> customers = listCustomerWithKey(txtSearch.getText());
        ObservableList<Customer> observableList = FXCollections.observableArrayList(customers);
        tblCustomer.setItems(observableList);
        IdCustomer.setCellValueFactory(new PropertyValueFactory<>("id_Customer"));
        NameCustomer.setCellValueFactory(new PropertyValueFactory<>("Name"));
        AgeCustomer.setCellValueFactory(new PropertyValueFactory<>("Age"));
        PhoneCustomer.setCellValueFactory(new PropertyValueFactory<>("Phone"));
        AddressCustomer.setCellValueFactory(new PropertyValueFactory<>("Address"));
        pagination.setCurrentPage(0);
         colupdate.setCellFactory(column -> new TableCell<Customer, Boolean>() {
            private Button button = new Button("Update");

            {
                button.setOnAction(event -> {
                    Customer ware = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/updatecustomer.fxml"));
                        AnchorPane newpopup;
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        MainCustomerController list = loader.getController();
                        list.UpdateAddrees.setText(ware.getAddress());
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                         popupStage.setOnCloseRequest(closeEvent -> {
                ListCustomerView();
            });
                       list.idcustomer=ware.getId();
                       
                        popupStage.setResizable(false);
                        popupStage.show();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("btn-design");
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
        pagination.setMaxPage(customers.size());
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListCustomerView();
    }
}



