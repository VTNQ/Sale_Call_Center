package com.mgteam.sale_call_center_manager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;

public class SecondaryController {

   @FXML
   void logout(ActionEvent event){
       javafx.scene.control.Alert alert=new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
       alert.setTitle("CONFIRMATION");
       alert.setHeaderText(null);
       alert.setContentText("Are you Logout?");
       alert.showAndWait().ifPresent(responsive->{
       if(responsive==ButtonType.CLOSE){
           alert.close();
       }
       if(responsive==ButtonType.OK){
           try {
               App.setRoot("Login");
           } catch (Exception e) {
           }
       }
       });
   }
}