/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee.util;

import com.mgteam.sale_call_center_employee.model.Export;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author tranp
 */
public class daodb {

    public static List<Export> ExportWarehouse() {
        ArrayList<Export> export = new ArrayList<>();
         Set<ObjectId> processedOrders = new HashSet<>();
        try {
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> Employee = DBConnection.getConnection().getCollection("Employee");
            MongoCollection<Document> Customer = DBConnection.getConnection().getCollection("Customer");
            MongoCollection<Document> Outgoinorder = DBConnection.getConnection().getCollection("OutGoingOrder");
            FindIterable<Document> orders = orderCollection.find(new Document());
            for (Document order : orders) {
                ObjectId orderid=order.getObjectId("_id");
                FindIterable<Document>pro=Product.find();
                for (Document document : pro) {
                    ObjectId idproduct=document.getObjectId("_id");
                      Document query = new Document("DetailOrder." + String.valueOf(idproduct), new Document("$exists", true));
                    Document detail=orderCollection.find(query).first();
                    if(detail!=null){
                         Document query1 = new Document("Detail." + String.valueOf(idproduct), new Document("$exists", true));
                    Document detailWarehouse=Warehouse.find(query1).first();
                    if(detailWarehouse!=null){
                        ObjectId idEmployee=order.getObjectId("id_Employee");
                        ObjectId idCustomer=order.getObjectId("id_Customer");
                        Document Emp=Employee.find(new Document("_id",idEmployee)).first();
                        Document cus=Customer.find(new Document("_id",idCustomer)).first();
                        Document incoming=Outgoinorder.find(Filters.and(Filters.eq("ID_Employee",idEmployee),Filters.eq("ID_Order",orderid))).first();
                        
                        if(Emp!=null && cus!=null && incoming!=null && !processedOrders.contains(orderid) ){
                            String nameCustomer=cus.getString("Name");
                            String EmployeeName=Emp.getString("Name");
                            int status=incoming.getInteger("status");
                            ObjectId idEmploye=Emp.getObjectId("_id");
                            int id_order=order.getObjectId("_id").hashCode();
                            ObjectId idWarehouse=detailWarehouse.getObjectId("_id");
                            ObjectId id_product=document.getObjectId("_id");
                            export.add(new Export(id_order, nameCustomer, EmployeeName, idEmploye, orderid, idWarehouse, status, id_product));
                            processedOrders.add(orderid);
                        }
                        
                    }
                    }
                }
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return export;
    }

    public static List<Export> DetailExportProduct(String Name, int idOrder) {
        ArrayList<Export> export = new ArrayList<>();

        try {
            Map<String, Integer> productQuantityMap = new HashMap<>();
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> category = DBConnection.getConnection().getCollection("Category");
            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> Customer = DBConnection.getConnection().getCollection("Customer");
            FindIterable<Document> orders = orderCollection.find(new Document());

            for (Document order : orders) {
                Document detailOrder = (Document) order.get("DetailOrder");
                List<Object> idProducts = (List<Object>) detailOrder.get("id_Product");
                ObjectId id_customer = order.getObjectId("id_Customer");

                for (Object idProduct : idProducts) {
                    if (idProduct instanceof String) {
                        try {
                            ObjectId objectId = new ObjectId((String) idProduct);
                            Document product = Product.find(new Document("_id", objectId)).first();
                            Document customer = Customer.find(new Document("_id", id_customer)).first();
                            ObjectId idcategory = product.getObjectId("ID_Category");
                            Document Category = category.find(new Document("_id", idcategory)).first();

                            if (product != null && customer != null && Category != null) {
                                String productName = product.getString("Name");
                                String customerName = customer.getString("Name");
                                int idOrderDetail = detailOrder.getObjectId("id_Order").hashCode();
                                boolean alreadyProcessed = productQuantityMap.containsKey(productName + idOrderDetail);

                                if (!alreadyProcessed && customerName.equals(Name) && idOrderDetail == idOrder) {
                                    int existingQuantity = productQuantityMap.getOrDefault(productName, 0);
                                    if (productQuantityMap.containsKey(productName)) {
                                        productQuantityMap.put(productName, existingQuantity + 1);
                                        productQuantityMap.put(productName + idOrderDetail, 1);
                                    } else {
                                        productQuantityMap.put(productName, 1);
                                    }

                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            for (Map.Entry<String, Integer> entry : productQuantityMap.entrySet()) {
                String productName = entry.getKey();
                int quantity = entry.getValue();
                Map<String, Object> productInfo = getProductInfo(Product, category, productName);

                if (productInfo != null) {
                    int price = (int) productInfo.get("price");
                    DecimalFormat formatter = new DecimalFormat("#,### $");
                    String formatprice = formatter.format(price);
                    String NameCategory = (String) productInfo.get("Category");
                    export.add(new Export(NameCategory, quantity, formatprice, productName));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return export;
    }

    private static Map<String, Object> getProductInfo(MongoCollection<Document> productCollection, MongoCollection<Document> categoryCollection, String Name) {
        Document product = productCollection.find(new Document("Name", Name)).first();
        if (product != null) {
            Map<String, Object> productInfo = new HashMap<>();
            productInfo.put("price", product.getInteger("Price"));
            ObjectId idCategory = product.getObjectId("ID_Category");
            Document category = categoryCollection.find(new Document("_id", idCategory)).first();
            if (category != null) {
                productInfo.put("Category", category.getString("Name"));
            }
            return productInfo;
        }
        return null;
    }

}
