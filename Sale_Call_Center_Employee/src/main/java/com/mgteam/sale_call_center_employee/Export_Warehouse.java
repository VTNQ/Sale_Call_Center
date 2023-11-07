/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.model.Export;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.daodb;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Sorts;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author tranp
 */
public class Export_Warehouse implements Initializable {

    @FXML
    private TableColumn<Export, Integer> colApprove = new TableColumn<>();
    boolean actionInProgress = false;
    @FXML
    private Pagination pagination = new Pagination();
    @FXML
    private Pagination pagination1 = new Pagination();
    @FXML
    private TableColumn<?, ?> colCustomer = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colEmployee = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colOrder = new TableColumn<>();

    @FXML
    private TableColumn<Export, Integer> colcancle = new TableColumn<>();
    private int itemsperPage = 5;
    private int totalItems;
    private int displaymode = 1;
    private int currentPageIndex = 0;
    @FXML
    private TableColumn<Export, Boolean> colprint = new TableColumn<>();

    @FXML
    private TableColumn<Export, Boolean> colproduct = new TableColumn<>();

    @FXML
    private TableView<Export> tblExport = new TableView<>();
    @FXML
    private TextField detailtxt = new TextField();

    @FXML
    private TextField textsearch = new TextField();
    @FXML
    private TableColumn<?, ?> colNameCategory = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colNameproduct = new TableColumn<>();
    private Export export;
    @FXML
    private TableColumn<?, ?> colQuality = new TableColumn<>();
    private ObjectId idorder;

    private void setidorder(ObjectId idorder) {
        this.idorder = idorder;
    }

    private ObjectId getidorder() {
        return idorder;
    }
    private ObjectId idssue;

    private void setidissue(ObjectId idissue) {
        idssue = idissue;
    }
    private ObjectId idWarehouse;

    private ObjectId getidissue() {
        return idssue;
    }
    private String Customer;

    private void setcustomer(String customer) {
        Customer = customer;
    }

    private String getcustomer() {
        return Customer;
    }

    @FXML
    private TableColumn<?, ?> colprice = new TableColumn<>();
    @FXML
    private TextField directoryfield;
    @FXML
    private Label customer;

    @FXML
    private Button buttonsave;
    @FXML
    private TableView<Export> tbldetail = new TableView<>();

    @FXML
    void Search(ActionEvent event) {
        currentPageIndex = 0;
        searchlistExport(textsearch.getText(), currentPageIndex);
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        } else if (currentPageIndex == 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }
    }

    @FXML
    void choice_Direct(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = new Stage(); // Tạo một Stage mới

        java.io.File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            System.out.println("Thư mục đã chọn: " + selectedDirectory.getAbsolutePath());
            directoryfield.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    void save(ActionEvent event) {
        if (directoryfield.getText() != null && !directoryfield.getText().isEmpty()) {
            exportDataToFile(directoryfield.getText());
            DialogAlert.DialogSuccess("Download successfully");
            Stage stage = (Stage) buttonsave.getScene().getWindow();
            stage.close();
        } else {
            // Handle the case where export or directoryfield.getText() is null or empty
        }
    }

    private void exportDataToFile(String output) {
        try {
            XWPFDocument document = new XWPFDocument();
            FileOutputStream out = new FileOutputStream(output + "\\Warehouse.docx");

            // Create title paragraph
            XWPFParagraph title = document.createParagraph();
            title.setAlignment(ParagraphAlignment.CENTER);
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Call Center");

            // Create table with 3 rows and 5 columns
            XWPFTable table = document.createTable(3, 5);

            // Define table headers
            String[] fields = {"Order", "ID issue", "Customer", "Employee", "Product"};
            for (int i = 0; i < fields.length; i++) {
                XWPFTableCell headerCell = table.getRow(0).getCell(i);
                XWPFParagraph headerParagraph = headerCell.getParagraphs().get(0);
                XWPFRun headerRun = headerParagraph.createRun();
                headerRun.setBold(true);
                headerRun.setText(fields[i]);
            }

            // Get product IDs as a comma-separated string
            StringBuilder idStringBuilder = new StringBuilder();
            List<String> idList = displayIdProducts(idWarehouse);
            if (idList != null && !idList.isEmpty()) {
                for (String id : idList) {
                    idStringBuilder.append(id).append(", ");
                }
            }
            String idString = idStringBuilder.length() > 0 ? idStringBuilder.substring(0, idStringBuilder.length() - 2) : "";

            // Define values for the table cells
            String[] values = {
                String.valueOf(getidorder().hashCode()),
                String.valueOf(Math.abs(getidissue().hashCode())),
                Customer,
                LoginController.username,
                idString
            };

            // Set values in the table
            for (int i = 1; i <= 3; i++) {
                for (int j = 0; j < 5; j++) { // Changed from 4 to 5
                    int dataIndex = (i - 1) * 5 + j; // Changed from 4 to 5
                    if (dataIndex < values.length) {
                        XWPFTableCell cell = table.getRow(i).getCell(j);
                        XWPFParagraph paragraph = cell.getParagraphs().get(0);
                        XWPFRun run = paragraph.createRun();
                        run.setText(values[dataIndex]);
                    }
                }
            }

            document.write(out);
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void SearchDetailExportproduct(String Name, ObjectId idorder, String txt, int pageindex) {
        displaymode = 2;
        List<Export> export = daodb.searchDetailExportProduct(Name, idorder, txt);
        ObservableList<Export> obserable = FXCollections.observableArrayList(export);
        totalItems = obserable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination1.setPageCount(pageCount);
        if (obserable.isEmpty()) {
            pagination1.setPageCount(1);
            tbldetail.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageindex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Export> Ass = obserable.subList(startIndex, endIndex);
        tbldetail.setItems(FXCollections.observableArrayList(Ass));
        colNameproduct.setCellValueFactory(new PropertyValueFactory<>("NameProduct"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colNameCategory.setCellValueFactory(new PropertyValueFactory<>("NameCategory"));
    }

    private void Detailexportproduct(String Name, ObjectId idOrder) {
        displaymode = 1;
        List<Export> export = daodb.DetailExportProduct(Name, idOrder);
        ObservableList<Export> obserable = FXCollections.observableArrayList(export);
        totalItems = obserable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination1.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Export> as = obserable.subList(startIndex, endIndex);
        tbldetail.setItems(FXCollections.observableArrayList(as));
        colNameproduct.setCellValueFactory(new PropertyValueFactory<>("NameProduct"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colNameCategory.setCellValueFactory(new PropertyValueFactory<>("NameCategory"));
    }

    private List<ObjectId> displayIdProduct(ObjectId id) {
        List<ObjectId> idList = new ArrayList<>();
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> ordercollection = DBConnection.getConnection().getCollection("Order");
        FindIterable<Document> order = ordercollection.find(new Document("_id", id));
        for (Document document : order) {
            FindIterable<Document> Productcollection = Product.find();
            for (Document document1 : Productcollection) {
                ObjectId idproduct = document1.getObjectId("_id");
                Document Detail = (Document) document.get("DetailOrder");
                Document idcol = (Document) Detail.get(String.valueOf(idproduct));
                if (idcol != null && !idList.contains(idproduct)) {
                    idList.add(idproduct);
                }
            }
        }
        return idList;
    }

    @FXML
    void find(ActionEvent event) {
        currentPageIndex = 0;
        SearchDetailExportproduct(getcustomer(), idorder, detailtxt.getText(), currentPageIndex);
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination1.setCurrentPageIndex(currentPageIndex);
        } else if (currentPageIndex == 0) {
            currentPageIndex = 0;
            pagination1.setCurrentPageIndex(currentPageIndex);
        }
    }

    private List<Integer> displayQuality(ObjectId id, ObjectId idorder) {
        List<Integer> idList = new ArrayList<>();
        MongoCollection<Document> order = DBConnection.getConnection().getCollection("Order");
        FindIterable<Document> productWarehouse = order.find(new Document("_id", idorder));
        for (Document document : productWarehouse) {
            Document Detail = (Document) document.get("DetailOrder");
            Document idcol = (Document) Detail.get(String.valueOf(id));
            if (idcol != null) {
                idList.add(idcol.getInteger("Quality"));
            }
        }
        return idList;
    }

    private List<String> displayIdProducts(ObjectId id) {
        List<String> idList = new ArrayList<>();
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
        FindIterable<Document> productWarehouse = Product.find(new Document());
        for (Document document : productWarehouse) {
            ObjectId id_product = document.getObjectId("_id");
            String Name = document.getString("Name");
            Document query = new Document("Detail." + id_product, new Document("$exists", true));
            Document WarehouseQuery = Warehouse.find(query).first();
            if (WarehouseQuery != null && WarehouseQuery.getObjectId("_id").equals(id) && !idList.contains(id_product)) {
                idList.add(Name);
            }
        }
        return idList;
    }

    private void searchlistExport(String search, int pageindex) {
        displaymode = 2;
        List<Export> export = daodb.SearchExportWarehouse(search);
        ObservableList<Export> obserable = FXCollections.observableArrayList(export);
        totalItems = obserable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
        if (obserable.isEmpty()) {
            pagination.setPageCount(1);
            tblExport.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageindex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Export> Ass = obserable.subList(startIndex, endIndex);
        tblExport.setItems(FXCollections.observableArrayList(Ass));
        colOrder.setCellValueFactory(new PropertyValueFactory<>("order"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colproduct.setCellFactory(column -> new TableCell<Export, Boolean>() {
            private MFXButton button = new MFXButton("Product");

            {
                button.setOnAction(event -> {
                    Export ex = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/DetailProduct_Warehouse.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Stage popupStage = new Stage();
                        Export_Warehouse export = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        export.Detailexportproduct(ex.getCustomer(), ex.getIdOrder());
                        String idorder = String.valueOf(ex.getOrder());
                        export.customer.setText(idorder);
                        export.setcustomer(ex.getCustomer());
                        export.setidorder(ex.getIdOrder());
                        popupStage.setResizable(false);
                        popupStage.showAndWait();
                    } catch (IOException ex1) {
                        ex1.printStackTrace();
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
        colprint.setCellFactory(column -> new TableCell<Export, Boolean>() {
            private MFXButton button = new MFXButton("Print");

            {

                button.setOnAction(event -> {
                    try {
                        Export ex = getTableView().getItems().get(getIndex());
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/Path.fxml"));
                        AnchorPane newpopup = loader.load();

                        Stage popupStage = new Stage();
                        Export_Warehouse export = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        export.setidorder(ex.getIdOrder());
                        export.setidissue(ex.getIdProduct());
                        export.setcustomer(ex.getCustomer());
                        export.idWarehouse = ex.getIdWarehouse();
                        popupStage.setResizable(false);
                        popupStage.showAndWait();
                    } catch (IOException ex1) {
                        ex1.printStackTrace();
                    }

                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button_class");
                if (item != null || !empty) {
                    int status = getTableView().getItems().get(getIndex()).getStatus();
                    if (status == 1) {
                        setGraphic(button);
                    } else {
                        setGraphic(button);
                        button.setDisable(true);
                    }

                } else {
                    setGraphic(null);
                }
            }

        });
        colApprove.setCellValueFactory(new PropertyValueFactory<>("status"));
        colApprove.setCellFactory(column -> new TableCell<Export, Integer>() {
            private MFXButton button = new MFXButton("Approve");

            {
                button.setOnAction(event -> {
                    Export ex = getTableView().getItems().get(getIndex());

                    if (ex.getStatus() == 0) {
                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                        alert.setTitle("CONFIRMATION");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you Approve?");
                        alert.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.OK) {
                                try {
                                    MongoCollection<Document> collection = DBConnection.getConnection().getCollection("OutGoingOrder");
                                    MongoCollection<Document> WarehouseOutgoing = DBConnection.getConnection().getCollection("WareHouse_OutGoingOrder");
                                    MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");

                                    Document filter = new Document("_id", ex.getIdProduct());
                                    Document update = new Document("$set", new Document("status", 1));
                                    collection.updateOne(filter, update);

                                    List<ObjectId> idList = displayIdProduct(ex.getIdWarehouse());

                                    for (ObjectId id : idList) {
                                        List<Integer> Quality = displayQuality(id, ex.getIdOrder());

                                        for (Integer export1 : Quality) {
                                            Document document = new Document("ID_WareHouse", ex.getIdWarehouse())
                                                    .append("ID_OutGoingOrder", ex.getIdProduct())
                                                    .append("ID_Product", id)
                                                    .append("Quality", export1);
                                            WarehouseOutgoing.insertOne(document);

                                            FindIterable<Document> warehouseCollection = Warehouse.find(
                                                    new Document("_id", ex.getIdWarehouse())
                                            );

                                            for (Document warehouseDocument : warehouseCollection) {
                                                Document Detail = (Document) warehouseDocument.get("Detail");

                                                if (Detail != null) {
                                                    Document idcol = (Document) Detail.get(String.valueOf(ex.getIdProduct()));
                                                    if (idcol != null) {
                                                        int newQuality = idcol.getInteger("Quality");

                                                        if (newQuality > 0) {
                                                            int qualityUpdate = newQuality - export1;

                                                            idcol.put("Quality", qualityUpdate);
                                                            Document filter1 = new Document("_id", ex.getIdWarehouse());
                                                            // Update the WareHouse Quality here.
                                                            Document wareHouseUpdate = new Document("$set",
                                                                    new Document("Detail." + ex.getIdProduct() + ".Quality", qualityUpdate));

                                                            Warehouse.updateOne(filter1, wareHouseUpdate);
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    DialogAlert.DialogSuccess("Approve successfully");
                                    ListExport();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
                }
            }

        });
        colcancle.setCellValueFactory(new PropertyValueFactory<>("status"));
        colcancle.setCellFactory(column -> new TableCell<Export, Integer>() {
            private MFXButton button = new MFXButton("cancel");

            {
                button.setOnAction(event -> {
                    Export ex = getTableView().getItems().get(getIndex());
                    if (ex.getStatus() == 0) {
                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                        alert.setTitle("CONFIRMATION");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you cancel?");
                        alert.showAndWait().ifPresent(responsive -> {
                            if (responsive == ButtonType.CLOSE) {
                                alert.close();
                            }
                            if (responsive == ButtonType.OK) {
                                try {
                                    MongoCollection<Document> collection = DBConnection.getConnection().getCollection("OutGoingOrder");
                                    MongoCollection<Document> WarehouseOutgoing = DBConnection.getConnection().getCollection("WareHouse_OutGoingOrder");
                                    Document filter = new Document("_id", ex.getIdProduct());
                                    Document update = new Document("$set", new Document("status", 2));
                                    collection.updateOne(filter, update);
                                    ListExport();
                                } catch (Exception e) {
                                }
                            }
                        });
                    } else {
                        DialogAlert.DialogError("canceled");
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

   public boolean areAllProductsAboveThreshold(List<ObjectId> productIds, Export ex) {
    MongoCollection<Document> wareHouse = DBConnection.getConnection().getCollection("WareHouse");

    for (ObjectId productId : productIds) {
        List<Integer> qualityList = displayQuality(productId, ex.getIdOrder());
        boolean anyProductBelowThreshold = false;

        for (Integer quality : qualityList) {
            Document warehouseDocument = wareHouse.find(Filters.and(
                    Filters.eq("Detail." + productId.toString(), new Document("$exists", true))
            )).sort(Sorts.ascending("Date")).limit(1).first();

            if (warehouseDocument != null) {
                Document detail = warehouseDocument.get("Detail", Document.class);
                Document idcol = detail.get(productId.toString(), Document.class);

                if (idcol != null) {
                    int productQuality = idcol.getInteger("Quality");
                    if (productQuality < quality) {
                        anyProductBelowThreshold = true;
                        return false;
                    }
                }
            }
        }

        if (!anyProductBelowThreshold) {
            return true;
        }
    }

    return true;
}

    private void ListExport() {
        displaymode = 1;
        List<Export> export = daodb.ExportWarehouse();
        ObservableList<Export> obserable = FXCollections.observableArrayList(export);
        totalItems = obserable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Export> as = obserable.subList(startIndex, endIndex);
        tblExport.setItems(FXCollections.observableArrayList(as));
        colOrder.setCellValueFactory(new PropertyValueFactory<>("order"));
        colCustomer.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colproduct.setCellFactory(column -> new TableCell<Export, Boolean>() {
            private MFXButton button = new MFXButton("Product");

            {
                button.setOnAction(event -> {
                    Export ex = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/DetailProduct_Warehouse.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Stage popupStage = new Stage();
                        Export_Warehouse export = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        export.Detailexportproduct(ex.getCustomer(), ex.getIdOrder());
                        String idorder = String.valueOf(ex.getOrder());
                        export.customer.setText(idorder);
                        export.setcustomer(ex.getCustomer());
                        export.setidorder(ex.getIdOrder());
                        popupStage.setResizable(false);
                        popupStage.showAndWait();
                    } catch (IOException ex1) {
                        ex1.printStackTrace();
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
        colprint.setCellFactory(column -> new TableCell<Export, Boolean>() {
            private MFXButton button = new MFXButton("Print");

            {

                button.setOnAction(event -> {
                    try {
                        Export ex = getTableView().getItems().get(getIndex());
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/Path.fxml"));
                        AnchorPane newpopup = loader.load();

                        Stage popupStage = new Stage();
                        Export_Warehouse export = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        export.setidorder(ex.getIdOrder());
                        export.setidissue(ex.getIdProduct());
                        export.setcustomer(ex.getCustomer());
                        export.idWarehouse = ex.getIdWarehouse();
                        popupStage.setResizable(false);
                        popupStage.showAndWait();
                    } catch (IOException ex1) {
                        ex1.printStackTrace();
                    }

                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button_class");
                if (item != null || !empty) {
                    int status = getTableView().getItems().get(getIndex()).getStatus();
                    if (status == 1) {
                        setGraphic(button);
                    } else {
                        setGraphic(button);
                        button.setDisable(true);
                    }

                } else {
                    setGraphic(null);
                }
            }

        });
        colApprove.setCellValueFactory(new PropertyValueFactory<>("status"));
        colApprove.setCellFactory(column -> new TableCell<Export, Integer>() {
            private MFXButton button = new MFXButton("Approve");

            {

                button.setOnAction(event -> {
                    Export ex = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> WareHouse = DBConnection.getConnection().getCollection("WareHouse");
                    List<ObjectId> idproduct = displayIdProduct(ex.getIdOrder());
                    List<String> outOfStockProducts = new ArrayList<>();
                     System.out.println(areAllProductsAboveThreshold(idproduct, ex));
                    for (ObjectId productId : idproduct) {
                        List<Integer> Qualityproduct = displayQuality(productId, ex.getIdOrder());

                        for (Integer a : Qualityproduct) {
                           
                            FindIterable<Document> warehousedocument = WareHouse.find(Filters.and(
                                    Filters.eq("Detail." + productId.toString(), new Document("$exists", true))
                            )).sort(Sorts.ascending("Date")).limit(1);

                            // Biến cờ để kiểm tra sản phẩm có hết hay không
                            for (Document a1 : warehousedocument) {
                                String date = a1.getString("Date");
                                Document detailProduct = a1.get("Detail", Document.class).get(productId.toString(), Document.class);
                                int quality = detailProduct.getInteger("Quality");
                                ObjectId id = a1.getObjectId("_id");
   
                               
                        
                                   
                                

                            }

                        }
                    }

                    ListExport();
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
                }
            }

        }
        );
        colcancle.setCellValueFactory(new PropertyValueFactory<>("status"));
        colcancle.setCellFactory(column -> new TableCell<Export, Integer>() {
            private MFXButton button = new MFXButton("cancel");

            {
                button.setOnAction(event -> {
                    Export ex = getTableView().getItems().get(getIndex());
                    if (ex.getStatus() == 0) {
                        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                        alert.setTitle("CONFIRMATION");
                        alert.setHeaderText(null);
                        alert.setContentText("Are you cancel?");
                        alert.showAndWait().ifPresent(responsive -> {
                            if (responsive == ButtonType.CLOSE) {
                                alert.close();
                            }
                            if (responsive == ButtonType.OK) {
                                try {
                                    MongoCollection<Document> collection = DBConnection.getConnection().getCollection("OutGoingOrder");
                                    MongoCollection<Document> WarehouseOutgoing = DBConnection.getConnection().getCollection("WareHouse_OutGoingOrder");
                                    Document filter = new Document("_id", ex.getIdProduct());
                                    Document update = new Document("$set", new Document("status", 2));
                                    collection.updateOne(filter, update);
                                    ListExport();
                                } catch (Exception e) {
                                }
                            }
                        });
                    } else {
                        DialogAlert.DialogError("canceled");
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
        ListExport();
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                ListExport();
            } else if (displaymode == 2) {
                searchlistExport(textsearch.getText(), currentPageIndex);
            }
            ListExport();
        });
        pagination1.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                Detailexportproduct(getcustomer(), getidorder());
            } else if (displaymode == 2) {
                SearchDetailExportproduct(getcustomer(), idorder, detailtxt.getText(), currentPageIndex);
            }
            Detailexportproduct(getcustomer(), getidorder());
        });
    }
}
