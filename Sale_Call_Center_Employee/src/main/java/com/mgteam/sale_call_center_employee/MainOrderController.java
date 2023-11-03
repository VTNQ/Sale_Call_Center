package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.model.Order;
import com.mgteam.sale_call_center_employee.model.Product;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXPagination;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    private TableColumn<?, ?> colPrice;

    @FXML
    private MFXTextField txtSearch;

    @FXML
    private TableColumn<?, ?> colDelete = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colIdProduct = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colNameProduct = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> col_Price = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colQuantity = new TableColumn<>();

    @FXML
    private MFXComboBox<String> listProduct = new MFXComboBox<>();

    @FXML
    private TableView<Product> listProductOrder = new TableView<>();

    @FXML
    private TableColumn<?, ?> colTotalPrice;

    @FXML
    private MFXTextField quantity = new MFXComboBox();

    @FXML
    private Label totalPrice;

    private Document list_Product;

    List<Product> list = new ArrayList<>();

    @FXML
    void add(ActionEvent event) {
        if (!listProduct.getSelectedItem().isEmpty()) {
            MongoCollection<Document> collection = DBConnection.getConnection().getCollection("Product");
            FindIterable<Document> documents = collection.find(Filters.eq("Name", listProduct.getSelectedItem()));
            int index = -1;
            String selectedProduct = listProduct.getSelectedItem();
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getName().equals(selectedProduct)) {
                    index = i;
                    break;
                }
            }
            for (Document document : documents) {
                if (index == -1) {
                    list.add(new Product(listProduct.getSelectedItem(), Integer.parseInt(quantity.getText()), Math.abs(document.getObjectId("_id").hashCode()), document.get("Price").hashCode()));
                } else {
                    Product product = list.get(index);
                    int oldQuantity = product.getQuality();
                    int newQuantity = Integer.parseInt(quantity.getText()) + oldQuantity;
                    product.setQuality(newQuantity);
                }
                ObservableList<Product> observableList = FXCollections.observableArrayList(list);
                listProductOrder.setItems(observableList);
                listProductOrder.getItems().setAll(list);
                colIdProduct.setCellValueFactory(new PropertyValueFactory<>("id_product"));
                colNameProduct.setCellValueFactory(new PropertyValueFactory<>("Name"));
                colQuantity.setCellValueFactory(new PropertyValueFactory<>("Quality"));
                col_Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
            }
        }
    }

    @FXML
    void create(ActionEvent event) {

    }

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

    public static List<com.mgteam.sale_call_center_employee.model.Order> ListOrderWithKey(String Key) {
        List<com.mgteam.sale_call_center_employee.model.Order> ArrayOrder = new ArrayList<>();
        try {
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> customerCollection = DBConnection.getConnection().getCollection("Customer");
            MongoCollection<Document> employeeCollection = DBConnection.getConnection().getCollection("Employee");
            Bson filterWithID = Filters.and(Filters.eq("id_Employee", LoginController.id_employee));

            FindIterable<Document> result = orderCollection.find(filterWithID);
            for (Document document : result) {
                id_order = document.getObjectId("_id").hashCode();
                ObjectId IdCustomer = document.getObjectId("id_Customer");
                ObjectId IdEmployee = document.getObjectId("id_Employee");
                Document CustomerAll = customerCollection.find(Filters.eq("_id", IdCustomer)).first();
                Document EmployeeAll = employeeCollection.find(Filters.eq("_id", IdEmployee)).first();
                Pattern regexPattern = Pattern.compile(".*" + Key + ".*", Pattern.CASE_INSENSITIVE);
                int id_filter = Math.abs(id_order);
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
                    boolean isSimilar1 = regexPattern.matcher(String.valueOf(id_filter)).matches();
                    boolean isSimilar2 = regexPattern.matcher(nameCustomer).matches();
                    if (String.valueOf(id_order).matches(Key) || nameCustomer.matches(Key) || isSimilar1 || isSimilar2) {
                        Document detailOrder = (Document) document.get("DetailOrder");
                        ArrayOrder.add(new Order(document.getObjectId("_id"), IdCustomer, IdEmployee, OrderDate, ShipDate, Status, detailOrder, nameCustomer, nameemployee, id_order));
                    }
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
                        main.displayProduct(orders.getId());
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
        List<Order> OrderCustomer = ListOrderWithKey(txtSearch.getText());
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
                        main.displayProduct(orders.getId());
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

    private void displayProduct(ObjectId idorder) {
        List<Product> productshow = ListProduct(idorder);
        ObservableList<Product> obserable = FXCollections.observableArrayList(productshow);
        tblProduct.setItems(obserable);
        idproduct.setCellValueFactory(new PropertyValueFactory<>("Id_product"));
        Nameproduct.setCellValueFactory(new PropertyValueFactory<>("Name"));
        colquality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
    }

    public static List<com.mgteam.sale_call_center_employee.model.Product> ListProduct(ObjectId idorder) {
        List<com.mgteam.sale_call_center_employee.model.Product> ArrayProduct = new ArrayList<>();
        MongoCollection<Document> OrderCollection = DBConnection.getConnection().getCollection("Order");
        MongoCollection<Document> ProductCollection = DBConnection.getConnection().getCollection("Product");
        FindIterable<Document> result = ProductCollection.find();
        for (Document document : result) {
            ObjectId idproduct = document.getObjectId("_id");
            Document query1 = new Document("DetailOrder." + String.valueOf(idproduct), new Document("$exists", true));
            query1.append("_id", idorder);
            Document detailWarehouse = OrderCollection.find(query1).first();
            if (detailWarehouse != null) {
                String nameProduct = document.getString("Name");
                Document Detail = (Document) detailWarehouse.get("DetailOrder");
                Document idcol = (Document) Detail.get(String.valueOf(idproduct));
                if (idcol != null) {
                    ArrayProduct.add(new Product(nameProduct, idcol.getInteger("Quality"), Math.abs(idproduct.hashCode()), document.getInteger("Price")));
                }
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
            stage.setScene(new Scene(anchorPane, 648, 480));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static List<String> ListProductAll() {
        List<String> ArrayProduct = new ArrayList<>();
        MongoCollection<Document> ProductCollection = DBConnection.getConnection().getCollection("Product");
        MongoIterable<Document> products = ProductCollection.find();
        for (Document product : products) {
            ArrayProduct.add(product.getString("Name"));
        }
        return ArrayProduct;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListOrderCustomer();
        listProduct.getItems().addAll(ListProductAll());
    }
}
