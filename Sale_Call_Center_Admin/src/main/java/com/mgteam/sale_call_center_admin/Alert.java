/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_admin;
/**
 *
 * @author tranp
 */
public class Alert {

    public static void DialogSuccess(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
        alert.setTitle("SUCCESS");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public static void Dialogerror(String message){
         javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
