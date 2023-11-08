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
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
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
import org.bson.types.ObjectId;

public class LoginController implements Initializable {

    @FXML
    private Button ChangePassword;

    @FXML
    private PasswordField PasswordField;
    public static ObjectId id_employee;
    public static String username;
    public static String name;
    public static String email;
    public static String phone;
    public static String since;
    @FXML
    private TextField UsernameField;
    @FXML
    private PasswordField NewPassword;

    @FXML
    private PasswordField RenewPassword;
    @FXML
    private MFXTextField Email;

    @FXML
    private TextField otp1 = new TextField();
    public static String user;
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
                    if (result.getInteger("usertype") == 1) {
                        DialogAlert.DialogSuccess("Login Success");
                        id_employee = result.getObjectId("_id");
                        username = UsernameField.getText();
                        name = result.getString("Name");
                        email = result.getString("Email");
                        phone = result.getString("Phone");
                        since = result.getString("Since");
                        App.setRoot("SalePerson");
                         if (istatus(user)) {
                            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/popupWarehouse.fxml"));
                            AnchorPane popup = loader.load();
                            Stage popupstage = new Stage();
                            popupstage.initModality(Modality.APPLICATION_MODAL);
                            popupstage.setScene(new Scene(popup));
                            popupstage.setOnCloseRequest(closeEvent -> {
                                MongoCollection<Document> updatecollection = DBConnection.getConnection().getCollection("Employee");
                                Bson filterupdate = Filters.eq("Username", user);
                                Bson updateStatus = Updates.set("status", 1);
                                UpdateResult updates = updatecollection.updateOne(filterupdate, updateStatus);
                            });
                            popupstage.setResizable(true);
                            popupstage.showAndWait();
                        }
                    }
                    if (result.getInteger("usertype") == 2) {
                        DialogAlert.DialogSuccess("Login Success");
                        id_employee = result.getObjectId("_id");
                        username = UsernameField.getText();
                        name = result.getString("Name");
                        email = result.getString("Email");
                        phone = result.getString("Phone");
                        since = result.getString("Since");
                        user = MD5.Md5(UsernameField.getText());
                        if (istatus(user)) {
                            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_employee/view/popupWarehouse.fxml"));
                            AnchorPane popup = loader.load();
                            Stage popupstage = new Stage();
                            popupstage.initModality(Modality.APPLICATION_MODAL);
                            popupstage.setScene(new Scene(popup));
                            popupstage.setOnCloseRequest(closeEvent -> {
                                MongoCollection<Document> updatecollection = DBConnection.getConnection().getCollection("Employee");
                                Bson filterupdate = Filters.eq("Username", user);
                                Bson updateStatus = Updates.set("status", 1);
                                UpdateResult updates = updatecollection.updateOne(filterupdate, updateStatus);
                            });
                            popupstage.setResizable(true);
                            popupstage.showAndWait();
                        }
                        App.setRoot("WarehouseStaff");
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (isFound == false) {
                DialogAlert.DialogError("Account not exist Or Username/Password Incorrect");
            }
        }
    }

    @FXML
    void Change(ActionEvent event) {
        if (!NewPassword.getText().isEmpty() && !RenewPassword.getText().isEmpty()) {
            if (NewPassword.getText().equals(RenewPassword.getText())) {
                if (NewPassword.getText().length() >= 8 && RenewPassword.getText().length() >= 8) {
                    MongoCollection<Document> collection = DBConnection.getConnection().getCollection("Employee");
                    Bson filter = Filters.eq("Username", user);
                    Bson upate = Updates.set("Password", MD5.Md5(RenewPassword.getText()));
                    Bson updateStatus = Updates.set("status", 1);
                    List<Bson> updatesOperations = new ArrayList<>();
                    updatesOperations.add(upate);
                    updatesOperations.add(updateStatus);
                    Bson combinedUpdates = Updates.combine(updatesOperations);
                    UpdateResult updates = collection.updateOne(filter, combinedUpdates);
                    if (updates.getModifiedCount() > 0) {
                        DialogAlert.DialogSuccess("Change Password successfully");
                        Stage stage = (Stage) ChangePassword.getScene().getWindow();
                        stage.close();
                    }
                }else{
                    DialogAlert.DialogError("Password must be at least 8 characters long");
                }
            }else{
                 DialogAlert.DialogError("New Password must match renew Password");
            }
        }else if (!NewPassword.getText().isEmpty() && RenewPassword.getText().isEmpty()) {
            DialogAlert.DialogError("Re_new Password is required");
        } else if (NewPassword.getText().isEmpty() && !RenewPassword.getText().isEmpty()) {
             DialogAlert.DialogError("New Password is required");
        } else if (NewPassword.getText().isEmpty() && RenewPassword.getText().isEmpty()) {
             DialogAlert.DialogError("New Password And Re_new Password is required");
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
            Bson filter = Filters.or(Filters.eq("Email", Email.getText()), Filters.eq("Username", Email.getText()));
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
            String otp = otp1.getText() + otp2.getText() + otp3.getText() + otp4.getText() + otp5.getText() + otp6.getText();
            Document employee = employees.find(Filters.and(Filters.eq("Email", Email.getText()), Filters.eq("OTP", otp))).first();
            if (employee != null) {
                MongoCollection<Document> Reports = DBConnection.getConnection().getCollection("Request");
                Document request = new Document("EmailEmployee", Email.getText()).append("status", 0);
                InsertOneResult insertOneResult = Reports.insertOne(request);
                DialogAlert.DialogSuccess("Send Success");
            }
        }else{
            DialogAlert.DialogError("Enter otp");
        }

    }

    private boolean istatus(String username) {
        MongoCollection<Document> Employee = DBConnection.getConnection().getCollection("Employee");
        Document query = new Document("Username", username);
        query.append("status", 0);
        Document result = Employee.find(query).first();
        return result != null;
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
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                otp1.clear();
            }
        });
        otp2.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                otp2.clear();
                otp1.requestFocus();
            }
        });
        otp3.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                otp3.clear();
                otp2.requestFocus();
            }
        });
        otp4.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                otp4.clear();
                otp3.requestFocus();
            }
        });
        otp5.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                otp5.clear();
                otp4.requestFocus();
            }
        });
        otp6.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                otp6.clear();
                otp5.requestFocus();
            }
        });
    }
}
