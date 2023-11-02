package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import io.github.palexdev.materialfx.controls.MFXButton;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.bson.Document;

public class MainController implements Initializable {

    @FXML
    private AnchorPane mainWarehouse;
    @FXML
    private TextField username = new TextField();

    @FXML
    private AnchorPane MainDisplay;

    @FXML
    private Label TotalCustomer = new Label();

    @FXML
    private Label TotalOrder = new Label();
    
    @FXML
    private MFXButton btnChangePassword=new MFXButton();

    @FXML
    private MFXButton btnCustomer=new MFXButton();

    @FXML
    private MFXButton btnHome=new MFXButton();

    @FXML
    private MFXButton btnOrder=new MFXButton();

    @FXML
    private MFXButton btnProfile=new MFXButton();

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

    @FXML
    void Import(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/ImportWarehouse.fxml"));
        try {
            AnchorPane ImportWarehouse = loader.load();
            mainWarehouse.getChildren().clear();
            mainWarehouse.getChildren().addAll(ImportWarehouse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Change(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/ChangePasswordWarehouse.fxml"));
        try {
            AnchorPane ExportWarehouse = loader.load();
            mainWarehouse.getChildren().clear();
            mainWarehouse.getChildren().addAll(ExportWarehouse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Product(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/Product.fxml"));
        try {
            AnchorPane ExportWarehouse = loader.load();
            mainWarehouse.getChildren().clear();
            mainWarehouse.getChildren().addAll(ExportWarehouse);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Export(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/ExportWarehouse.fxml"));
        try {
            AnchorPane ExportWarehouse = loader.load();
            mainWarehouse.getChildren().clear();
            mainWarehouse.getChildren().addAll(ExportWarehouse);
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TotalCustomer.setText(String.valueOf(TotalCustomer()));
        username.setText(LoginController.username);
    }

}
