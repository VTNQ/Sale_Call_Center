/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee.util;

import com.mgteam.sale_call_center_employee.LoginController;
import com.mgteam.sale_call_center_employee.model.Category;
import com.mgteam.sale_call_center_employee.model.Export;
import com.mgteam.sale_call_center_employee.model.Product;
import com.mgteam.sale_call_center_employee.model.Supply;
import com.mgteam.sale_call_center_employee.model.Warehouse;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import com.mongodb.client.model.Sorts;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

/**
 *
 * @author tranp
 */
public class daodb {

    public static List<Product> searchListProduct(String search) {
        ArrayList<Product> arr = new ArrayList<>();
        Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> Category = DBConnection.getConnection().getCollection("Category");
        FindIterable<Document> productcollection = Product.find().sort(Sorts.descending("_id"));
        for (Document document : productcollection) {
            ObjectId idCategory = document.getObjectId("ID_Category");
            Document filterCategory = Category.find(Filters.eq("_id", idCategory)).first();
            if (filterCategory != null && regexPattern.matcher(document.getString("Name")).matches()) {
                ObjectId id = document.getObjectId("_id");
                String NameProduct = document.getString("Name");
                String NameCategory = filterCategory.getString("Name");
                int price = document.getInteger("Price");
                DecimalFormat formatter = new DecimalFormat("#,### $");
                String formatprice = formatter.format(price);
                arr.add(new Product(id, NameProduct, formatprice, NameCategory));
            }
        }
        return arr;
    }

    public static List<Product> ListProduct() {
        ArrayList<Product> arr = new ArrayList<>();
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> Category = DBConnection.getConnection().getCollection("Category");
        FindIterable<Document> productcollection = Product.find().sort(Sorts.descending("_id"));
        for (Document document : productcollection) {
            ObjectId idCategory = document.getObjectId("ID_Category");
            Document filterCategory = Category.find(Filters.eq("_id", idCategory)).first();
            if (filterCategory != null) {
                ObjectId id = document.getObjectId("_id");
                String NameProduct = document.getString("Name");
                String NameCategory = filterCategory.getString("Name");
                int price = document.getInteger("Price");
                DecimalFormat formatter = new DecimalFormat("#,### $");
                String formatprice = formatter.format(price);
                arr.add(new Product(id, NameProduct, formatprice, NameCategory));
            }
        }
        return arr;
    }

    public static List<Supply> searchsupply(String search) {
        ArrayList<Supply> categories = new ArrayList<>();
        Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
        MongoCollection<Document> supply = DBConnection.getConnection().getCollection("Supply");

        FindIterable<Document> categoryDocuments = supply.find(Filters.regex("Name", regexPattern)).sort(Sorts.descending("_id"));

        for (Document document : categoryDocuments) {
            String name = document.getString("Name");
            String address = document.getString("Address");
            ObjectId idCategory = document.getObjectId("_id");
            categories.add(new Supply(name, address, idCategory));
        }
        return categories;
    }

    public static List<Category> searchListCategory(String search) {
        ArrayList<Category> categories = new ArrayList<>();
        Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");

        FindIterable<Document> categoryDocuments = categoryCollection.find(Filters.regex("Name", regexPattern)).sort(Sorts.descending("_id"));

        for (Document document : categoryDocuments) {
            String name = document.getString("Name");
            ObjectId idCategory = document.getObjectId("_id");
            categories.add(new Category(name, idCategory));
        }
        return categories;
    }

    public static List<Category> ListCategory() {
        ArrayList<Category> category = new ArrayList<>();
        MongoCollection<Document> cate = DBConnection.getConnection().getCollection("Category");
        FindIterable<Document> categorycollection = cate.find().sort(Sorts.descending("_id"));
        for (Document document : categorycollection) {
            String name = document.getString("Name");
            ObjectId idcategory = document.getObjectId("_id");
            category.add(new Category(name, idcategory));
        }
        return category;
    }

    public static List<Supply> ListSupply() {
        ArrayList<Supply> supply = new ArrayList<>();
        MongoCollection<Document> cate = DBConnection.getConnection().getCollection("Supply");
        FindIterable<Document> categorycollection = cate.find().sort(Sorts.descending("_id"));
        for (Document document : categorycollection) {
            String name = document.getString("Name");
            String Address = document.getString("Address");
            ObjectId id = document.getObjectId("_id");
            supply.add(new Supply(name, Address, id));
        }
        return supply;
    }

    public static List<Export> SearchExportWarehouse(String search) {
        ArrayList<Export> export = new ArrayList<>();
        Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
        Set<ObjectId> processedOrders = new HashSet<>();
        try {
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");

            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> Employee = DBConnection.getConnection().getCollection("Employee");
            MongoCollection<Document> Customer = DBConnection.getConnection().getCollection("Customer");

            FindIterable<Document> orders = orderCollection.find(new Document()).sort(Sorts.descending("_id"));
            for (Document order : orders) {
                ObjectId orderid = order.getObjectId("_id");
                FindIterable<Document> pro = Product.find();
                for (Document document : pro) {
                    ObjectId idproduct = document.getObjectId("_id");
                    Document query = new Document("DetailOrder." + String.valueOf(idproduct), new Document("$exists", true));
                    Document detail = orderCollection.find(query).first();
                    if (detail != null) {
                        Document query1 = new Document("Detail." + String.valueOf(idproduct), new Document("$exists", true));

                        ObjectId idEmployee = order.getObjectId("id_Employee");
                        ObjectId idCustomer = order.getObjectId("id_Customer");
                        Document Emp = Employee.find(new Document("_id", idEmployee)).first();
                        Document cus = Customer.find(new Document("_id", idCustomer)).first();

                        if (Emp != null && cus != null && !processedOrders.contains(orderid)) {
                            boolean isSimilar = regexPattern.matcher(String.valueOf(order.getObjectId("_id").hashCode())).matches();
                            if (isSimilar || regexPattern.matcher(cus.getString("Name")).matches()) {
                                String nameCustomer = cus.getString("Name");
                                String EmployeeName = Emp.getString("Name");
                                int status = order.getInteger("status");
                                ObjectId idEmploye = Emp.getObjectId("_id");
                                int id_order = Math.abs(order.getObjectId("_id").hashCode());

                                export.add(new Export(id_order, nameCustomer, EmployeeName, idEmploye, orderid, status));
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

    public static List<Export> ExportWarehouse() {
        ArrayList<Export> export = new ArrayList<>();
        Set<ObjectId> processedOrders = new HashSet<>();
        try {
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> Employee = DBConnection.getConnection().getCollection("Employee");
            MongoCollection<Document> Customer = DBConnection.getConnection().getCollection("Customer");
            FindIterable<Document> orders = orderCollection.find(new Document()).sort(Sorts.descending("_id"));
            for (Document order : orders) {
                ObjectId orderid = order.getObjectId("_id");
                FindIterable<Document> pro = Product.find();
                for (Document document : pro) {
                    ObjectId idproduct = document.getObjectId("_id");
                    Document query = new Document("DetailOrder." + String.valueOf(idproduct), new Document("$exists", true));
                    Document detail = orderCollection.find(query).first();
                    if (detail != null) {

                        ObjectId idEmployee = order.getObjectId("id_Employee");
                        ObjectId idCustomer = order.getObjectId("id_Customer");
                        Document Emp = Employee.find(new Document("_id", idEmployee)).first();
                        Document cus = Customer.find(new Document("_id", idCustomer)).first();

                        if (Emp != null && cus != null && !processedOrders.contains(orderid)) {
                            String nameCustomer = cus.getString("Name");
                            String EmployeeName = Emp.getString("Name");
                            int status = order.getInteger("status");
                            ObjectId idEmploye = Emp.getObjectId("_id");
                            int id_order = Math.abs(order.getObjectId("_id").hashCode());

                            export.add(new Export(id_order, nameCustomer, EmployeeName, idEmploye, orderid, status));
                            processedOrders.add(orderid);
                        }

                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return export;
    }

    public static List<Warehouse> SearchDetailProductWarehouse(String Nameproduct, ObjectId id) {
        ArrayList<Warehouse> ProductWare = new ArrayList<>();
        Pattern regexPattern = Pattern.compile(".*" + Nameproduct + ".*", Pattern.CASE_INSENSITIVE);
        try {
            // Kết nối đến cơ sở dữ liệu MongoDB
            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> Ware = DBConnection.getConnection().getCollection("WareHouse");
            MongoCollection<Document> category = DBConnection.getConnection().getCollection("Category");

            // Lấy tất cả sản phẩm
            FindIterable<Document> Warehouse = Ware.find().sort(Sorts.descending("_id"));
            for (Document document : Warehouse) {
                FindIterable<Document> product = Product.find();
                for (Document document1 : product) {
                    ObjectId idproduct = document1.getObjectId("_id");
                    Document Detail = (Document) document.get("Detail");
                    Document idcol = (Document) Detail.get(String.valueOf(idproduct));
                    ObjectId idcategory = document1.getObjectId("ID_Category");
                    Document categoryfilter = category.find(Filters.eq("_id", idcategory)).first();
                    if (categoryfilter != null && idcol != null && document.getObjectId("_id").equals(id) && regexPattern.matcher(document1.getString("Name")).matches()) {
                        String nameproduct = document1.getString("Name");
                        ObjectId idwarehouse = document.getObjectId("_id");
                        int Quality = idcol.getInteger("Quality");
                        int price = document1.getInteger("Price");
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatprice = formatter.format(price);
                        String namecategory = categoryfilter.getString("Name");
                        ProductWare.add(new Warehouse(idwarehouse, nameproduct, Quality, formatprice, namecategory, idproduct));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ProductWare;
    }

    public static List<Warehouse> DetailProductWarehouse(ObjectId id) {
        ArrayList<Warehouse> ProductWare = new ArrayList<>();
        try {
            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> Ware = DBConnection.getConnection().getCollection("WareHouse");
            MongoCollection<Document> category = DBConnection.getConnection().getCollection("Category");

            // Lấy tất cả sản phẩm
            FindIterable<Document> Warehouse = Ware.find().sort(Sorts.descending("_id"));
            for (Document document : Warehouse) {
                FindIterable<Document> product = Product.find();
                for (Document document1 : product) {
                    ObjectId idproduct = document1.getObjectId("_id");
                    Document Detail = (Document) document.get("Detail");
                    Document idcol = (Document) Detail.get(String.valueOf(idproduct));
                    ObjectId idcategory = document1.getObjectId("ID_Category");
                    Document categoryfilter = category.find(Filters.eq("_id", idcategory)).first();
                    if (categoryfilter != null && idcol != null && document.getObjectId("_id").equals(id)) {
                        String nameproduct = document1.getString("Name");
                        ObjectId idwarehouse = document.getObjectId("_id");
                        int Quality = idcol.getInteger("Quality");
                        int price = document1.getInteger("Price");
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatprice = formatter.format(price);
                        String namecategory = categoryfilter.getString("Name");
                        ProductWare.add(new Warehouse(idwarehouse, nameproduct, Quality, formatprice, namecategory, idproduct));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ProductWare;
    }

    public static List<Warehouse> SearchWarehouseReceipt(String query) {
        List<Warehouse> Warehouses = new ArrayList<>();
        Set<ObjectId> Warehousereceipt = new HashSet<>();
        Pattern regexPattern = Pattern.compile(".*" + query + ".*", Pattern.CASE_INSENSITIVE);
        try {
            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> IncomingOrder = DBConnection.getConnection().getCollection("InComingOrder");
            MongoCollection<Document> Ware = DBConnection.getConnection().getCollection("WareHouse");
            MongoCollection<Document>Supply=DBConnection.getConnection().getCollection("Supply");
            FindIterable<Document> Warehousecollection = Ware.find().sort(Sorts.descending("_id"));
            for (Document document : Warehousecollection) {
                FindIterable<Document> Productcollection = Product.find();
                for (Document document1 : Productcollection) {
                    ObjectId idproduct = document1.getObjectId("_id");
                    Document Detail = (Document) document.get("Detail");
                    Document idcol = (Document) Detail.get(String.valueOf(idproduct));
                    ObjectId idincoming = document.getObjectId("ID_InComingOrder");

                    Document comingFileter = IncomingOrder.find(Filters.eq("_id", idincoming)).first();
                    if (idcol != null && comingFileter != null && comingFileter.getObjectId("Id_Employee").equals(LoginController.id_employee) && !Warehousereceipt.contains(idincoming)) {
                        boolean isSimilar = regexPattern.matcher(String.valueOf(Math.abs(idincoming.hashCode()))).matches();
                        ObjectId idSupplier=comingFileter.getObjectId("id_Supplier");
                        Document supplierFilter=Supply.find(Filters.eq("_id",idSupplier)).first();
                        if (isSimilar && supplierFilter!=null) {
                            String Date = document.getString("Date");
                            LocalDate sinceDate = LocalDate.parse(Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            String supplier = supplierFilter.getString("Name");
                            ObjectId idwarehouse = document.getObjectId("_id");
                            int status = comingFileter.getInteger("status");
                            String statustring = "";
                            if (status == 0) {
                                statustring = "no process";
                            } else if (status == 1) {
                                statustring = "approved";
                            } else if (status == 2) {
                                statustring = "canceled";
                            }
                            Warehouses.add(new Warehouse(Math.abs(idincoming.hashCode()), formattedSince, statustring, supplier, idwarehouse));
                            Warehousereceipt.add(idincoming);
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Warehouses;
    }

    public static List<Warehouse> WarehouseReceipt() {
        List<Warehouse> Warehouses = new ArrayList<>();
        Set<ObjectId> Warehousereceipt = new HashSet<>();
        try {
            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> IncomingOrder = DBConnection.getConnection().getCollection("InComingOrder");
            MongoCollection<Document> Ware = DBConnection.getConnection().getCollection("WareHouse");
            MongoCollection<Document>Supply=DBConnection.getConnection().getCollection("Supply");
            FindIterable<Document> Warehousecollection = Ware.find().sort(Sorts.descending("_id"));
            for (Document document : Warehousecollection) {
                FindIterable<Document> Productcollection = Product.find();
                for (Document document1 : Productcollection) {
                    ObjectId idproduct = document1.getObjectId("_id");
                    Document Detail = (Document) document.get("Detail");
                    Document idcol = (Document) Detail.get(String.valueOf(idproduct));
                    ObjectId idincoming = document.getObjectId("ID_InComingOrder");
                    Document comingFileter = IncomingOrder.find(Filters.eq("_id", idincoming)).first();
                    if (idcol != null && comingFileter != null && comingFileter.getObjectId("Id_Employee").equals(LoginController.id_employee) && !Warehousereceipt.contains(idincoming)) {
                        ObjectId idSupply=comingFileter.getObjectId("id_Supplier");
                        Document Supplycollection=Supply.find(Filters.eq("_id",idSupply)).first();
                        if(Supplycollection!=null){
                              String Date = document.getString("Date");
                        LocalDate sinceDate = LocalDate.parse(Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        String supplier = Supplycollection.getString("Name");
                        ObjectId idwarehouse = document.getObjectId("_id");
                        int status = comingFileter.getInteger("status");
                        String statustring = "";
                        if (status == 0) {
                            statustring = "no process";
                        } else if (status == 1) {
                            statustring = "approved";
                        } else if (status == 2) {
                            statustring = "canceled";
                        }
                        Warehouses.add(new Warehouse(Math.abs(idincoming.hashCode()), formattedSince, statustring, supplier, idwarehouse));
                        Warehousereceipt.add(idincoming);
                        }
                      
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Warehouses;
    }

    public static List<Export> searchDetailExportProduct(String Name, ObjectId idorder, String search) {
        List<Export> exports = new ArrayList<>();

        try {
            MongoCollection<Document> productCollection = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> customerCollection = DBConnection.getConnection().getCollection("Customer");
            MongoCollection<Document> warehouseCollection = DBConnection.getConnection().getCollection("WareHouse");
            Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
            FindIterable<Document> productDocuments = productCollection.find();

            for (Document productDocument : productDocuments) {
                ObjectId productID = productDocument.getObjectId("_id");
                ObjectId categoryID = productDocument.getObjectId("ID_Category");
                Document categoryFilter = categoryCollection.find(Filters.eq("_id", categoryID)).first();

                if (categoryFilter != null) {

                    Document orderDocument = orderCollection.find(Filters.eq("_id", idorder)).first();

                    Document warehouseFilter = warehouseCollection.find(Filters.eq("Detail." + productID, new Document("$exists", true))).first();
                    int warehouseTotalQuality = (warehouseFilter != null) ? calculateWarehouseTotalQuality(warehouseFilter) : 0;

                    if (orderDocument != null) {
                        ObjectId customerID = orderDocument.getObjectId("id_Customer");

                        Document customerFilter = customerCollection.find(Filters.eq("_id", customerID)).first();
                        Document detail = (Document) orderDocument.get("DetailOrder");
                        Document idcol = (Document) detail.get(String.valueOf(productID));

                        if (idcol != null && regexPattern.matcher(productDocument.getString("Name")).matches()) {
                            String nameProduct = productDocument.getString("Name");
                            int quality = idcol.getInteger("Quality");
                            int price = productDocument.getInteger("Price");
                            DecimalFormat formatter = new DecimalFormat("#,### $");
                            String formatPrice = formatter.format(price);
                            String nameCategory = categoryFilter.getString("Name");
                            exports.add(new Export(nameCategory, quality, formatPrice, warehouseTotalQuality, nameProduct));
                        }
                    }
                }
            }

            exports.sort(Comparator.comparing(Export::getQuality).thenComparing(Export::getTotalQuality, Comparator.reverseOrder()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exports;
    }

    public static List<Export> detailExportProduct(String name, ObjectId idOrder) {
        List<Export> exports = new ArrayList<>();

        try {
            MongoCollection<Document> productCollection = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> customerCollection = DBConnection.getConnection().getCollection("Customer");
            MongoCollection<Document> warehouseCollection = DBConnection.getConnection().getCollection("WareHouse");
            FindIterable<Document> productDocuments = productCollection.find();

            for (Document productDocument : productDocuments) {
                ObjectId productID = productDocument.getObjectId("_id");
                ObjectId categoryID = productDocument.getObjectId("ID_Category");

               
                Document categoryFilter = categoryCollection.find(Filters.eq("_id", categoryID)).first();

                if (categoryFilter != null) {
                   
                    Document orderDocument = orderCollection.find(Filters.eq("_id", idOrder)).first();

          
                    Document warehouseFilter = warehouseCollection.find(Filters.eq("Detail." + productID, new Document("$exists", true))).first();
                    int warehouseTotalQuality = (warehouseFilter != null) ? calculateWarehouseTotalQuality(warehouseFilter) : 0;

                    if (orderDocument != null) {
                        ObjectId customerID = orderDocument.getObjectId("id_Customer");

                     
                        Document customerFilter = customerCollection.find(Filters.eq("_id", customerID)).first();
                        Document detail = (Document) orderDocument.get("DetailOrder");
                        Document idcol = (Document) detail.get(String.valueOf(productID));

                        if (idcol != null) {
                            String nameProduct = productDocument.getString("Name");
                            int quality = idcol.getInteger("Quality");
                            int price = productDocument.getInteger("Price");
                            DecimalFormat formatter = new DecimalFormat("#,### $");
                            String formatPrice = formatter.format(price);
                            String nameCategory = categoryFilter.getString("Name");
                            exports.add(new Export(nameCategory, quality, formatPrice, warehouseTotalQuality, nameProduct));
                        }
                    }
                }
            }

            exports.sort(Comparator.comparing(Export::getQuality).thenComparing(Export::getTotalQuality, Comparator.reverseOrder()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exports;
    }

    private static int calculateWarehouseTotalQuality(Document warehouseFilter) {
        Document detailWarehouse = (Document) warehouseFilter.get("Detail");
        int totalQuality = 0;

        for (String productId : detailWarehouse.keySet()) {
            Document productDetail = (Document) detailWarehouse.get(productId);
            totalQuality += productDetail.getInteger("Quality");
        }

        return totalQuality;
    }

}
