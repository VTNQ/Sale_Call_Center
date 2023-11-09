/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_employee.util;

import com.mgteam.sale_call_center_employee.LoginController;
import com.mgteam.sale_call_center_employee.model.Category;
import com.mgteam.sale_call_center_employee.model.Export;
import com.mgteam.sale_call_center_employee.model.Import;
import com.mgteam.sale_call_center_employee.model.Product;
import com.mgteam.sale_call_center_employee.model.Warehouse;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
        FindIterable<Document> productcollection = Product.find();
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
        FindIterable<Document> productcollection = Product.find();
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

    public static List<Category> searchListCategory(String search) {
        ArrayList<Category> categories = new ArrayList<>();
        Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
        MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");

        FindIterable<Document> categoryDocuments = categoryCollection.find(Filters.regex("Name", regexPattern));

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
        FindIterable<Document> categorycollection = cate.find();
        for (Document document : categorycollection) {
            String name = document.getString("Name");
            ObjectId idcategory = document.getObjectId("_id");
            category.add(new Category(name, idcategory));
        }
        return category;
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

               
                            ObjectId idEmployee = order.getObjectId("id_Employee");
                            ObjectId idCustomer = order.getObjectId("id_Customer");
                            Document Emp = Employee.find(new Document("_id", idEmployee)).first();
                            Document cus = Customer.find(new Document("_id", idCustomer)).first();
                         

                            if (Emp != null && cus != null  && !processedOrders.contains(orderid)) {
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
            FindIterable<Document> orders = orderCollection.find(new Document());
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
    

                            if (Emp != null && cus != null  && !processedOrders.contains(orderid)) {
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
public static List<Warehouse>SearchDetailProductWarehouse(String Nameproduct,ObjectId id){
         ArrayList<Warehouse> ProductWare = new ArrayList<>();
         Pattern regexPattern = Pattern.compile(".*" + Nameproduct + ".*", Pattern.CASE_INSENSITIVE);
        try {
            // Kết nối đến cơ sở dữ liệu MongoDB
            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> Ware = DBConnection.getConnection().getCollection("WareHouse");
            MongoCollection<Document> category = DBConnection.getConnection().getCollection("Category");

            // Lấy tất cả sản phẩm
            FindIterable<Document> Warehouse = Ware.find();
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
            // Kết nối đến cơ sở dữ liệu MongoDB
            MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> Ware = DBConnection.getConnection().getCollection("WareHouse");
            MongoCollection<Document> category = DBConnection.getConnection().getCollection("Category");

            // Lấy tất cả sản phẩm
            FindIterable<Document> Warehouse = Ware.find();
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
            FindIterable<Document> Warehousecollection = Ware.find();
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
                        if (isSimilar) {
                            String Date = document.getString("Date");
                            LocalDate sinceDate = LocalDate.parse(Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            String supplier = comingFileter.getString("Supplier");
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
            FindIterable<Document> Warehousecollection = Ware.find();
            for (Document document : Warehousecollection) {
                FindIterable<Document> Productcollection = Product.find();
                for (Document document1 : Productcollection) {
                    ObjectId idproduct = document1.getObjectId("_id");
                    Document Detail = (Document) document.get("Detail");
                    Document idcol = (Document) Detail.get(String.valueOf(idproduct));
                    ObjectId idincoming = document.getObjectId("ID_InComingOrder");
                    Document comingFileter = IncomingOrder.find(Filters.eq("_id", idincoming)).first();
                    if (idcol != null && comingFileter != null && comingFileter.getObjectId("Id_Employee").equals(LoginController.id_employee) && !Warehousereceipt.contains(idincoming)) {
                        String Date = document.getString("Date");
                        LocalDate sinceDate = LocalDate.parse(Date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        String supplier = comingFileter.getString("Supplier");
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Warehouses;
    }

    public static List<Export> searchDetailExportProduct(String Name, ObjectId idorder, String search) {
        List<Export> exports = new ArrayList<>();
        Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
        try {
            MongoCollection<Document> productCollection = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> customerCollection = DBConnection.getConnection().getCollection("Customer");

            // Find products first.
            FindIterable<Document> productDocuments = productCollection.find();

            for (Document productDocument : productDocuments) {
                ObjectId productID = productDocument.getObjectId("_id");
                ObjectId categoryID = productDocument.getObjectId("ID_Category");

                // Check if the category exists in the Category collection.
                Document categoryFilter = categoryCollection.find(Filters.eq("_id", categoryID)).first();

                if (categoryFilter != null) {
                    // Once we have found the product and its category, proceed to find the order.
                    // Find the order document with the specified id.
                    Document orderDocument = orderCollection.find(Filters.eq("_id", idorder)).first();

                    if (orderDocument != null) {
                        ObjectId customerID = orderDocument.getObjectId("id_Customer");

                        // Check if the customer exists in the Customer collection.
                        Document customerFilter = customerCollection.find(Filters.eq("_id", customerID)).first();

                        Document Detail = (Document) orderDocument.get("DetailOrder");
                        Document idcol = (Document) Detail.get(String.valueOf(productID));
                        if (idcol != null && regexPattern.matcher(productDocument.getString("Name")).matches()) {
                            String NameProduct = productDocument.getString("Name");
                            int Quality = idcol.getInteger("Quality");
                            int price = productDocument.getInteger("Price");
                            DecimalFormat formatter = new DecimalFormat("#,### $");
                            String formatprice = formatter.format(price);
                            String nameCategory = categoryFilter.getString("Name");
                            exports.add(new Export(nameCategory, Quality, formatprice, NameProduct));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exports;
    }

    public static List<Export> DetailExportProduct(String Name, ObjectId idOrder) {
        List<Export> exports = new ArrayList<>();

        try {
            MongoCollection<Document> productCollection = DBConnection.getConnection().getCollection("Product");
            MongoCollection<Document> categoryCollection = DBConnection.getConnection().getCollection("Category");
            MongoCollection<Document> orderCollection = DBConnection.getConnection().getCollection("Order");
            MongoCollection<Document> customerCollection = DBConnection.getConnection().getCollection("Customer");

            // Find products first.
            FindIterable<Document> productDocuments = productCollection.find();

            for (Document productDocument : productDocuments) {
                ObjectId productID = productDocument.getObjectId("_id");
                ObjectId categoryID = productDocument.getObjectId("ID_Category");

                // Check if the category exists in the Category collection.
                Document categoryFilter = categoryCollection.find(Filters.eq("_id", categoryID)).first();

                if (categoryFilter != null) {
                    // Once we have found the product and its category, proceed to find the order.
                    // Find the order document with the specified id.
                    Document orderDocument = orderCollection.find(Filters.eq("_id", idOrder)).first();

                    if (orderDocument != null) {
                        ObjectId customerID = orderDocument.getObjectId("id_Customer");

                        // Check if the customer exists in the Customer collection.
                        Document customerFilter = customerCollection.find(Filters.eq("_id", customerID)).first();

                        Document Detail = (Document) orderDocument.get("DetailOrder");
                        Document idcol = (Document) Detail.get(String.valueOf(productID));
                        if (idcol != null) {
                            String NameProduct = productDocument.getString("Name");
                            int Quality = idcol.getInteger("Quality");
                            int price = productDocument.getInteger("Price");
                            DecimalFormat formatter = new DecimalFormat("#,### $");
                            String formatprice = formatter.format(price);
                            String nameCategory = categoryFilter.getString("Name");
                            exports.add(new Export(nameCategory, Quality, formatprice, NameProduct));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exports;
    }

    public static List<Import> SearchDetailWarehouseApproval(String search, ObjectId idWarehouse) {
        List<Import> imports = new ArrayList<>();
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> Category = DBConnection.getConnection().getCollection("Category");
        MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");
        Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
        try {

            FindIterable<Document> productCollection = Product.find();
            for (Document document : productCollection) {
                ObjectId id = document.getObjectId("_id");
                ObjectId idCategory = document.getObjectId("ID_Category");
                Bson query = Filters.and(
                        Filters.eq("_id", idWarehouse),
                        Filters.exists("Detail." + id, true)
                );
                Document WarehouseQury = Warehouse.find(query).first();
                Document categoryFilter = Category.find(Filters.eq("_id", idCategory)).first();
                if (WarehouseQury != null && categoryFilter != null) {
                    String nameproduct = document.getString("Name");
                    Document Detail = (Document) WarehouseQury.get("Detail");
                    Document idcol = (Document) Detail.get(String.valueOf(id));
                    if (idcol != null && regexPattern.matcher(nameproduct).matches()) {
                        int Quality = idcol.getInteger("Quality");
                        int price = document.getInteger("Price");
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatprice = formatter.format(price);
                        String nameCategory = categoryFilter.getString("Name");
                        imports.add(new Import(nameproduct, nameCategory, formatprice, Quality));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imports;
    }

    public static List<Import> DetailWarehouseApproval(ObjectId idWarehouse) {
        List<Import> imports = new ArrayList<>();
        MongoCollection<Document> Product = DBConnection.getConnection().getCollection("Product");
        MongoCollection<Document> Category = DBConnection.getConnection().getCollection("Category");
        MongoCollection<Document> Warehouse = DBConnection.getConnection().getCollection("WareHouse");

        try {

            FindIterable<Document> productCollection = Product.find();
            for (Document document : productCollection) {
                ObjectId id = document.getObjectId("_id");
                ObjectId idCategory = document.getObjectId("ID_Category");
                Bson query = Filters.and(
                        Filters.eq("_id", idWarehouse),
                        Filters.exists("Detail." + id, true)
                );
                Document WarehouseQury = Warehouse.find(query).first();
                Document categoryFilter = Category.find(Filters.eq("_id", idCategory)).first();
                if (WarehouseQury != null && categoryFilter != null) {
                    String nameproduct = document.getString("Name");
                    Document Detail = (Document) WarehouseQury.get("Detail");
                    Document idcol = (Document) Detail.get(String.valueOf(id));
                    if (idcol != null) {
                        int Quality = idcol.getInteger("Quality");
                        int price = document.getInteger("Price");
                        DecimalFormat formatter = new DecimalFormat("#,### $");
                        String formatprice = formatter.format(price);
                        String nameCategory = categoryFilter.getString("Name");
                        imports.add(new Import(nameproduct, nameCategory, formatprice, Quality));
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return imports;
    }

    public static List<Import> SearchWarehouseApproval(String search) {
        Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
        List<Import> imports = new ArrayList<>();
        Set<ObjectId> Wareimport = new HashSet();

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

                        if (Employeecheck != null && !Wareimport.contains(idincoming)
                                && incomingfilter.getObjectId("Id_Employee").equals(LoginController.id_employee)
                                && regexPattern.matcher(incomingfilter.getString("Supplier")).matches()) {

                            String nameEmployee = Employeecheck.getString("Name");
                            String date = document.getString("Date");
                            LocalDate sinceDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            int status = incomingfilter.getInteger("status");
                            ObjectId idWarehouse = document.getObjectId("_id");
                            String Supply = incomingfilter.getString("Supplier");
                            imports.add(new Import(nameEmployee, formattedSince, status, idWarehouse, idEmployee, idincoming, Supply));
                            Wareimport.add(idincoming);
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
    List<Import> importList = new ArrayList<>();
    Set<ObjectId> warehouseImports = new HashSet();

    try {
       

        MongoCollection<Document> warehouseCollection = DBConnection.getConnection().getCollection("WareHouse");
        MongoCollection<Document> incomingOrderCollection = DBConnection.getConnection().getCollection("InComingOrder");
        MongoCollection<Document> employeeCollection = DBConnection.getConnection().getCollection("Employee");
        MongoCollection<Document> productCollection = DBConnection.getConnection().getCollection("Product");

        FindIterable<Document> warehouseDocuments = warehouseCollection.find();
        for (Document warehouseDocument : warehouseDocuments) {
            Document detail = warehouseDocument.get("Detail", Document.class);
            for (String productKey : detail.keySet()) {
                ObjectId idProduct = new ObjectId(productKey);

                FindIterable<Document> productDocuments = productCollection.find(eq("_id", idProduct));
                for (Document productDocument : productDocuments) {
                    Document idProductCheck = (Document) detail.get(idProduct.toHexString());
                    ObjectId idIncoming = warehouseDocument.getObjectId("ID_InComingOrder");
                    Document incomingFilter = incomingOrderCollection.find(eq("_id", idIncoming)).first();

                    if (idProductCheck != null && incomingFilter != null) {
                        ObjectId idEmployee = incomingFilter.getObjectId("Id_Employee");
                        Document employeeCheck = employeeCollection.find(eq("_id", idEmployee)).first();

                        if (employeeCheck != null && !warehouseImports.contains(idIncoming) && idEmployee.equals(LoginController.id_employee)) {
                            String nameEmployee = employeeCheck.getString("Name");
                            String date = warehouseDocument.getString("Date");
                            LocalDate sinceDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                            String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            int status = incomingFilter.getInteger("status");
                            ObjectId idWarehouse = warehouseDocument.getObjectId("_id");
                            String supply = incomingFilter.getString("Supplier");
                            importList.add(new Import(nameEmployee, formattedSince, status, idWarehouse, idEmployee, idIncoming, supply));
                            warehouseImports.add(idIncoming);
                        }
                    }
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return importList;
}

}
