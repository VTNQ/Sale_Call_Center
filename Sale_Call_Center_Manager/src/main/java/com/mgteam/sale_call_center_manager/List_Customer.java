/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.connect.util.daodb;
import com.mgteam.sale_call_center_manager.model.Customer;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author tranp
 */
public class List_Customer implements Initializable {

    @FXML
    private TableColumn<?, ?> colEndorder = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> colDemand = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colEmployee = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colidorder = new TableColumn<>();
    @FXML
    private Pagination detailPagination=new Pagination();

    @FXML
    private TableView<Customer> tblDetailPushedCustomer = new TableView<>();
    @FXML
    private TableColumn<?, ?> colName = new TableColumn<>();
    @FXML
    private Label Customer;
    @FXML
    private TableColumn<Customer, Boolean> colOrder = new TableColumn<>();
    @FXML
    private Pagination pagination=new Pagination();
    private int itemsperPage = 5;
    private int totalItems;
    private String Name;
    private int displaymode = 1;
    private int currentPageIndex = 0;
    @FXML
    private TableColumn<?, ?> colStartOrder = new TableColumn<>();

    @FXML
    private TableView<Customer> tblCustomer = new TableView<>();
    @FXML
    private MFXTextField txtfind;

    private void searchFilter(String name,int pageIndex) {
        displaymode=2;
        List<Customer> customerAll = daodb.SearchListCustomer(name);
        ObservableList<Customer> obserable = FXCollections.observableArrayList(customerAll);
         totalItems = obserable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
        if (customerAll.isEmpty()) {
            pagination.setPageCount(1);
            tblCustomer.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);

        List<Customer> Ass = obserable.subList(startIndex, endIndex);
        tblCustomer.setItems(FXCollections.observableArrayList(Ass));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colStartOrder.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndorder.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        colOrder.setCellValueFactory(new PropertyValueFactory<>("order"));
    }

    @FXML
    void find(ActionEvent event) {
        currentPageIndex=0;
        searchFilter(txtfind.getText(),currentPageIndex);
          if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }else if(currentPageIndex==0){
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }
    }

    private void DetailpurchasedOrder(String Name) {
        List<Customer> CutomerAll = daodb.getdetailpushedCustomer(Name);
        ObservableList<Customer> obserable = FXCollections.observableArrayList(CutomerAll);
        totalItems = obserable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        detailPagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Customer> as = obserable.subList(startIndex, endIndex);
        tblDetailPushedCustomer.setItems(FXCollections.observableArrayList(as));
        colidorder.setCellValueFactory(new PropertyValueFactory<>("id_order"));
        colDemand.setCellValueFactory(new PropertyValueFactory<>("Demand"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
    }

    private void List_Customer() {
        displaymode=1;
        List<Customer> CustomerAll = daodb.ListCustomer();
        ObservableList<Customer> observable = FXCollections.observableArrayList(CustomerAll);
        totalItems = observable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Customer> as = observable.subList(startIndex, endIndex);
        tblCustomer.setItems(FXCollections.observableArrayList(as));
        colName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colStartOrder.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEndorder.setCellValueFactory(new PropertyValueFactory<>("EndDate"));
        colOrder.setCellValueFactory(new PropertyValueFactory<>("order"));
        colOrder.setCellFactory(column -> new TableCell<Customer, Boolean>() {
            private MFXButton button = new MFXButton("History order");

            {
                button.setOnAction(event -> {
                    try {
                        Customer customer = getTableView().getItems().get(getIndex());
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Detail_customer.fxml"));
                        AnchorPane pane = loader.load();
                        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), pane);
                        fadeTransition.setFromValue(0);
                        fadeTransition.setToValue(1);
                        List_Customer list = loader.getController();
                        list.Customer.setText(customer.getName());
                        list.Name=customer.getName();
                        list.DetailpurchasedOrder(customer.getName());
                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(pane));
                        popupStage.setResizable(false);
                        fadeTransition.play();
                        popupStage.showAndWait();

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
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List_Customer();
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if(displaymode==1){
                List_Customer();
            }else if(displaymode==2){
                searchFilter(txtfind.getText(),currentPageIndex);
            }
            List_Customer();
        });
        detailPagination.currentPageIndexProperty().addListener((obs,oldIndex,newIndex)->{
        currentPageIndex=newIndex.intValue();
            DetailpurchasedOrder(Name);
        });
    }
}
