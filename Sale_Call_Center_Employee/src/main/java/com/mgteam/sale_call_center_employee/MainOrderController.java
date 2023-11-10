package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.model.Order;
import com.mgteam.sale_call_center_employee.model.Product;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXPagination;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class MainOrderController extends MainController implements Initializable {

    private static int id_order;
    @FXML
    private MFXTextField address;

    @FXML
    private MFXDatePicker age;

    @FXML
    private MFXTextField name;

    @FXML
    private MFXTextField phone;

    @FXML
    private TableColumn<?, ?> IdOrder = new TableColumn<>();

    @FXML
    private TableColumn<Order, Boolean> ListProduct = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> Nameproduct = new TableColumn<>();
    private ObjectId idlastorder;

    private void setidlastorder(ObjectId idorder) {
        this.idlastorder = idorder;
    }

    private ObjectId getidlastorder() {
        return idlastorder;
    }
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
    private TableColumn<Order, String> StatusOrder = new TableColumn<>();
    @FXML
    private TableView<Order> tblOrder = new TableView<>();

    @FXML
    private MFXPagination pagination = new MFXPagination();

    @FXML
    private MFXPagination PaginationProduct = new MFXPagination();

    @FXML
    private TableColumn<?, ?> colPrice = new TableColumn<>();

    @FXML
    private MFXTextField txtSearch;

    @FXML
    private TableColumn<Product, Boolean> colDelete = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colIdProduct = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colNameProduct = new TableColumn<>();
    String nameproduct;
    @FXML
    private TableColumn<?, ?> col_Price = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colQuantity = new TableColumn<>();

    @FXML
    private MFXComboBox<String> listProduct = new MFXComboBox<>();

    @FXML
    private MFXComboBox<String> listCustomer = new MFXComboBox<>();
    @FXML
    private TableColumn<Order, Boolean> colprint = new TableColumn<>();
    @FXML
    private TableView<Product> listProductOrder = new TableView<>();
    @FXML
    private Button buttonsave = new Button();
    @FXML
    private TableColumn<?, ?> colTotalPrice;

    @FXML
    private MFXTextField quantity = new MFXComboBox();

    @FXML
    private Label totalPrice = new Label();

    private Document list_Product;

    List<Product> list = new ArrayList<>();

    @FXML
    private TextField directoryfield = new TextField();

    @FXML
    void choice_Direct(ActionEvent event) {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        Stage stage = new Stage(); // Tạo một Stage mới

        java.io.File selectedDirectory = directoryChooser.showDialog(stage);
        if (selectedDirectory != null) {
            System.out.println("Thư mục đã chọn: " + selectedDirectory.getAbsolutePath());
            directoryfield.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    void save(ActionEvent event) throws FileNotFoundException, IOException {
        if (!directoryfield.getText().isEmpty()) {

            List<String> idList = displayIdProducts(getidlastorder());
            List<Integer> Quality = displayQuality(getidlastorder());
            List<String> Price = displayIdprice(getidlastorder());

            // Tạo một tệp Word mới
            XWPFDocument document = new XWPFDocument();

            // Tạo tiêu đề đơn hàng
            XWPFParagraph orderTitle = document.createParagraph();
            XWPFRun orderTitleRun = orderTitle.createRun();
            orderTitleRun.setText("Đơn hàng " + Math.abs(getidlastorder().hashCode()));
            orderTitleRun.setBold(true);
            orderTitleRun.setFontSize(14);

            // Tạo tiêu đề hóa đơn
            XWPFParagraph title = document.createParagraph();
            XWPFRun titleRun = title.createRun();
            titleRun.setText("Hóa đơn");
            titleRun.setBold(true);
            titleRun.setFontSize(16);

            int totalPrice = 0;
            String totalstring = null;

            for (int i = 0; i < idList.size(); i++) {
                String product = idList.get(i);
                Integer quality = Quality.get(i);
                String price = Price.get(i);

                // Loại bỏ dấu "," và "$" ra khỏi chuỗi giá và chuyển đổi thành số nguyên
                int pricestr = Integer.parseInt(price.replaceAll("[^0-9]", ""));

                // Tạo một đoạn văn bản cho mỗi sản phẩm
                XWPFParagraph item = document.createParagraph();
                XWPFRun itemRun = item.createRun();
                itemRun.setText("Sản phẩm: " + product + ", Chất lượng: " + quality + ", Giá: " + price);
                itemRun.setFontSize(12);
                totalPrice += pricestr * quality;

                // Định dạng tổng số tiền
                DecimalFormat formatter = new DecimalFormat("#,### $");
                totalstring = formatter.format(totalPrice);
            }

            // Tạo khoảng cách giữa danh sách sản phẩm và tổng số tiền
            for (int i = 0; i < 2; i++) {
                XWPFParagraph space = document.createParagraph();
                XWPFRun spaceRun = space.createRun();
                spaceRun.addBreak();
            }

            XWPFParagraph total = document.createParagraph();
            total.setAlignment(ParagraphAlignment.RIGHT);  // Căn phải tổng số tiền
            XWPFRun totalRun = total.createRun();
            totalRun.setText("Tổng số tiền: " + totalstring);
            totalRun.setBold(true);

            // Lưu tệp Word vào một tên tệp cụ thể (hoặc sử dụng đường dẫn tùy ý)
            FileOutputStream out = new FileOutputStream(directoryfield.getText() + "\\bill.docx");
            document.write(out);
            out.close();
            DialogAlert.DialogSuccess("Download successfully");
            Stage stage = (Stage) buttonsave.getScene().getWindow();
            stage.close();
        } else {
            DialogAlert.DialogError("Please select the link");
        }

    }

    private List<String> displayIdProducts(ObjectId id) {
        List<String> idList = new ArrayList<>();
        MongoCollection<Document> productCollection = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
        FindIterable<Document> productDocuments = productCollection.find();

        for (Document productDocument : productDocuments) {
            ObjectId productId = productDocument.getObjectId("_id");
            String productName = productDocument.getString("Name");

            Document orderQuery = new Document("DetailOrder." + productId, new Document("$exists", true));
            Document orderDocument = orderCollection.find(orderQuery).first();

            if (orderDocument != null && orderDocument.getObjectId("_id").equals(id)) {
                idList.add(productName);
            }
        }

        return idList;
    }

    private List<String> displayIdprice(ObjectId idorder) {
        List<String> idList = new ArrayList<>();
        MongoCollection<Document> order = DBConnection.getConnection().getCollection("Order");
        MongoCollection<Document> product = DBConnection.getConnection().getCollection("Product");
        FindIterable<Document> productWarehouse = product.find();
        for (Document document : productWarehouse) {

            ObjectId id = document.getObjectId("_id");
            FindIterable<Document> ordercollection = order.find(new Document("_id", idorder));
            for (Document document1 : ordercollection) {
                Document Detail = (Document) document1.get("DetailOrder");
                Document idcol = (Document) Detail.get(String.valueOf(id));
                if (idcol != null) {
                    int price = document.getInteger("Price");
                    DecimalFormat formatter = new DecimalFormat("#,### $");
                    String formatprice = formatter.format(price);
                    idList.add(formatprice);

                }
            }
        }

        return idList;
    }

    private List<Integer> displayQuality(ObjectId idorder) {
        List<Integer> idList = new ArrayList<>();
        MongoCollection<Document> order = DBConnection.getConnection().getCollection("Order");
        MongoCollection<Document> product = DBConnection.getConnection().getCollection("Product");
        FindIterable<Document> productWarehouse = product.find();
        for (Document document : productWarehouse) {

            ObjectId id = document.getObjectId("_id");
            FindIterable<Document> ordercollection = order.find(new Document("_id", idorder));
            for (Document document1 : ordercollection) {
                Document Detail = (Document) document1.get("DetailOrder");
                Document idcol = (Document) Detail.get(String.valueOf(id));
                if (idcol != null) {
                    idList.add(idcol.getInteger("Quality"));
                }
            }
        }
        return idList;
    }

    @FXML
    void Addcus(ActionEvent event) {
        if (!name.getText().isEmpty() && age.getValue() != null && !phone.getText().isEmpty() && !address.getText().isEmpty()) {
            boolean isFound = false;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            String YearOfSince = age.getValue().format(formatter);
            MongoCollection<Document> ListCustomer = DBConnection.getConnection().getCollection("Customer");
            MongoIterable<Document> iterable = ListCustomer.find(Filters.and(Filters.eq("Name", name.getText()), Filters.eq("Phone", phone.getText())));
            for (Document customer : iterable) {
                isFound = true;
                DialogAlert.DialogError("Customer Is Exist");
            }
            if (isFound == false) {
                MongoCollection<Document> Customers = DBConnection.getConnection().getCollection("Customer");
                Customers.insertOne(new Document("Name", name.getText()).append("Age", YearOfSince).append("Phone", phone.getText()).append("Address", address.getText()));
                DialogAlert.DialogSuccess("Add Customer Success");
                name.setText("");
                age.setValue(null);
                phone.setText("");
                address.setText("");
            }

        } else {
            DialogAlert.DialogError("Please enter all!");
        }
    }

    @FXML
    void AddCustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/AddCustomer.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(anchorPane));

            stage.setResizable(false);
            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void updateAndDisplayTotalPrice() {
        int totalPrice = 0;
        for (Product product : list) {
            totalPrice += product.getTotalQuality();
        }

        this.totalPrice.setText(Integer.toString(totalPrice));
    }

    @FXML
    void add(ActionEvent event) {

        MongoCollection<Document> collection = DBConnection.getConnection().getCollection("Product");
        FindIterable<Document> documents = collection.find(Filters.eq("Name", listProduct.getSelectedItem()));
        int index = -1;
        if (listCustomer.getValue() == null && listProduct != null) {
            DialogAlert.DialogError("Please enter in full");
            return;
        }
        String selectedProduct = listProduct.getSelectedItem();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getName().equals(selectedProduct)) {
                index = i;
                break;
            }
        }
        for (Document document : documents) {
            if (index == -1) {

                Product newProduct = new Product();
                newProduct.setName(listProduct.getSelectedItem());
                newProduct.setCustomer(listCustomer.getValue());
                newProduct.setQuality(Integer.parseInt(quantity.getText()));
                newProduct.setId_product(Math.abs(document.getObjectId("_id").hashCode()));
                newProduct.setPrice(document.get("Price").hashCode());
                int price = document.get("Price").hashCode();
                int Quality = Integer.parseInt(quantity.getText());
                int total = +price * Quality;
                newProduct.setTotalQuality(total);
                list.add(newProduct);
                listCustomer.setDisable(true);

            } else {
                Product product = list.get(index);
                int oldQuantity = product.getQuality();
                int newQuantity = Integer.parseInt(quantity.getText()) + oldQuantity;
                int price = document.get("Price").hashCode();
                product.setQuality(newQuantity);
                int total = +price * newQuantity;
                product.setTotalQuality(total);

            }
            ObservableList<Product> observableList = FXCollections.observableArrayList(list);
            listProductOrder.setItems(observableList);

            listProductOrder.getItems().setAll(list);
            colIdProduct.setCellValueFactory(new PropertyValueFactory<>("id_product"));
            colNameProduct.setCellValueFactory(new PropertyValueFactory<>("Name"));
            colQuantity.setCellValueFactory(new PropertyValueFactory<>("Quality"));
            col_Price.setCellValueFactory(new PropertyValueFactory<>("Price"));
            colTotalPrice.setCellValueFactory(new PropertyValueFactory<>("totalQuality"));
            colDelete.setCellFactory(column -> new TableCell<Product, Boolean>() {
                private Button button = new Button("Delete");

                {
                    button.setOnAction(event -> {
                        Product selectedProduct = getTableView().getItems().get(getIndex());
                        listProductOrder.getItems().remove(selectedProduct);
                        for (Product originalProduct : list) {
                            if (originalProduct.getName().equals(selectedProduct.getName())) {

                                originalProduct.setQuality(0);
                                originalProduct.setQuality(originalProduct.getQuality() + selectedProduct.getQuality());
                                break;
                            }
                        }
                    });
                }

                @Override
                protected void updateItem(Boolean item, boolean empty) {
                    super.updateItem(item, empty);
                    button.getStyleClass().add("button-error");
                    if (item != null || !empty) {
                        setGraphic(button);
                    } else {
                        setGraphic(null);
                    }
                }
            });
        }
        updateAndDisplayTotalPrice();

    }

    @FXML
    void create(ActionEvent event) {

        ObservableList<Product> productList = listProductOrder.getItems();
        if (productList.isEmpty()) {
            DialogAlert.DialogError("Not entered yet to add");
        } else {
            MongoCollection<Document> order = DBConnection.getConnection().getCollection("Order");
            boolean isboolean = true;
            ObjectId insertedId = null;
            Document productDetails = new Document();
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);
            ObjectId idcustomer = null;
            for (Product warehouse : productList) {
                Document productDetail = new Document();
                idcustomer = getCategoryIDByName(warehouse.getCustomer());
                productDetail.append("Quality", warehouse.getQuality());
                productDetails.append(String.valueOf(getproductName(warehouse.getName())), productDetail);

            }
            Document warehouseDocument = new Document();
            warehouseDocument.append("DetailOrder", productDetails);
            warehouseDocument.append("id_Customer", idcustomer);
            warehouseDocument.append("Order_date", formattedDate);
            warehouseDocument.append("id_Employee", LoginController.id_employee);
            warehouseDocument.append("Ship_date", "");
            warehouseDocument.append("status", 0);
            order.insertOne(warehouseDocument);
            DialogAlert.DialogSuccess("Add successfully");
            productList.clear();
            listProductOrder.setItems(FXCollections.observableArrayList(productList));
            listCustomer.setDisable(false);
            quantity.setText("");
            listProduct.setValue(null);
            listCustomer.setValue(null);
        }

    }

    public ObjectId getproductName(String productName) {
        Map<String, ObjectId> categoryNameToIdMap = getproductNameToIdMap();

        return categoryNameToIdMap.get(productName);
    }

    public Map<String, ObjectId> getproductNameToIdMap() {
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Product");
        Map<String, ObjectId> categoryNameToIdMap = new HashMap<>();

        FindIterable<Document> result = categoryCollection.find();
        for (Document document : result) {
            String categoryName = document.getString("Name");
            ObjectId categoryId = document.getObjectId("_id");
            categoryNameToIdMap.put(categoryName, categoryId);
        }

        return categoryNameToIdMap;
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
                int id_filter = Math.abs(id_order);
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
                ArrayOrder.add(new Order(document.getObjectId("_id"), IdCustomer, IdEmployee, OrderDate, ShipDate, Status, detailOrder, nameCustomer, nameemployee, id_filter));
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
                        ArrayOrder.add(new Order(document.getObjectId("_id"), IdCustomer, IdEmployee, OrderDate, ShipDate, Status, detailOrder, nameCustomer, nameemployee, id_filter));
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
        colprint.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private MFXButton button = new MFXButton("Print");

            {
                button.setOnAction(event -> {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("view/Bill.fxml"));
                    Order orders = getTableView().getItems().get(getIndex());

                    try {
                        AnchorPane Detail = loader.load();
                        Stage stage = new Stage();
                        MainOrderController main = loader.getController();
                        main.displayProduct(orders.getId());
                        main.setidlastorder(orders.getId());
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
        Map<String, Integer> statusMappings = new HashMap<>();
        statusMappings.put("Waiting for delivery", 1);
        statusMappings.put("Ongoing deliveries", 2);
        statusMappings.put("Delivered", 3);
        statusMappings.put("Cancelled", 4);

        StatusOrder.setCellFactory(column -> new TableCell<Order, String>() {
            private ComboBox<String> statusComboBox = new ComboBox<>();

            {
                statusComboBox.getItems().addAll("pending", "Waiting for delivery", "Ongoing deliveries", "Delivered", "Cancelled");

                statusComboBox.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    String newStatus = statusComboBox.getValue();
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = currentDate.format(formatter);

                    String currentStatus = order.getStatus();

                    if (canChangeStatus(currentStatus, newStatus)) {

                        statusComboBox.setValue(newStatus);

                        int mappedValue = statusMappings.getOrDefault(newStatus, 0);
                        MongoCollection<Document> orderUpdate = DBConnection.getConnection().getCollection("Order");
                        Document filter = new Document("_id", order.getId());

                        Document update = new Document("$set", new Document("status", mappedValue));

                        if ("Delivered".equals(newStatus)) {
                           update.append("$set", new Document("Ship_date", formattedDate).append("status", mappedValue));
                        }

                        orderUpdate.updateOne(filter, update);
                        ListOrderCustomer();

                    } else {
                        Platform.runLater(() -> {
                            DialogAlert.DialogError("Unable to change pregnancy:" + currentStatus + " to " + newStatus);
                            statusComboBox.setValue(currentStatus);
                        });
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    if ("Waiting for delivery".equals(item) || "Ongoing deliveries".equals(item)) {
                        statusComboBox.setValue(item);
                        setGraphic(statusComboBox);
                    } else {
                        Label label = new Label();
                        label.setText(item);
                        setGraphic(label);
                    }
                }
            }
        });
        pagination.setCurrentPage(0);
        pagination.setMaxPage(OrderCustomer.size());
    }

    private boolean canChangeStatus(String currentStatus, String newStatus) {
        if ("Ongoing deliveries".equals(currentStatus) && "Waiting for delivery".equals(newStatus)) {
            return false; // Disallow changing from "Ongoing deliveries" to "Waiting for delivery"
        } else if ("Waiting for delivery".equals(currentStatus) && ("Pending".equals(newStatus))) {
            return false; // Disallow changing from "Waiting for delivery" to "Ongoing deliveries" or "Pending"
        } else if ("Ongoing deliveries".equals(currentStatus) && ("Waiting for delivery".equals(newStatus) || "pending".equals(newStatus))) {
            return false; // Disallow changing from "Ongoing deliveries" to "Waiting for delivery"
        }

        return true;
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
        colprint.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private MFXButton button = new MFXButton("Print");

            {
                button.setOnAction(event -> {
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("view/Bill.fxml"));
                    Order orders = getTableView().getItems().get(getIndex());

                    try {
                        AnchorPane Detail = loader.load();
                        Stage stage = new Stage();
                        MainOrderController main = loader.getController();
                        main.displayProduct(orders.getId());
                        main.setidlastorder(orders.getId());
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
        Map<String, Integer> statusMappings = new HashMap<>();
        statusMappings.put("Waiting for delivery", 1);
        statusMappings.put("Ongoing deliveries", 2);
        statusMappings.put("Delivered", 3);
        statusMappings.put("Cancelled", 4);

        StatusOrder.setCellFactory(column -> new TableCell<Order, String>() {
            private ComboBox<String> statusComboBox = new ComboBox<>();

            {
                statusComboBox.getItems().addAll("pending", "Waiting for delivery", "Ongoing deliveries", "Delivered", "Cancelled");

                statusComboBox.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    String newStatus = statusComboBox.getValue();
                    LocalDate currentDate = LocalDate.now();
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String formattedDate = currentDate.format(formatter);

                    String currentStatus = order.getStatus();

                    if (canChangeStatus(currentStatus, newStatus)) {

                        statusComboBox.setValue(newStatus);

                        int mappedValue = statusMappings.getOrDefault(newStatus, 0);
                        MongoCollection<Document> orderUpdate = DBConnection.getConnection().getCollection("Order");
                        Document filter = new Document("_id", order.getId());

                        Document update = new Document("$set", new Document("status", mappedValue));

                        if ("Delivered".equals(newStatus)) {
                           update.append("$set", new Document("Ship_date", formattedDate).append("status", mappedValue));
                        }

                        orderUpdate.updateOne(filter, update);
                        ListOrderCustomer();

                    } else {
                        Platform.runLater(() -> {
                            DialogAlert.DialogError("Unable to change pregnancy:" + currentStatus + " to " + newStatus);
                            statusComboBox.setValue(currentStatus);
                        });
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    if ("Waiting for delivery".equals(item) || "Ongoing deliveries".equals(item)) {
                        statusComboBox.setValue(item);
                        setGraphic(statusComboBox);
                    } else {
                        Label label = new Label();
                        label.setText(item);
                        setGraphic(label);
                    }
                }
            }
        });
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
    void popupcustomer(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/Createcustomerorder.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(anchorPane));
            stage.setResizable(false);
            stage.setOnCloseRequest(closeEvent -> {
                listCustomer.getItems().clear();
                listCustomer.getItems().addAll(ListCustomerAll());
            });
            stage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void addOrder(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/AddOrder.fxml"));
            AnchorPane anchorPane = loader.load();
            MainOrderController order = loader.getController();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(anchorPane, 648, 480));
            stage.setOnCloseRequest(closeEvent -> {
                ListOrderCustomer();
            });
            order.totalPrice.setText("0");
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

    public static List<String> ListCustomerAll() {
        List<String> ArrayCustomer = new ArrayList<>();
        MongoCollection<Document> CustomerCollection = DBConnection.getConnection().getCollection("Customer");
        MongoIterable<Document> customers = CustomerCollection.find();
        for (Document customer : customers) {
            ArrayCustomer.add(customer.getString("Name"));
        }
        return ArrayCustomer;
    }

    public Map<String, ObjectId> getCategoryNameToIdMap() {
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Customer");
        Map<String, ObjectId> categoryNameToIdMap = new HashMap<>();

        FindIterable<Document> result = categoryCollection.find();
        for (Document document : result) {
            String categoryName = document.getString("Name");
            ObjectId categoryId = document.getObjectId("_id");
            categoryNameToIdMap.put(categoryName, categoryId);
        }

        return categoryNameToIdMap;
    }

    public ObjectId getCategoryIDByName(String categoryName) {
        Map<String, ObjectId> categoryNameToIdMap = getCategoryNameToIdMap();

        // Lấy ID từ Map
        return categoryNameToIdMap.get(categoryName);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListOrderCustomer();
        listProduct.getItems().addAll(ListProductAll());
        listCustomer.getItems().addAll(ListCustomerAll());

    }
}
