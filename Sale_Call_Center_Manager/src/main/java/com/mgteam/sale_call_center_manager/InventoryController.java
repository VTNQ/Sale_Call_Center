/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.model.Iventory;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 *
 * @author tranp
 */
public class InventoryController {
     @FXML
    private TableColumn<?, ?> ID_Iventory;

    @FXML
    private TableColumn<?, ?> NameProduct;

    @FXML
    private TableColumn<?, ?> colDate;

    @FXML
    private TableColumn<?, ?> colPrice;

    @FXML
    private TableColumn<?, ?> colQuality;

    @FXML
    private TableView<Iventory> tblInventory;
    
}
