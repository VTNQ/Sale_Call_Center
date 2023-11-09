/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.model.Warehouse;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.daodb;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * FXML Controller class
 *
 * @author tranp
 */
public class ListWarehouseController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ComboBox<String> Nameproduct = new ComboBox<>();
    @FXML
    private TextField Namesupply;
    private int itemsperPage = 5;

    @FXML
    private TextField txtsearch = new TextField();
    @FXML
    private TextField txtfield = new TextField();
    @FXML
    private Pagination pagination1 = new Pagination();
    private int totalItems;
    private int displaymode = 1;
    private int currentPageIndex = 0;
    @FXML
    private Pagination pagination = new Pagination();
    @FXML
    private TextField Quality;
    @FXML
    private TableColumn<Warehouse, Boolean> colProduct = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colSuppliers = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> colNamproduct = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colProce = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colQuality = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colSupplier = new TableColumn<>();

    @FXML
    private TableColumn<Warehouse, Boolean> coldelete = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colidproduct = new TableColumn<>();

    @FXML
    private TableView<Warehouse> tblAddlist = new TableView<>();
    @FXML
    private TableColumn<?, ?> colNameCategory = new TableColumn<>();
    private boolean isNamesupplySet = false;
    @FXML
    private TableColumn<?, ?> colNameproduct = new TableColumn<>();
    private ObjectId id;

    private void setid(ObjectId id) {
        this.id = id;
    }

    private ObjectId getid() {
        return id;
    }
    @FXML
    private TableColumn<?, ?> colprice = new TableColumn<>();
    boolean exists = false;
    @FXML
    private TableView<Warehouse> tbldetail = new TableView<>();
    @FXML
    private TableColumn<?, ?> coldate = new TableColumn<>();
    @FXML
    private TableColumn<Warehouse, String> colstatus = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> colid = new TableColumn<>();
    boolean isDataValid = true;
    @FXML
    private TableView<Warehouse> tblWarehouse = new TableView<>();

    private void searchdisplay(String name, int pageIndex) {
        displaymode = 2;
        List<Warehouse> ware = daodb.SearchWarehouseReceipt(name);
        ObservableList<Warehouse> obserable = FXCollections.observableArrayList(ware);
        totalItems = obserable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
        if (obserable.isEmpty()) {
            pagination.setPageCount(1);
            tblWarehouse.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Warehouse> Ass = obserable.subList(startIndex, endIndex);
        tblWarehouse.setItems(FXCollections.observableArrayList(Ass));
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colstatus.setCellFactory(column -> new TableCell<Warehouse, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colstatus.widthProperty());
                    setGraphic(text);
                }
            }

        });

        colProduct.setCellFactory(column -> new TableCell<Warehouse, Boolean>() {
            private Button button = new Button("Product");

            {
                button.setOnAction(event -> {
                    Warehouse ware = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/detailProductWarehouse.fxml"));
                        AnchorPane newpopup;
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        ListWarehouseController list = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        ListWarehouseController List = loader.getController();
                        List.listdetailWarehouse(ware.getIdwarehouse());
                        list.setid(ware.getIdwarehouse());
                        popupStage.setResizable(false);
                        popupStage.show();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("btn-design");
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
        colSuppliers.setCellValueFactory(new PropertyValueFactory<>("suppliers"));
    }

    private List<String> displayIdProducts(ObjectId id) {
        List<String> idList = new ArrayList<>();
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
        FindIterable<Document> Warehousecollection = Warehouse.find(new Document());
        for (Document document : Warehousecollection) {
            FindIterable<Document> productcollection = Product.find();
            for (Document document1 : productcollection) {
                ObjectId id_product = document1.getObjectId("_id");
                String Name = document1.getString("Name");
                Document Detail = (Document) document.get("Detail");
                Document idcol = (Document) Detail.get(String.valueOf(id_product));
                if (idcol != null && document.getObjectId("_id").equals(id) && !idList.contains(id_product)) {
                    idList.add(Name);
                }
            }
        }
        return idList;
    }

    private void searchListdetailWarehouse(String name, ObjectId id, int pageindex) {
        displaymode = 2;
        List<Warehouse> warehouse = daodb.SearchDetailProductWarehouse(name, id);
        ObservableList<Warehouse> obserable = FXCollections.observableArrayList(warehouse);
        totalItems = obserable.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination1.setPageCount(pageCount);
        if (obserable.isEmpty()) {
            pagination1.setPageCount(1);
            tblWarehouse.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageindex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Warehouse> Ass = obserable.subList(startIndex, endIndex);
        tbldetail.setItems(FXCollections.observableArrayList(Ass));
        colNameproduct.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));

        colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colNameCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    private void listdetailWarehouse(ObjectId id) {
        displaymode = 1;
        List<Warehouse> warehouse = daodb.DetailProductWarehouse(id);
        ObservableList<Warehouse> obserable = FXCollections.observableArrayList(warehouse);
        totalItems = obserable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination1.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Warehouse> as = obserable.subList(startIndex, endIndex);
        tbldetail.setItems(FXCollections.observableArrayList(as));
        colNameproduct.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));

        colprice.setCellValueFactory(new PropertyValueFactory<>("price"));
        colNameCategory.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    private void ListWarehouse() {
        displaymode = 1;
        List<Warehouse> ware = daodb.WarehouseReceipt();
        ObservableList<Warehouse> obserable = FXCollections.observableArrayList(ware);
        totalItems = obserable.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Warehouse> as = obserable.subList(startIndex, endIndex);
        tblWarehouse.setItems(FXCollections.observableArrayList(as));
        colid.setCellValueFactory(new PropertyValueFactory<>("id"));
        coldate.setCellValueFactory(new PropertyValueFactory<>("Date"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colstatus.setCellFactory(column -> new TableCell<Warehouse, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colstatus.widthProperty());
                    setGraphic(text);
                }
            }

        });

        colProduct.setCellFactory(column -> new TableCell<Warehouse, Boolean>() {
            private Button button = new Button("Product");

            {
                button.setOnAction(event -> {
                    Warehouse ware = getTableView().getItems().get(getIndex());
                    try {
                        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/detailProductWarehouse.fxml"));
                        AnchorPane newpopup;
                        newpopup = loader.load();
                        Stage popupStage = new Stage();
                        ListWarehouseController list = loader.getController();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        ListWarehouseController List = loader.getController();
                        List.listdetailWarehouse(ware.getIdwarehouse());
                        list.setid(ware.getIdwarehouse());
                        popupStage.setResizable(false);
                        popupStage.show();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("btn-design");
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
        colSuppliers.setCellValueFactory(new PropertyValueFactory<>("suppliers"));
    }

    @FXML
    void AddWarehouse(ActionEvent event) {
        String nameValue = Nameproduct.getValue(); // Assuming you have a ComboBox for nameProduct
        ObservableList<Warehouse> productList = tblAddlist.getItems();

        String qualityString = Quality.getText();
        String nameSupply = Namesupply.getText();

        // Kiểm tra xem Namesupply và Quality đã được nhập chưa
        if (nameSupply.isEmpty() && qualityString.isEmpty()) {
            // Nếu một trong hai trường không hợp lệ, đặt biến isDataValid thành false
            DialogAlert.DialogError("Please enter in full");
            return;
            // Hiển thị cảnh báo cho người dùng hoặc thực hiện xử lý cần thiết
        }
        try {
            int quality = Integer.parseInt(qualityString);
            if (quality < 0) {
                DialogAlert.DialogError("Quality must be a non-negative integer");
            } else {
                boolean exists = false;

                for (Warehouse warehouse : productList) {
                    if (warehouse.getNameProduct().equals(nameValue)) {
                        exists = true;
                        int qualityOld = warehouse.getQuality();
                        int qualityTotal = qualityOld + quality;
                        warehouse.setQuality(qualityTotal); // Cập nhật chất lượng
                        break;
                    }
                }

                if (!exists) {
                    if (!isNamesupplySet) {
                        Warehouse warehouse = new Warehouse();
                        warehouse.setNameProduct(nameValue);
                        warehouse.setQuality(quality);
                        warehouse.setId(Math.abs(getproductName(nameValue).hashCode()));
                        warehouse.setPrice(getFormattedPrice(nameValue));
                        warehouse.setSuppliers(nameSupply);
                        isNamesupplySet = true;
                        Namesupply.setEditable(false);
                        productList.add(warehouse);
                    } else {
                        Warehouse warehouse = new Warehouse();
                        warehouse.setNameProduct(nameValue);
                        warehouse.setQuality(quality);
                        warehouse.setId(Math.abs(getproductName(nameValue).hashCode()));
                        warehouse.setPrice(getFormattedPrice(nameValue));
                        warehouse.setSuppliers(nameSupply);
                        warehouse.setIdProduct(getproductName(nameValue));
                        isNamesupplySet = false;
                        productList.add(warehouse);
                    }

                }

                if (isDataValid) {
                    // Chỉ khi dữ liệu là hợp lệ, bạn mới thực hiện các thay đổi liên quan đến TableView và cột
                    // Refresh TableView và các cột
                    tblAddlist.setItems(FXCollections.observableArrayList(productList));
                    tblAddlist.getItems().setAll(productList);
                    colidproduct.setCellValueFactory(new PropertyValueFactory<>("id"));
                    colNamproduct.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
                    colQuality.setCellValueFactory(new PropertyValueFactory<>("quality"));
                    colProce.setCellValueFactory(new PropertyValueFactory<>("price"));
                    colSupplier.setCellValueFactory(new PropertyValueFactory<>("suppliers"));
                    coldelete.setCellFactory(column -> new TableCell<Warehouse, Boolean>() {
                        private Button button = new Button("Delete");

                        {
                            button.setOnAction(event -> {
                        Warehouse selectedProduct = getTableView().getItems().get(getIndex());
                        tblAddlist.getItems().remove(selectedProduct);
                        ObservableList<Warehouse> productList = tblAddlist.getItems();
                        for (Warehouse originalProduct :  productList) {
                            if (originalProduct.getNameProduct().equals(selectedProduct.getNameProduct())) {
                                
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
            }

        } catch (NumberFormatException e) {
             DialogAlert.DialogError("Quality must be a valid non-negative integer");
        }

    }

    @FXML
    void Create(ActionEvent event) {
        ObservableList<Warehouse> productList = tblAddlist.getItems();
        if (productList.isEmpty()) {
            DialogAlert.DialogError("Not entered yet to add");
        } else {
            MongoCollection<Document> IncomingOrder = DBConnection.getConnection().getCollection("InComingOrder");
            MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
            boolean isboolean = true;
            ObjectId insertedId = null;
            Document productDetails = new Document();
            LocalDate currentDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String formattedDate = currentDate.format(formatter);
            for (Warehouse warehouse : productList) {
                if (isboolean) {
                    Document document = new Document("Id_Employee", LoginController.id_employee)
                            .append("Supplier", warehouse.getSuppliers())
                            .append("status", 0);
                    IncomingOrder.insertOne(document);
                    insertedId = document.get("_id", ObjectId.class);
                    isboolean = false;
                }

                Document productDetail = new Document();
                productDetail.append("Quality", warehouse.getQuality());
                productDetails.append(String.valueOf(getproductName(warehouse.getNameProduct())), productDetail);
            }

            // Tạo một tài liệu chứa tất cả thông tin sản phẩm và ID của đơn hàng
            Document warehouseDocument = new Document();
            warehouseDocument.append("Detail", productDetails);
            warehouseDocument.append("ID_InComingOrder", insertedId);
            warehouseDocument.append("Date", formattedDate);
            Warehouse.insertOne(warehouseDocument);
            DialogAlert.DialogSuccess("Add successfully");
            Namesupply.setText("");
            Quality.setText("");
            productList.clear();
            tblAddlist.setItems(FXCollections.observableArrayList(productList));
            Namesupply.setEditable(true);
        }

    }

    @FXML
    void Appreceipt(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/Add_Receipt.fxml"));
        AnchorPane newpopup;
        try {
            newpopup = loader.load();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(newpopup));
            popupStage.setResizable(false);
            popupStage.show();
            popupStage.setOnCloseRequest(closeEvent -> {
                ListWarehouse();
            });

        } catch (IOException ex) {
            ex.printStackTrace();
        }
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

    public Map<String, String> getproductPriceToIdMap() {
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Product");
        Map<String, String> categoryNameToIdMap = new HashMap<>();

        FindIterable<Document> result = categoryCollection.find();
        for (Document document : result) {
            int price = document.getInteger("Price");
            DecimalFormat formatter = new DecimalFormat("#,### $");
            String formatprice = formatter.format(price);
            String Name = document.getString("Name");
            categoryNameToIdMap.put(Name, formatprice);
        }

        return categoryNameToIdMap;
    }

    public String getFormattedPrice(String productName) {
        Map<String, String> priceMap = getproductPriceToIdMap();
        return priceMap.get(productName);
    }

    public ObjectId getproductName(String productName) {
        Map<String, ObjectId> categoryNameToIdMap = getproductNameToIdMap();

        return categoryNameToIdMap.get(productName);
    }

    @FXML
    void searchdetail(ActionEvent event) {
        currentPageIndex = 0;
        searchListdetailWarehouse(txtfield.getText(), getid(), currentPageIndex);
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination1.setCurrentPageIndex(currentPageIndex);
        } else if (currentPageIndex == 0) {
            currentPageIndex = 0;
            pagination1.setCurrentPageIndex(currentPageIndex);
        }

    }

    @FXML
    void search(ActionEvent event) {
        currentPageIndex = 0;
        searchdisplay(txtsearch.getText(), currentPageIndex);
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        } else if (currentPageIndex == 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ListWarehouse();
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                ListWarehouse();
            } else if (displaymode == 2) {
                searchdisplay(txtsearch.getText(), currentPageIndex);
            }

        });
        pagination1.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                listdetailWarehouse(getid());
            } else if (displaymode == 2) {
                searchListdetailWarehouse(txtfield.getText(), getid(), currentPageIndex);
            }
        });
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Product");
        ObservableList<String> categoryList = FXCollections.observableArrayList();
        FindIterable<Document> result = categoryCollection.find();
        for (Document document : result) {
            String categoryName = document.getString("Name");
            categoryList.add(categoryName);
        }
        Nameproduct.setItems(categoryList);
        if (!categoryList.isEmpty()) {
            Nameproduct.setValue(categoryList.get(0));
        }

    }
}
