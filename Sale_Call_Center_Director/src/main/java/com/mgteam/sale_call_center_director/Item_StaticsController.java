/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mgteam.sale_call_center_director;

import com.mgteam.sale_call_center_director.util.daodb;
import com.mgteam.sale_call_center_director.util.model.product;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.types.ObjectId;

/**
 * FXML Controller class
 *
 * @author tranp
 */
public class Item_StaticsController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TableColumn<?, ?> Name=new TableColumn<>();
    @FXML
    private TableView<product> tblstatistics=new TableView<>();
    @FXML
    private TableColumn<?, ?> category=new TableColumn<>();

    @FXML
    private TableColumn<product, Boolean> detail=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> totalorder=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> Customer=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> date=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> order=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> status=new TableColumn<>();
    @FXML
    private Label product=new Label();
    @FXML
    private TableView<product> tbldetail=new TableView<>();
    @FXML
    private TableColumn<?, ?> totalproduct=new TableColumn<>();
    private void detailStatics(ObjectId id){
        List<product>product=daodb.ordernotyeprocessed(id);
        ObservableList<product>observable=FXCollections.observableArrayList(product);
        tbldetail.setItems(observable);
        order.setCellValueFactory(new PropertyValueFactory<>("order"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        Customer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        status.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void productStatics(){
        List<product>product=daodb.listProductTotalQualitySoldAndCategoryName();
        ObservableList<product>observable=FXCollections.observableArrayList(product);
        tblstatistics.setItems(observable);
        Name.setCellValueFactory(new PropertyValueFactory<>("product"));
        category.setCellValueFactory(new PropertyValueFactory<>("Category"));
        totalproduct.setCellValueFactory(new PropertyValueFactory<>("totalproduct"));
        totalorder.setCellValueFactory(new PropertyValueFactory<>("ordercount"));
        detail.setCellValueFactory(new PropertyValueFactory<>("istatic"));
        detail.setCellFactory(column->new TableCell<product,Boolean>(){
        private Button button=new Button("Detail");
                {
                button.setOnAction(event->{
                    product pr=getTableView().getItems().get(getIndex());
             FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_director/staticsorder.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Stage popupStage = new Stage();
                        Item_StaticsController item=loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        item.detailStatics(pr.getIdproduct());
                        item.product.setText("Product:"+pr.getProduct());
                        popupStage.setResizable(false);
                        popupStage.showAndWait();
                    } catch (IOException ex1) {
                        ex1.printStackTrace();
                    }

                });
                }
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty); 
                button.getStyleClass().add("button-design");
                if(item!=null || !empty){
                    setGraphic(button);
                }else{
                    setGraphic(null);
                }
            }
        
        });
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        productStatics();
    }    
    
}
