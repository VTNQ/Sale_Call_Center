/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee.util;

import com.mgteam.sale_call_center_employee.model.Export;
import com.mgteam.sale_call_center_employee.model.Import;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
                ObjectId orderid = order.getObjectId("_id");
                FindIterable<Document> pro = Product.find();
                for (Document document : pro) {
                    ObjectId idproduct = document.getObjectId("_id");
                    Document query = new Document("DetailOrder." + String.valueOf(idproduct), new Document("$exists", true));
                    Document detail = orderCollection.find(query).first();
                    if (detail != null) {
                        Document query1 = new Document("Detail." + String.valueOf(idproduct), new Document("$exists", true));
                        Document detailWarehouse = Warehouse.find(query1).first();
                        if (detailWarehouse != null) {
                            ObjectId idEmployee = order.getObjectId("id_Employee");
                            ObjectId idCustomer = order.getObjectId("id_Customer");
                            Document Emp = Employee.find(new Document("_id", idEmployee)).first();
                            Document cus = Customer.find(new Document("_id", idCustomer)).first();
                            Document incoming = Outgoinorder.find(Filters.and(Filters.eq("ID_Employee", idEmployee), Filters.eq("ID_Order", orderid))).first();

                            if (Emp != null && cus != null && incoming != null && !processedOrders.contains(orderid)) {
                                String nameCustomer = cus.getString("Name");
                                String EmployeeName = Emp.getString("Name");
                                int status = incoming.getInteger("status");
                                ObjectId idEmploye = Emp.getObjectId("_id");
                                int id_order = order.getObjectId("_id").hashCode();
                                ObjectId idWarehouse = detailWarehouse.getObjectId("_id");
                                ObjectId id_outgoing = incoming.getObjectId("_id");
                                export.add(new Export(id_order, nameCustomer, EmployeeName, idEmploye, orderid, idWarehouse, status, id_outgoing));
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

                ObjectId id_customer = order.getObjectId("id_Customer");
                FindIterable<Document> product = Product.find();
                for (Document document : product) {
                    ObjectId idProduct = document.getObjectId("_id");

                    ObjectId idCategory = document.getObjectId("ID_Category");
                    Document cus = Customer.find(Filters.and(Filters.eq("_id", id_customer), Filters.eq("Name", Name))).first();
                    Document Detail = (Document) order.get("DetailOrder");
                    Document categorycollection = category.find(Filters.eq("_id", idCategory)).first();
                    if (cus != null && categorycollection != null && Detail != null && order.getObjectId("_id").hashCode() == idOrder) {
                        Document idcol = (Document) Detail.get(String.valueOf(idProduct));
                        if (idcol != null) {
                            String nameProduct = document.getString("Name");
                            int Quality = idcol.getInteger("Quality");
                            int price = document.getInteger("Price");
                            DecimalFormat formatter = new DecimalFormat("#,### $");
                            String formatprice = formatter.format(price);
                            String Namecategory = categorycollection.getString("Name");
                            export.add(new Export(Namecategory, Quality, formatprice, nameProduct));
                        }

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return export;
    }

    public static List<Import> DetailWarehouseApproval(List<ObjectId> idProducts,ObjectId idWarehouse) {
    List<Import> imports = new ArrayList<>();
    MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
    MongoCollection<Document> Category = DBConnection.getConnection().getCollection("Category");
    MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
    
    try {
        for (ObjectId idProduct : idProducts) {
            FindIterable<Document> Warehousecollection = Warehouse.find();
            for (Document document : Warehousecollection) {
                Document Detail = (Document) document.get("Detail");
                Document idcol = (Document) Detail.get(String.valueOf(idProduct));
                Document Productcollection = Product.find(Filters.eq("_id", idProduct)).first();
                if (Productcollection != null && idcol != null) {
                    ObjectId idCategory = Productcollection.getObjectId("ID_Category");
                    Document categorycollection = Category.find(Filters.eq("_id", idCategory)).first();
                    if (categorycollection != null && document.getObjectId("_id").equals(idWarehouse)) {
                        String nameProduct = Productcollection.getString("Name");
                        String nameCategory = categorycollection.getString("Name");
                        int Quality = idcol.getInteger("Quality");
                        int price = Productcollection.getInteger("Price");
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatprice = formatter.format(price);
                        imports.add(new Import(nameProduct, nameCategory, formatprice, Quality));
                    }
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return imports;
}

    public static List<Import> WarehouseApproval() {
        List<Import> Import = new ArrayList<>();
        Set<ObjectId> Wareimport = new HashSet<>();
        MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
        MongoCollection<Document> InComingOrder = DBConnection.getConnection().getCollection("InComingOrder");
        MongoCollection<Document> Employee = DBConnection.getConnection().getCollection("Employee");
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        try {
            FindIterable<Document> Ware = Warehouse.find();
            for (Document document : Ware) {
                FindIterable<Document> product = Product.find();
                for (Document Productcollection : product) {
                    ObjectId idProduct = Productcollection.getObjectId("_id");
                    Document Detail = (Document) document.get("Detail");
                    Document idproductcheck = (Document) Detail.get(String.valueOf(idProduct));
                    ObjectId idincoming = document.getObjectId("ID_InComingOrder");
                    Document incomingfilter = InComingOrder.find(Filters.eq("_id", idincoming)).first();

                    if (idproductcheck != null && incomingfilter != null) {
                        ObjectId idEmployee = incomingfilter.getObjectId("Id_Employee");
                        Document Employeecheck = Employee.find(Filters.eq("_id", idEmployee)).first();
                        if (Employeecheck != null && !Wareimport.contains(idincoming)) {
                            String nameEmployee = Employeecheck.getString("Name");
                            String date = document.getString("Date");
                            LocalDate sinceDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            int status = incomingfilter.getInteger("status");
                            ObjectId idWarehouse = document.getObjectId("_id");
                            String Supply=incomingfilter.getString("supplier");
                            Import.add(new Import(nameEmployee, formattedSince, status, idWarehouse, idEmployee,idincoming,Supply));
                            Wareimport.add(idincoming);
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Import;
    }
}
