/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.model.Category;
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
import javafx.scene.control.Button;
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
public class ListCategoryController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableColumn<?, ?> colName = new TableColumn<>();

    @FXML
    private TableColumn<Category, Boolean> colUpdate = new TableColumn<>();
    @FXML
    private TextField Namecategory;
    @FXML
    private Pagination pagination = new Pagination();
    private int itemsperPage = 5;
    private int totalItems;
    private int displaymode = 1;
    private int currentPageIndex = 0;
    @FXML
    private TableColumn<Category, Boolean> coldelete = new TableColumn<>();
    @FXML
    private TextField updatenameCategory;
    @FXML
    private TableView<Category> tblcategory = new TableView<>();
    private ObjectId idCategory;
    @FXML
    private TextField txtfind = new TextField();

    @FXML
    void find(ActionEvent event) {
        currentPageIndex = 0;
        searchCategory(txtfind.getText(), currentPageIndex);
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        } else if (currentPageIndex == 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }
    }

    @FXML
    void showaddcategory(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/AddCategory.fxml"));
        AnchorPane newpopup;
        try {
            newpopup = loader.load();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(newpopup));
            popupStage.setResizable(false);
            popupStage.show();
            popupStage.setOnCloseRequest(closeEvent -> {
                Listcategory();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    private void searchCategory(String text, int pageindex) {
        displaymode = 2;
        List<Category> cate = daodb.searchListCategory(txtfind.getText());
        ObservableList<Category> obserable = FXCollections.observableArrayList(cate);
        totalItems = obserable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
        if (obserable.isEmpty()) {
            pagination.setPageCount(1);
            tblcategory.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageindex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Category> Ass = obserable.subList(startIndex, endIndex);
        tblcategory.setItems(FXCollections.observableArrayList(Ass));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        coldelete.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        colUpdate.setCellFactory(column -> new TableCell<Category, Boolean>() {
            private Button button = new Button("Update");

            {
                button.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/UpdateCategory.fxml"));
                        AnchorPane newpopup;
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        ListCategoryController List = loader.getController();
                        List.updatenameCategory.setText(category.getName());
                        List.idCategory = category.getId();
                        popupStage.setResizable(false);
                        popupStage.show();
                        popupStage.setOnCloseRequest(closeEvent -> {
                            Listcategory();
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
        coldelete.setCellFactory(column -> new TableCell<Category, Boolean>() {
            private Button button = new Button("Delete");

            {
                button.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> Category = DBConnection.getConnection().getCollection("Category");
                    MongoCollection<Document> product = DBConnection.getConnection().getCollection("Product");
                    Document categoryfilter = new Document("ID_Category", category.getId());
                    long cout = product.countDocuments(categoryfilter);
                    if (cout <= 0) {
                        Document filter = new Document("_id", category.getId());
                        Category.deleteOne(filter);
                        DialogAlert.DialogSuccess("Delete successfully");
                        Listcategory();
                    } else {
                        DialogAlert.DialogError("Cannot Delete");
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

    private void Listcategory() {
        displaymode = 1;
        List<Category> cate = daodb.ListCategory();
        ObservableList<Category> obserable = FXCollections.observableArrayList(cate);
        totalItems = obserable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, totalItems);
        List<Category> as = obserable.subList(startIndex, endIndex);
        tblcategory.setItems(FXCollections.observableArrayList(as));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colUpdate.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        coldelete.setCellValueFactory(new PropertyValueFactory<>("isActive"));
        colUpdate.setCellFactory(column -> new TableCell<Category, Boolean>() {
            private Button button = new Button("Update");

            {
                button.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());

                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/UpdateCategory.fxml"));
                        AnchorPane newpopup;
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        ListCategoryController List = loader.getController();
                        List.updatenameCategory.setText(category.getName());
                        List.idCategory = category.getId();
                        popupStage.setResizable(false);
                        popupStage.show();
                        popupStage.setOnCloseRequest(closeEvent -> {
                            Listcategory();
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
        coldelete.setCellFactory(column -> new TableCell<Category, Boolean>() {
            private Button button = new Button("Delete");

            {
                button.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> Category = DBConnection.getConnection().getCollection("Category");
                    MongoCollection<Document> product = DBConnection.getConnection().getCollection("Product");
                    Document categoryfilter = new Document("ID_Category", category.getId());
                    long cout = product.countDocuments(categoryfilter);
                    if (cout <= 0) {
                        Document filter = new Document("_id", category.getId());
                        Category.deleteOne(filter);
                        DialogAlert.DialogSuccess("Delete successfully");
                        Listcategory();
                    } else {
                        DialogAlert.DialogError("Cannot Delete");
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
    void UpdateCategory(ActionEvent event) {
        if (!updatenameCategory.getText().isEmpty()) {
            MongoCollection<Document> Category = DBConnection.getConnection().getCollection("Category");
            MongoCollection<Document> product = DBConnection.getConnection().getCollection("Product");
            Document productcollection = new Document("ID_Category", idCategory);
            Document categorycollection = new Document("Name", updatenameCategory.getText());
            long count = product.countDocuments(productcollection);
            long coutexists = Category.countDocuments(categorycollection);
            if (count <= 0) {
                if (coutexists <= 0) {
                    Document filter = new Document("_id", idCategory);
                    Document update = new Document("$set", new Document("Name", updatenameCategory.getText()));
                    UpdateResult updateresult = Category.updateOne(filter, update);
                    if (updateresult.getModifiedCount() > 0) {
                        DialogAlert.DialogSuccess("Update successfully");
                    }
                } else {
                    DialogAlert.DialogError("Name is EXISTS");
                }
            } else {
                DialogAlert.DialogError("Update failed");
            }
        }

    }

    @FXML
    void Addcategory(ActionEvent event) {
        MongoCollection<Document> category = DBConnection.getConnection().getCollection("Category");
        Document document = new Document("Name", Namecategory.getText());
        long count = category.countDocuments(document);
        if (count > 0) {
            DialogAlert.DialogError("Name category is exist");
        } else {
            category.insertOne(document);
            DialogAlert.DialogSuccess("Add category successfully");
            Namecategory.setText("");
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        Listcategory();
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                Listcategory();
            } else if (displaymode == 2) {
                searchCategory(txtfind.getText(), currentPageIndex);
            }
        });
    }

}
