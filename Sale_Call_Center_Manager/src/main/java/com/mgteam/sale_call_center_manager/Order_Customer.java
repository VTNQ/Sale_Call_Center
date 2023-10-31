/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.connect.util.daodb;
import com.mgteam.sale_call_center_manager.model.Order;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author tranp
 */
public class Order_Customer implements Initializable {

    private int displayingOrder = 1;
    private int id_order;
    
    @FXML
    private DatePicker orderDate=new DatePicker();
    @FXML
    private MFXTextField textfield;
    @FXML
    private TableColumn<?, ?> colDay = new TableColumn<>();
    @FXML
    private Label total;
    private String NameCustomer;
    @FXML
    private Pagination pagnation = new Pagination();
    private int itemsperPage = 5;
    private int totalItems;
    private int currentPageIndex = 0;
    @FXML
    private TableView<Order> tblDetail = new TableView<>();
    @FXML
    private TableColumn<?, ?> colEmployee = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colcustomer = new TableColumn<>();
    @FXML
    private Label customer = new Label();
    @FXML
    private TableColumn<?, ?> colid = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> colProduct;
    private String Email;
    private String Employee;
    @FXML
    private TextArea AreaRequest;
    @FXML
    private TableColumn<?, ?> colQuality = new TableColumn<>();
    @FXML
    private TableColumn<?, ?> colPrice = new TableColumn<>();

    @FXML
    private TableColumn<Order, Boolean> coldetail = new TableColumn<>();

    @FXML
    private TableColumn<Order, Boolean> colRequest = new TableColumn<>();

    @FXML
    private TableColumn<Order, String> colstatus = new TableColumn<>();
    @FXML
    private TableView<Order> tblOrder = new TableView<>();
    
    @FXML
    private Pagination detailPagination=new Pagination();

    private void setcustomer(String customer) {
        this.customer.setText(customer);
    }

    private void setEmail(String Email) {
        this.Email = Email;
    }

    private String getEmail() {
        return Email;
    }

    private void setEmployee(String Employee) {
        this.Employee = Employee;
    }
    private void setNameCustomer(String nameCustomer){
        this.NameCustomer=nameCustomer;
    }
    private String getNameCustomer(){
        return NameCustomer;
    }
    private String getEmployee() {
        return Employee;
    }

    private void detailorder(String name, int id_order) {
        List<Order> detailorder = daodb.getdetailCustomer(name, id_order);
        ObservableList<Order> observableList = FXCollections.observableArrayList(detailorder);
        totalItems = observableList.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        detailPagination.setPageCount(pageCount);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Order> Ass = observableList.subList(startIndex, endIndex);
        tblDetail.setItems(FXCollections.observableArrayList(Ass));
        colProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));

    }

    private void searchorder(String name, int pageintdex) {
        displayingOrder = 3;
        List<Order> orderest = daodb.SearchorderCustomer(name);
        ObservableList<Order> obserableList = FXCollections.observableArrayList(orderest);
        totalItems = obserableList.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagnation.setPageCount(pageCount);
        if (orderest.isEmpty()) {
            pagnation.setPageCount(1);
            tblOrder.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex = pageintdex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);

        List<Order> Ass = obserableList.subList(startIndex, endIndex);
        tblOrder.setItems(FXCollections.observableArrayList(Ass));
        colcustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("Day"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colRequest.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        colRequest.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Request");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/create_request.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Stage popupStage = new Stage();
                        Order_Customer Order = loader.getController();
                        Order.setEmail(order.getEmail());
                        Order.setEmployee(order.getEmployee());
                        Order.setNameCustomer(order.getName());
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    String status = getTableView().getItems().get(getIndex()).getStatus();
                    if (status != null && status.equals("Importing goods")) {
                        button.getStyleClass().add("btn-design");
                        setGraphic(button);
                    } else {
                        setGraphic(null);
                    }
                } else {
                    setGraphic(null);
                }
            }
        });

        coldetail.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        coldetail.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Detail");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Detail_order.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Order_Customer Order = loader.getController();
                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        Order.setcustomer(order.getName());
                        Order.detailorder(order.getName(), order.getIdOrder());
                        List<Order> orders = daodb.getdetailCustomer(order.getName(), order.getIdOrder());
                        int totalprice = daodb.calculateTotalPrice(orders);
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatterprice = formatter.format(totalprice);
                        Order.total.setText(formatterprice);
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                button.getStyleClass().add("btn-design");
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
    }

    @FXML
    void Findsearch(ActionEvent event) {
        currentPageIndex = 0;
        searchorder(textfield.getText(), currentPageIndex);
    }

    @FXML
    void All(ActionEvent event) {
        displayingOrder = 1;

        displayorder();
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagnation.setCurrentPageIndex(currentPageIndex);
        }
    }

    private void orderyet() {
        displayingOrder = 4;
        List<Order> orderList = daodb.OrderCustomerYet();
        ObservableList<Order> obserableList = FXCollections.observableArrayList(orderList);
        totalItems = obserableList.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagnation.setPageCount(pageCount);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Order> Ass = obserableList.subList(startIndex, endIndex);
        tblOrder.setItems(FXCollections.observableArrayList(Ass));
        colcustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("Day"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colstatus.setCellFactory(tc -> new TableCell<Order, String>() {
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
        colid.setCellValueFactory(new PropertyValueFactory<>("IdOrder"));
        colRequest.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        colRequest.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Request");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/create_request.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Stage popupStage = new Stage();
                        Order_Customer Order = loader.getController();
                        Order.setEmail(order.getEmail());
                        Order.setEmployee(order.getEmployee());
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override

            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    String status = getTableView().getItems().get(getIndex()).getStatus();
                    if (status != null && status.equals("Importing goods")) {
                        button.getStyleClass().add("btn-design");
                        setGraphic(button);
                    } else {
                        setGraphic(null);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        coldetail.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        coldetail.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Detail");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Detail_order.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Order_Customer Order = loader.getController();
                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        Order.setcustomer(order.getName());

                        Order.detailorder(order.getName(), order.getIdOrder());
                        List<Order> orders = daodb.getdetailCustomer(order.getName(), order.getIdOrder());
                        int totalprice = daodb.calculateTotalPrice(orders);
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatterprice = formatter.format(totalprice);
                        Order.total.setText(formatterprice);
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                button.getStyleClass().add("btn-design");
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
    }

    @FXML
    void OrderYet(ActionEvent event) {
        orderyet();
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagnation.setCurrentPageIndex(currentPageIndex);
        }
    }

    private void ordernotyet() {
        displayingOrder = 2;
        List<Order> orderList = daodb.OrderCustomerNotYet();
        ObservableList<Order> obserableList = FXCollections.observableArrayList(orderList);
        totalItems = obserableList.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagnation.setPageCount(pageCount);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);

        List<Order> Ass = obserableList.subList(startIndex, endIndex);

        tblOrder.setItems(FXCollections.observableArrayList(Ass));
        colcustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("Day"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colstatus.setCellFactory(tc -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colstatus.widthProperty()); // Set the wrapping width to the column width
                    setGraphic(text);
                }
            }
        });
        colid.setCellValueFactory(new PropertyValueFactory<>("IdOrder"));
        colRequest.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        colRequest.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Request");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/create_request.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Stage popupStage = new Stage();
                        Order_Customer Order = loader.getController();
                        Order.setEmail(order.getEmail());
                        Order.setEmployee(order.getEmployee());
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    String status = getTableView().getItems().get(getIndex()).getStatus();
                    if (status != null && status.equals("Importing goods")) {
                        button.getStyleClass().add("btn-design");
                        setGraphic(button);
                    } else {
                        setGraphic(null);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        coldetail.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        coldetail.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Detail");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Detail_order.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Order_Customer Order = loader.getController();
                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        Order.setcustomer(order.getName());
                        Order.detailorder(order.getName(), order.getIdOrder());
                        List<Order> orders = daodb.getdetailCustomer(order.getName(), order.getIdOrder());
                        int totalprice = daodb.calculateTotalPrice(orders);
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatterprice = formatter.format(totalprice);
                        Order.total.setText(formatterprice);
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                button.getStyleClass().add("btn-design");
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
    }

    @FXML
    void OrderNotYet(ActionEvent event) {

        ordernotyet();
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagnation.setCurrentPageIndex(currentPageIndex);
        }
    }
    private void filterDay(String Date){
        displayingOrder=5;
        List<Order>orderfilter=daodb.getdateOrder(Date);
   ObservableList<Order> observableList = FXCollections.observableArrayList(orderfilter);
        totalItems = observableList.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagnation.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Order> As = observableList.subList(startIndex, endIndex);
        tblOrder.setItems(FXCollections.observableArrayList(As));
        colcustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("Day"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colstatus.setCellFactory(tc -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colstatus.widthProperty()); // Set the wrapping width to the column width
                    setGraphic(text);
                }
            }
        });
        colid.setCellValueFactory(new PropertyValueFactory<>("IdOrder"));
        colRequest.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        colRequest.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Request");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/create_request.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Stage popupStage = new Stage();
                        Order_Customer Order = loader.getController();
                        Order.setEmail(order.getEmail());
                        Order.setEmployee(order.getEmployee());
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    String status = getTableView().getItems().get(getIndex()).getStatus();
                    if (status != null && status.equals("Importing goods")) {
                        button.getStyleClass().add("btn-design");
                        setGraphic(button);
                    } else {
                        setGraphic(null);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        coldetail.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        coldetail.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Detail");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Detail_order.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Order_Customer Order = loader.getController();
                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        Order.setcustomer(order.getName());
                        Order.id_order=order.getIdOrder();
                        Order.detailorder(order.getName(), order.getIdOrder());
                        List<Order> orders = daodb.getdetailCustomer(order.getName(), order.getIdOrder());
                        int totalprice = daodb.calculateTotalPrice(orders);
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatterprice = formatter.format(totalprice);
                        Order.total.setText(formatterprice);
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                button.getStyleClass().add("btn-design");
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
    }
    private void displayorder() {

        List<Order> OrderList = daodb.getorderCustomer();
        ObservableList<Order> observableList = FXCollections.observableArrayList(OrderList);
        totalItems = observableList.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagnation.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Order> As = observableList.subList(startIndex, endIndex);

        tblOrder.setItems(FXCollections.observableArrayList(As));
        colcustomer.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmployee.setCellValueFactory(new PropertyValueFactory<>("Employee"));
        colDay.setCellValueFactory(new PropertyValueFactory<>("Day"));
        colstatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colstatus.setCellFactory(tc -> new TableCell<Order, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colstatus.widthProperty()); // Set the wrapping width to the column width
                    setGraphic(text);
                }
            }
        });
        colid.setCellValueFactory(new PropertyValueFactory<>("IdOrder"));
        colRequest.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        colRequest.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Request");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/create_request.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Stage popupStage = new Stage();
                        Order_Customer Order = loader.getController();
                        Order.setEmail(order.getEmail());
                        Order.setEmployee(order.getEmployee());
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    String status = getTableView().getItems().get(getIndex()).getStatus();
                    if (status != null && status.equals("Importing goods")) {
                        button.getStyleClass().add("btn-design");
                        setGraphic(button);
                    } else {
                        setGraphic(null);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        coldetail.setCellValueFactory(new PropertyValueFactory<>("Detail"));
        coldetail.setCellFactory(column -> new TableCell<Order, Boolean>() {
            private final MFXButton button = new MFXButton("Detail");

            {
                button.setOnAction(event -> {
                    Order order = getTableView().getItems().get(getIndex());
                    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Detail_order.fxml"));
                    try {
                        AnchorPane newpopup = loader.load();
                        Order_Customer Order = loader.getController();
                        Stage popupStage = new Stage();
                        popupStage.initModality(Modality.APPLICATION_MODAL);
                        popupStage.setScene(new Scene(newpopup));
                        Order.setcustomer(order.getName());
                        Order.id_order=order.getIdOrder();
                        Order.detailorder(order.getName(), order.getIdOrder());
                        List<Order> orders = daodb.getdetailCustomer(order.getName(), order.getIdOrder());
                        int totalprice = daodb.calculateTotalPrice(orders);
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatterprice = formatter.format(totalprice);
                        Order.total.setText(formatterprice);
                        popupStage.setResizable(false);
                        popupStage.showAndWait();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                button.getStyleClass().add("btn-design");
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    setGraphic(button);
                } else {
                    setGraphic(null);
                }
            }

        });
    }

    @FXML
    void SendRequest(ActionEvent event) {
        String username = "tranp6648@gmail.com";
        String password = "zmaa lqss pbup xpwm";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(getEmail()));
            message.setSubject("processing request");
            String body = "Hello," + getEmployee() + "\n" + AreaRequest.getText();
            message.setText(body);
            Transport.send(message);
            Alert.DialogSuccess("request sent successfully");
            AreaRequest.setText("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayorder();
        orderDate.valueProperty().addListener((obs,oldValue,newValue)->{
        if(newValue!=null){
            String selectedDate=newValue.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            filterDay(selectedDate);
        }
        });
        detailPagination.currentPageIndexProperty().addListener((obs,oldIndex,newIndex)->{
        currentPageIndex=newIndex.intValue();
            detailorder(getNameCustomer(), displayingOrder);
        });
        pagnation.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displayingOrder == 1) {
                displayorder();
            } else if (displayingOrder == 2) {
                ordernotyet();
            } else if (displayingOrder == 3) {
                searchorder(textfield.getText(), currentPageIndex);
            }else if(displayingOrder==4){
                orderyet();
            }else if(displayingOrder==5){
                filterDay(orderDate.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }
            
            displayorder();
        });
    }
}
