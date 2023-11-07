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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
    private TableColumn<?, ?> colQuality;

    @FXML
    private TableColumn<?, ?> colcategory;

    @FXML
    private TableColumn<?, ?> colproduct;
    @FXML
    private Label reverse;
    @FXML
    private TableColumn<?, ?> colsupply;

    @FXML
    private TableColumn<?, ?> coltotal;

    @FXML
    private TableView<revenue_statistics> tblrevenue;
    private void revenue_statistics(){
        List<revenue_statistics>revenue=daodb.revenue_Statistics();
        ObservableList<revenue_statistics>observable=FXCollections.observableArrayList(revenue);
        tblrevenue.setItems(observable);
        colproduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colcategory.setCellValueFactory(new PropertyValueFactory<>("category"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        coltotal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        revenue_statistics();
        reverse.setText(daodb.calculateTotalRevenue());
    }    
    
}
