/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.model.Import;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.daodb;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
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
 *
 * @author tranp
 */
public class Import_Warehouse implements Initializable {

    @FXML
    private TableColumn<Import, Integer> colApprove = new TableColumn<>();
    @FXML
    private TextField txtsearchdetail = new TextField();
    @FXML
    private Pagination pagination1 = new Pagination();

    @FXML
    private TableColumn<?, ?> colNameCategory = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colNameproduct = new TableColumn<>();
    private int itemsperPage = 5;
    private int totalItems;
    private int displaymode = 1;
    private int currentPageIndex = 0;
    @FXML
    private Pagination pagination = new Pagination();

    @FXML
    private TableColumn<?, ?> colQuality = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colprice = new TableColumn<>();
    ObjectId idware = null;
    @FXML
    private TableView<Import> tbldetail = new TableView<>();
    @FXML
    private TableColumn<Import, Integer> colCancel = new TableColumn<>();
    @FXML
    private TextField search = new TextField();
    @FXML
    private TableColumn<?, ?> colDate = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> colsupply = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> colEmployee = new TableColumn<>();

    @FXML
    private TableColumn<Import, Boolean> colProduct = new TableColumn<>();

    @FXML
    private TableView<Import> tblimport = new TableView<>();

    private void searchdetailproduct(ObjectId idWare, String search, int pageIndex) {
        displaymode = 2;
        List<Import> importsearch = daodb.SearchDetailWarehouseApproval(search, idWare);
        ObservableList<Import> obserable = FXCollections.observableArrayList(importsearch);
        totalItems = obserable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination1.setPageCount(pageCount);
        if (obserable.isEmpty()) {
            pagination1.setPageCount(1);
            tbldetail.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Import> as = obserable.subList(startIndex, endIndex);
        tbldetail.setItems(FXCollections.observableArrayList(as));
        colNameproduct.setCellValueFactory(new PropertyValueFactory<>("NameProduct"));
        colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colNameCategory.setCellValueFactory(new PropertyValueFactory<>("NameCategory"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
    }

    private void deailProduct(ObjectId idWare) {
        displaymode = 1;
        List<Import> ImportApprovedetail = daodb.DetailWarehouseApproval(idWare);
        ObservableList<Import> obserable = FXCollections.observableArrayList(ImportApprovedetail);
        totalItems = obserable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination1.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Import> as = obserable.subList(startIndex, endIndex);
        tbldetail.setItems(FXCollections.observableArrayList(as));
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
            Document WarehouseQuery = Warehouse.find(Filters.eq("_id", id)).first();
            if (WarehouseQuery != null && !idList.contains(id_product)) {
                Document Detail = (Document) WarehouseQuery.get("Detail");
                Document idcol = (Document) Detail.get(String.valueOf(id_product));
                if (idcol != null) {
                    idList.add(id_product);
                }

            }
        }
        return idList;
    }

    @FXML
    void searchDetail(ActionEvent event) {
        currentPageIndex = 0;
        searchdetailproduct(idware, txtsearchdetail.getText(), currentPageIndex);
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination1.setCurrentPageIndex(currentPageIndex);
        } else if (currentPageIndex == 0) {
            currentPageIndex = 0;
            pagination1.setCurrentPageIndex(currentPageIndex);
        }
    }

    private void seaarchImportapproval(String search, int pageindex) {
        displaymode = 2;
        List<Import> ImportApprove = daodb.SearchWarehouseApproval(search);
        ObservableList<Import> searchInventorry = FXCollections.observableArrayList(ImportApprove);
        totalItems = searchInventorry.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
        if (searchInventorry.isEmpty()) {
            pagination.setPageCount(1);
            tblimport.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageindex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Import> Ass = searchInventorry.subList(startIndex, endIndex);

        tblimport.setItems(FXCollections.observableArrayList(Ass));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colApprove.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCancel.setCellValueFactory(new PropertyValueFactory<>("status"));
        colsupply.setCellValueFactory(new PropertyValueFactory<>("supply"));
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

                        Import.deailProduct(imp.getIdWarehouse());
                        Import.idware = imp.getIdWarehouse();
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

            {
                button.setOnAction(event -> {
                    Import imp = getTableView().getItems().get(getIndex());
                    if (imp.getStatus() == 0) {
                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                        alert.setTitle("CONFIRMATION");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you Approve?");
                        alert.showAndWait().ifPresent(responsive -> {
                            if (responsive == ButtonType.CLOSE) {
                                alert.close();
                            }
                            if (responsive == ButtonType.OK) {
                                MongoCollection<Document> collection = DBConnection.getConnection().getCollection("InComingOrder");
                                MongoCollection<Document> WarehouseIntgoing = DBConnection.getConnection().getCollection("WareHouse_InComingOrder");
                                Document filter = new Document("_id", imp.getIdincoming());
                                Document update = new Document("$set", new Document("status", 1));
                                collection.updateOne(filter, update);
                                List<ObjectId> idList = displayidProduct(imp.getIdWarehouse());
                                for (ObjectId import1 : idList) {
                                    Document document = new Document("ID_WareHouse", imp.getIdWarehouse()).append("ID_InComingOrder", imp.getIdincoming()).append("ID_Product", import1);
                                    WarehouseIntgoing.insertOne(document);
                                }
                                Importapproval();
                            }
                        });
                    } else {
                        DialogAlert.DialogError("Approved");
                    }

                });

            }

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

            {
                button.setOnAction(event -> {
                    Import imp = getTableView().getItems().get(getIndex());
                    if (imp.getStatus() == 0) {
                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                        alert.setTitle("CONFIRMATION");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you Cancel?");
                        alert.showAndWait().ifPresent(responsive -> {
                            if (responsive == ButtonType.CLOSE) {
                                alert.close();
                            }
                            if (responsive == ButtonType.OK) {
                                MongoCollection<Document> collection = DBConnection.getConnection().getCollection("InComingOrder");
                                Document filter = new Document("_id", imp.getIdincoming());
                                Document update = new Document("$set", new Document("status", 2));
                                collection.updateOne(filter, update);
                            }
                            Importapproval();
                        });
                    } else {
                        DialogAlert.DialogError("Canceled");
                    }

                });
            }

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

    @FXML
    void find(ActionEvent event) {
        currentPageIndex = 0;
        seaarchImportapproval(search.getText(), currentPageIndex);
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        } else if (currentPageIndex == 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }

    }

    private void Importapproval() {
        displaymode = 1;
        List<Import> ImportApprove = daodb.WarehouseApproval();
        ObservableList<Import> obserable = FXCollections.observableArrayList(ImportApprove);
        totalItems = obserable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Import> as = obserable.subList(startIndex, endIndex);
        tblimport.setItems(FXCollections.observableArrayList(as));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colApprove.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCancel.setCellValueFactory(new PropertyValueFactory<>("status"));
        colsupply.setCellValueFactory(new PropertyValueFactory<>("supply"));
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

                        Import.deailProduct(imp.getIdWarehouse());
                        Import.idware = imp.getIdWarehouse();
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

            {
                button.setOnAction(event -> {
                    Import imp = getTableView().getItems().get(getIndex());
                    if (imp.getStatus() == 0) {
                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                        alert.setTitle("CONFIRMATION");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you Approve?");
                        alert.showAndWait().ifPresent(responsive -> {
                            if (responsive == ButtonType.CLOSE) {
                                alert.close();
                            }
                            if (responsive == ButtonType.OK) {
                                MongoCollection<Document> collection = DBConnection.getConnection().getCollection("InComingOrder");
                                MongoCollection<Document> WarehouseIntgoing = DBConnection.getConnection().getCollection("WareHouse_InComingOrder");
                                Document filter = new Document("_id", imp.getIdincoming());
                                Document update = new Document("$set", new Document("status", 1));
                                collection.updateOne(filter, update);
                                List<ObjectId> idList = displayidProduct(imp.getIdWarehouse());
                                for (ObjectId import1 : idList) {
                                    Document document = new Document("ID_WareHouse", imp.getIdWarehouse()).append("ID_InComingOrder", imp.getIdincoming()).append("ID_Product", import1);
                                    WarehouseIntgoing.insertOne(document);
                                }
                                Importapproval();
                            }
                        });
                    } else {
                        DialogAlert.DialogError("Approved");
                    }

                });

            }

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

            {
                button.setOnAction(event -> {
                    Import imp = getTableView().getItems().get(getIndex());
                    if (imp.getStatus() == 0) {
                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                        alert.setTitle("CONFIRMATION");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you Cancel?");
                        alert.showAndWait().ifPresent(responsive -> {
                            if (responsive == ButtonType.CLOSE) {
                                alert.close();
                            }
                            if (responsive == ButtonType.OK) {
                                MongoCollection<Document> collection = DBConnection.getConnection().getCollection("InComingOrder");
                                Document filter = new Document("_id", imp.getIdincoming());
                                Document update = new Document("$set", new Document("status", 2));
                                collection.updateOne(filter, update);
                            }
                            Importapproval();
                        });
                    } else {
                        DialogAlert.DialogError("Canceled");
                    }

                });
            }

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
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                Importapproval();
            } else if (displaymode == 2) {
                seaarchImportapproval(search.getText(), currentPageIndex);
            }
            Importapproval();
        });
        pagination1.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                deailProduct(idware);
            } else if (displaymode == 2) {
                searchdetailproduct(idware, txtsearchdetail.getText(), currentPageIndex);
            }
            deailProduct(idware);
        });

    }
}
