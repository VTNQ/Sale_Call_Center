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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
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

    public static List<revenue_statistics> filterYear(String selectedYear) {
        ArrayList<revenue_statistics> revenueList = new ArrayList<>();
        MongoCollection<Document> productCollection = DBConnect.getConnection().getCollection("Product");
        MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
        MongoCollection<Document> categoryCollection = DBConnect.getConnection().getCollection("Category");

        FindIterable<Document> productDocuments = productCollection.find();

        for (Document productDocument : productDocuments) {
            ObjectId productId = productDocument.getObjectId("_id");
            int totalQualitySold = 0;
            int totalPrice = 0;
            String nameProduct;
            String nameCategory;
            ObjectId categoryId = productDocument.getObjectId("ID_Category");
            Document category = categoryCollection.find(Filters.eq("_id", categoryId)).first();

            if (category != null) {
                nameProduct = productDocument.getString("Name");
                nameCategory = category.getString("Name");
                Document query1 = new Document("DetailOrder." + String.valueOf(productId), new Document("$exists", true));
                FindIterable<Document>ordercollecion=orderCollection.find(query1);
                for (Document productDocument1 : ordercollecion) {
                    String date = productDocument1.getString("Order_date");
                    if ("All Years".equals(selectedYear) || dateMatchesSelectedYear(date, selectedYear)) {


                        Document detailOrder = (Document) productDocument1.get("DetailOrder");
                        Document productDetail = (Document) detailOrder.get(productId.toString());

                        if (productDetail != null) {
                            Integer quality = productDetail.getInteger("Quality");
                            int price = productDocument.getInteger("Price");

                            if (quality != null) {
                                totalQualitySold += quality;
                                totalPrice += quality * price;
                            }
                        }

                    }

                }

                DecimalFormat formatter = new DecimalFormat("#,### $");
                String formatPrice = formatter.format(totalPrice);

                revenueList.add(new revenue_statistics(nameProduct, nameCategory, totalQualitySold, formatPrice));
            }
        }

        return revenueList;
    }
public static List<revenue_statistics> filterQuarter(String selectedQuarter) {
   ArrayList<revenue_statistics> revenueList = new ArrayList<>();
        MongoCollection<Document> productCollection = DBConnect.getConnection().getCollection("Product");
        MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
        MongoCollection<Document> categoryCollection = DBConnect.getConnection().getCollection("Category");

        FindIterable<Document> productDocuments = productCollection.find();

        for (Document productDocument : productDocuments) {
            ObjectId productId = productDocument.getObjectId("_id");
            int totalQualitySold = 0;
            int totalPrice = 0;
            String nameProduct;
            String nameCategory;
            ObjectId categoryId = productDocument.getObjectId("ID_Category");
            Document category = categoryCollection.find(Filters.eq("_id", categoryId)).first();

            if (category != null) {
                nameProduct = productDocument.getString("Name");
                nameCategory = category.getString("Name");
                Document query1 = new Document("DetailOrder." + String.valueOf(productId), new Document("$exists", true));
                FindIterable<Document>ordercollecion=orderCollection.find(query1);
                for (Document productDocument1 : ordercollecion) {
                    String date = productDocument1.getString("Order_date");
                    if ("All".equals(selectedQuarter) || dateMatchesSelectedQuarter(date, selectedQuarter)) {


                        Document detailOrder = (Document) productDocument1.get("DetailOrder");
                        Document productDetail = (Document) detailOrder.get(productId.toString());

                        if (productDetail != null) {
                            Integer quality = productDetail.getInteger("Quality");
                            int price = productDocument.getInteger("Price");

                            if (quality != null) {
                                totalQualitySold += quality;
                                totalPrice += quality * price;
                            }
                        }

                    }

                }

                DecimalFormat formatter = new DecimalFormat("#,### $");
                String formatPrice = formatter.format(totalPrice);

                revenueList.add(new revenue_statistics(nameProduct, nameCategory, totalQualitySold, formatPrice));
            }
        }

        return revenueList;
}
public static List<revenue_statistics> filtermonth(String selectedQuarter) {
   ArrayList<revenue_statistics> revenueList = new ArrayList<>();
        MongoCollection<Document> productCollection = DBConnect.getConnection().getCollection("Product");
        MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
        MongoCollection<Document> categoryCollection = DBConnect.getConnection().getCollection("Category");

        FindIterable<Document> productDocuments = productCollection.find();

        for (Document productDocument : productDocuments) {
            ObjectId productId = productDocument.getObjectId("_id");
            int totalQualitySold = 0;
            int totalPrice = 0;
            String nameProduct;
            String nameCategory;
            ObjectId categoryId = productDocument.getObjectId("ID_Category");
            Document category = categoryCollection.find(Filters.eq("_id", categoryId)).first();

            if (category != null) {
                nameProduct = productDocument.getString("Name");
                nameCategory = category.getString("Name");
                Document query1 = new Document("DetailOrder." + String.valueOf(productId), new Document("$exists", true));
                FindIterable<Document>ordercollecion=orderCollection.find(query1);
                for (Document productDocument1 : ordercollecion) {
                    String date = productDocument1.getString("Order_date");
                    if ("All year".equals(selectedQuarter) || dateMatchesSelectedMonth(date, selectedQuarter)) {


                        Document detailOrder = (Document) productDocument1.get("DetailOrder");
                        Document productDetail = (Document) detailOrder.get(productId.toString());

                        if (productDetail != null) {
                            Integer quality = productDetail.getInteger("Quality");
                            int price = productDocument.getInteger("Price");

                            if (quality != null) {
                                totalQualitySold += quality;
                                totalPrice += quality * price;
                            }
                        }

                    }

                }

                DecimalFormat formatter = new DecimalFormat("#,### $");
                String formatPrice = formatter.format(totalPrice);

                revenueList.add(new revenue_statistics(nameProduct, nameCategory, totalQualitySold, formatPrice));
            }
        }

        return revenueList;
}
public static boolean dateMatchesSelectedMonth(String date, String selectedMonth) {
    if (date == null || selectedMonth == null) {
        return false; // Handle the case where date or selectedMonth is null
    }

    try {
        // Parse the date string
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate parsedDate = LocalDate.parse(date, formatter);

        // Extract the month from the parsed date
        String month = parsedDate.getMonth().toString();

        // Compare the extracted month with the selected month
        return month.equalsIgnoreCase(selectedMonth);
    } catch (DateTimeParseException e) {
        // Handle parsing errors if the date is in an incorrect format
        return false;
    }
}
private static boolean dateMatchesSelectedQuarter(String date, String selectedQuarter) {
    if (date == null || "All".equals(selectedQuarter)) {
        return false; // Handle the case where date is null or selectedQuarter is "All"
    }

    // Parse the date string and extract the month
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
        Date parsedDate = dateFormat.parse(date);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(parsedDate);
        int month = calendar.get(Calendar.MONTH);

        if ("1".equals(selectedQuarter)) {
            // Quarter 1: January to March
            return (month >= Calendar.JANUARY && month <= Calendar.MARCH);
        } else if ("2".equals(selectedQuarter)) {
            // Quarter 2: April to June
            return (month >= Calendar.APRIL && month <= Calendar.JUNE);
        } else if ("3".equals(selectedQuarter)) {
            // Quarter 3: July to September
            return (month >= Calendar.JULY && month <= Calendar.SEPTEMBER);
        } else if ("4".equals(selectedQuarter)) {
            // Quarter 4: October to December
            return (month >= Calendar.OCTOBER && month <= Calendar.DECEMBER);
        } else {
            return false; // Invalid quarter
        }
    } catch (ParseException e) {
        // Handle date parsing error
        e.printStackTrace();
        return false;
    }
}
 public static List<revenue_statistics>revenue_Employeemonth(String selectedmonth){
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
                            String date = orderDocument.getString("Order_date");
                            if ("All year".equals(selectedmonth) || dateMatchesSelectedMonth(date, selectedmonth)) {
                                if (quality != null && price != null) {
                                    totalQuality += quality;
                                    totalPrice += quality * price;
                                }
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
    private static boolean dateMatchesSelectedYear(String date, String selectedYear) {
        if (date == null) {
            return false; // Handle the case where date is null
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date parsedDate = dateFormat.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(parsedDate);
            int yearFromDate = calendar.get(Calendar.YEAR);
            return selectedYear.equals(String.valueOf(yearFromDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static List<product> searchListproduct(String search) {
        Pattern regexPattern = Pattern.compile(".*" + search + ".*", Pattern.CASE_INSENSITIVE);
        ArrayList<product> Product = new ArrayList<>();
        MongoCollection<Document> productCollection = DBConnect.getConnection().getCollection("Product");
        MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
        MongoCollection<Document> categoryCollection = DBConnect.getConnection().getCollection("Category");

        // Construct a query to search for products by name
        Bson nameFilter = Filters.regex("Name", regexPattern);
        FindIterable<Document> productDocuments = productCollection.find(nameFilter);

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
public static List<revenue_statistics> revenue_Employeeprince(String selectedyear) {
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
                            String date = orderDocument.getString("Order_date");
                            if ("All".equals(selectedyear) || dateMatchesSelectedQuarter(date, selectedyear)) {
                                if (quality != null && price != null) {
                                    totalQuality += quality;
                                    totalPrice += quality * price;
                                }
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
    public static List<revenue_statistics> revenue_Statistics() {
        ArrayList<revenue_statistics> reverse = new ArrayList<>();
        MongoCollection<Document> productCollection = DBConnect.getConnection().getCollection("Product");
        MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
        MongoCollection<Document> categoryCollection = DBConnect.getConnection().getCollection("Category");

        FindIterable<Document> productDocuments = productCollection.find();
        for (Document productDocument : productDocuments) {
            ObjectId idproduct = productDocument.getObjectId("_id");
            int totalQualitySold = 0;
            String formatprice = "0$";
            int totalprice = 0;
            String nameProduct;
            String namecategory;
            ObjectId idcategory = productDocument.getObjectId("ID_Category");
            Document category = categoryCollection.find(Filters.eq("_id", idcategory)).first();
            Document query1 = new Document("Detail." + String.valueOf(idproduct), new Document("$exists", true));

            if (category != null) {

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
                reverse.add(new revenue_statistics(nameProduct, namecategory, totalQualitySold, formatprice));

            }
        }
        return reverse;
    }
public static List<revenue_statistics> revenue_Employeeyear(String selectedYear) {
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
                        String date = orderDocument.getString("Order_date");
                        
                        if (selectedYear != null && ("All Years".equals(selectedYear) || dateMatchesSelectedYear(date, selectedYear))) {
                            if (quality != null && price != null) {
                                totalQuality += quality;
                                totalPrice += quality * price;
                            }
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

  public static List<product> orderNotYetProcessed(Object idProduct) {
    ArrayList<product> products = new ArrayList<>();
    MongoCollection<Document> orderCollection = DBConnect.getConnection().getCollection("Order");
    MongoCollection<Document> customerCollection = DBConnect.getConnection().getCollection("Customer");
    FindIterable<Document> orders = orderCollection.find(new Document("status", 0));

    List<Document> orderList = new ArrayList<>();
    String customerName = "Unknown Customer"; // Giá trị mặc định

    for (Document document : orders) {
        Document detailOrder = document.get("DetailOrder", Document.class);
        Document productDetail = detailOrder.get(idProduct.toString(), Document.class);
        ObjectId idCustomer = document.getObjectId("id_Customer");
        Document customer = customerCollection.find(Filters.eq("_id", idCustomer)).first();

        if (customer != null) {
            customerName = customer.getString("Name");
        }

        String dateString = document.getString("Order_date");

        if (dateString != null) {
            // Chuyển đổi chuỗi ngày thành đối tượng Date
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

        products.add(new product(formattedOrderDate, Math.abs(orderId.hashCode()), customerName, statusString));
    }

    return products;
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
