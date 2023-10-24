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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

public class MainOrderController extends MainController implements Initializable {
    
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
    
    public static List<com.mgteam.sale_call_center_employee.model.Order>ListOrder(){
        List<com.mgteam.sale_call_center_employee.model.Order>ArrayOrder=new ArrayList<>();
        MongoCollection<Document>orderCollection=DBConnection.getConnection().getCollection("order");
        MongoCollection<Document>customerCollection=DBConnection.getConnection().getCollection("Customer");
        MongoCollection<Document>employeeCollection=DBConnection.getConnection().getCollection("Employee");
        Bson filterWithID=Filters.eq("id_employee", id_employee);
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
            ArrayOrder.add(new Order(id, IdCustomer, IdEmployee, OrderDate, ShipDate, status, detailOrder));
        }
        return ArrayOrder;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
}
