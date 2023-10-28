/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.connect.util.daodb;
import com.mgteam.sale_call_center_manager.model.Iventory;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author tranp
 */
public class InventoryController implements Initializable {

    @FXML
    private TableColumn<?, ?> ID_Iventory;

    @FXML
    private Pagination pagination=new Pagination();
    private int displaymode = 1;
    private int itemsperPage = 5;
    private int totalItems;
    private int currentPageIndex = 0;
    @FXML
    private TableColumn<?, ?> IDproduct;
    @FXML
    private TableColumn<?, ?> NameProduct;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private MFXTextField txtfind = new MFXTextField();
    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQuality;

    @FXML
    private TableView<Iventory> tblInventory;

    void SearchInventory(String name,int pageindex) {
        totalItems=2;
        List<Iventory> listiventory = daodb.SearchInventory(name);
        ObservableList<Iventory> searchInventorry = FXCollections.observableArrayList(listiventory);
        totalItems = searchInventorry.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
         if (listiventory.isEmpty()) {
            pagination.setPageCount(1);
            tblInventory.setItems(FXCollections.observableArrayList());
            return;
        }
          int startIndex = pageindex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Iventory>Ass=searchInventorry.subList(startIndex, endIndex);
        tblInventory.setItems(FXCollections.observableArrayList(Ass));
        ID_Iventory.setCellValueFactory(new PropertyValueFactory<>("id_Eventory"));
        IDproduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Qualiry"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        NameProduct.setCellValueFactory(new PropertyValueFactory<>("name_product"));
    }

    @FXML
    void findInventory(ActionEvent event) {
         currentPageIndex=0;
        SearchInventory(txtfind.getText(),currentPageIndex);
         if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }else if(currentPageIndex==0){
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }
    }

    private void diplayIventory() {
        totalItems=1;
        List<Iventory> InventoryList = daodb.getIventory();
        ObservableList<Iventory> Obserable = FXCollections.observableArrayList(InventoryList);
        totalItems = Obserable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Iventory> Ass = Obserable.subList(startIndex, endIndex);
        tblInventory.setItems(FXCollections.observableArrayList(Ass));
        ID_Iventory.setCellValueFactory(new PropertyValueFactory<>("id_Eventory"));
        IDproduct.setCellValueFactory(new PropertyValueFactory<>("product"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Qualiry"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("price"));
        NameProduct.setCellValueFactory(new PropertyValueFactory<>("name_product"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        diplayIventory();
        pagination.currentPageIndexProperty().addListener((obs,oldIndex,newIndedx)->{
        currentPageIndex=newIndedx.intValue();
        if(displaymode==1){
            diplayIventory();
        }else if(displaymode==2){
            SearchInventory(txtfind.getText(), currentPageIndex);
        }
        });
    }
}
