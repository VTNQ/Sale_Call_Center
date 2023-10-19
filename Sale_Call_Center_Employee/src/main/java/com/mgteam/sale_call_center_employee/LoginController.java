package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mgteam.sale_call_center_employee.util.MD5;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Projections;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.util.Observer;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.bson.Document;
import org.bson.conversions.Bson;

public class LoginController implements Initializable {

    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField UsernameField;

    @FXML
    private MFXTextField Email;

    @FXML
    private TextField otp1 = new TextField();

    @FXML
    private TextField otp2 = new TextField();

    @FXML
    private TextField otp3 = new TextField();

    @FXML
    private TextField otp4 = new TextField();

    @FXML
    private TextField otp5 = new TextField();

    @FXML
    private TextField otp6 = new TextField();

    @FXML
    void login(ActionEvent event) {
        if (UsernameField.getText().isEmpty() && PasswordField.getText().isEmpty()) {
            UsernameField.getStyleClass().add("text_field_error");
            UsernameField.applyCss();
            PasswordField.getStyleClass().add("text_field_error");
            PasswordField.applyCss();
            DialogAlert.DialogError("Username and Password is require");
        } else if (UsernameField.getText().isEmpty()) {
            UsernameField.getStyleClass().add("text_field_error");
            UsernameField.applyCss();
            DialogAlert.DialogError("Username is require");
        } else if (PasswordField.getText().isEmpty()) {
            PasswordField.getStyleClass().add("text_field_error");
            PasswordField.applyCss();
            DialogAlert.DialogError("Password is require");
        } else if (!UsernameField.getText().isEmpty() && !PasswordField.getText().isEmpty()) {
            MongoCollection<Document> empCollection = DBConnection.getConnection().getCollection("Employee");
            Bson filter = Filters.and(Filters.eq("Username", MD5.Md5(UsernameField.getText())), Filters.eq("Password", MD5.Md5(PasswordField.getText())));
            MongoIterable<Document> results = empCollection.find(filter);
            boolean isFound = false;
            for (Document result : results) {
                isFound = true;
                try {
                    if (result.getString("UserType").equals(1)) {
                        DialogAlert.DialogSuccess("Login Success");
                        App.setRoot("WarehouseStaff");
                    }
                    if (result.getString("UserType").equals(2)) {
                        DialogAlert.DialogSuccess("Login Success");
                        App.setRoot("SalePerson");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (isFound == false) {
                DialogAlert.DialogError("Account not exist");
            }
        }
    }

    @FXML
    void forgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/ForgetPassword.fxml"));
            AnchorPane anchorPane = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(anchorPane, 400, 300));
            stage.showAndWait();
            stage.setResizable(false);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private String GetOTP() {
        Random r = new Random();
        int otp = 100000 + r.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendConfirmationEmail(String otp, String email) {

        String username = "votannamquoc@gmail.com";
        String password = "whrn lkpj vpyt nyjb";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Account Information");
            String body = "Your OTP:" + otp;
            message.setText(body);
            Transport.send(message);
            System.out.println("Email sent successfully.");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void checkEmail(ActionEvent event) {
        if (!Email.getText().isEmpty()) {
            MongoCollection<Document> EmailEmployee = DBConnection.getConnection().getCollection("Employee");
            Bson filter = Filters.or(Filters.eq("Email", Email.getText()), Filters.eq("username", Email.getText()));
            MongoIterable<Document> result = EmailEmployee.find(filter).projection(Projections.fields(Projections.include("Email")));
            String otp = GetOTP();
            Bson update = Updates.set("OTP", otp);
            UpdateResult updateResult = EmailEmployee.updateOne(filter, update);
            for (Document document : result) {
                sendConfirmationEmail(otp, document.getString("Email"));
                DialogAlert.DialogSuccess("Please get OTP in your Email");
            }
        } else {
            DialogAlert.DialogError("Email/Username is require");
            Email.getStyleClass().add("text_field_error");
            Email.applyCss();
        }
    }

    @FXML
    void sendReport(ActionEvent event) {
        if (!otp1.getText().isEmpty() && !otp2.getText().isEmpty() && !otp3.getText().isEmpty() && !otp4.getText().isEmpty() && !otp5.getText().isEmpty() && !otp6.getText().isEmpty()) {
            MongoCollection<Document> employees = DBConnection.getConnection().getCollection("Employee");
            String otp=otp1.getText()+otp2.getText()+otp3.getText()+otp4.getText()+otp5.getText()+otp6.getText();
            Document employee=employees.find(Filters.and(Filters.eq("Email", Email.getText()),Filters.eq("OTP", otp))).first();
            if(employee!=null){
                Document request=new Document("EmailEmployee", Email.getText());
                InsertOneResult insertOneResult=employees.insertOne(request);
                DialogAlert.DialogSuccess("Send Success");
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        otp1.textProperty().addListener(((observable, oldvalue, newvalue) -> {
            if (newvalue.length() == 1) {
                otp2.requestFocus();
            }
        }));
        otp2.textProperty().addListener(((observable, oldvalue, newvalue) -> {
            if (newvalue.length() == 1) {
                otp3.requestFocus();
            } else if (newvalue.isEmpty()) {
                otp1.requestFocus();
            }
        }));
        otp3.textProperty().addListener(((observable, oldvalue, newvalue) -> {
            if (newvalue.length() == 1) {
                otp4.requestFocus();
            } else if (newvalue.isEmpty()) {
                otp2.requestFocus();
            }
        }));
        otp4.textProperty().addListener(((observable, oldvalue, newvalue) -> {
            if (newvalue.length() == 1) {
                otp5.requestFocus();
            } else if (newvalue.isEmpty()) {
                otp3.requestFocus();
            }
        }));
        otp5.textProperty().addListener(((observable, oldvalue, newvalue) -> {
            if (newvalue.length() == 1) {
                otp6.requestFocus();
            } else if (newvalue.isEmpty()) {
                otp4.requestFocus();
            }
        }));
        otp6.textProperty().addListener(((observable, oldvalue, newvalue) -> {
            if (newvalue.isEmpty()) {
                otp5.requestFocus();
            }
        }));
        otp1.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE));
        });
        otp2.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE));
        });
        otp3.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE));
        });
        otp4.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE));
        });
        otp5.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE));
        });
        otp6.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE));
        });
    }
}
