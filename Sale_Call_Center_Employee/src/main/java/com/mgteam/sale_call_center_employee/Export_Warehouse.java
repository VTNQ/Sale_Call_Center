/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.model.Export;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.daodb;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
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
import org.bson.types.ObjectId;

/**
 *
 * @author tranp
 */
public class Export_Warehouse implements Initializable {

    @FXML
    private TableColumn<Export, Integer> colApprove = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colCustomer = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colEmployee = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colOrder = new TableColumn<>();

    @FXML
    private TableColumn<Export, Integer> colcancle = new TableColumn<>();

    @FXML
    private TableColumn<Export, Boolean> colprint = new TableColumn<>();

    @FXML
    private TableColumn<Export, Boolean> colproduct = new TableColumn<>();

    @FXML
    private TableView<Export> tblExport = new TableView<>();

    @FXML
    private TableColumn<?, ?> colNameCategory = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colNameproduct = new TableColumn<>();
    private Export export;
    @FXML
    private TableColumn<?, ?> colQuality = new TableColumn<>();
    private ObjectId idorder;
    private void setidorder(ObjectId idorder){
        this.idorder=idorder;
    }
    private ObjectId getidorder(){
        return idorder;
    }
       private ObjectId idssue;
    private void setidissue(ObjectId idissue){
        idssue=idissue;
    }
    private ObjectId getidissue(){
        return idssue;
    }
    private String Customer;
    private void setcustomer(String customer){
        Customer=customer;
    }
    private String getcustomer(){
        return Customer;
    }
    
    @FXML
    private TableColumn<?, ?> colprice = new TableColumn<>();
    @FXML
    private TextField directoryfield;
    @FXML
    private Label customer;

    @FXML
    private TableView<Export> tbldetail = new TableView<>();

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
    if ( directoryfield.getText() != null && !directoryfield.getText().isEmpty()) {
        exportDataToFile( directoryfield.getText());
    } else {
        // Handle the case where export or directoryfield.getText() is null or empty
    }
}

      private void exportDataToFile(String output) {
    try {
        XWPFDocument document = new XWPFDocument();
        FileOutputStream out = new FileOutputStream(output + "\\Warehouse.docx");
        XWPFParagraph title = document.createParagraph();
        title.setAlignment(ParagraphAlignment.CENTER);
        XWPFRun titleRun = title.createRun();
        titleRun.setText("Call Center");
        XWPFTable table = document.createTable(3, 5);

        String[] fields = {"Order", "ID issue", "Customer", "Employee"};

        // In các trường vào hàng đầu tiên của bảng
        for (int i = 0; i < fields.length; i++) {
            XWPFTableCell headerCell = table.getRow(0).getCell(i);
            XWPFParagraph headerParagraph = headerCell.getParagraphs().get(0);
            XWPFRun headerRun = headerParagraph.createRun();
            headerRun.setBold(true);
            headerRun.setText(fields[i]);
        }

        StringBuilder idStringBuilder = new StringBuilder();
        List<ObjectId> idList = displayIdProducts();
        if (idList != null && !idList.isEmpty()) {
            for (ObjectId id : idList) {
                idStringBuilder.append(id).append(", ");
            }
        }
        String idString = idStringBuilder.length() > 0 ? idStringBuilder.substring(0, idStringBuilder.length() - 2) : "";

        String[] values = {getidorder().toString(), idssue.toString(), Customer, LoginController.username};

        for (int i = 1; i <= 3; i++) {
            for (int j = 0; j < 4; j++) {
                int dataIndex = (i - 1) * 4 + j;
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


    private void Detailexportproduct(String Name, int idOrder) {
        List<Export> export = daodb.DetailExportProduct(Name, idOrder);
        ObservableList<Export> obserable = FXCollections.observableArrayList(export);
        tbldetail.setItems(obserable);
        colNameproduct.setCellValueFactory(new PropertyValueFactory<>("NameProduct"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colNameCategory.setCellValueFactory(new PropertyValueFactory<>("NameCategory"));
    }

    private List<ObjectId> displayIdProducts() {
        List<ObjectId> idList = new ArrayList<>();
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
        FindIterable<Document> productWarehouse = Product.find(new Document());
        for (Document document : productWarehouse) {
            ObjectId id_product = document.getObjectId("_id");
            Document query = new Document("Detail." + id_product, new Document("$exists", true));
            Document WarehouseQuery = Warehouse.find(query).first();
            if (WarehouseQuery != null) {
                idList.add(id_product);
            }
        }
        return idList;
    }

    private void ListExport() {
        List<Export> export = daodb.ExportWarehouse();
        ObservableList<Export> obserable = FXCollections.observableArrayList(export);
        tblExport.setItems(obserable);
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
                        export.Detailexportproduct(ex.getCustomer(), ex.getOrder());
                        String idorder = String.valueOf(ex.getOrder());
                        export.customer.setText(idorder);
                      
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
                        Export_Warehouse export=loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                         export.setidorder(ex.getIdOrder());
                       export.setidissue(ex.getIdProduct());
                       export.setcustomer(ex.getCustomer());
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
        colApprove.setCellValueFactory(new PropertyValueFactory<>("status"));
        colApprove.setCellFactory(column -> new TableCell<Export, Integer>() {
            private MFXButton button = new MFXButton("Approve");

            {
                button.setOnAction(event -> {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                    alert.setTitle("CONFIRMATION");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you Approve?");
                    alert.showAndWait().ifPresent(responsive -> {
                        if (responsive == ButtonType.CLOSE) {
                            alert.close();
                        }
                        if (responsive == ButtonType.OK) {
                            try {
                                Export ex = getTableView().getItems().get(getIndex());
                                MongoCollection<Document> collection = DBConnection.getConnection().getCollection("OutGoingOrder");
                                MongoCollection<Document> WarehouseOutgoing = DBConnection.getConnection().getCollection("WareHouse_OutGoingOrder");
                                Document filter = new Document("_id", ex.getIdProduct());
                                Document update = new Document("$set", new Document("status", 1));
                                collection.updateOne(filter, update);
                                List<ObjectId> idList = displayIdProducts();
                                for (ObjectId id : idList) {
                                    Document document = new Document("ID_WareHouse", ex.getIdWarehouse()).append("ID_OutGoingOrder", ex.getIdProduct()).append("ID_Product", id);
                                    WarehouseOutgoing.insertOne(document);
                                }

                                ListExport();
                            } catch (Exception e) {
                            }
                        }
                    });

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
            private MFXButton button = new MFXButton("Cancle");

            {
                button.setOnAction(event -> {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                    alert.setTitle("CONFIRMATION");
                    alert.setHeaderText(null);
                    alert.setContentText("Are you Cancle?");
                    alert.showAndWait().ifPresent(responsive -> {
                        if (responsive == ButtonType.CLOSE) {
                            alert.close();
                        }
                        if (responsive == ButtonType.OK) {
                            try {
                                Export ex = getTableView().getItems().get(getIndex());
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
    }
}
