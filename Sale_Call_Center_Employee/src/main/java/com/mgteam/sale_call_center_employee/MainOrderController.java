package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.model.Order;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPagination;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class MainOrderController implements Initializable {
    
    private ObjectId id_order;

    @FXML
    private TableColumn<Order, Boolean> ListProduct = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> NameCustomer = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> NameEmployee = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> OrderDay = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> ShipDay = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> StatusOrder = new TableColumn<>();
    @FXML
    private TableView<Order> tblOrder = new TableView<>();

    @FXML
    private MFXPagination pagination = new MFXPagination();

    @FXML
    private MFXPagination PaginationProduct = new MFXPagination();

    public static List<com.mgteam.sale_call_center_employee.model.Order> ListOrder() {
        List<com.mgteam.sale_call_center_employee.model.Order> ArrayOrder = new ArrayList<>();
        try {
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> customerCollection = DBConnection.getConnection().getCollection("Customer");
            MongoCollection<Document> employeeCollection = DBConnection.getConnection().getCollection("Employee");
            Bson filterWithID = Filters.eq("id_Employee", LoginController.id_employee);

            FindIterable<Document> result = orderCollection.find(filterWithID);
            for (Document document : result) {
                ObjectId id = document.getObjectId("_id");
                ObjectId IdCustomer = document.getObjectId("id_Customer");
                ObjectId IdEmployee = document.getObjectId("id_Employee");
                Document CustomerAll = customerCollection.find(Filters.eq("_id", IdCustomer)).first();
                Document EmployeeAll = employeeCollection.find(Filters.eq("_id", IdEmployee)).first();
                String nameCustomer = CustomerAll.getString("Name");
                String nameemployee = EmployeeAll.getString("Name");
                String OrderDate = document.getString("Order_date");
                String ShipDate = document.getString("Ship_date");
                Integer status = document.getInteger("status");
                Document detailOrder = (Document) document.get("DetailOrder");
                ArrayOrder.add(new Order(id, IdCustomer, IdEmployee, OrderDate, ShipDate, status, detailOrder, nameCustomer, nameemployee));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ArrayOrder;
    }

    private void ListOrderCustomer() {
        List<Order> OrderCustomer = ListOrder();
        ObservableList<Order> obserableList = FXCollections.observableArrayList(OrderCustomer);
        tblOrder.setItems(obserableList);
        NameCustomer.setCellValueFactory(new PropertyValueFactory<>("NameCustomer"));
        NameEmployee.setCellValueFactory(new PropertyValueFactory<>("NameEmployee"));
        OrderDay.setCellValueFactory(new PropertyValueFactory<>("Order_date"));
        ShipDay.setCellValueFactory(new PropertyValueFactory<>("Ship_date"));
        ListProduct.setCellValueFactory(new PropertyValueFactory<>("Product"));
        ListProduct.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private MFXButton button = new MFXButton("Detail");

            {
                button.setOnAction(event -> {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("view/DetailProduct.fxml"));
                    Order orders=getTableView().getItems().get(getIndex());
                    id_order=orders.getId();
                    try {
                        AnchorPane Detail = loader.load();
                        Stage stage = new Stage();
                        stage.initModality(Modality.WINDOW_MODAL);
                        stage.setScene(new Scene(Detail));
                        stage.setResizable(false);
                        stage.showAndWait();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
        pagination.setCurrentPage(0);
        pagination.setMaxPage(OrderCustomer.size());
    }

    public static List<com.mgteam.sale_call_center_employee.model.Product> ListProduct() {
        List<com.mgteam.sale_call_center_employee.model.Product>ArrayProduct=new ArrayList<>();
        
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListOrderCustomer();
    }

}
