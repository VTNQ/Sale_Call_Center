/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.model.Supply;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.daodb;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
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

/**
 * FXML Controller class
 *
 * @author tranp
 */
public class SupplyController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableColumn<?, ?> colAddress = new TableColumn<>();

    @FXML
    private TableColumn<Supply, Boolean> colDelete = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colName = new TableColumn<>();

    @FXML
    private TableColumn<Supply, Boolean> colupdate = new TableColumn<>();
    @FXML
    private TextField UpdaateNamesupply = new TextField();
    private ObjectId idsupply = new ObjectId();
    @FXML
    private ComboBox<String> UpdateAddress = new ComboBox<>();
    @FXML
    private TableView<Supply> tblsupply = new TableView<>();
    @FXML
    private ComboBox<String> Address = new ComboBox<>();

    @FXML
    private TextField textfield = new TextField();
    @FXML
    private Button button = new Button();
    private int itemsperPage = 5;
    private int totalItems;
    private int displaymode = 1;
    private int currentPageIndex = 0;
    @FXML
    private Pagination pagination = new Pagination();

    private void searchsupply(String search,int pageindex) {
        displaymode=2;
        List<Supply> supply = daodb.searchsupply(search);
        ObservableList<Supply> observable = FXCollections.observableArrayList(supply);
        totalItems = observable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
         if (supply.isEmpty()) {
            pagination.setPageCount(1);
            tblsupply.setItems(FXCollections.observableArrayList());
            return;
        }
          int startIndex = pageindex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Supply>Ass=observable.subList(startIndex, endIndex);
        tblsupply.setItems(FXCollections.observableArrayList(Ass));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colupdate.setCellFactory(column -> new TableCell<Supply, Boolean>() {
            private Button button = new Button("Update");

            {
                button.setOnAction(event -> {
                    Supply supply = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/updateSupply.fxml"));
                        AnchorPane newpopup;
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        SupplyController list = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        list.UpdaateNamesupply.setText(supply.getName());
                        list.UpdateAddress.setValue(supply.getAddress());
                        list.idsupply = supply.getId();
                        popupStage.setOnCloseRequest(closeEvent -> {
                            showsupply();
                        });
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
                button.getStyleClass().add("button-design");
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
        colDelete.setCellFactory(column -> new TableCell<Supply, Boolean>() {
            private Button button = new Button("Delete");

            {
                button.setOnAction(event -> {
                    Supply supply = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> Supply = DBConnection.getConnection().getCollection("Supply");
                    MongoCollection<Document> InComingOrder = DBConnection.getConnection().getCollection("InComingOrder");
                    Document supplyfilter = new Document("id_Supplier", supply.getId());
                    Document supplydelete = new Document("_id", supply.getId());
                    long count = InComingOrder.countDocuments(supplyfilter);
                    if (count > 0) {
                        DialogAlert.DialogError("Cannot Delete");
                    } else {
                        Supply.deleteOne(supplydelete);
                        DialogAlert.DialogSuccess("Delete Successfully");
                        showsupply();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button-design");
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
    }

    @FXML
    void Update(ActionEvent event) {
        MongoCollection<Document> Supply = DBConnection.getConnection().getCollection("Supply");
        Document filter = new Document("_id", idsupply);
        Document updates = new Document("$set", new Document("Name", UpdaateNamesupply.getText()).append("Address", UpdateAddress.getValue()));
        if (UpdaateNamesupply.getText().isEmpty() && UpdateAddress.getValue() == null) {
            DialogAlert.DialogError("Please enter complete information");
        } else {
            if (!isNameExists(UpdaateNamesupply.getText(), Supply)) {
                MongoCollection<Document> InComingOrder = DBConnection.getConnection().getCollection("InComingOrder");
                Document supplyfilter = new Document("id_Supplier", idsupply);
                long count = InComingOrder.countDocuments(supplyfilter);
                if (count > 0) {
                    DialogAlert.DialogError("Cannot Update");
                } else {
                    UpdateResult update = Supply.updateOne(filter, updates);
                    DialogAlert.DialogSuccess("Update supply successfully");
                }

            } else {
                DialogAlert.DialogError("Name Supply is Exists");
            }
        }

    }
    @FXML
    private TextField Namesuuply = new TextField();

    private void showsupply() {
        displaymode=1;
        List<Supply> supply = daodb.ListSupply();
        ObservableList<Supply> observable = FXCollections.observableArrayList(supply);
        totalItems = observable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Supply> Ass = observable.subList(startIndex, endIndex);
        tblsupply.setItems(FXCollections.observableArrayList(Ass));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colAddress.setCellValueFactory(new PropertyValueFactory<>("Address"));
        colupdate.setCellFactory(column -> new TableCell<Supply, Boolean>() {
            private Button button = new Button("Update");

            {
                button.setOnAction(event -> {
                    Supply supply = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/updateSupply.fxml"));
                        AnchorPane newpopup;
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        SupplyController list = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        list.UpdaateNamesupply.setText(supply.getName());
                        list.UpdateAddress.setValue(supply.getAddress());
                        list.idsupply = supply.getId();
                        popupStage.setOnCloseRequest(closeEvent -> {
                            showsupply();
                        });
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
                button.getStyleClass().add("button-design");
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
        colDelete.setCellFactory(column -> new TableCell<Supply, Boolean>() {
            private Button button = new Button("Delete");

            {
                button.setOnAction(event -> {
                    Supply supply = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> Supply = DBConnection.getConnection().getCollection("Supply");
                    MongoCollection<Document> InComingOrder = DBConnection.getConnection().getCollection("InComingOrder");
                    Document supplyfilter = new Document("id_Supplier", supply.getId());
                    Document supplydelete = new Document("_id", supply.getId());
                    long count = InComingOrder.countDocuments(supplyfilter);
                    if (count > 0) {
                        DialogAlert.DialogError("Cannot Delete");
                    } else {
                        Supply.deleteOne(supplydelete);
                        DialogAlert.DialogSuccess("Delete Successfully");
                        showsupply();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button-design");
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
    }

    @FXML
    void showaddsupply(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/Addsupply.fxml"));
        AnchorPane newpopup;
        try {
            newpopup = loader.load();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(newpopup));
            popupStage.setResizable(false);
            popupStage.show();
            popupStage.setOnCloseRequest(closeEvent -> {
                showsupply();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void add(ActionEvent event) {
        if (!Namesuuply.getText().isEmpty() && Address.getValue() != null) {
            MongoCollection<Document> Supply = DBConnection.getConnection().getCollection("Supply");
            if (!isNameExists(Namesuuply.getText(), Supply)) {
                Document query = new Document("Name", Namesuuply.getText()).append("Address", Address.getValue());
                Supply.insertOne(query);
                DialogAlert.DialogSuccess("Add Supply Successfully");
                Namesuuply.setText("");
            } else {
                DialogAlert.DialogError("Supply already exists");
            }

        } else {

        }
    }

    private boolean isNameExists(String Name, MongoCollection<Document> collection) {
        Document existingDocument = collection.find(new Document("Name", Name)).first();
        return existingDocument != null;
    }

    @FXML
    void find(ActionEvent event) {
       currentPageIndex=0;
        searchsupply(textfield.getText(),currentPageIndex);
         if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }else if(currentPageIndex==0){
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showsupply();
        ObservableList<String> country = FXCollections.observableArrayList(
                "United States",
                "China",
                "India",
                "Brazil",
                "Russia",
                "United Kingdom",
                "France",
                "Germany",
                "Japan",
                "South Korea",
                "Australia",
                "Canada",
                "Mexico",
                "South Africa",
                "Saudi Arabia",
                "United Arab Emirates",
                "Turkey",
                "Indonesia",
                "Singapore",
                "Malaysia");
        Address.setItems(country);
        Address.setValue("United States");
        UpdateAddress.setItems(country);
        pagination.currentPageIndexProperty().addListener((obs,oldIndex,newIndedx)->{
        currentPageIndex=newIndedx.intValue();
        if(displaymode==1){
            showsupply();
        }else if(displaymode==2){
            searchsupply(textfield.getText(), currentPageIndex);
        }
            showsupply();
        });
    }

}
