package com.mgteam.sale_call_center_manager;

import com.mgteam.sale_call_center_manager.connect.DBconnect;
import com.mgteam.sale_call_center_manager.connect.MD5;
import com.mgteam.sale_call_center_manager.connect.util.daodb;
import com.mgteam.sale_call_center_manager.connect.util.validate;
import com.mgteam.sale_call_center_manager.model.Manager;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import io.github.palexdev.materialfx.controls.MFXButton;
import io.github.palexdev.materialfx.controls.MFXTextField;
import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import javafx.animation.FadeTransition;

import javafx.animation.ScaleTransition;
import javafx.animation.RotateTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.BoxBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.bson.Document;
import org.bson.conversions.Bson;

public class MainController implements Initializable {

    @FXML
    private MFXTextField Email;

    @FXML
    private TextField txtsearch = new TextField();
    @FXML
    private TableColumn<Manager, String> colApprove = new TableColumn<>();

    @FXML
    private TableColumn<Manager, String> colCancle = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colEmail = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colName = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colSince = new TableColumn<>();

    @FXML
    private TableColumn<?, ?> colphone = new TableColumn<>();

    @FXML
    private TableView<Manager> tblEmp = new TableView<>();
    @FXML
    private MFXTextField Name;

    @FXML
    private MFXTextField Phone;
    @FXML
    private Label User=new Label();

    @FXML
    private DatePicker Since;

    @FXML
    private MFXTextField Username;

    @FXML
    private AnchorPane maindisplay;

    private void searchdiplay(String key) {
        List<Manager> resulist = daodb.SearchgetManagerAccount(key);
        ObservableList<Manager> observableList = FXCollections.observableArrayList(resulist);
        tblEmp.setItems(observableList);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colSince.setCellValueFactory(new PropertyValueFactory<>("Since"));
        colphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colApprove.setCellValueFactory(new PropertyValueFactory<>("status"));
        colApprove.setCellFactory(column -> new TableCell<Manager, String>() {
            private final MFXButton button = new MFXButton("Approve");

            {
                button.setOnAction(event -> {
                    Manager mg = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> employy = DBconnect.getdatabase().getCollection("Employee");
                    MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Request");
                    Document query = new Document("EmailEmployee", mg.getEmail()).append("status", 1);
                    Document result = collection.find(query).first();
                    if (result != null) {
                        Alert.Dialogerror("Approved");
                    } else {
                        Document requestDocument = collection.find(eq("EmailEmployee", mg.getEmail())).first();
                        if (requestDocument != null) {
                            Bson Filter = Filters.eq("EmailEmployee", mg.getEmail());
                            Bson update = Updates.set("status", 1);
                            UpdateResult updates = collection.updateOne(Filter, update);
                            Bson Fitermg = Filters.eq("Email", mg.getEmail());
                            Bson Updatepass = Updates.set("Password", mg.getUsername());
                            UpdateResult up = employy.updateOne(Fitermg, Updatepass);
                            Alert.DialogSuccess("Update successfully");
                            AccountEmployee();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button-success");
                if (!empty || item != null) {
                    setGraphic(button);
                    if (item == null) {
                        setGraphic(null);
                    } else if (item.equals("")) {
                        setGraphic(null);
                    } else if (item.equals("2")) {
                        setGraphic(button);
                        button.setDisable(true);
                    } else {
                        setGraphic(button);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        colCancle.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCancle.setCellFactory(column -> new TableCell<Manager, String>() {
            private final MFXButton button = new MFXButton("Cancel");

            {
                button.setOnAction(event -> {
                    Manager mg = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Request");
                    Document query = new Document("EmailEmployee", mg.getEmail()).append("status", 2);
                    Document result = collection.find(query).first();
                    if (result != null) {
                        Alert.Dialogerror("Canceled");
                    } else {
                        Bson Filter = Filters.eq("EmailEmployee", mg.getEmail());
                        Bson updates = Updates.set("status", 2);
                        UpdateResult update = collection.updateOne(Filter, updates);
                        if (update.getModifiedCount() > 0) {
                            Alert.DialogSuccess("Update successfully");
                            AccountEmployee();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button-error");
                if (!empty || item != null) {
                    setGraphic(button);
                    if (item == null) {
                        setGraphic(null);
                    } else if (item.equals("")) {
                        setGraphic(null);
                    } else if (item.equals("1")) {
                        setGraphic(button);
                        button.setDisable(true);
                    } else {
                        setGraphic(button);
                    }
                } else {
                    setGraphic(null);
                }

            }

        });
    }

    @FXML
    void Inventory(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/inventory.fxml"));
        try {
            AnchorPane classpane = loader.load();
            ScaleTransition scaleTransition=new ScaleTransition(Duration.seconds(0.5),classpane);
            scaleTransition.setFromX(0.5);
            scaleTransition.setFromX(0.5);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
            maindisplay.getChildren().clear();
            maindisplay.getChildren().addAll(classpane);
            scaleTransition.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML

void Order(ActionEvent event) {
   try {
    FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/order_customer.fxml"));
    AnchorPane classpane = loader.load();

    TranslateTransition transition = new TranslateTransition(Duration.seconds(2.5), maindisplay);
    transition.setFromX(0);
    transition.setToX(maindisplay.getWidth());
   TranslateTransition reverstation = new TranslateTransition(Duration.seconds(2.5), maindisplay);
    reverstation.setFromX(maindisplay.getWidth());
    reverstation.setToX(0);
    transition.setOnFinished(e -> {
        try {
            maindisplay.getChildren().clear();
            maindisplay.getChildren().addAll(classpane);
     
            reverstation.play();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
    });

    transition.play();

} catch (Exception e) {
    e.printStackTrace();
}
}

 @FXML
void change_pass(ActionEvent event) {
     try {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Change_pass.fxml"));
        AnchorPane classpane = loader.load();

        // Đặt hiệu ứng RotateTransition
        RotateTransition rotateOut = new RotateTransition(Duration.seconds(0.5), maindisplay);
        rotateOut.setAxis(Rotate.Y_AXIS);
        rotateOut.setFromAngle(0);
        rotateOut.setToAngle(90);

        rotateOut.setOnFinished(e -> {
            maindisplay.getChildren().clear();
            maindisplay.getChildren().addAll(classpane);

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

    private void AccountEmployee() {
        List<Manager> resulist = daodb.getManagerAccount();
        ObservableList<Manager> observableList = FXCollections.observableArrayList(resulist);
        tblEmp.setItems(observableList);
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colSince.setCellValueFactory(new PropertyValueFactory<>("Since"));
        colphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colApprove.setCellValueFactory(new PropertyValueFactory<>("status"));
        colApprove.setCellFactory(column -> new TableCell<Manager, String>() {
            private final MFXButton button = new MFXButton("Approve");

            {
                button.setOnAction(event -> {
                    Manager mg = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> employy = DBconnect.getdatabase().getCollection("Employee");
                    MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Request");
                    Document query = new Document("EmailEmployee", mg.getEmail()).append("status", 1);
                    Document result = collection.find(query).first();
                    if (result != null) {
                        Alert.Dialogerror("Approved");
                    } else {
                        Document requestDocument = collection.find(eq("EmailEmployee", mg.getEmail())).first();
                        if (requestDocument != null) {
                            Bson Filter = Filters.eq("EmailEmployee", mg.getEmail());
                            Bson update = Updates.set("status", 1);
                            UpdateResult updates = collection.updateOne(Filter, update);
                            Bson Fitermg = Filters.eq("Email", mg.getEmail());
                            Bson Updatepass = Updates.set("Password", mg.getUsername());
                            UpdateResult up = employy.updateOne(Fitermg, Updatepass);
                            Alert.DialogSuccess("Update successfully");
                            AccountEmployee();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button-success");
                if (!empty || item != null) {
                    setGraphic(button);
                    if (item == null) {
                        setGraphic(null);
                    } else if (item.equals("")) {
                        setGraphic(null);
                    } else if (item.equals("2")) {
                        setGraphic(button);
                        button.setDisable(true);
                    } else {
                        setGraphic(button);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        colCancle.setCellValueFactory(new PropertyValueFactory<>("status"));
        colCancle.setCellFactory(column -> new TableCell<Manager, String>() {
            private final MFXButton button = new MFXButton("Cancel");

            {
                button.setOnAction(event -> {
                    Manager mg = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Request");
                    Document query = new Document("EmailEmployee", mg.getEmail()).append("status", 2);
                    Document result = collection.find(query).first();
                    if (result != null) {
                        Alert.Dialogerror("Canceled");
                    } else {
                        Bson Filter = Filters.eq("EmailEmployee", mg.getEmail());
                        Bson updates = Updates.set("status", 2);
                        UpdateResult update = collection.updateOne(Filter, updates);
                        if (update.getModifiedCount() > 0) {
                            Alert.DialogSuccess("Update successfully");
                            AccountEmployee();
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                button.getStyleClass().add("button-error");
                if (!empty || item != null) {
                    setGraphic(button);
                    if (item == null) {
                        setGraphic(null);
                    } else if (item.equals("")) {
                        setGraphic(null);
                    } else if (item.equals("1")) {
                        setGraphic(button);
                        button.setDisable(true);
                    } else {
                        setGraphic(button);
                    }
                } else {
                    setGraphic(null);
                }

            }

        });
    }

    @FXML
    void Customer(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/List_customer.fxml"));
        try {
             AnchorPane classpane = loader.load();

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), maindisplay);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> {
            maindisplay.getChildren().clear();
            maindisplay.getChildren().addAll(classpane);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), maindisplay);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.play();
        });

        fadeOut.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Employee(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/mgteam/sale_call_center_manager/Create_Employee.fxml"));
        try {
            AnchorPane classpane = loader.load();
            RotateTransition rotateTransition=new RotateTransition(Duration.seconds(1),classpane);
  rotateTransition.setFromAngle(-90);
  rotateTransition.setToAngle(0);
            SequentialTransition sequentialTransition=new SequentialTransition(classpane,rotateTransition);
            maindisplay.getChildren().clear();
            maindisplay.getChildren().addAll(classpane);
            sequentialTransition.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendApproveEmail(String Email) {
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
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(Email));
            message.setSubject("Account Information");
            String body = "Hello,\n\nUsername:" + this.Username.getText() + "\nPassword:" + this.Username.getText();
            message.setText(body);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void AddEmployee(ActionEvent event) {
        if (!Name.getText().isEmpty() && !Email.getText().isEmpty() && !Username.getText().isEmpty() && Since.getValue() != null && !Phone.getText().isEmpty()) {
            if (validate.validateEmail(Email.getText())) {
                if (validate.validatePhoneNumber(Phone.getText())) {
                    if (validate.validateUsername(Username.getText())) {

                        MongoCollection<Document> collection = DBconnect.getdatabase().getCollection("Employee");
                        Document existingUser = collection.find(Filters.eq("Username", MD5.encryPassword(Username.getText()))).first();
                        Document existingEmail = collection.find(Filters.eq("Email", Email.getText())).first();
                        Document existPhone = collection.find(Filters.eq("Phone", Phone.getText())).first();
                        if (existingUser == null) {
                            if (existingEmail == null) {
                                if (existPhone == null) {
                                    DateTimeFormatter local = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    String since = Since.getValue().format(local);
                                    Document doc1 = new Document("Name", Name.getText()).append("Email", Email.getText()).append("Username", MD5.encryPassword(Username.getText())).append("Since", since).append("Password", MD5.encryPassword(Username.getText())).append("usertype", 1).append("empMgr", 0).append("status", 0).append("Phone", Phone.getText());
                                    InsertOneResult result = collection.insertOne(doc1);
                                    Alert.DialogSuccess("Add Employee Successfully");
                                    sendConfirmationEmailEmployee(Email.getText());
                                    Name.setText("");
                                    Since.setValue(null);
                                    Username.setText("");
                                    Phone.setText("");
                                    Email.setText("");
                                } else {
                                    Alert.Dialogerror("Phone is Exists");
                                    Name.setText("");
                                    Since.setValue(null);
                                    Username.setText("");
                                    Phone.setText("");
                                    Email.setText("");
                                }
                            } else {
                                Alert.Dialogerror("Email is Exists");
                                Name.setText("");
                                Since.setValue(null);
                                Username.setText("");
                                Phone.setText("");
                                Email.setText("");
                            }
                        } else {
                            Alert.Dialogerror("Username is Exists");
                            Name.setText("");
                            Since.setValue(null);
                            Username.setText("");
                            Phone.setText("");
                            Email.setText("");
                        }

                    } else {
                        Alert.Dialogerror("Username format is not correct");
                        Name.setText("");
                        Since.setValue(null);
                        Username.setText("");
                        Phone.setText("");
                        Email.setText("");
                    }
                } else {
                    Alert.Dialogerror("Phone format is not correct");
                    Name.setText("");
                    Since.setValue(null);
                    Username.setText("");
                    Phone.setText("");
                    Email.setText("");
                }
            } else {
                Alert.Dialogerror("Email format is not correct");
                Name.setText("");
                Since.setValue(null);
                Username.setText("");
                Phone.setText("");
                Email.setText("");
            }

        } else {
            Alert.Dialogerror("Please enter complete information");
            Name.setText("");
            Since.setValue(null);
            Username.setText("");
            Phone.setText("");
            Email.setText("");
        }

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
            String body = "Hello,\n\nUsername:" + this.Username.getText() + "\nPassword:" + this.Username.getText();
            message.setText(body);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void Home(ActionEvent event) {
        try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AccountEmployee();
        txtsearch.textProperty().addListener((observable, oldvalue, newvalue) -> {
            searchdiplay(newvalue);
        });
        User.setText(LoginController.user);
    }
}
