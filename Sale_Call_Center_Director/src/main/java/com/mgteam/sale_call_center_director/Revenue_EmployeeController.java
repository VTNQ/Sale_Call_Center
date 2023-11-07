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
        @FXML
    private TableColumn<?, ?> colEmployee;

    @FXML
    private TableColumn<?, ?> colQuality;

    @FXML
    private TableColumn<?, ?> coltotal;

    @FXML
    private TableView<revenue_statistics> tblEmployee;
    private void revenueEmployee(){
        List<revenue_statistics>renue=daodb.revenue_Employee();
        ObservableList<revenue_statistics>observable=FXCollections.observableArrayList(renue);
        tblEmployee.setItems(observable);
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        revenueEmployee();
    }    
    
}
