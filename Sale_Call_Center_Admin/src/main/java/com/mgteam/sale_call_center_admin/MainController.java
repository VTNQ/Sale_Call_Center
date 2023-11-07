package com.mgteam.sale_call_center_admin;

import com.mgteam.sale_call_center_admin.connect.DBConnect;
import com.mgteam.sale_call_center_admin.connect.MD5;
import com.mgteam.sale_call_center_admin.connect.util.daodb;
import com.mgteam.sale_call_center_admin.connect.util.validate;
import com.mgteam.sale_call_center_admin.model.Employee;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Pagination;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javax.mail.Session;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
    private Pagination pagination;
    @FXML
    private ComboBox<String> Position;
    @FXML
    private TableColumn<Employee, String> colName;
    @FXML
    private TextField txtName;
    @FXML
    private TableColumn<Employee, String> colEmail;

    @FXML
    private TableColumn<Employee, String> colSince;
    @FXML
    private TableColumn<Employee, String> colPostion;
    @FXML
    private TableColumn<Employee, String> colphone;
    @FXML
    private TableColumn<Employee, String> isreset;
    @FXML
    private TableView<Employee> tblAccount;

    @FXML
    private MFXTextField Phone;
    private int displaymode = 1;
    private int itemsperPage = 5;
    private int totalItems;
    private int currentPageIndex = 0;
    @FXML
    private DatePicker Since;

    @FXML
    private MFXTextField username;
    private String reset_username;
    @FXML
    private TableColumn<Employee, String> colCancel;

    @FXML
    void Addsubmit(ActionEvent event) {
        if (!Name.getText().isEmpty() && !username.getText().isEmpty() && Since.getValue() != null && !Phone.getText().isEmpty() && !Email.getText().isEmpty()) {
            if (validate.validateEmail(Email.getText())) {
                if (validate.validatePhoneNumber(Phone.getText())) {
                    if (validate.validateUsername(username.getText())) {
                        MongoCollection<Document> collection = DBConnect.getdatabase().getCollection("Admin");
                        Document existingUser = collection.find(Filters.eq("Username", MD5.encryPassword(username.getText()))).first();
                        Document existingEmail = collection.find(Filters.eq("Email", Email.getText())).first();
                        Document existPhone = collection.find(Filters.eq("Phone", Phone.getText())).first();
                        if (existingUser == null) {
                            if (existingEmail == null) {
                                if (existPhone == null) {
                                    DateTimeFormatter local = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                                    String since = Since.getValue().format(local);
                                    if (Position.getValue().equals("Manager")) {
                                        Document doc1 = new Document("Name", Name.getText()).append("Username", MD5.encryPassword(username.getText())).append("Since", since).append("Phone", Phone.getText()).append("Email", Email.getText()).append("Password", MD5.encryPassword(username.getText())).append("Usertype", 2).append("status",0);
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
                                        Document doc1 = new Document("Name", Name.getText()).append("Username", MD5.encryPassword(username.getText())).append("Since", since).append("Phone", Phone.getText()).append("Email", Email.getText()).append("Password", MD5.encryPassword(username.getText())).append("Usertype", 3).append("status", 0);
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

    private void searchdisplay(String search,int pageindex) {
        displaymode=2;
        List<Employee> listAccout = daodb.getAcccountwithKey(search);
        ObservableList<Employee> observableList = FXCollections.observableArrayList(listAccout);
        totalItems=observableList.size();
        int Pagecount=(totalItems+itemsperPage-1)/itemsperPage;
        pagination.setPageCount(Pagecount);
        if(listAccout.isEmpty()){
            pagination.setPageCount(1);
            tblAccount.setItems(FXCollections.observableArrayList());
            return;
        }
        int startIndex=pageindex*itemsperPage;
        int endIndex=Math.min(startIndex+itemsperPage, totalItems);
        startIndex=Math.min(startIndex, totalItems);
        List<Employee>Ass=listAccout.subList(startIndex, endIndex);
        tblAccount.setItems(FXCollections.observableArrayList(Ass));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        isreset.setCellValueFactory(new PropertyValueFactory<>("isReset"));
        isreset.setCellFactory(column -> new TableCell<Employee, String>() {
            private final MFXButton sumit = new MFXButton("Approve");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                sumit.getStyleClass().add("button-success");
                {
                    sumit.setOnAction(event -> {
                        Employee emp = getTableView().getItems().get(getIndex());
                        MongoCollection<Document> employy = DBConnect.getdatabase().getCollection("Admin");
                        MongoCollection<Document> collection = DBConnect.getdatabase().getCollection("Request");
                        Document query = new Document("EmailEmployee", emp.getEmail()).append("status", 1);
                        Document result = collection.find(query).first();
                        if (result != null) {
                            Alert.Dialogerror("Approved");
                        } else {
                            Document requestDocument = collection.find(eq("EmailEmployee", emp.getEmail())).first();
                            if (requestDocument != null) {
                                Bson Filter = Filters.eq("EmailEmployee", emp.getEmail());
                                Bson updates = Updates.set("status", 1);
                                UpdateResult update = collection.updateOne(Filter, updates);
                                Bson filteremp = Filters.eq("Email", emp.getEmail());
                                Bson updatepass = Updates.set("Password", emp.getUsername());
                                UpdateResult up = employy.updateOne(filteremp, updatepass);

                                Alert.DialogSuccess("Update successfully");
                                displayrecord();
                            }
                        }

                    });
                }
                if (!empty || item != null) {
                    setGraphic(sumit);
                    if (item == null) {
                        setGraphic(null);
                    } else if (item.equals("")) {
                        setGraphic(null);
                    } else if (item.equals("2")) {
                        setGraphic(sumit);
                        sumit.setDisable(true);
                    } else {
                        setGraphic(sumit);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        colCancel.setCellValueFactory(new PropertyValueFactory<>("isReset"));
        colCancel.setCellFactory(column -> new TableCell<Employee, String>() {
            private final MFXButton sumit = new MFXButton("Cancel");

            {
                sumit.setOnAction(even -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> collection = DBConnect.getdatabase().getCollection("Request");
                    Document query = new Document("EmailEmployee", emp.getEmail()).append("status", 2);
                    Document result = collection.find(query).first();
                    if (result != null) {
                        Alert.Dialogerror("Canceled");
                    } else {
                        Bson Filter = Filters.eq("EmailEmployee", emp.getEmail());
                        Bson updates = Updates.set("status", 2);
                        UpdateResult update = collection.updateOne(Filter, updates);
                        if (update.getModifiedCount() > 0) {
                            Alert.DialogSuccess("Update successfully");
                            displayrecord();
                        }
                    }

                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                sumit.getStyleClass().add("button-error");
                if (!empty || item != null) {
                    setGraphic(sumit);
                    if (item == null) {
                        setGraphic(null);
                    } else if (item.equals("")) {
                        setGraphic(null);
                    } else if (item.equals("1")) {
                        setGraphic(sumit);
                        sumit.setDisable(true);
                    } else {
                        setGraphic(sumit);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        colPostion.setCellValueFactory(new PropertyValueFactory<>("position"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colSince.setCellValueFactory(new PropertyValueFactory<>("since"));
        colphone.setCellValueFactory(new PropertyValueFactory<>("phone"));

    }
    private void sendApproved(String email){
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
            String body = "Hello,"+email+"\nYour request has been accepted";
            message.setText(body);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     private void sendCancel(String email){
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
            String body = "Hello,"+email+"\nYour request has been rejected";
            message.setText(body);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayrecord() {
        totalItems=1;
        List<Employee> resulist = daodb.getAccountEmployee();
        ObservableList<Employee> observableList = FXCollections.observableArrayList(resulist);
        totalItems = observableList.size();
        int pageCount = (totalItems + itemsperPage - 1) / itemsperPage;
        pagination.setPageCount(pageCount);
        int startIndex = currentPageIndex * itemsperPage;
        int endIndex = Math.min(startIndex + itemsperPage, totalItems);
        startIndex = Math.min(startIndex, totalItems);
        List<Employee> Ass = observableList.subList(startIndex, endIndex);
        tblAccount.setItems(FXCollections.observableArrayList(Ass));
        colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        isreset.setCellValueFactory(new PropertyValueFactory<>("isReset"));
        isreset.setCellFactory(column -> new TableCell<Employee, String>() {
            private final MFXButton sumit = new MFXButton("Approve");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                sumit.getStyleClass().add("button-success");
                {
                    sumit.setOnAction(event -> {
                        Employee emp = getTableView().getItems().get(getIndex());
                        MongoCollection<Document> employy = DBConnect.getdatabase().getCollection("Employee");
                        MongoCollection<Document> collection = DBConnect.getdatabase().getCollection("Request");
                        Document query = new Document("EmailEmployee", emp.getEmail()).append("status", 1);
                        Document result = collection.find(query).first();
                        if (result != null) {
                            Alert.Dialogerror("Approved");
                        } else {
                            Document requestDocument = collection.find(eq("EmailEmployee", emp.getEmail())).first();
                            if (requestDocument != null) {
                                Bson Filter = Filters.eq("EmailEmployee", emp.getEmail());
                                Bson updates = Updates.set("status", 1);
                                UpdateResult update = collection.updateOne(Filter, updates);
                                Bson filteremp = Filters.eq("Email", emp.getEmail());
                                Bson updatepass = Updates.set("Password", emp.getUsername());
                                UpdateResult up = employy.updateOne(filteremp, updatepass);

                                Alert.DialogSuccess("Update successfully");
                                displayrecord();
                                sendApproved(emp.getEmail());
                            }
                        }

                    });
                }
                if (!empty || item != null) {
                    setGraphic(sumit);
                    if (item == null) {
                        setGraphic(null);
                    } else if (item.equals("")) {
                        setGraphic(null);
                    } else if (item.equals("2")) {
                        setGraphic(sumit);
                        sumit.setDisable(true);
                    } else {
                        setGraphic(sumit);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        colCancel.setCellValueFactory(new PropertyValueFactory<>("isReset"));
        colCancel.setCellFactory(column -> new TableCell<Employee, String>() {
            private final MFXButton sumit = new MFXButton("Cancel");

            {
                sumit.setOnAction(even -> {
                    Employee emp = getTableView().getItems().get(getIndex());
                    MongoCollection<Document> collection = DBConnect.getdatabase().getCollection("Request");
                    Document query = new Document("EmailEmployee", emp.getEmail()).append("status", 2);
                    Document result = collection.find(query).first();
                    if (result != null) {
                        Alert.Dialogerror("Canceled");
                    } else {
                        Bson Filter = Filters.eq("EmailEmployee", emp.getEmail());
                        Bson updates = Updates.set("status", 2);
                        UpdateResult update = collection.updateOne(Filter, updates);
                        if (update.getModifiedCount() > 0) {
                            Alert.DialogSuccess("Update successfully");
                            displayrecord();
                            sendCancel(emp.getEmail());
                        }
                    }

                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                sumit.getStyleClass().add("button-error");
                if (!empty || item != null) {
                    setGraphic(sumit);
                    if (item == null) {
                        setGraphic(null);
                    } else if (item.equals("")) {
                        setGraphic(null);
                    } else if (item.equals("1")) {
                        setGraphic(sumit);
                        sumit.setDisable(true);
                    } else {
                        setGraphic(sumit);
                    }
                } else {
                    setGraphic(null);
                }
            }

        });
        colPostion.setCellValueFactory(new PropertyValueFactory<>("position"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        colSince.setCellValueFactory(new PropertyValueFactory<>("since"));
        colphone.setCellValueFactory(new PropertyValueFactory<>("phone"));

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
    void findName(ActionEvent event) {
          currentPageIndex=0;
        searchdisplay(txtName.getText(),currentPageIndex);
         if (currentPageIndex != 0) {
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
        }else if(currentPageIndex==0){
            currentPageIndex = 0;
            pagination.setCurrentPageIndex(currentPageIndex);
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
       pagination.currentPageIndexProperty().addListener((obs,oldIndex,newIndedx)->{
       currentPageIndex=newIndedx.intValue();
       if(displaymode==1){
           displayrecord();
       }else{
           searchdisplay(txtName.getText(), currentPageIndex);
       }
       displayrecord();
       });
    }
}
