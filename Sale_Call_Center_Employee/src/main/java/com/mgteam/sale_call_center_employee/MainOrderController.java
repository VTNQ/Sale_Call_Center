package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.model.Order;
import com.mgteam.sale_call_center_employee.model.Product;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPagination;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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

public class MainOrderController extends MainController implements Initializable {

    private static int id_order;

    @FXML
    private TableColumn<?, ?> IdOrder = new TableColumn<>();

    @FXML
    private TableColumn<Order, Boolean> ListProduct = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> Nameproduct = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colquality = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> idproduct = new TableColumn<>();

    @FXML
    private TableView<Product> tblProduct = new TableView<>();
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

    @FXML
    private MFXTextField txtSearch;

    public static List<com.mgteam.sale_call_center_employee.model.Order> ListOrder() {
        List<com.mgteam.sale_call_center_employee.model.Order> ArrayOrder = new ArrayList<>();
        try {
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> customerCollection = DBConnection.getConnection().getCollection("Customer");
            MongoCollection<Document> employeeCollection = DBConnection.getConnection().getCollection("Employee");
            Bson filterWithID = Filters.eq("id_Employee", LoginController.id_employee);

            FindIterable<Document> result = orderCollection.find(filterWithID);
            for (Document document : result) {
                id_order = document.getObjectId("_id").hashCode();
                ObjectId IdCustomer = document.getObjectId("id_Customer");
                ObjectId IdEmployee = document.getObjectId("id_Employee");
                Document CustomerAll = customerCollection.find(Filters.eq("_id", IdCustomer)).first();
                Document EmployeeAll = employeeCollection.find(Filters.eq("_id", IdEmployee)).first();
                String nameCustomer = CustomerAll.getString("Name");
                String nameemployee = EmployeeAll.getString("Name");
                String OrderDate = document.getString("Order_date");
                String ShipDate = document.getString("Ship_date");
                int status = document.getInteger("status");
                String Status = "";
                switch (status) {
                    case 0:
                        Status = "pending";
                        break;
                    case 1:
                        Status = "Waiting for delivery";
                        break;
                    case 2:
                        Status = "Ongoing deliveries";
                        break;
                    case 3:
                        Status = "Delivered";
                        break;
                    case 4:
                        Status = "Cancelled";
                        break;
                }
                Document detailOrder = (Document) document.get("DetailOrder");
                ArrayOrder.add(new Order(document.getObjectId("_id"), IdCustomer, IdEmployee, OrderDate, ShipDate, Status, detailOrder, nameCustomer, nameemployee, id_order));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ArrayOrder;
    }

    public static List<com.mgteam.sale_call_center_employee.model.Order> ListOrderWithKey() {
        List<com.mgteam.sale_call_center_employee.model.Order> ArrayOrder = new ArrayList<>();
        try {
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> customerCollection = DBConnection.getConnection().getCollection("Customer");
            MongoCollection<Document> employeeCollection = DBConnection.getConnection().getCollection("Employee");
            Bson filterWithID = Filters.and(Filters.eq("id_Employee", LoginController.id_employee), Filters.or(Filters.eq("_id", id_order), Filters.eq("z", id_order)));

            FindIterable<Document> result = orderCollection.find(filterWithID);
            for (Document document : result) {
                id_order = document.getObjectId("_id").hashCode();
                ObjectId IdCustomer = document.getObjectId("id_Customer");
                ObjectId IdEmployee = document.getObjectId("id_Employee");
                Document CustomerAll = customerCollection.find(Filters.eq("_id", IdCustomer)).first();
                Document EmployeeAll = employeeCollection.find(Filters.eq("_id", IdEmployee)).first();
                if (CustomerAll != null && EmployeeAll != null) {
                    String nameCustomer = CustomerAll.getString("Name");
                    String nameemployee = EmployeeAll.getString("Name");
                    String OrderDate = document.getString("Order_date");
                    String ShipDate = document.getString("Ship_date");
                    int status = document.getInteger("status");
                String Status = "";
                switch (status) {
                    case 0:
                        Status = "pending";
                        break;
                    case 1:
                        Status = "Waiting for delivery";
                        break;
                    case 2:
                        Status = "Ongoing deliveries";
                        break;
                    case 3:
                        Status = "Delivered";
                        break;
                    case 4:
                        Status = "Cancelled";
                        break;
                }
                    Document detailOrder = (Document) document.get("DetailOrder");
                    ArrayOrder.add(new Order(document.getObjectId("_id"), IdCustomer, IdEmployee, OrderDate, ShipDate, Status, detailOrder, nameCustomer, nameemployee, id_order));
                }

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
        IdOrder.setCellValueFactory(new PropertyValueFactory<>("id_order"));
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
                    Order orders = getTableView().getItems().get(getIndex());
                    id_order = orders.getId().hashCode();
                    try {
                        AnchorPane Detail = loader.load();
                        Stage stage = new Stage();
                        MainOrderController main = loader.getController();
                        main.displayProduct();
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
        StatusOrder.setCellValueFactory(new PropertyValueFactory<>("Status"));
        pagination.setCurrentPage(0);
        pagination.setMaxPage(OrderCustomer.size());
    }

    private void ListOrderCustomerWithKey() {
        List<Order> OrderCustomer = ListOrderWithKey();
        ObservableList<Order> obserableList = FXCollections.observableArrayList(OrderCustomer);
        tblOrder.setItems(obserableList);
        IdOrder.setCellValueFactory(new PropertyValueFactory<>("id_order"));
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
                    Order orders = getTableView().getItems().get(getIndex());
                    id_order = orders.getId().hashCode();
                    try {
                        AnchorPane Detail = loader.load();
                        Stage stage = new Stage();
                        MainOrderController main = loader.getController();
                        main.displayProduct();
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
        StatusOrder.setCellValueFactory(new PropertyValueFactory<>("Status"));
        pagination.setCurrentPage(0);
        pagination.setMaxPage(OrderCustomer.size());
    }

    private void displayProduct() {
        List<Product> productshow = ListProduct();
        ObservableList<Product> obserable = FXCollections.observableArrayList(productshow);
        tblProduct.setItems(obserable);
        idproduct.setCellValueFactory(new PropertyValueFactory<>("Id_product"));
        Nameproduct.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colquality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
    }

    public static List<com.mgteam.sale_call_center_employee.model.Product> ListProduct() {
        List<com.mgteam.sale_call_center_employee.model.Product> ArrayProduct = new ArrayList<>();
        MongoCollection<Document> OrderCollection = DBConnection.getConnection().getCollection("Order");
        MongoCollection<Document> ProductCollection = DBConnection.getConnection().getCollection("Product");
        FindIterable<Document> result = OrderCollection.find();
        for (Document document : result) {
            Document detailOrder = (Document) document.get("DetailOrder");
            List<Object> idProducts = (List<Object>) detailOrder.get("id_Product");
            for (Object id_product : idProducts) {
                System.out.println(id_product);
            }
        }

        return ArrayProduct;
    }

    private static int getidProduct(MongoCollection<Document> productCollection, String productName) {
        Document product = productCollection.find(new Document("Name", productName)).first();
        if (product != null) {
            return Math.abs(product.getObjectId("_id").hashCode());
        }
        return 0;
    }

    @FXML
    void Search(ActionEvent event) {
        if (txtSearch.getText().isEmpty()) {
            ListOrderCustomer();
        } else {
            ListOrderCustomerWithKey();
        }
    }

    @FXML
    void addOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/AddOrder.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(anchorPane, 400, 300));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListOrderCustomer();
    }

}
