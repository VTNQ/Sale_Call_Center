package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.model.Order;
import com.mgteam.sale_call_center_employee.model.Product;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXPagination;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private static ObjectId id_order;

    @FXML
    private TableColumn<Order, Boolean> ListProduct = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> Nameproduct=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colquality=new TableColumn<>();

    @FXML
    private TableColumn<?, ?> idproduct=new TableColumn<>();

    @FXML
    private TableView<Product> tblProduct=new TableView<>();
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
                    Order orders = getTableView().getItems().get(getIndex());
                    id_order = orders.getId();
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
        Map<String, Integer> productQuantityMap = new HashMap<>();
        List<com.mgteam.sale_call_center_employee.model.Product> ArrayProduct = new ArrayList<>();
        MongoCollection<Document> OrderCollection = DBConnection.getConnection().getCollection("Order");
        MongoCollection<Document> ProductCollection = DBConnection.getConnection().getCollection("Product");
        FindIterable<Document> result = OrderCollection.find(Filters.eq("_id", id_order));
        for (Document document : result) {
            Document detailOrder = (Document) document.get("DetailOrder");
            List<Object> idProducts = (List<Object>) detailOrder.get("id_Product");
            for (Object id_product : idProducts) {
                if (id_product instanceof String) {
                    ObjectId id = new ObjectId((String) id_product);
                    Document product_collection = ProductCollection.find(new Document("_id", id)).first();
                    if (product_collection != null) {
                        String productName = product_collection.getString("Name");
                        if (productQuantityMap.containsKey(productName)) {
                            int existingQuantity = productQuantityMap.get(productName);
                            productQuantityMap.put(productName, existingQuantity + 1);
                        } else {
                            productQuantityMap.put(productName, 1);
                        }
                    }
                }
            }
        }
        for (Map.Entry<String, Integer> entry : productQuantityMap.entrySet()) {
            String productName = entry.getKey();
            int quality = entry.getValue();
            int idProduct = getidProduct(ProductCollection, productName);
            ArrayProduct.add(new Product(productName, quality, idProduct));
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListOrderCustomer();

    }

}
