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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author tranp
 */
public class Revenue_EmployeeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private int itemsperPage = 5;
    private int totalItems;
    private int displaymode = 1;
    private int currentPageIndex = 0;
        @FXML
    private ComboBox<String> year=new ComboBox<>();
        @FXML
    private TableColumn<?, ?> colEmployee;
        
    @FXML
    private ComboBox<String> month=new ComboBox<>();
    @FXML
    private ComboBox<String> precious=new ComboBox<>();
 @FXML
    private Pagination pagination = new Pagination();
    @FXML
    private TableColumn<?, ?> colQuality;

    @FXML
    private TableColumn<?, ?> coltotal;

    @FXML
    private TableView<revenue_statistics> tblEmployee;
    private void renueEmployeeyear(String year){
        displaymode=2;
         List<revenue_statistics>renue=daodb.revenue_Employeeyear(year);
        ObservableList<revenue_statistics>observable=FXCollections.observableArrayList(renue);
        totalItems = observable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
         List<revenue_statistics> as = observable.subList(startIndex, endIndex);
        tblEmployee.setItems(FXCollections.observableArrayList(as));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    private void revenueEmployee(){
        displaymode=1;
        List<revenue_statistics>renue=daodb.revenue_Employee();
        ObservableList<revenue_statistics>observable=FXCollections.observableArrayList(renue);
         totalItems = observable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
         List<revenue_statistics> as = observable.subList(startIndex, endIndex);
        tblEmployee.setItems(FXCollections.observableArrayList(as));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    private void revenEmployeeprice(String price){
        displaymode=3;
        List<revenue_statistics>renue=daodb.revenue_Employeeprince(price);
        ObservableList<revenue_statistics>observable=FXCollections.observableArrayList(renue);
         totalItems = observable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
         List<revenue_statistics> as = observable.subList(startIndex, endIndex);
        tblEmployee.setItems(FXCollections.observableArrayList(as));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    private void revenEmployeemonth(String month){
         displaymode=4;
        List<revenue_statistics>renue=daodb.revenue_Employeemonth(month);
        ObservableList<revenue_statistics>observable=FXCollections.observableArrayList(renue);
         totalItems = observable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
         List<revenue_statistics> as = observable.subList(startIndex, endIndex);
        tblEmployee.setItems(FXCollections.observableArrayList(as));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        revenueEmployee();
         ObservableList<String> month = FXCollections.observableArrayList(
                "All year","January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
          this.month.setItems(month);
          this.month.setValue("All year");
        
          ObservableList<String> months = FXCollections.observableArrayList(
                "All Years", "2022", "2023", "2024");
        year.setItems(months);
        year.setValue("All Years");
        year.setOnAction(event->{
            renueEmployeeyear(year.getValue());
        });
        ObservableList<String> precious = FXCollections.observableArrayList(
                "All Q", "Q1", "Q2", "Q3", "Q4");
        this.precious.setItems(precious);
        this.precious.setValue("All Q");
         this.precious.setOnAction(event->{
            revenEmployeeprice(this.precious.getValue());
        });
           this.month.setOnAction(event->{
              revenEmployeemonth(this.month.getValue());
          });
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
           if(displaymode==1){
               revenueEmployee();
           } else if(displaymode==2){
               renueEmployeeyear(year.getValue());
           }else if(displaymode==3){
                revenEmployeeprice(this.precious.getValue());
           }else if(displaymode==4){
                 revenEmployeemonth(this.month.getValue());
           }
            revenueEmployee();
           
        });
    }    
    
}
