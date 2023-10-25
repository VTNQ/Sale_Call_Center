/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.connect.DBconnect;
import com.mgteam.sale_call_center_manager.connect.util.daodb;
import com.mgteam.sale_call_center_manager.model.Order;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.beans.Observable;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author tranp
 */
public class Order_Customer implements Initializable {

    @FXML
    private MFXTextField textfield;
    @FXML
    private TableColumn<?, ?> colDay = new TableColumn<>();
    @FXML
    private Label total;
    @FXML
    private TableView<Order> tblDetail = new TableView<>();
    @FXML
    private TableColumn<?, ?> colEmployee = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colcustomer = new TableColumn<>();
    @FXML
    private Label customer = new Label();
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
    private TableColumn<?, ?> colstatus = new TableColumn<>();
    @FXML
    private TableView<Order> tblOrder = new TableView<>();

    private void setcustomer(String customer) {
        this.customer.setText(customer);
    }
    private void setEmail(String Email){
        this.Email=Email;
    }
    private String getEmail(){
        return Email;
    }
    private void setEmployee(String Employee){
        this.Employee=Employee;
    }
    private String getEmployee(){
        return Employee;
    }
    private void detailorder(String name) {
        List<Order> detailorder = daodb.getdetailCustomer(name);
        ObservableList<Order> observableList = FXCollections.observableArrayList(detailorder);
        tblDetail.setItems(observableList);
        colProduct.setCellValueFactory(new PropertyValueFactory<>("name"));
        colQuality.setCellValueFactory(new PropertyValueFactory<>("Quality"));
        colPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));
    }


    @FXML
    void Findsearch(ActionEvent event) {
        List<Order> orderest = daodb.SearchorderCustomer(textfield.getText());
        ObservableList<Order> obserableList = FXCollections.observableArrayList(orderest);
        tblOrder.setItems(obserableList);
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
                       Order_Customer Order=loader.getController();
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
                button.getStyleClass().add("btn-design");
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    setGraphic(button);
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
                        Order.detailorder(order.getName());
                        List<Order> orders = daodb.getdetailCustomer(order.getName());
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
        tblOrder.setItems(observableList);
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
                       Order_Customer Order=loader.getController();
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
                button.getStyleClass().add("btn-design");
                super.updateItem(item, empty);
                if (item != null || !empty) {
                    setGraphic(button);
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
                        Order.detailorder(order.getName());
                        List<Order> orders = daodb.getdetailCustomer(order.getName());
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
            String body = "Hello," + getEmployee() +"\n" +AreaRequest.getText();
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

    }
}
