package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.connect.DBconnect;
import com.mgteam.sale_call_center_manager.connect.MD5;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.transform.Result;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class LoginController implements Initializable {

    @FXML
    private Button ChangePassword;
    @FXML
    private AnchorPane OTP;
    @FXML
    private TextField one = new TextField();
    private boolean stopLogin = false;
    @FXML
    private PasswordField NewPassword;

    @FXML
    private PasswordField RenewPassword;
    @FXML
    private TextField three = new TextField();

    @FXML
    private TextField two = new TextField();
    @FXML
    private TextField four = new TextField();
    @FXML
    private AnchorPane forgot;
    private String Email;
    public static String userName;
    public static String user;
    public static ObjectId idEmployee;
    @FXML
    private AnchorPane maindisplay;

    @FXML
    private PasswordField password;

    @FXML
    private TextField email;

    @FXML
    private TextField username = new TextField();

    @FXML
    void login(ActionEvent event) {
        if (username.getText().isEmpty() && password.getText().isEmpty()) {
            Alert.Dialogerror("Username and Password are required");
            return;
        }

        if (username.getText().isEmpty()) {
            Alert.Dialogerror("Username is required");
            return;
        }

        if (password.getText().isEmpty()) {
            Alert.Dialogerror("Password is required");
            return;
        }

        MongoCollection<Document> empCollection = DBconnect.getdatabase().getCollection("Admin");
        Bson filter = Filters.and(
                Filters.eq("Username", MD5.encryPassword(username.getText())),
                Filters.eq("Password", MD5.encryPassword(password.getText())),
                Filters.eq("Usertype", 2)
        );

        MongoIterable<Document> results = empCollection.find(filter);
        boolean isFound = false;

        for (Document result : results) {
            isFound = true;
            if (result.getInteger("Usertype") == 2) {
                Alert.DialogSuccess("Login Success");
                user = username.getText();
                userName = MD5.encryPassword(username.getText());
                ProgressBar progressBar = new ProgressBar();
                progressBar.setPrefWidth(180);

                VBox vbox = new VBox(progressBar);
                vbox.setSpacing(10);
                vbox.setAlignment(Pos.CENTER);

                Scene scene = new Scene(vbox, 200, 60);
                
                Stage progressDialogStage = new Stage();
                 progressDialogStage.setResizable(false);
                 progressDialogStage.initStyle(StageStyle.TRANSPARENT);
                progressDialogStage.setScene(scene);

                Task<Void> loginTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        while (true) {
                            for (int i = 0; i <= 100; i++) {
                                double progress = i / 100.0;
                                if (stopLogin) {
                                    break;
                                }
                                Thread.sleep(50);
                            }

                            if (progressDialogStage.isShowing()) {
                                idEmployee = result.getObjectId("_id");
                                App.setRoot("secondary");
                                Platform.runLater(() -> progressDialogStage.close());
                                break;
                            }
                        }
                        return null;
                    }
                };
                if (isstatus(MD5.encryPassword(username.getText()))) {
                    openPopup();

                }
                progressDialogStage.show();
                new Thread(loginTask).start();
            }
        }

        if (!isFound) {
            Alert.Dialogerror("Account not exist Or Username/Password Incorrect");
        }
    }

    void openPopup() {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("popup.fxml"));
            AnchorPane popup = loader.load();

            Stage popupstage = new Stage();
            popupstage.initModality(Modality.APPLICATION_MODAL);
            popupstage.setScene(new Scene(popup));
            popupstage.setOnCloseRequest(closeEvent -> {

                MongoCollection<Document> updateCollection = DBconnect.getdatabase().getCollection("Admin");
                Bson filterUpdate = Filters.eq("Username", userName);
                Bson updateStatus = Updates.set("status", 1);
                UpdateResult updates = updateCollection.updateOne(filterUpdate, updateStatus);

                // Hiển thị thanh tiến trình
                ProgressBar progressBar = new ProgressBar();
                progressBar.setPrefWidth(180);

                VBox vbox = new VBox(progressBar);
                vbox.setSpacing(10);
                vbox.setAlignment(Pos.CENTER);

                Scene scene = new Scene(vbox, 200, 60);

                Stage progressDialogStage = new Stage();
                progressDialogStage.setScene(scene);

                Task<Void> loginTask = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        while (true) {
                            for (int i = 0; i <= 100; i++) {
                                double progress = i / 100.0;
                                if (stopLogin) {
                                    break;
                                }
                                Thread.sleep(50);
                            }

                            if (progressDialogStage.isShowing()) {

                                Platform.runLater(() -> progressDialogStage.close());
                                break;
                            }
                        }
                        return null;
                    }
                };

                progressDialogStage.show();
                new Thread(loginTask).start();
            });

            popupstage.setResizable(true);
            popupstage.showAndWait();

        } catch (IOException ex) {
            ex.printStackTrace();
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

    private boolean isstatus(String Username) {
        MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Admin");
        Document query = new Document("Username", Username);
        query.append("status", 0);
        Document result = collection.find(query).first();
        return result != null;
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
        if (email.getText().isEmpty()) {
            Alert.Dialogerror("Enter your Email");
        } else {
            MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Admin");

            String otp = generateOTP();
            Bson filter = Filters.and(
                    Filters.eq("Email", email.getText()),
                    Filters.eq("Usertype", 2)
            );
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

    }

    @FXML
    void Change(ActionEvent event) {
        if (!NewPassword.getText().isEmpty() && !RenewPassword.getText().isEmpty()) {
            if (NewPassword.getText().equals(RenewPassword.getText())) {
                if (NewPassword.getText().length() >= 8 && RenewPassword.getText().length() >= 8) {
                    MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Admin");
                    Bson filter = Filters.eq("Username", userName);
                    Bson upate = Updates.set("Password", MD5.encryPassword(RenewPassword.getText()));
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
                        ProgressBar progressBar = new ProgressBar();
                        progressBar.setPrefWidth(180);

                        VBox vbox = new VBox(progressBar);
                        vbox.setSpacing(10);
                        vbox.setAlignment(Pos.CENTER);

                        Scene scene = new Scene(vbox, 200, 60);

                        Stage progressDialogStage = new Stage();
                        progressDialogStage.setScene(scene);

                        Task<Void> loginTask = new Task<Void>() {
                            @Override
                            protected Void call() throws Exception {
                                while (true) {
                                    for (int i = 0; i <= 100; i++) {
                                        double progress = i / 100.0;
                                        if (stopLogin) {
                                            break;
                                        }
                                        Thread.sleep(50);
                                    }

                                    if (progressDialogStage.isShowing()) {

                                        Platform.runLater(() -> progressDialogStage.close());
                                        break;
                                    }
                                }
                                return null;
                            }
                        };

                        progressDialogStage.show();
                        new Thread(loginTask).start();
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

    @FXML
    void submit(ActionEvent event) {
        if (one.getText().isEmpty() && two.getText().isEmpty() && three.getText().isEmpty() && four.getText().isEmpty()) {
            Alert.Dialogerror("Enter Otp");
        } else {
            MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Admin");
            MongoCollection<Document> RequestCollection = DBconnect.getdatabase().getCollection("Request");
            String otp = one.getText() + two.getText() + three.getText() + four.getText();
            Bson filter = Filters.and(
                    Filters.eq("Email", getEmail()),
                    Filters.eq("OTP", otp)
            );
            Document result = collection.find(filter).first();
            Document doc1 = new Document("EmailEmployee", email.getText()).append("status", 0);

            if (result != null) {
                MongoCollection<Document> reports = DBconnect.getdatabase().getCollection("Request");
                Document existingRequest = reports.find(Filters.eq("EmailEmployee", getEmail())).first();
                if (existingRequest != null) {
                    reports.updateOne(Filters.eq("EmailEmployee", getEmail()), Updates.set("status", 0));
                    Alert.DialogSuccess("request sent successfully");

                } else {
                    InsertOneResult insertOneResult = RequestCollection.insertOne(doc1);
                    Alert.DialogSuccess("request sent successfully");

                }

                try {
                    App.setRoot("Login");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } else {
                Alert.Dialogerror("request sent failed");
            }
        }

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        one.textProperty().addListener((observable, oldvalue, newvalue) -> {
            if (newvalue.length() == 1) {
                two.requestFocus();
            }
        });
        two.textProperty().addListener((observable, oldvalue, newvalue) -> {
            if (newvalue.length() == 1) {
                three.requestFocus();
            } else if (newvalue.isEmpty()) {
                one.requestFocus();
            }
        });
        three.textProperty().addListener((observale, oldvalue, newvalue) -> {
            if (newvalue.length() == 1) {
                four.requestFocus();
            } else if (newvalue.isEmpty()) {
                two.requestFocus();
            }
        });
        four.textProperty().addListener((observale, oldvalue, newvalue) -> {
            if (newvalue.isEmpty()) {
                three.requestFocus();
            }
        });
        one.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                one.clear();
            }
        });
        two.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                two.clear();
                one.requestFocus();
            }
        });
        three.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                three.clear();
                two.requestFocus();
            }

        });

        four.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.BACK_SPACE)) {
                four.clear();
                three.requestFocus();
            }

        });
    }
}
