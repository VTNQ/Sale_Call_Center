/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mgteam.sale_call_center_director;

import com.mgteam.sale_call_center_director.util.daodb;
import com.mgteam.sale_call_center_director.util.model.revenue_statistics;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author tranp
 */
public class Revenue_statisticsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ComboBox<String> precious = new ComboBox<>();
    
    @FXML
    private Pagination pagination=new Pagination();
    @FXML
    private TableColumn<?, ?> colQuality;
    @FXML
    private ComboBox<String> month=new ComboBox<>();
    @FXML
    private TableColumn<?, ?> colcategory;
    @FXML
    private ComboBox<String> year = new ComboBox<>();
    @FXML
    private TableColumn<?, ?> colproduct;
   
    private int itemsperPage = 5;
    private int totalItems;
    private int displaymode = 1;
    private int currentPageIndex = 0;
    @FXML
    private Label reverse;
    @FXML
    private TableColumn<?, ?> colsupply;

    @FXML
    private TableColumn<?, ?> coltotal;

    @FXML
    private TableView<revenue_statistics> tblrevenue;

    private void revenue_statistics() {
        displaymode = 1;
        List<revenue_statistics> revenue = daodb.revenue_Statistics();
        ObservableList<revenue_statistics> observable = FXCollections.observableArrayList(revenue);
        totalItems = observable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<revenue_statistics> as = observable.subList(startIndex, endIndex);
        tblrevenue.setItems(FXCollections.observableArrayList(as));
        colproduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colcategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    private void filteryear(String year) {
        displaymode = 2;
        List<revenue_statistics> revenue = daodb.filterYear(year);
        ObservableList<revenue_statistics> observable = FXCollections.observableArrayList(revenue);
        totalItems = observable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<revenue_statistics> as = observable.subList(startIndex, endIndex);
        tblrevenue.setItems(FXCollections.observableArrayList(as));
        colproduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colcategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    private void filtermonth(String month){
         displaymode=4;
    List<revenue_statistics> revenue = daodb.filtermonth(month);
        ObservableList<revenue_statistics> observable = FXCollections.observableArrayList(revenue);
        totalItems = observable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<revenue_statistics> as = observable.subList(startIndex, endIndex);
        tblrevenue.setItems(FXCollections.observableArrayList(as));
        colproduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colcategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
private void filterprince(String prince){
    displaymode=3;
    List<revenue_statistics> revenue = daodb.filterQuarter(prince);
        ObservableList<revenue_statistics> observable = FXCollections.observableArrayList(revenue);
        totalItems = observable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<revenue_statistics> as = observable.subList(startIndex, endIndex);
        tblrevenue.setItems(FXCollections.observableArrayList(as));
        colproduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colcategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
}
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        revenue_statistics();
          ObservableList<String> month = FXCollections.observableArrayList(
                "All year","January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
          this.month.setItems(month);
          this.month.setValue("All year");
          this.month.setOnAction(event->{
              filtermonth(this.month.getValue());
          });
        ObservableList<String> months = FXCollections.observableArrayList(
                "All Years", "2022", "2023", "2024");
        ObservableList<String> precious = FXCollections.observableArrayList(
                "All", "1", "2", "3", "4");
        this.precious.setItems(precious);
        this.precious.setValue("All");
        year.setItems(months);
        year.setValue("All Years");
        reverse.setText(daodb.calculateTotalRevenue());
        year.setOnAction(event -> {
            filteryear(year.getValue());
        });
        this.precious.setOnAction(event->{
            filterprince(this.precious.getValue());
        });
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                revenue_statistics();
            } else if (displaymode == 2) {
                filteryear(year.getValue());
            }else if(displaymode==3){
                filterprince(this.precious.getValue());
            }else if(displaymode==4){
                  filtermonth(this.month.getValue());
            }
            revenue_statistics();
        });
    }
}
