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
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
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
    private ComboBox<String> FilterMonthorder;
    @FXML
    private MFXTextField Email;
    @FXML
    private ComboBox<String> filterMonth;
    @FXML
    private AreaChart<String, Number> customerchart;
    @FXML
    private Label inventory;
    @FXML
    private BarChart<String, Number> totalInventory;
    @FXML
    private ComboBox<String> FilterInventory;

    @FXML
    private Label order;
    private int itemsperPage = 5;
    private int totalItems;
    @FXML
    private Label customer;
    private int displaymode = 1;
    private int currentPageIndex = 0;
    @FXML
    private ComboBox<String> postion = new ComboBox<>();
    @FXML
    private TextField txtsearch = new TextField();

    @FXML
    private TableColumn<Manager, String> colApprove = new TableColumn<>();

    @FXML
    private LineChart<String, Number> chartOrder;
    @FXML
    private TableColumn<Manager, String> colCancle = new TableColumn<>();

    @FXML
    private TableColumn<Manager, String> colEmail = new TableColumn<>();
    @FXML
    private TableColumn<Manager, String> colPostion = new TableColumn<>();

    @FXML
    private TableColumn<Manager, String> colName = new TableColumn<>();

    @FXML
    private TableColumn<Manager, String> colSince = new TableColumn<>();

    @FXML
    private TableColumn<Manager, String> colphone = new TableColumn<>();

    @FXML
    private TableView<Manager> tblEmp = new TableView<>();
    @FXML
    private MFXTextField Name;

    @FXML
    private MFXTextField Phone;

    @FXML
    private Label User = new Label();

    @FXML
    private DatePicker Since;
    @FXML
    private Pagination pagination = new Pagination();
    @FXML
    private MFXTextField Username;

    @FXML
    private AnchorPane maindisplay;

    private String coutTotalorder() {
        MongoCollection<Document> orderCollection = DBconnect.getdatabase().getCollection("Order");
        long totalOrders = orderCollection.countDocuments();
        return String.valueOf(totalOrders);
    }

    private void searchdiplay(String key) {
        displaymode = 2;
        List<Manager> resulist = daodb.SearchgetManagerAccount(key);
        ObservableList<Manager> observableList = FXCollections.observableArrayList(resulist);
        totalItems = observableList.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Manager> As = observableList.subList(startIndex, endIndex);
        tblEmp.setItems(FXCollections.observableArrayList(As));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colName.widthProperty());
                    setGraphic(text);
                }
            }

        });
        colPostion.setCellValueFactory(new PropertyValueFactory<>("position"));
        colPostion.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colPostion.widthProperty());
                    setGraphic(text);
                }
            }

        });
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colEmail.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colName.widthProperty());
                    setGraphic(text);
                }
            }
        });
        colSince.setCellValueFactory(new PropertyValueFactory<>("Since"));
        colSince.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colSince.widthProperty());
                    setGraphic(text);
                }
            }

        });
        colphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colphone.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colphone.widthProperty());
                    setGraphic(text);
                }
            }

        });
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
            ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5), classpane);
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

            TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), maindisplay);
            transition.setFromX(0);
            transition.setToX(maindisplay.getWidth());
            TranslateTransition reverstation = new TranslateTransition(Duration.seconds(0.5), maindisplay);
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
        displaymode = 1;
        List<Manager> resulist = daodb.getManagerAccount();
        ObservableList<Manager> observableList = FXCollections.observableArrayList(resulist);
        totalItems = observableList.size();
        int pageCounts = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCounts);
        currentPageIndex = Math.min(currentPageIndex, pageCounts - 1);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.max(startIndex, 0);
        List<Manager> As = observableList.subList(startIndex, endIndex);;
        tblEmp.setItems(FXCollections.observableArrayList(As));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        colName.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colName.widthProperty());
                    setGraphic(text);
                }
            }

        });
        colPostion.setCellValueFactory(new PropertyValueFactory<>("position"));
        colPostion.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colPostion.widthProperty());
                    setGraphic(text);
                }
            }

        });
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colEmail.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colName.widthProperty());
                    setGraphic(text);
                }
            }
        });
        colSince.setCellValueFactory(new PropertyValueFactory<>("Since"));
        colSince.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colSince.widthProperty());
                    setGraphic(text);
                }
            }

        });
        colphone.setCellValueFactory(new PropertyValueFactory<>("phone"));
        colphone.setCellFactory(column -> new TableCell<Manager, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Text text = new Text(item);
                    text.wrappingWidthProperty().bind(colphone.widthProperty());
                    setGraphic(text);
                }
            }

        });
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
    void Find(ActionEvent event) {
        searchdiplay(txtsearch.getText());
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }
    }

    private String totalCustomer() {
        MongoCollection<Document> Customer = DBconnect.getdatabase().getCollection("Customer");
        long totalorders = Customer.countDocuments();
        return String.valueOf(totalorders);
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
            RotateTransition rotateTransition = new RotateTransition(Duration.seconds(1), classpane);
            rotateTransition.setFromAngle(-90);
            rotateTransition.setToAngle(0);
            SequentialTransition sequentialTransition = new SequentialTransition(classpane, rotateTransition);
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
                                    if (postion.getValue().equals("Warehouse")) {
                                        DateTimeFormatter local = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        String since = Since.getValue().format(local);
                                        Document doc1 = new Document("Name", Name.getText()).append("Email", Email.getText()).append("Username", MD5.encryPassword(Username.getText())).append("Since", since).append("Password", MD5.encryPassword(Username.getText())).append("usertype", 1).append("status", 0).append("Phone", Phone.getText());
                                        InsertOneResult result = collection.insertOne(doc1);
                                        Alert.DialogSuccess("Add Employee Successfully");
                                        sendConfirmationEmailEmployee(Email.getText());
                                        Name.setText("");
                                        Since.setValue(null);
                                        Username.setText("");
                                        Phone.setText("");
                                        Email.setText("");
                                    } else if (postion.getValue().equals("SalePerson")) {
                                        DateTimeFormatter local = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                        String since = Since.getValue().format(local);
                                        Document doc1 = new Document("Name", Name.getText()).append("Email", Email.getText()).append("Username", MD5.encryPassword(Username.getText())).append("Since", since).append("Password", MD5.encryPassword(Username.getText())).append("usertype", 2).append("status", 0).append("Phone", Phone.getText());
                                        InsertOneResult result = collection.insertOne(doc1);
                                        Alert.DialogSuccess("Add Employee Successfully");
                                        sendConfirmationEmailEmployee(Email.getText());
                                        Name.setText("");
                                        Since.setValue(null);
                                        Username.setText("");
                                        Phone.setText("");
                                        Email.setText("");
                                    }

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

    private String totalIventory() {
        MongoCollection<Document> Inventory = DBconnect.getdatabase().getCollection("WareHouse");
        long totalInventory = Inventory.countDocuments();
        return String.valueOf(totalInventory);
    }

    @FXML
    void Home(ActionEvent event) {
        try {
            App.setRoot("secondary");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void countCustomerIdByOrderDate(String selectedMonth, AreaChart<String, Number> aeachart) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        Month month = Month.from(monthFormatter.parse(selectedMonth));
        int monthValue = month.getValue();
        String formattedMonth = String.format("%02d", monthValue);

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), monthValue, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        MongoCollection<Document> orders = DBconnect.getdatabase().getCollection("Order");
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<String> dateStrings = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateStrings.add(date.format(formatter));
        }

        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.in("Order_date", dateStrings));
        filters.add(Filters.exists("id_Customer"));

        long[] counts = new long[dateStrings.size()];
        int index = 0;

        for (String dateString : dateStrings) {
            filters.set(0, Filters.eq("Order_date", dateString));
            long count = orders.countDocuments(Filters.and(filters));
            counts[index++] = count;
        }

        for (int i = 0; i < dateStrings.size(); i++) {
            series.getData().add(new XYChart.Data<>(dateStrings.get(i), counts[i]));
        }

        if (aeachart != null) {
            aeachart.getData().clear();
            aeachart.getData().add(series);
        }
    }

    private void countorderIDbyorderDate(String selectedMonth, LineChart<String, Number> chartOrder) {
        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        Month month = Month.from(monthFormatter.parse(selectedMonth));
        int monthvalue = month.getValue();
        String formattedMonth = String.format("%02d", monthvalue);
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), monthvalue, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        MongoCollection<Document> orders = DBconnect.getdatabase().getCollection("Order");
        XYChart.Series<String, Number> seris = new XYChart.Series<>();

        List<String> dateStrings = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            dateStrings.add(date.format(formatter));
        }

        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.in("Order_date", dateStrings));
        filters.add(Filters.exists("_id"));

        long[] counts = new long[dateStrings.size()];
        int index = 0;

        for (String dateString : dateStrings) {
            filters.set(0, Filters.eq("Order_date", dateString));
            long count = orders.countDocuments(Filters.and(filters));
            counts[index++] = count;
        }

        for (int i = 0; i < dateStrings.size(); i++) {
            seris.getData().add(new XYChart.Data<>(dateStrings.get(i), counts[i]));
        }

        if (chartOrder != null) {
            chartOrder.getData().clear();
            chartOrder.getData().add(seris);
        }
    }

    private void displaytotalInventoryData(String selectedMonth, BarChart<String, Number> totalInventoryChart) {
        DateTimeFormatter monthformatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
        Month month = Month.from(monthformatter.parse(selectedMonth));
        int monthvalue = month.getValue();
        String formattedMonth = String.format("%02d", monthvalue);
        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), monthvalue, 1);
        LocalDate Enddate = startDate.plusMonths(1).minusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        MongoCollection<Document> orders = DBconnect.getdatabase().getCollection("WareHouse");
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        List<String> dateStrings = new ArrayList<>();
        for (LocalDate date = startDate; !date.isAfter(Enddate); date = date.plusDays(1)) {
            dateStrings.add(date.format(formatter));
        }

        List<Bson> filters = new ArrayList<>();
        filters.add(Filters.in("Date", dateStrings));
        filters.add(Filters.exists("_id"));

        long[] counts = new long[dateStrings.size()];
        int index = 0;

        for (String dateString : dateStrings) {
            filters.set(0, Filters.eq("Date", dateString));
            long count = orders.countDocuments(Filters.and(filters));
            counts[index++] = count;
        }

        for (int i = 0; i < dateStrings.size(); i++) {
            series.getData().add(new XYChart.Data<>(dateStrings.get(i), counts[i]));
        }

        if (totalInventoryChart != null) {
            totalInventoryChart.getData().clear();
            totalInventoryChart.getData().add(series);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        customer.setText(totalCustomer());
        order.setText(coutTotalorder());
        inventory.setText(totalIventory());
        AccountEmployee();
        User.setText(LoginController.user);
        ObservableList<String> data = FXCollections.observableArrayList("SalePerson", "Warehouse");
        postion.setItems(data);
        postion.setValue("SalePerson");
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                AccountEmployee();
            } else {
                searchdiplay(txtsearch.getText());
            }
            AccountEmployee();
        });
        ObservableList<String> months = FXCollections.observableArrayList(
                "January", "February", "March", "April", "May", "June",
                "July", "August", "September", "October", "November", "December");
        filterMonth.setItems(months);
        filterMonth.setValue("January");
        String selectedMonth = filterMonth.getValue();
        countCustomerIdByOrderDate(selectedMonth, customerchart);
        filterMonth.setOnAction(event -> {
            String selectedmonth = filterMonth.getValue();
            countCustomerIdByOrderDate(selectedmonth, customerchart);
        });
        FilterMonthorder.setItems(months);
        FilterMonthorder.setValue("January");
        countorderIDbyorderDate(FilterMonthorder.getValue(), chartOrder);
        FilterMonthorder.setOnAction(event -> {
            countorderIDbyorderDate(FilterMonthorder.getValue(), chartOrder);
        });
        FilterInventory.setItems(months);
        FilterInventory.setValue("January");
        displaytotalInventoryData(FilterInventory.getValue(), totalInventory);
        FilterInventory.setOnAction(event -> {
            displaytotalInventoryData(FilterInventory.getValue(), totalInventory);
        });
    }

}
