/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.model.Import;
import com.mgteam.sale_call_center_employee.model.Product;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.daodb;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
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
public class ProductController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField textfield = new TextField();
    @FXML
    private TextField NameProduct = new TextField();
    @FXML
    private TableColumn<?, ?> colCategory = new TableColumn<>();

    @FXML
    private Pagination pagination = new Pagination();
    private int itemsperPage = 5;
    private int totalItems;
    private int displaymode = 1;
    private int currentPageIndex = 0;

    @FXML
    private TableColumn<Product, Boolean> colDelete = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colName = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colprice = new TableColumn<>();

    @FXML
    private TableColumn<Product, Boolean> colupdate = new TableColumn<>();

    @FXML
    private ComboBox<String> Namecategory = new ComboBox<>();
    @FXML
    private TextField EditName = new TextField();

    @FXML
    private TextField EditNameprice = new TextField();

    @FXML
    private ComboBox<String> EditnameCategory = new ComboBox<>();
    @FXML
    private TextField Price = new TextField();
    private ObjectId idProduct;
    @FXML
    private TableView<Product> tblproduct = new TableView<>();

    @FXML
    void UpdateProduct(ActionEvent event) {
        String priceString = EditNameprice.getText();
        int priceValue = Integer.parseInt(priceString);
        if (!EditNameprice.getText().isEmpty() && !EditName.getText().isEmpty() && EditnameCategory.getValue() != null) {
            try {
                if (priceValue > 0) {
                    MongoCollection<Document> product = DBConnection.getConnection().getCollection("Product");
                    MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
                    Document Warehousecollection = new Document("Detail." + String.valueOf(idProduct), new Document("$exists", true));
                    long count = Warehouse.countDocuments(Warehousecollection);
                    if (count > 0) {
                        DialogAlert.DialogError("Can not update");
                    } else {
                        Document filter = new Document("_id", idProduct);
                        Document update = new Document("$set", new Document("Name", EditName.getText()).append("Price", priceValue).append("ID_Category", getCategoryIDByName(EditnameCategory.getValue())));
                        UpdateResult updateresult = product.updateOne(filter, update);
                        if (updateresult.getModifiedCount() > 0) {
                            DialogAlert.DialogSuccess("Update successfully");
                        }
                    }

                } else {
                    DialogAlert.DialogError("Price must be a non-negative number");

                }
            } catch (NumberFormatException e) {
                DialogAlert.DialogError("Price is not valid");
            }
        } else {
            DialogAlert.DialogError("Information required");
        }

    }

    @FXML
    void find(ActionEvent event) {

        currentPageIndex = 0;
        SearchlistProduct(textfield.getText(), currentPageIndex);
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        } else if (currentPageIndex == 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }
    }

    @FXML
    void showaddproduct(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/Product.fxml"));
        AnchorPane newpopup;
        try {
            newpopup = loader.load();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(newpopup));
            popupStage.setResizable(false);
            popupStage.show();
            popupStage.setOnCloseRequest(closeEvent -> {
                ListProduct();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void SearchlistProduct(String name, int pageindex) {
        displaymode = 2;
        List<Product> product = daodb.searchListProduct(name);
        ObservableList<Product> obserable = FXCollections.observableArrayList(product);
        totalItems = obserable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
        if (obserable.isEmpty()) {
            pagination.setPageCount(1);
            tblproduct.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageindex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Product> as = obserable.subList(startIndex, endIndex);
        tblproduct.setItems(FXCollections.observableArrayList(as));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colprice.setCellValueFactory(new PropertyValueFactory<>("priceformat"));
        colupdate.setCellValueFactory(new PropertyValueFactory<>("isactive"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("isactive"));
        colupdate.setCellFactory(column -> new TableCell<Product, Boolean>() {
            private Button button = new Button("Update");

            {
                button.setOnAction(event -> {
                    Product pro = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/UpdateProduct.fxml"));
                    AnchorPane newpopup;
                    try {
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        ProductController product = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        product.EditName.setText(pro.getName());
                        String cleanedPriceString = pro.getPriceformat().replaceAll("[^\\d]", "");
                        int price = Integer.parseInt(cleanedPriceString);
                        product.EditNameprice.setText(String.valueOf(price));
                        product.EditnameCategory.setValue(pro.getCategory());
                        product.idProduct = pro.getId();
                        popupStage.setResizable(false);
                        popupStage.show();
                        popupStage.setOnCloseRequest(closeEvent -> {
                            ListProduct();
                        });
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
        colDelete.setCellFactory(column -> new TableCell<Product, Boolean>() {
            private Button button = new Button("Delete");

            {
                button.setOnAction(event -> {
                    Product pro = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
                    Document Warehousecollection = new Document("Detail." + String.valueOf(pro.getId()), new Document("$exists", true));
                    long count = Warehouse.countDocuments(Warehousecollection);
                    if (count > 0) {
                        DialogAlert.DialogError("Delete fail");
                    } else {
                        Document filter = new Document("_id", pro.getId());
                        MongoCollection<Document> product = DBConnection.getConnection().getCollection("Product");
                        product.deleteOne(filter);
                        DialogAlert.DialogSuccess("Delete successfully");
                        ListProduct();
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

    private void ListProduct() {
        displaymode = 1;
        List<Product> product = daodb.ListProduct();
        ObservableList<Product> obserable = FXCollections.observableArrayList(product);
        totalItems = obserable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Product> as = obserable.subList(startIndex, endIndex);
        tblproduct.setItems(FXCollections.observableArrayList(as));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colprice.setCellValueFactory(new PropertyValueFactory<>("priceformat"));
        colupdate.setCellValueFactory(new PropertyValueFactory<>("isactive"));
        colDelete.setCellValueFactory(new PropertyValueFactory<>("isactive"));
        colupdate.setCellFactory(column -> new TableCell<Product, Boolean>() {
            private Button button = new Button("Update");

            {
                button.setOnAction(event -> {
                    Product pro = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/UpdateProduct.fxml"));
                    AnchorPane newpopup;
                    try {
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        ProductController product = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        product.EditName.setText(pro.getName());
                        String cleanedPriceString = pro.getPriceformat().replaceAll("[^\\d]", "");
                        int price = Integer.parseInt(cleanedPriceString);
                        product.EditNameprice.setText(String.valueOf(price));
                        product.EditnameCategory.setValue(pro.getCategory());
                        product.idProduct = pro.getId();
                        popupStage.setResizable(false);
                        popupStage.show();
                        popupStage.setOnCloseRequest(closeEvent -> {
                            ListProduct();
                        });
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
        colDelete.setCellFactory(column -> new TableCell<Product, Boolean>() {
            private Button button = new Button("Delete");

            {
                button.setOnAction(event -> {
                    Product pro = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
                    Document Warehousecollection = new Document("Detail." + String.valueOf(pro.getId()), new Document("$exists", true));
                    long count = Warehouse.countDocuments(Warehousecollection);
                    if (count > 0) {
                        DialogAlert.DialogError("Delete fail");
                    } else {
                        Document filter = new Document("_id", pro.getId());
                        MongoCollection<Document> product = DBConnection.getConnection().getCollection("Product");
                        product.deleteOne(filter);
                        DialogAlert.DialogSuccess("Delete successfully");
                        ListProduct();
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
    void AddProduct(ActionEvent event) {
        if (!NameProduct.getText().isEmpty() && !Price.getText().isEmpty()) {
            String priceString = Price.getText();

            try {

                int priceValue = Integer.parseInt(priceString);

                if (priceValue >= 0) {
                    MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Product");
                    Document document = new Document("Name", NameProduct.getText())
                            .append("Price", priceValue)
                            .append("ID_Category", getCategoryIDByName(Namecategory.getValue()));
                    categoryCollection.insertOne(document);
                    DialogAlert.DialogSuccess("Add product successfully");
                    NameProduct.setText("");
                    Price.setText("");

                } else {
                    DialogAlert.DialogError("Price must be a non-negative number");

                }
            } catch (NumberFormatException e) {

                DialogAlert.DialogError("Price is not valid");
            }
        } else {
            DialogAlert.DialogError("You must enter enough information");
        }

    }

    public Map<String, ObjectId> getCategoryNameToIdMap() {
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");
        Map<String, ObjectId> categoryNameToIdMap = new HashMap<>();

        FindIterable<Document> result = categoryCollection.find();
        for (Document document : result) {
            String categoryName = document.getString("Name");
            ObjectId categoryId = document.getObjectId("_id");
            categoryNameToIdMap.put(categoryName, categoryId);
        }

        return categoryNameToIdMap;
    }

    public ObjectId getCategoryIDByName(String categoryName) {
        Map<String, ObjectId> categoryNameToIdMap = getCategoryNameToIdMap();

        // Lấy ID từ Map
        return categoryNameToIdMap.get(categoryName);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        ListProduct();
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
           if(displaymode==1){
               ListProduct();
           }else if(displaymode==2){
               SearchlistProduct(textfield.getText(), currentPageIndex);
           }
        });
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");
        ObservableList<String> categoryList = FXCollections.observableArrayList();
        FindIterable<Document> result = categoryCollection.find();
        for (Document document : result) {
            String categoryName = document.getString("Name");
            categoryList.add(categoryName);
        }
        Namecategory.setItems(categoryList);
        if (!categoryList.isEmpty()) {
            Namecategory.setValue(categoryList.get(0));
        }
        EditnameCategory.setItems(categoryList);

    }

}
