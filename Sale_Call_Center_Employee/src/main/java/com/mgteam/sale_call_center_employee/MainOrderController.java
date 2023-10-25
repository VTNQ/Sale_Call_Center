package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.model.Order;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class MainOrderController  implements Initializable {
    
    @FXML
    private TableColumn<?, ?> ListProduct;

    @FXML
    private TableColumn<?, ?> NameCustomer;

    @FXML
    private TableColumn<?, ?> NameEmployee;

    @FXML
    private TableColumn<?, ?> OrderDay;

    @FXML
    private TableColumn<?, ?> ShipDay;

    @FXML
    private TableColumn<?, ?> StatusOrder;
     @FXML
    private TableView<Order> tblOrder;
    
    public static List<com.mgteam.sale_call_center_employee.model.Order>ListOrder(){
        List<com.mgteam.sale_call_center_employee.model.Order>ArrayOrder=new ArrayList<>();
        try {
            MongoCollection<Document>orderCollection=DBConnection.getConnection().getCollection("Order");
        MongoCollection<Document>customerCollection=DBConnection.getConnection().getCollection("Customer");
        MongoCollection<Document>employeeCollection=DBConnection.getConnection().getCollection("Employee");
        Bson filterWithID=Filters.eq("id_Employee", LoginController.id_employee);
      
        FindIterable<Document>result=orderCollection.find(filterWithID);
        for (Document document : result) {
            ObjectId id=document.getObjectId("_id");
            ObjectId IdCustomer=document.getObjectId("id_Customer");
            ObjectId IdEmployee=document.getObjectId("id_Employee");
            Document CustomerAll=customerCollection.find(Filters.eq("_id", IdCustomer)).first();
            Document EmployeeAll=employeeCollection.find(Filters.eq("_id", IdEmployee)).first();
            String nameCustomer=CustomerAll.getString("Name");
            String nameemployee=EmployeeAll.getString("Name");
            String OrderDate=document.getString("Order_date");
            String ShipDate=document.getString("Ship_date");
            Integer status=document.getInteger("status");
            Document detailOrder=(Document)document.get("DetailOrder");
            ArrayOrder.add(new Order(id, IdCustomer, IdEmployee, OrderDate, ShipDate, status, detailOrder,nameCustomer,nameemployee));
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return ArrayOrder;
    }
    private void ListOrderCustomer(){
        List<Order>OrderCustomer=ListOrder();
        ObservableList<Order>obserableList=FXCollections.observableArrayList(OrderCustomer);
        tblOrder.setItems(obserableList);
        NameCustomer.setCellValueFactory(new PropertyValueFactory<>("NameCustomer"));
        NameEmployee.setCellValueFactory(new PropertyValueFactory<>("NameEmployee"));
        OrderDay.setCellValueFactory(new PropertyValueFactory<>("Order_date"));
        ShipDay.setCellValueFactory(new PropertyValueFactory<>("Ship_date"));
        
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       ListOrderCustomer();
    }    
    
}
