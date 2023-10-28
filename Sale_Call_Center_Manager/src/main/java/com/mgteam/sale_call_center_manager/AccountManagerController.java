/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.bson.Document;
import org.bson.conversions.Bson;

/**
 *
 * @author tranp
 */
public class AccountManagerController implements Initializable{

    @FXML
    private MFXTextField Email;

    @FXML
    private MFXTextField Name;

    @FXML
    private MFXTextField Phone;

    @FXML
    private DatePicker Since;

    @FXML
    private MFXTextField Username;

    @FXML
    private TableColumn<Manager, String> colApprove;

    @FXML
    private TableColumn<Manager, String> colCancle;

    @FXML
    private TableColumn<Manager, String> colEmail;

    @FXML
    private TableColumn<Manager, String> colName;

    @FXML
    private TableColumn<Manager, String> colPostion;

    @FXML
    private TableColumn<Manager, String> colSince;

    @FXML
    private TableColumn<Manager, String> colphone;

    @FXML
    private Pagination pagination;

    @FXML
    private ComboBox<String> postion;
    private int itemsperPage = 5;
    private int totalItems;
    private int displaymode = 1;
    private int currentPageIndex = 0;
    @FXML
    private TableView<Manager> tblEmp;

    @FXML
    private TextField txtsearch;

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

    @FXML
    void Find(ActionEvent event) {
        searchdiplay(txtsearch.getText());
        if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        AccountEmployee();
         pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            currentPageIndex = newIndex.intValue();
            if (displaymode == 1) {
                AccountEmployee();
            } else {
                searchdiplay(txtsearch.getText());
            }
            AccountEmployee();
        });
            ObservableList<String> data = FXCollections.observableArrayList("SalePerson", "Warehouse");
        postion.setItems(data);
        postion.setValue("SalePerson");
       
    }
}
