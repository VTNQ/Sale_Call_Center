/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.model.Import;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.daodb;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author tranp
 */
public class Import_Warehouse implements Initializable {

    @FXML
    private TableColumn<Import, Integer> colApprove=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colNameCategory=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colNameproduct=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colQuality=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colprice=new TableColumn<>();

    @FXML
    private TableView<Import> tbldetail=new TableView<>();
    @FXML
    private TableColumn<Import, Integer> colCancel=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colDate=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colEmployee=new TableColumn<>();

    @FXML
    private TableColumn<Import, Boolean> colProduct=new TableColumn<>();

    @FXML
    private TableView<Import> tblimport=new TableView<>();

    private void deailProduct(List<ObjectId> idproduct, ObjectId idWare) {
        List<Import> ImportApprovedetail = daodb.DetailWarehouseApproval(idproduct,idWare);
        ObservableList<Import> obserable = FXCollections.observableArrayList(ImportApprovedetail);
        tbldetail.setItems(obserable);
        colNameproduct.setCellValueFactory(new PropertyValueFactory<>("NameProduct"));
        colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colNameCategory.setCellValueFactory(new PropertyValueFactory<>("NameCategory"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
    }

    private List<ObjectId> displayidProduct(ObjectId id) {
        List<ObjectId> idList = new ArrayList<>();
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
        FindIterable<Document> productWarehouse = Product.find(new Document());
        for (Document document : productWarehouse) {
            ObjectId id_product = document.getObjectId("_id");
            Document query = new Document("Detail." + id_product, new Document("$exists", true));
            Document WarehouseQuery = Warehouse.find(query).first();
            if (WarehouseQuery != null && WarehouseQuery.getObjectId("_id").equals(id) && !idList.contains(id_product)) {
                idList.add(id_product);
            }
        }
        return idList;
    }

    private void Importapproval() {
        List<Import> ImportApprove = daodb.WarehouseApproval();
        ObservableList<Import> obserable = FXCollections.observableArrayList(ImportApprove);
        tblimport.setItems(obserable);
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colApprove.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCancel.setCellValueFactory(new PropertyValueFactory<>("status"));
        colProduct.setCellFactory(column -> new TableCell<Import, Boolean>() {
            private MFXButton button = new MFXButton("Product");

            {
                button.setOnAction(event -> {
                    try {
                        Import imp = getTableView().getItems().get(getIndex());
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/ImportDetailProduct.fxml"));
                        AnchorPane newpopup = loader.load();
                        Stage popupStage = new Stage();
                        Import_Warehouse Import = loader.getController();
                        List<ObjectId> idList = displayidProduct(imp.getIdWarehouse());
                      
                            Import.deailProduct(idList, imp.getIdWarehouse());

                       
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        popupStage.setResizable(false);
                        popupStage.showAndWait();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button_class");
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
        colApprove.setCellFactory(column -> new TableCell<>() {
            private MFXButton button = new MFXButton("Approve");

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button-success");
                if (item != null || !empty) {
                    if (item == 2) {
                        setGraphic(button);
                        button.setDisable(true);
                    } else {
                        setGraphic(button);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        colCancel.setCellFactory(column -> new TableCell<Import, Integer>() {
            private MFXButton button = new MFXButton("Cancel");

            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button-error");
                if (item != null || !empty) {
                    if (item == 1) {
                        setGraphic(button);
                        button.setDisable(true);
                    } else {
                        setGraphic(button);
                    }
                }
            }

        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Importapproval();
    }
}
