package com.mgteam.sale_call_center_director;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class SecondaryController implements Initializable{

    @FXML
    private Label label=new Label();
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       label.setText(LoginController.username);
       
    }

}
