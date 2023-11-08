package com.mgteam.sale_call_center_director;

import com.mgteam.sale_call_center_director.connect.DBConnect;
import com.mgteam.sale_call_center_director.util.Dialog;
import com.mgteam.sale_call_center_director.util.MD5;
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
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
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
    private PasswordField Password;

    @FXML
    private TextField Username;
    public static ObjectId id_employee;
    public static String username;
    public static String name;
    public static String email;
    public static String phone;
    public static String user;
    public static String since;
    @FXML
    private MFXTextField Email = new MFXTextField();

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
    private Button ChangePassword = new Button();

    @FXML
    private PasswordField NewPassword = new PasswordField();

    @FXML
    private PasswordField RenewPassword = new PasswordField();

    private boolean istatus(String username) {
        MongoCollection<Document> Employee = DBConnect.getConnection().getCollection("Admin");
        Document query = new Document("Username", username);
        query.append("status", 0);
        query.append("Usertype", 3);
        Document result = Employee.find(query).first();
        return result != null;
    }

    @FXML
    void Change(ActionEvent event) {
        if (!NewPassword.getText().isEmpty() && !RenewPassword.getText().isEmpty()) {
            if (NewPassword.getText().equals(RenewPassword.getText())) {
                if (NewPassword.getText().length() >= 8 && RenewPassword.getText().length() >= 8) {
                    MongoCollection<Document> collection = DBConnect.getConnection().getCollection("Employee");
                    Bson filter = Filters.and(Filters.eq("Username", user), Filters.eq("Usertype", 3));
                    Bson upate = Updates.set("Password", MD5.Md5(RenewPassword.getText()));
                    Bson updateStatus = Updates.set("status", 1);
                    List<Bson> updatesOperations = new ArrayList<>();
                    updatesOperations.add(upate);
                    updatesOperations.add(updateStatus);
                    Bson combinedUpdates = Updates.combine(updatesOperations);
                    UpdateResult updates = collection.updateOne(filter, combinedUpdates);
                    if (updates.getModifiedCount() > 0) {
                        Alert.DialogSuccess("Change Password successfully");
                        Stage stage = (Stage) ChangePassword.getScene().getWindow();
                        stage.close();
                    }
                } else {
                    Alert.Dialogerror("Password must be at least 8 characters long");
                }
            } else {
                Alert.Dialogerror("New Password must match renew Password");
            }
        } else if (!NewPassword.getText().isEmpty() && RenewPassword.getText().isEmpty()) {
            Alert.Dialogerror("Re_new Password is required");
        } else if (NewPassword.getText().isEmpty() && !RenewPassword.getText().isEmpty()) {
            Alert.Dialogerror("New Password is required");
        } else if (NewPassword.getText().isEmpty() && RenewPassword.getText().isEmpty()) {
            Alert.Dialogerror("New Password And Re_new Password is required");
        }
    }

     private String GetOTP() {
        Random r = new Random();
        int otp = 100000 + r.nextInt(900000);
        return String.valueOf(otp);
    }

    private void sendOTP(String recipient, String otp) {
        String username = "tranp6648@gmail.com";
        String password = "zmaa lqss pbup xpwm";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
            message.setSubject("Code OTP");
            message.setText("Code OTP is: " + otp);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 @FXML
    void forgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_director/ForgetPassword.fxml"));
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
    @FXML

    void sendReport(ActionEvent event) {
        if (!otp1.getText().isEmpty() && !otp2.getText().isEmpty() && !otp3.getText().isEmpty()
                && !otp4.getText().isEmpty() && !otp5.getText().isEmpty() && !otp6.getText().isEmpty()) {
            String otp = otp1.getText() + otp2.getText() + otp3.getText() + otp4.getText() + otp5.getText() + otp6.getText();

            MongoCollection<Document> employees = DBConnect.getConnection().getCollection("Admin");
            Document employee = employees.find(Filters.and(Filters.eq("Email", Email.getText()), Filters.eq("OTP", otp))).first();

            if (employee != null) {

                MongoCollection<Document> reports = DBConnect.getConnection().getCollection("Request");
                Document existingRequest = reports.find(Filters.eq("EmailEmployee", Email.getText())).first();

                if (existingRequest != null) {

                    reports.updateOne(Filters.eq("EmailEmployee", Email.getText()), Updates.set("status", 0));
                    Alert.DialogSuccess("Send Success");
                } else {

                    Document request = new Document("EmailEmployee", Email.getText()).append("status", 0);
                    InsertOneResult insertOneResult = reports.insertOne(request);
                    Alert.DialogSuccess("Send Success");
                }
            } else {
                Alert.Dialogerror("Invalid OTP");
            }
        } else {
            Alert.Dialogerror("Please enter OTP");
        }
    }

    @FXML
    void checkEmail(ActionEvent event) {
        if(Email.getText().isEmpty()){
            Alert.Dialogerror("Enter your Email");
        }else{
             MongoCollection<Document> collection = DBConnect.getConnection().getCollection("Admin");

        String otp = GetOTP();
        Bson filter = Filters.and(
                Filters.eq("Email", Email.getText()),
                Filters.eq("Usertype", 3)
        );
        Bson updates = Updates.set("OTP", otp);
        UpdateResult update = collection.updateOne(filter, updates);

        if (update.getModifiedCount() > 0) {
            sendOTP(Email.getText(), otp);
            Alert.DialogSuccess("Update successfully");
           

        } else {
            Alert.Dialogerror("Can't find your gmail");
        }
        }
          
    }

    @FXML
    void Login(ActionEvent event) {
        if (Username.getText().isEmpty() && Password.getText().isEmpty()) {

            Dialog.Dialogerror("Username and Password is require");
        } else if (Username.getText().isEmpty()) {
            Dialog.Dialogerror("Username is require");
        } else if (Password.getText().isEmpty()) {
            Dialog.Dialogerror("Password is require");
        } else if (!Username.getText().isEmpty() && !Password.getText().isEmpty()) {
            MongoCollection<Document> empCollection = DBConnect.getConnection().getCollection("Admin");
            Bson filter = Filters.and(Filters.eq("Username", MD5.Md5(Username.getText())), Filters.eq("Password", MD5.Md5(Password.getText())));
            MongoIterable<Document> results = empCollection.find(filter);
            boolean isFound = false;
            for (Document result : results) {
                isFound = true;
                try {
                    if (result.getInteger("Usertype") == 3) {
                        Dialog.DialogSuccess("Login Success");
                        id_employee = result.getObjectId("_id");
                        username = Username.getText();
                        user = MD5.Md5(Username.getText());
                        name = result.getString("Name");
                        email = result.getString("Email");
                        phone = result.getString("Phone");
                        since = result.getString("Since");
                        if (istatus(user)) {
                            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_director/popup.fxml"));
                            AnchorPane popup = loader.load();
                            Stage popupstage = new Stage();
                            popupstage.initModality(Modality.APPLICATION_MODAL);
                            popupstage.setScene(new Scene(popup));
                            popupstage.setOnCloseRequest(closeEvent -> {
                                MongoCollection<Document> updatecollection = DBConnect.getConnection().getCollection("Admin");
                                Bson filterupdate = Filters.and(Filters.eq("Username", user), Filters.eq("Usertype", 3));
                                Bson updateStatus = Updates.set("status", 1);
                                UpdateResult updates = updatecollection.updateOne(filterupdate, updateStatus);
                            });
                            popupstage.setResizable(true);
                            popupstage.showAndWait();
                        }
                        App.setRoot("secondary");
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
            if (isFound == false) {
                Dialog.Dialogerror("Account not exist Or Username/Password Incorrect");
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
