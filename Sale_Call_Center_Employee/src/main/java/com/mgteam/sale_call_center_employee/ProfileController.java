package com.mgteam.sale_call_center_employee;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProfileController implements Initializable {

    @FXML
    private TextField email=new TextField();

    @FXML
    private TextField name=new TextField();

    @FXML
    private TextField phone=new TextField();

    @FXML
    private TextField since=new TextField();

    @FXML
    private TextField username=new TextField();

    @FXML
    void Edit(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("view/EditProfile.fxml"));
        AnchorPane newpopup;
        try {
            newpopup = loader.load();
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setScene(new Scene(newpopup));
            popupStage.setResizable(false);
            popupStage.showAndWait();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        username.setText(LoginController.username);
        name.setText(LoginController.name);
        email.setText(LoginController.email);
        phone.setText(LoginController.phone);
        since.setText(LoginController.since);
    }

}
