package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.connect.DBconnect;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.sun.mail.imap.IdleManager;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;

import javafx.animation.ScaleTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class MainController implements Initializable {

    @FXML
    private ComboBox<String> FilterMonthorder = new ComboBox<>();
    @FXML
    private ComboBox<String> filterMonth = new ComboBox<>();
    @FXML
    private AreaChart<String, Number> customerchart;
    @FXML
    private Label inventory = new Label();
    @FXML
    private BarChart<String, Number> totalInventory;
    @FXML
    private ComboBox<String> FilterInventory = new ComboBox<>();

    @FXML
    private Label order = new Label();
    @FXML
    private Label customer = new Label();
    @FXML
    private LineChart<String, Number> chartOrder;

    @FXML
    private Label User = new Label();
    
    public static ObjectId IdManager;

    @FXML
    private AnchorPane maindisplay;

    private String coutTotalorder() {
        MongoCollection<Document> orderCollection = DBconnect.getdatabase().getCollection("Order");
        long totalOrders = orderCollection.countDocuments();
        return String.valueOf(totalOrders);
    }

    @FXML
    void Inventory(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/inventory.fxml"));
        try {
            AnchorPane classpane = loader.load();
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), classpane);
            scaleTransition.setFromX(0.5);
            scaleTransition.setFromX(0.5);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            maindisplay.getChildren().clear();
            maindisplay.getChildren().addAll(classpane);
            scaleTransition.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML

    void Order(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/order_customer.fxml"));
            AnchorPane classpane = loader.load();

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), maindisplay);
            transition.setFromX(0);
            transition.setToX(maindisplay.getWidth());
            TranslateTransition reverstation = new TranslateTransition(Duration.seconds(0.5), maindisplay);
            reverstation.setFromX(maindisplay.getWidth());
            reverstation.setToX(0);
            transition.setOnFinished(e -> {
                try {
                    maindisplay.getChildren().clear();
                    maindisplay.getChildren().addAll(classpane);

                    reverstation.play();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            });

            transition.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void change_pass(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Change_pass.fxml"));
            AnchorPane classpane = loader.load();

            // Đặt hiệu ứng RotateTransition
            RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.5), maindisplay);
            rotateOut.setAxis(Rotate.Y_AXIS);
            rotateOut.setFromAngle(0);
            rotateOut.setToAngle(90);

            rotateOut.setOnFinished(e -> {
                maindisplay.getChildren().clear();
                maindisplay.getChildren().addAll(classpane);

                RotateTransition rotateIn = new RotateTransition(Duration.seconds(0.5), maindisplay);
                rotateIn.setAxis(Rotate.Y_AXIS);
                rotateIn.setFromAngle(90);
                rotateIn.setToAngle(0);
                rotateIn.play();
            });

            rotateOut.play();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void logout(ActionEvent event) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText(null);
        alert.setContentText("Are you Logout?");
        alert.showAndWait().ifPresent(responsive -> {
            if (responsive == ButtonType.CLOSE) {
                alert.close();
            }
            if (responsive == ButtonType.OK) {
                try {
                    App.setRoot("Login");
                } catch (Exception e) {
                }
            }
        });
    }

    private String totalCustomer() {
        int count=0;
        MongoCollection<Document> Customer = DBconnect.getdatabase().getCollection("Customer");
        MongoCollection<Document> Employee = DBconnect.getdatabase().getCollection("Employee");
        MongoCollection<Document> Manager = DBconnect.getdatabase().getCollection("Admin");
        FindIterable<Document>findIterable=Employee.find(Filters.eq("id_Manager",LoginController.idEmployee));
        for (Document document : findIterable) {
            ObjectId id_employee=document.getObjectId("_id");
            FindIterable<Document>documents=Customer.find(Filters.eq("id_employee", id_employee));
            for (Document customer : documents) {
                count++;
            }
        }
        return String.valueOf(count);
    }

    @FXML
    void Customer(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/List_customer.fxml"));
        try {
            AnchorPane classpane = loader.load();

            FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), maindisplay);
            fadeOut.setFromValue(1);
            fadeOut.setToValue(0);
            fadeOut.setOnFinished(e -> {
                maindisplay.getChildren().clear();
                maindisplay.getChildren().addAll(classpane);

                FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), maindisplay);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(1);
                fadeIn.play();
            });

            fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Employee(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Create_Employee.fxml"));
        try {
            AnchorPane classpane = loader.load();

            maindisplay.getChildren().clear();
            maindisplay.getChildren().addAll(classpane);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String totalIventory() {
        MongoCollection<Document> Inventory = DBconnect.getdatabase().getCollection("WareHouse");
        long totalInventory = Inventory.countDocuments();
        return String.valueOf(totalInventory);
    }

    @FXML
    void Home(ActionEvent event) {
        try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void countCustomerIdByOrderDate(String selectedMonth, AreaChart<String, Number> aeachart) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        Month month = Month.from(monthFormatter.parse(selectedMonth));
        int monthValue = month.getValue();
        String formattedMonth = String.format("%02d", monthValue);

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), monthValue, 1);
        LocalDate endDate = startDate.plusMonths(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        MongoCollection<Document> orders = DBconnect.getdatabase().getCollection("Order");
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<String> dateStrings = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateStrings.add(date.format(formatter));
        }

        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.in("Order_date", dateStrings));
        filters.add(Filters.exists("id_Customer"));

        long[] counts = new long[dateStrings.size()];
        int index = 0;

        for (String dateString : dateStrings) {
            filters.set(0, Filters.eq("Order_date", dateString));
            long count = orders.countDocuments(Filters.and(filters));
            counts[index++] = count;
        }

        for (int i = 0; i < dateStrings.size(); i++) {
            series.getData().add(new XYChart.Data<>(dateStrings.get(i), counts[i]));
        }

        if (aeachart != null) {
            aeachart.getData().clear();
            aeachart.getData().add(series);
        }
    }

    private void countorderIDbyorderDate(String selectedMonth, LineChart<String, Number> chartOrder) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        Month month = Month.from(monthFormatter.parse(selectedMonth));
        int monthvalue = month.getValue();
        String formattedMonth = String.format("%02d", monthvalue);
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), monthvalue, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        MongoCollection<Document> orders = DBconnect.getdatabase().getCollection("Order");
        XYChart.Series<String, Number> seris = new XYChart.Series<>();

        List<String> dateStrings = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateStrings.add(date.format(formatter));
        }

        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.in("Order_date", dateStrings));

        long[] counts = new long[dateStrings.size()];
        int index = 0;

        for (String dateString : dateStrings) {
            filters.set(0, Filters.eq("Order_date", dateString));
            long count = orders.countDocuments(Filters.and(filters));
            
            counts[index++] = count;
        }

        for (int i = 0; i < dateStrings.size(); i++) {
            seris.getData().add(new XYChart.Data<>(dateStrings.get(i), counts[i]));
        }

        if (chartOrder != null) {
            chartOrder.getData().clear();
            chartOrder.getData().add(seris);
        }
    }

    private void displaytotalInventoryData(String selectedMonth, BarChart<String, Number> totalInventoryChart) {
        DateTimeFormatter monthformatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        Month month = Month.from(monthformatter.parse(selectedMonth));
        int monthvalue = month.getValue();
        String formattedMonth = String.format("%02d", monthvalue);
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), monthvalue, 1);
        LocalDate Enddate = startDate.plusMonths(1).minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        MongoCollection<Document> orders = DBconnect.getdatabase().getCollection("Order");
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<String> dateStrings = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(Enddate); date = date.plusDays(1)) {
            dateStrings.add(date.format(formatter));
        }

        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.in("Order_date", dateStrings));
        filters.add(Filters.exists("_id"));

        long[] counts = new long[dateStrings.size()];
        int index = 0;

        for (String dateString : dateStrings) {
            filters.set(0, Filters.eq("Order_date", dateString));
            long count = orders.countDocuments(Filters.and(filters));
            counts[index++] = count;
        }

        for (int i = 0; i < dateStrings.size(); i++) {
            series.getData().add(new XYChart.Data<>(dateStrings.get(i), counts[i]));
        }

        if (totalInventoryChart != null) {
            totalInventoryChart.getData().clear();
            totalInventoryChart.getData().add(series);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        User.setText(LoginController.user);
        customer.setText(totalCustomer());
        order.setText(coutTotalorder());
        inventory.setText(totalIventory());
        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        String currentMonthName = currentMonth.name().substring(0, 1) + currentMonth.name().substring(1).toLowerCase();
        FilterMonthorder.setItems(months);
        FilterMonthorder.setValue(currentMonthName);
        countorderIDbyorderDate(FilterMonthorder.getValue(), chartOrder);
        FilterMonthorder.setOnAction(event -> {
            countorderIDbyorderDate(FilterMonthorder.getValue(), chartOrder);
        });
        
    }

}
