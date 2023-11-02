/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.mgteam.sale_call_center_employee;

import com.mgteam.sale_call_center_employee.dialog.DialogAlert;
import com.mgteam.sale_call_center_employee.util.DBConnection;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 * FXML Controller class
 *
 * @author tranp
 */
public class ProductController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private TextField NameProduct;
    
    @FXML
    private ComboBox<String> Namecategory;
    
    @FXML
    private TextField Price;

    @FXML
    void AddProduct(ActionEvent event) {
        if (!NameProduct.getText().isEmpty() && !Price.getText().isEmpty()) {
            String priceString = Price.getText();
            
            try {
                
                double priceValue = Double.parseDouble(priceString);
                
                if (priceValue >= 0) {
                    MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Product");
                    Document document = new Document("Name", NameProduct.getText())
                            .append("Price", priceValue)
                            .append("ID_Category", getCategoryIDByName(Namecategory.getValue()));
                    categoryCollection.insertOne(document);
                    DialogAlert.DialogSuccess("Add product successfully");
                    NameProduct.setText("");
                    Price.setText("");
                } else {
                    DialogAlert.DialogError("Price must be a non-negative number");
                    
                }
            } catch (NumberFormatException e) {
                
                DialogAlert.DialogError("Price is not valid");
            }
        } else {
            DialogAlert.DialogError("You must enter enough information");
        }
        
    }

    public Map<String, ObjectId> getCategoryNameToIdMap() {
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");
        Map<String, ObjectId> categoryNameToIdMap = new HashMap<>();
        
        FindIterable<Document> result = categoryCollection.find();
        for (Document document : result) {
            String categoryName = document.getString("Name");
            ObjectId categoryId = document.getObjectId("_id");
            categoryNameToIdMap.put(categoryName, categoryId);
        }
        
        return categoryNameToIdMap;
    }

    public ObjectId getCategoryIDByName(String categoryName) {
        Map<String, ObjectId> categoryNameToIdMap = getCategoryNameToIdMap();

        // Lấy ID từ Map
        return categoryNameToIdMap.get(categoryName);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");
        ObservableList<String> categoryList = FXCollections.observableArrayList();
        FindIterable<Document> result = categoryCollection.find();
        for (Document document : result) {
            String categoryName = document.getString("Name");
            categoryList.add(categoryName);
        }
        Namecategory.setItems(categoryList);
        if (!categoryList.isEmpty()) {
            Namecategory.setValue(categoryList.get(0));
        }
        
    }    
    
}
