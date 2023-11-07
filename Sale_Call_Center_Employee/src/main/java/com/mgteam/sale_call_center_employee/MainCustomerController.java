package com.mgteam.sale_call_center_employee;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import io.github.palexdev.materialfx.controls.MFXPagination;
import io.github.palexdev.materialfx.controls.MFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainCustomerController extends MainController implements Initializable {

    @FXML
    private TableColumn<?, ?> IdCustomer;

    @FXML
    private TableColumn<?, ?> ListProduct;

    @FXML
    private TableColumn<?, ?> NameCustomer;

    @FXML
    private TableColumn<?, ?> NameEmployee;

    @FXML
    private TableColumn<?, ?> NearBuy;

    @FXML
    private TableColumn<?, ?> StartBuy;

    @FXML
    private TableColumn<?, ?> StatusOrder;

    @FXML
    private MFXPagination pagination;

    @FXML
    private TableView<?> tblOrder;

    @FXML
    private MFXTextField txtSearch;
    
    @FXML
    private MFXTextField address;

    @FXML
    private MFXComboBox<?> age;

    @FXML
    private MFXTextField name;

    @FXML
    private MFXTextField phone;

    @FXML
    void Search(ActionEvent event) {

    }

    @FXML
    void addCustomer(ActionEvent event) {
        
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
  
    }    
    
}
