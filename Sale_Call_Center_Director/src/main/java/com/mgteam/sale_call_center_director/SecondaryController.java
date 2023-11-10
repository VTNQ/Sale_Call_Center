package com.mgteam.sale_call_center_director;
import com.mgteam.sale_call_center_director.connect.DBConnect;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class SecondaryController implements Initializable{

    @FXML
    private Label label=new Label();
      @FXML
    private ComboBox<String> FilterMonthorder;

    @FXML
    private LineChart<String, Number> chartOrder;
    @FXML
    private AnchorPane maindisplay = new AnchorPane();
        @FXML
    void home(ActionEvent event) {
        try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            ex.printStackTrace();
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
  @FXML
    void change(ActionEvent event) {
  try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_director/forgotpassword.fxml"));
            AnchorPane newPane = loader.load();
            FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), newPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
            maindisplay.getChildren().clear();
            maindisplay.getChildren().add(newPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void product(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_director/item_Statics.fxml"));
            AnchorPane newPane = loader.load();
            FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), newPane);
            fadeIn.setFromValue(0.0);
            fadeIn.setToValue(1.0);
            fadeIn.play();
            maindisplay.getChildren().clear();
            maindisplay.getChildren().add(newPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void revenue_Employee(ActionEvent event) {
 try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_director/revenue_Employee.fxml"));
            AnchorPane newPane = loader.load();
            maindisplay.getChildren().clear();
            maindisplay.getChildren().add(newPane);
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
    void revenue_statistics(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_director/revenue_statistics.fxml"));
        try {
            AnchorPane ExportWarehouse = loader.load();
            RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.5), maindisplay);
            rotateOut.setAxis(Rotate.Y_AXIS);
            rotateOut.setFromAngle(0);
            rotateOut.setToAngle(90);
            rotateOut.setOnFinished(e -> {
                maindisplay.getChildren().clear();
                maindisplay.getChildren().addAll(ExportWarehouse);
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
private void countOrderIDByOrderDate(String selectedMonth, LineChart<String, Number> chartOrder) {
    DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
    Month month = Month.from(monthFormatter.parse(selectedMonth));
    int monthValue = month.getValue();
    String formattedMonth = String.format("%02d", monthValue);
    LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), monthValue, 1);
    LocalDate endDate = startDate.plusMonths(1).minusDays(1);
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    MongoCollection<Document> orders = DBConnect.getConnection().getCollection("Order");

    List<String> dateStrings = new ArrayList<>();
    for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
        dateStrings.add(date.format(formatter));
    }

    Bson filter = Filters.and(
            Filters.in("Order_date", dateStrings),
            Filters.exists("_id")
    );

    List<Document> results = new ArrayList<>();
    orders.find(filter).into(results);

    Map<String, Long> dateCountMap = results.stream()
            .collect(Collectors.groupingBy(
                    document -> document.getString("Order_date"),
                    Collectors.counting()
            ));

    XYChart.Series<String, Number> series = new XYChart.Series<>();
    for (String dateString : dateStrings) {
        long count = dateCountMap.getOrDefault(dateString, 0L);
        series.getData().add(new XYChart.Data<>(dateString, count));
    }

    if (chartOrder != null) {
        chartOrder.getData().clear();
        chartOrder.getData().add(series);
    }
}

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       label.setText(LoginController.username);
        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        LocalDate currentDate = LocalDate.now();
        Month currentMonth = currentDate.getMonth();
        String currentMonthName = currentMonth.name().substring(0, 1) + currentMonth.name().substring(1).toLowerCase();
        FilterMonthorder.setItems(months);
        FilterMonthorder.setValue(currentMonthName);
        
        countOrderIDByOrderDate(FilterMonthorder.getValue(), chartOrder);
        FilterMonthorder.setOnAction(event -> {
            countOrderIDByOrderDate(FilterMonthorder.getValue(), chartOrder);
        });
    }

}
