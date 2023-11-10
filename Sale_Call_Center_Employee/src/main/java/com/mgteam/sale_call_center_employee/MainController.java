package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Filters.lte;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import org.bson.Document;

import org.bson.types.ObjectId;

public class MainController implements Initializable {

    @FXML
    private AnchorPane mainWarehouse;
    @FXML
    private TextField username = new TextField();

    @FXML
    private AnchorPane MainDisplay;

    @FXML
    private Label user = new Label();
    @FXML
    private Label TotalCustomer = new Label();
    @FXML
    private LineChart<String, Number> chartext;
    @FXML
    private ComboBox<String> FilterMonthorder = new ComboBox();
    @FXML
    private Label TotalOrder = new Label();

    @FXML
    private MFXButton btnChangePassword = new MFXButton();

    @FXML
    private MFXButton btnCustomer = new MFXButton();

    @FXML
    private MFXButton btnHome = new MFXButton();

    @FXML
    private MFXButton btnOrder = new MFXButton();

    @FXML
    private MFXButton btnProfile = new MFXButton();
    @FXML
    private Label ingoing = new Label();
    @FXML
    private Label totalgoing = new Label();

    @FXML
    void Homewarehouse(ActionEvent event) {
        try {
            App.setRoot("WarehouseStaff");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void changePassword(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/changePassword.fxml"));
        try {
            AnchorPane changPassword = loader.load();
            MainDisplay.getChildren().clear();
            MainDisplay.getChildren().setAll(changPassword);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        btnChangePassword.getStyleClass().add("bg-active");
        btnHome.getStyleClass().remove("bg-active");
        btnOrder.getStyleClass().remove("bg-active");
        btnCustomer.getStyleClass().remove("bg-active");
        btnProfile.getStyleClass().remove("bg-active");
    }
    private String totaloutgoing = null;
    private String totalingoing = null;

    private String counttotalingoing() {
        if (totalingoing == null) {
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("WareHouse_InComingOrder");
            long totalOrders = orderCollection.countDocuments();
            totalingoing = String.valueOf(totalOrders);
        }
        return totalingoing;
    }

    private String countTotalOrder() {
        if (totaloutgoing == null) {
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("WareHouse_OutGoingOrder");
            long totalOrders = orderCollection.countDocuments();
            totaloutgoing = String.valueOf(totalOrders);
        }
        return totaloutgoing;
    }

    @FXML
    void Warehouse(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/ListWarehouse.fxml"));
            AnchorPane previousView = loader.load();
            RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.5), mainWarehouse);
            rotateOut.setAxis(Rotate.X_AXIS); // Sử dụng trục X
            rotateOut.setFromAngle(0);
            rotateOut.setToAngle(90); // Điều chỉnh góc quay
            rotateOut.setOnFinished(e -> {
                mainWarehouse.getChildren().clear();
                mainWarehouse.getChildren().addAll(previousView);
                RotateTransition rotateIn = new RotateTransition(Duration.seconds(0.5), mainWarehouse);
                rotateIn.setAxis(Rotate.X_AXIS); // Sử dụng trục X
                rotateIn.setFromAngle(90); // Điều chỉnh góc quay
                rotateIn.setToAngle(0);
                rotateIn.play();
            });
            rotateOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void Supply(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/Supply.fxml"));
            AnchorPane newPane = loader.load();
            FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), newPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
            mainWarehouse.getChildren().clear();
            mainWarehouse.getChildren().add(newPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Change(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/ChangePasswordWarehouse.fxml"));
        try {
            AnchorPane ExportWarehouse = loader.load();
            RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.5), mainWarehouse);
            rotateOut.setAxis(Rotate.Y_AXIS);
            rotateOut.setFromAngle(0);
            rotateOut.setToAngle(90);
            rotateOut.setOnFinished(e -> {
                mainWarehouse.getChildren().clear();
                mainWarehouse.getChildren().addAll(ExportWarehouse);
                RotateTransition rotateIn = new RotateTransition(Duration.seconds(0.5), mainWarehouse);
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
    void Category(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/ListCategory.fxml"));
            AnchorPane newPane = loader.load();
            mainWarehouse.getChildren().clear();
            mainWarehouse.getChildren().add(newPane);
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(1000), newPane);
            scaleTransition.setFromX(0.5);
            scaleTransition.setToX(1.0);
            scaleTransition.setFromY(0.5);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Product(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/ListProduct.fxml"));
        try {
            AnchorPane ExportWarehouse = loader.load();
            TranslateTransition slidein = new TranslateTransition(Duration.millis(1000), ExportWarehouse);
            slidein.setFromY(600);
            slidein.setToY(0);
            slidein.play();
            mainWarehouse.getChildren().clear();
            mainWarehouse.getChildren().addAll(ExportWarehouse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Export(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/ExportWarehouse.fxml"));
            AnchorPane newPane = loader.load();
            TranslateTransition slideIn = new TranslateTransition(Duration.millis(1000), newPane);
            slideIn.setFromX(600);
            slideIn.setToX(0);
            slideIn.play();
            mainWarehouse.getChildren().clear();
            mainWarehouse.getChildren().add(newPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void customer(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("MainCustomer.fxml"));
        try {
            AnchorPane HomeCustomer = loader.load();
            MainDisplay.getChildren().clear();
            MainDisplay.getChildren().setAll(HomeCustomer);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        btnChangePassword.getStyleClass().remove("bg-active");
        btnHome.getStyleClass().remove("bg-active");
        btnOrder.getStyleClass().remove("bg-active");
        btnCustomer.getStyleClass().add("bg-active");
        btnProfile.getStyleClass().remove("bg-active");
    }

    @FXML
    void home(ActionEvent event) {
        MongoCollection<Document> EmployeeCollection = DBConnection.getConnection().getCollection("Employee");
        FindIterable<Document> iterable = EmployeeCollection.find();
        try {
            for (Document document : iterable) {
                if (document.getInteger("status") == 1) {
                    App.setRoot("SalePerson");
                } else {
                    App.setRoot("WarehouseStaff");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        btnChangePassword.getStyleClass().remove("bg-active");
        btnHome.getStyleClass().add("bg-active");
        btnOrder.getStyleClass().remove("bg-active");
        btnCustomer.getStyleClass().remove("bg-active");
        btnProfile.getStyleClass().remove("bg-active");
    }

    @FXML
    void logout(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText(null);
        alert.setContentText("Are You Logout?");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.CANCEL) {
                alert.close();
            }
            if (response == ButtonType.OK) {
                try {
                    LoginController.username = "";
                    App.setRoot("login");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @FXML
    void order(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("MainOrder.fxml"));
        try {
            AnchorPane HomeOrder = loader.load();
            MainDisplay.getChildren().clear();
            MainDisplay.getChildren().setAll(HomeOrder);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        btnChangePassword.getStyleClass().remove("bg-active");
        btnHome.getStyleClass().remove("bg-active");
        btnOrder.getStyleClass().add("bg-active");
        btnCustomer.getStyleClass().remove("bg-active");
        btnProfile.getStyleClass().remove("bg-active");
    }

    @FXML
    void profile(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("Profile.fxml"));
        try {
            AnchorPane Profile = loader.load();
            MainDisplay.getChildren().clear();
            MainDisplay.getChildren().setAll(Profile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        btnChangePassword.getStyleClass().remove("bg-active");
        btnHome.getStyleClass().remove("bg-active");
        btnOrder.getStyleClass().remove("bg-active");
        btnCustomer.getStyleClass().remove("bg-active");
        btnProfile.getStyleClass().add("bg-active");
    }

    private int TotalCustomer() {
        int count = 0;
        MongoCollection<Document> totalCustomer = DBConnection.getConnection().getCollection("Customer");
        MongoIterable<Document> result = totalCustomer.find();
        for (Document document : result) {
            count++;
        }
        return count;
    }

    private void countOrderIDByOrderDate(String selectedMonth, LineChart<String, Number> chartOrder) {
        try {

            MongoCollection<Document> ordersCollection = DBConnection.getConnection().getCollection("OutGoingOrder");
            MongoCollection<Document> warehouseOrdersCollection = DBConnection.getConnection().getCollection("WareHouse_OutGoingOrder");

            // Chuyển đổi tên tháng thành số
            DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
            Month month = Month.from(monthFormatter.parse(selectedMonth));
            int monthValue = month.getValue();

            // Tạo ngày bắt đầu và kết thúc của tháng
            LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), monthValue, 1);
            LocalDate endDate = startDate.plusMonths(1).minusDays(1);

            int totalCount = 0;

            FindIterable<Document> outgoing = ordersCollection.find(and(gte("Date", startDate.toString()), lte("Date", endDate.toString())));
            for (Document document : outgoing) {
                ObjectId id = document.getObjectId("_id");
                Document ware = warehouseOrdersCollection.find(eq("ID_OutGoingOrder", id)).first();
                if (ware != null) {
                    totalCount++;
                }
            }

            // Cập nhật biểu đồ với số lượng tổng cộng
            XYChart.Series<String, Number> series = new XYChart.Series<>();
            series.getData().add(new XYChart.Data<>(selectedMonth, totalCount));

            if (chartOrder != null) {
                chartOrder.getData().clear();
                chartOrder.getData().add(series);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TotalCustomer.setText(String.valueOf(TotalCustomer()));
        username.setText(LoginController.username);
        user.setText("Welcome," + LoginController.username);
        totalgoing.setText(countTotalOrder());
        ingoing.setText(counttotalingoing());
        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        String currentMonthName = currentMonth.name().substring(0, 1) + currentMonth.name().substring(1).toLowerCase();
        FilterMonthorder.setItems(months);
        FilterMonthorder.setValue(currentMonthName);
        countOrderIDByOrderDate(FilterMonthorder.getValue(), chartext);
        FilterMonthorder.setOnAction(event -> {
            countOrderIDByOrderDate(FilterMonthorder.getValue(), chartext);
        });
    }

}
