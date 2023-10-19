package com.mgteam.sale_call_center_admin;

import com.mgteam.sale_call_center_admin.connect.DBConnect;
import com.mgteam.sale_call_center_admin.connect.MD5;
import com.mgteam.sale_call_center_admin.connect.util.daodb;
import com.mgteam.sale_call_center_admin.connect.util.validate;
import com.mgteam.sale_call_center_admin.model.Employee;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javax.mail.Session;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MainController implements Initializable {

    @FXML
    private MFXTextField Name;
    @FXML
    private MFXTextField Email;

    @FXML
    private ComboBox<String> Position;
    @FXML
    private TableColumn<Employee, String> colName;

    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private TableColumn<Employee, String> colSince;
    @FXML
    private TableColumn<Employee, String> colPostion;
    @FXML
    private TableColumn<Employee, String> colphone;
    @FXML
    private TableColumn<Employee, Boolean> isreset;
    @FXML
    private TableView<Employee> tblAccount;

    @FXML
    private MFXTextField Phone;

    @FXML
    private DatePicker Since;

    @FXML
    private MFXTextField username;
    private String reset_username;

    @FXML
    void Addsubmit(ActionEvent event) {
        if (!Name.getText().isEmpty() && !username.getText().isEmpty() && Since.getValue() != null && !Phone.getText().isEmpty() && !Email.getText().isEmpty()) {
            if (validate.validateEmail(Email.getText())) {
                if (validate.validatePhoneNumber(Phone.getText())) {
                    if (validate.validateUsername(username.getText())) {
                        MongoCollection<Document> collection = DBConnect.getdatabase().getCollection("Employee");
                        Document existingUser = collection.find(Filters.eq("Username", MD5.encryPassword(username.getText()))).first();
                        Document existingEmail = collection.find(Filters.eq("Email", Email.getText())).first();
                        Document existPhone = collection.find(Filters.eq("Phone", Phone.getText())).first();
                        if (existingUser == null) {
                            if (existingEmail == null) {
                                if (existPhone == null) {
                                    DateTimeFormatter local = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    String since = Since.getValue().format(local);
                                    if (Position.getValue().equals("Manager")) {
                                        Document doc1 = new Document("name", Name.getText()).append("Username", MD5.encryPassword(username.getText())).append("Since", since).append("Phone", Phone.getText()).append("Email", Email.getText()).append("Password", MD5.encryPassword(username.getText())).append("usertype", 0).append("empMgr", 1).append("status", 0);
                                        InsertOneResult result = collection.insertOne(doc1);
                                        Alert.DialogSuccess("Add Employee successfully");
                                        displayrecord();
                                        sendConfirmationEmailEmployee(Email.getText());
                                        Name.setText("");
                                        Since.setValue(null);
                                        username.setText("");
                                        Phone.setText("");
                                        Email.setText("");
                                    } else if (Position.getValue().equals("Director")) {
                                        Document doc1 = new Document("name", Name.getText()).append("Username", MD5.encryPassword(username.getText())).append("Since", since).append("Phone", Phone.getText()).append("Email", Email.getText()).append("Password", MD5.encryPassword(username.getText())).append("usertype", 3).append("empMgr", 0).append("status", 0);
                                        InsertOneResult result = collection.insertOne(doc1);
                                        Alert.DialogSuccess("Add Employee successfully");
                                        displayrecord();
                                        sendConfirmationEmailEmployee(Email.getText());
                                        Name.setText("");
                                        Since.setValue(null);
                                        username.setText("");
                                        Phone.setText("");
                                        Email.setText("");
                                    }

                                } else {
                                    Alert.Dialogerror("Phone is Exists");
                                }

                            } else {
                                Alert.Dialogerror("Email is Exists");
                            }

                        } else {
                            Alert.Dialogerror("Username is Exists");
                        }
                    } else {
                        Alert.Dialogerror("Username format is not correct");
                    }

                } else {
                    Alert.Dialogerror("Phone format is not correct");
                    Name.setText("");
                    Since.setValue(null);
                    username.setText("");
                    Phone.setText("");
                    Email.setText("");
                }

            } else {
                Alert.Dialogerror("Email format is not correct");
                Name.setText("");
                Since.setValue(null);
                username.setText("");
                Phone.setText("");
                Email.setText("");
            }

        } else {
            Alert.Dialogerror("Please enter complete information");
            Name.setText("");
            Since.setValue(null);
            username.setText("");
            Phone.setText("");
            Email.setText("");
        }

    }

    private void displayrecord() {
        List<Employee> resulist = daodb.getAccoutEmployee();
        ObservableList<Employee> observableList = FXCollections.observableArrayList(resulist);
        tblAccount.setItems(observableList);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        isreset.setCellValueFactory(new PropertyValueFactory<>("isReset"));
        isreset.setCellFactory(column -> new TableCell<Employee, Boolean>() {
            private final Button sumit = new Button("Reset");

            {
                sumit.setOnAction(event -> {
                    MongoCollection<Document> collection = DBConnect.getdatabase().getCollection("Employee");
                    Bson filter = Filters.and(
                            Filters.eq("username", reset_username),
                            Filters.eq("status", 1)
                    );
                    Bson update = Updates.combine(
                            Updates.set("Password", reset_username),
                            Updates.set("Status", 0)
                    );

                    UpdateResult result = collection.updateOne(filter, update);
                    if (result.getModifiedCount() > 0) {
                        Alert.DialogSuccess("reset password successfully");
                    } else {
                        Alert.Dialogerror("reset password failed");
                    }
                });
            }

            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    setGraphic(sumit);
                }
            }

        });
        colPostion.setCellValueFactory(new PropertyValueFactory<>("position"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colSince.setCellValueFactory(new PropertyValueFactory<>("since"));
        colphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        tblAccount.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                Employee ex = tblAccount.getSelectionModel().getSelectedItem();
                reset_username = ex.getUsername();
            }
        });
    }

    private void sendConfirmationEmailEmployee(String email) {
        String username = "tranp6648@gmail.com";
        String password = "zmaa lqss pbup xpwm";
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Account Information");
            String body = "Hello,\n\nUsername:" + this.username.getText() + "\nPassword:" + this.username.getText();
            message.setText(body);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void LogOut(ActionEvent event) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
        alert.setTitle("CONFIRMATION");
        alert.setHeaderText(null);
        alert.setContentText("Are you logout");
        alert.showAndWait().ifPresent(reponsive -> {
            if (reponsive == ButtonType.CANCEL) {
                alert.close();
            }
            if (reponsive == ButtonType.OK) {
                try {
                    App.setRoot("Login");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        displayrecord();
        ObservableList<String> data = FXCollections.observableArrayList("Manager", "Director");
        Position.setItems(data);
        Position.setValue("Manager");
    }
}
