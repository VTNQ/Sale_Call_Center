/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_director.util;

import com.mgteam.sale_call_center_director.connect.DBConnect;
import com.mgteam.sale_call_center_director.util.model.product;
import com.mgteam.sale_call_center_director.util.model.revenue_statistics;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author tranp
 */
public class daodb {

    public static List<revenue_statistics> revenue_Statistics() {
        ArrayList<revenue_statistics> reverse = new ArrayList<>();
        MongoCollection<Document> productCollection = DBConnect.getConnection().getCollection("Product");
        MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
        MongoCollection<Document> categoryCollection = DBConnect.getConnection().getCollection("Category");
        MongoCollection<Document> Warehouse = DBConnect.getConnection().getCollection("WareHouse");
        MongoCollection<Document> InComingOrder = DBConnect.getConnection().getCollection("InComingOrder");
        FindIterable<Document> productDocuments = productCollection.find();
        for (Document productDocument : productDocuments) {
            ObjectId idproduct = productDocument.getObjectId("_id");
            int totalQualitySold = 0;
            String formatprice = null;
            int totalprice = 0;
            String nameProduct;
            String namecategory;
            ObjectId idcategory = productDocument.getObjectId("ID_Category");
            Document category = categoryCollection.find(Filters.eq("_id", idcategory)).first();
            Document query1 = new Document("Detail." + String.valueOf(idproduct), new Document("$exists", true));
            Document detailWarehouse = Warehouse.find(query1).first();
            if (detailWarehouse != null && category != null) {
                ObjectId idincoming = detailWarehouse.getObjectId("ID_InComingOrder");
                Document incomingordercollection = InComingOrder.find(Filters.eq("_id", idincoming)).first();
                if (incomingordercollection != null) {
                          nameProduct = productDocument.getString("Name");
                    namecategory = category.getString("Name");
                    Document query = new Document("DetailOrder." + idproduct, new Document("$exists", true));
                    FindIterable<Document> orderDocuments = orderCollection.find(query);
                    for (Document orderDocument : orderDocuments) {
                        Document detailOrder = (Document) orderDocument.get("DetailOrder");
                        Document productDetail = (Document) detailOrder.get(idproduct.toString());

                        if (productDetail != null) {
                            Integer quality = productDetail.getInteger("Quality");
                            int price = productDocument.getInteger("Price");
                            if (quality != null) {
                                totalQualitySold += quality;
                                totalprice += (int) quality * price;
                                DecimalFormat formatter = new DecimalFormat("#,### $");
                                formatprice = formatter.format(totalprice);
                            }
                        }
                    }
                    reverse.add(new revenue_statistics( nameProduct, namecategory, totalQualitySold, formatprice));
                }
            }
        }
        return reverse;
    }

   public static List<revenue_statistics> revenue_Employee() {
    ArrayList<revenue_statistics> arr = new ArrayList<>();
    MongoCollection<Document> Product = DBConnect.getConnection().getCollection("Product");
    MongoCollection<Document> Order = DBConnect.getConnection().getCollection("Order");
    MongoCollection<Document> Employee = DBConnect.getConnection().getCollection("Employee");
    FindIterable<Document> ProductCollection = Product.find();

    // Lặp qua tất cả nhân viên
    for (Document employeeDocument : Employee.find()) {
        ObjectId idEmployee = employeeDocument.getObjectId("_id");
        String NameEmployee = employeeDocument.getString("Name");
        int totalQuality = 0;
        int totalPrice = 0;

        // Lặp qua tất cả sản phẩm
        for (Document productDocument : ProductCollection) {
            ObjectId idproduct = productDocument.getObjectId("_id");
            Document query = new Document("DetailOrder." + idproduct, new Document("$exists", true));
            FindIterable<Document> orderDocuments = Order.find(query);

            // Lặp qua tất cả đơn hàng của sản phẩm
            for (Document orderDocument : orderDocuments) {
                if (idEmployee.equals(orderDocument.getObjectId("id_Employee"))) {
                    Document detailOrder = (Document) orderDocument.get("DetailOrder");
                    Document productDetail = (Document) detailOrder.get(idproduct.toString());

                    if (productDetail != null) {
                        Integer quality = productDetail.getInteger("Quality");
                        Integer price = productDocument.getInteger("Price");

                        if (quality != null && price != null) {
                            totalQuality += quality;
                            totalPrice += quality * price;
                        }
                    }
                }
            }
        }

        DecimalFormat formatter = new DecimalFormat("#,### $");
        String totalprice = formatter.format(totalPrice);
        arr.add(new revenue_statistics(totalQuality, totalprice, NameEmployee));
    }

    return arr;
}

    public static String calculateTotalRevenue() {
        MongoCollection<Document> productCollection = DBConnect.getConnection().getCollection("Product");
        MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
        FindIterable<Document> productDocuments = productCollection.find();
        double totalRevenue = 0.0;

        for (Document productDocument : productDocuments) {
            ObjectId productId = productDocument.getObjectId("_id");
            Document query = new Document("DetailOrder." + productId, new Document("$exists", true));
            FindIterable<Document> orderDocuments = orderCollection.find(query);

            for (Document orderDocument : orderDocuments) {
                Document detailOrder = (Document) orderDocument.get("DetailOrder");
                Document productDetail = (Document) detailOrder.get(productId.toString());

                if (productDetail != null) {
                    Integer quantity = productDetail.getInteger("Quality");
                    int price = productDocument.getInteger("Price");

                    if (quantity != null) {
                        double productRevenue = quantity * price;
                        totalRevenue += productRevenue;
                    }
                }
            }
        }

        DecimalFormat formatter = new DecimalFormat("#,###.## $");
        String formattedTotalRevenue = formatter.format(totalRevenue);

        return formattedTotalRevenue;
    }

    public static List<product> ordernotyeprocessed(Object idproduct) {
        ArrayList<product> product = new ArrayList<>();
        MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
        MongoCollection<Document> Customercollection = DBConnect.getConnection().getCollection("Customer");
        FindIterable<Document> orders = orderCollection.find(new Document("status", 0));

        List<Document> orderList = new ArrayList<>();
        String customerName = "Unknown Customer"; // Default value

        for (Document document : orders) {
            Document detailOrder = (Document) document.get("DetailOrder");
            Document productDetail = (Document) detailOrder.get(String.valueOf(idproduct));
            ObjectId idCustomer = document.getObjectId("id_Customer");
            Document Customer = Customercollection.find(Filters.eq("_id", idCustomer)).first();

            if (Customer != null) {
                customerName = Customer.getString("Name");
            }

            if (productDetail != null) {
                String dateString = document.getString("Order_date");

                // Parse the date string to a Date object
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date orderDate = null;

                try {
                    orderDate = dateFormat.parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if (orderDate != null) {
                    document.append("parsedOrderDate", orderDate);
                    orderList.add(document);
                }
            }
        }

        orderList.sort(Comparator.comparing(doc -> doc.getDate("parsedOrderDate")));

        for (Document sortedOrder : orderList) {
            ObjectId orderId = sortedOrder.getObjectId("_id");
            Date orderDate = sortedOrder.getDate("parsedOrderDate");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedOrderDate = dateFormat.format(orderDate);
            int status = sortedOrder.getInteger("status");
            String statusString = "";
            if (status == 0) {
                statusString = "no process";
            }
            product.add(new product(formattedOrderDate, Math.abs(orderId.hashCode()), customerName, statusString));

        }
        return product;
    }

    public static List<product> listProductTotalQualitySoldAndCategoryName() {
        ArrayList<product> Product = new ArrayList<>();
        MongoCollection<Document> productCollection = DBConnect.getConnection().getCollection("Product");
        MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
        MongoCollection<Document> categoryCollection = DBConnect.getConnection().getCollection("Category");

        FindIterable<Document> productDocuments = productCollection.find();

        for (Document productDocument : productDocuments) {
            ObjectId productId = productDocument.getObjectId("_id");
            int totalQualitySold = 0;
            ObjectId idCategory = productDocument.getObjectId("ID_Category");
            Document category = categoryCollection.find(Filters.eq("_id", idCategory)).first();
            String categoryName = category.getString("Name");

            // Construct a query to find orders containing this product
            Document query = new Document("DetailOrder." + productId, new Document("$exists", true));
            FindIterable<Document> orderDocuments = orderCollection.find(query);

            int orderCount = 0; // Initialize order count

            for (Document orderDocument : orderDocuments) {
                orderCount++; // Increment order count for each matching order
                Document detailOrder = (Document) orderDocument.get("DetailOrder");
                Document productDetail = (Document) detailOrder.get(productId.toString());

                if (productDetail != null) {
                    Integer quality = productDetail.getInteger("Quality");
                    if (quality != null) {
                        totalQualitySold += quality;
                    }
                }
            }
            Product.add(new product(productDocument.getString("Name"), categoryName, totalQualitySold, orderCount, productId));
        }
        return Product;
    }

}
