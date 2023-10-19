package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.connect.DBconnect;
import com.mgteam.sale_call_center_manager.connect.MD5;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.bson.Document;
import org.bson.conversions.Bson;

public class LoginController {

    @FXML
    private AnchorPane OTP;
    @FXML
    private TextField one;

    @FXML
    private TextField three;

    @FXML
    private TextField two;
    @FXML
    private TextField four;
    @FXML
    private AnchorPane forgot;
    private String Email;
    @FXML
    private AnchorPane maindisplay;

    @FXML
    private PasswordField password;

    @FXML
    private TextField email;
    @FXML
    private TextField username;

    @FXML
    void login(ActionEvent event) {
        if(!username.getText().isEmpty() && !password.getText().isEmpty()){
            MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Employee");
        Bson filter = Filters.and(
                Filters.eq("Username", MD5.encryPassword(username.getText())),
                Filters.eq("Password", MD5.encryPassword(password.getText())),
                Filters.eq("usertype", 0),
                Filters.eq("empMgr", 1)
        );
        Document result = collection.find(filter).first();
        if (result != null) {
            Alert.DialogSuccess("Login successfully");
            try {
                App.setRoot("secondary");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else {
            Alert.Dialogerror("Login failed");
        }
        }else if(!username.getText().isEmpty() && password.getText().isEmpty()){
            Alert.Dialogerror("Password is required");
        }else if(username.getText().isEmpty() && !password.getText().isEmpty()){
            Alert.Dialogerror("Username is required");
        }else if(username.getText().isEmpty() && password.getText().isEmpty()){
            Alert.Dialogerror("Username And Password is required");
        }
        
    }

    @FXML
    void showRegisterStage(MouseEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/forgot_password.fxml"));
        try {
            AnchorPane classpane = loader.load();
            maindisplay.getChildren().clear();
            maindisplay.getChildren().addAll(classpane);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private String generateOTP() {
        Random random = new Random();
        int otp = 1000 + random.nextInt(9000);
        return String.valueOf(otp);
    }

    private void setEmail(String Emailtext) {
        Email = Emailtext;
    }

    private String getEmail() {
        return Email;
    }

    @FXML
    void Back(ActionEvent event) {
        try {
            App.setRoot("Login");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void forgot_password(ActionEvent event) {
        MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Employee");
        String otp = generateOTP();
        Bson filter = Filters.eq("Email", email.getText());
        Bson updates = Updates.set("OTP", otp);
        UpdateResult update = collection.updateOne(filter, updates);

        if (update.getModifiedCount() > 0) {
            sendOTP(email.getText(), otp);
            Alert.DialogSuccess("Update successfully");
            setEmail(email.getText());
            forgot.setVisible(false);
            OTP.setVisible(true);

        } else {
            Alert.Dialogerror("Can't find your gmail");
        }
    }

    @FXML
    void submit(ActionEvent event) {
        MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Employee");
        String otp = one.getText() + two.getText() + three.getText() + four.getText();
        Bson filter = Filters.and(
                Filters.eq("Email", getEmail()),
                Filters.eq("OTP", otp)
        );
        Document result = collection.find(filter).first();
        
        if(result!=null){
              Alert.DialogSuccess("request sent successfully");
               
            try {
                App.setRoot("Login");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }else{
              Alert.Dialogerror("request sent failed");
        }
    }
}
