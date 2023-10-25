/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mgteam.sale_call_center_manager.connect.util;

import com.mgteam.sale_call_center_manager.connect.DBconnect;
import com.mgteam.sale_call_center_manager.model.Customer;
import com.mgteam.sale_call_center_manager.model.Manager;
import com.mgteam.sale_call_center_manager.model.Order;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import static com.mongodb.client.model.Filters.eq;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author tranp
 */
public class daodb {

    public static Map<ObjectId, Date[]> getdateOrder() {
        MongoCollection<Document> Employee = DBconnect.getdatabase().getCollection("Employee");
        MongoCollection<Document> Order = DBconnect.getdatabase().getCollection("Order");
        MongoCollection<Document> Customer = DBconnect.getdatabase().getCollection("Customer");
        FindIterable<Document> OrderList = Order.find();
        Map<ObjectId, Date[]> OrderMap = new HashMap<>();
        for (Document document : OrderList) {
            ObjectId idCustomer = document.getObjectId("id_Customer");
            String orderDate = document.getString("Order_date");
            if (orderDate != null) {
                Date Dateorder = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    Dateorder = sdf.parse(orderDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (Dateorder != null) {
                    if (OrderMap.containsKey(idCustomer)) {
                        Date[] dates = OrderMap.get(idCustomer);
                        if (Dateorder.after(dates[0])) {
                            dates[0] = Dateorder;
                        }
                        if (Dateorder.before(dates[1])) {
                            dates[1] = Dateorder;
                        }
                    } else {
                        Date[] dates = new Date[]{Dateorder, Dateorder};
                        OrderMap.put(idCustomer, dates);
                    }
                }
            }
        }
        return OrderMap;
    }

    public static List<Customer> getdetailpushedCustomer(String Name) {
        ArrayList<Customer>Array=new ArrayList<>();
        MongoCollection<Document> orderCollection = DBconnect.getdatabase().getCollection("Order");
        MongoCollection<Document> EmployeeCollection = DBconnect.getdatabase().getCollection("Employee");
        MongoCollection<Document> CustomerCollection = DBconnect.getdatabase().getCollection("Customer");
        FindIterable<Document> order = orderCollection.find();
        for (Document document : order) {
            ObjectId id = document.getObjectId("_id");
            ObjectId idCustomer = document.getObjectId("id_Customer");
            ObjectId idEmployee = document.getObjectId("id_Employee");
            Document customer = CustomerCollection.find(new Document("_id", idCustomer)).first();
            Document Employee = EmployeeCollection.find(new Document("_id", idEmployee)).first();
            String NameCustomer = customer.getString("Name");
            if (customer != null && Employee != null && NameCustomer.equals(Name)) {
                int id_order = id.hashCode();
                String demand = customer.getString("Demand");
                String nameEmployee = customer.getString("Name");
                Array.add(new Customer(nameEmployee, demand, id_order));
            }
        }
        return Array;
    }

    public static List<Order> getdetailCustomer(String Name) {

        Map<String, Integer> productQuantityMap = new HashMap<>();
        ArrayList<Order> ordest = new ArrayList<>();
        MongoCollection<Document> orderCollection = DBconnect.getdatabase().getCollection("Order");
        MongoCollection<Document> productCollection = DBconnect.getdatabase().getCollection("Product");
        MongoCollection<Document> CustomeCollection = DBconnect.getdatabase().getCollection("Customer");

        FindIterable<Document> orders = orderCollection.find(new Document());

        for (Document order : orders) {
            Document detailOrder = (Document) order.get("DetailOrder");

            List<Object> idProducts = (List<Object>) detailOrder.get("id_Product");
            ObjectId id_customer = order.getObjectId("id_Customer");

            for (Object idProduct : idProducts) {
                if (idProduct instanceof String) {
                    try {
                        ObjectId objectId = new ObjectId((String) idProduct);

                        Document product = productCollection.find(new Document("_id", objectId)).first();
                        Document customer1 = CustomeCollection.find(new Document("_id", id_customer)).first();
                        if (product != null && customer1 != null) {
                            String productName = product.getString("Name");
                            String customerName = customer1.getString("Name");

                            if (customerName.equals(Name)) {

                                if (productQuantityMap.containsKey(productName)) {
                                    int existingQuantity = productQuantityMap.get(productName);
                                    productQuantityMap.put(productName, existingQuantity + 1);
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

            DecimalFormat formatter = new DecimalFormat("#,### $");
            int price = getProductPrice(productCollection, productName);
            String formatPrice = formatter.format(price);
            ordest.add(new Order(productName, quantity, formatPrice));
        }

        return ordest;

    }

    public static int calculateTotalPrice(List<Order> orders) {
        int total = 0;
        for (Order order : orders) {
            int quality = order.getQuality();
            String price = order.getPrice();
            String numericPrice = price.replaceAll("[^\\d]", "");
            int pricetotal = Integer.parseInt(numericPrice);
            total += quality * pricetotal;
        }
        return total;
    }

    private static int getProductPrice(MongoCollection<Document> productCollection, String productName) {
        Document product = productCollection.find(new Document("Name", productName)).first();
        if (product != null) {
            return product.getInteger("Price");
        }
        return 0;
    }

    public static List<Customer> ListCustomer() {
        List<Customer> customer = new ArrayList<>();
        Map<ObjectId, Date[]> Ordermap = getdateOrder();
        Set<ObjectId> processedCustomer = new HashSet<>();
        try {
            MongoCollection<Document> Employee = DBconnect.getdatabase().getCollection("Employee");
            MongoCollection<Document> Order = DBconnect.getdatabase().getCollection("Order");
            MongoCollection<Document> Customer = DBconnect.getdatabase().getCollection("Customer");
            FindIterable<Document> OrderList = Order.find();
            for (Document document : OrderList) {
                ObjectId idCustomer = document.getObjectId("id_Customer");
                ObjectId idEmployee = document.getObjectId("id_Employee");
                Document customerAll = Customer.find(Filters.eq("_id", idCustomer)).first();
                Document EmployeeProcess = Employee.find(Filters.eq("_id", idEmployee)).first();
                if (customerAll != null && EmployeeProcess != null && !processedCustomer.contains(idCustomer)) {
                    String nameCustomer = customerAll.getString("Name");
                    Date[] Orderdate = Ordermap.get(idCustomer);
                    Date StartDate = Orderdate[0];
                    Date EndDate = Orderdate[1];
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String formatStart = sdf.format(StartDate);
                    String formatEnd = sdf.format(EndDate);
                    customer.add(new Customer(nameCustomer, formatStart, formatEnd));
                    processedCustomer.add(idCustomer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return customer;
    }

    public static List<Order> SearchorderCustomer(String Name) {
        List<Order> ordercustomer = new ArrayList<>();
        try {
            MongoCollection<Document> collections = DBconnect.getdatabase().getCollection("Employee");
            MongoCollection<Document> Order = DBconnect.getdatabase().getCollection("Order");
            MongoCollection<Document> Customer = DBconnect.getdatabase().getCollection("Customer");
            FindIterable<Document> orders = Order.find();

            Pattern regexPattern = Pattern.compile(".*" + Name + ".*", Pattern.CASE_INSENSITIVE);

            for (Document order : orders) {
                ObjectId idCustomer = order.getObjectId("id_Customer");
                ObjectId idEmployee = order.getObjectId("id_Employee");

                Document customerDocument = Customer.find(Filters.and(Filters.eq("_id", idCustomer), Filters.regex("Name", regexPattern))).first();

                if (customerDocument != null) {
                    String customerName = customerDocument.getString("Name");

                    Document EmployeeDocument = collections.find(Filters.eq("_id", idEmployee)).first();

                    if (EmployeeDocument != null) {
                        String EmployeeName = EmployeeDocument.getString("Name");
                        String dayOrder = order.getString("Order_date");
                        LocalDate sinceDate = LocalDate.parse(dayOrder, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                        String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        int status = order.getInteger("status");
                        String Email = EmployeeDocument.getString("Email");
                        String statusString = "";

                        switch (status) {
                            case 0:
                                statusString = "Importing goods";
                                break;
                            case 1:
                                statusString = "Delivering";
                                break;
                            case 2:
                                statusString = "Cancelled";
                                break;
                            case 3:
                                statusString = "Delivered";
                                break;
                            default:
                                throw new AssertionError();
                        }

                        ordercustomer.add(new Order(customerName, formattedSince, EmployeeName, statusString, Email));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ordercustomer;
    }

    public static List<Manager> SearchgetManagerAccount(String search) {
        List<Manager> MG = new ArrayList<>();
        try {
            MongoCollection<Document> collections = DBconnect.getdatabase().getCollection("Employee");
            MongoCollection<Document> requestCollection = DBconnect.getdatabase().getCollection("Request");
            Document query = new Document("Name", new Document("$regex", ".*" + search + ".*"));
            FindIterable<Document> cursor = collections.find(query);
            for (Document document : cursor) {
                int usertype = document.getInteger("usertype");
                if (usertype == 1 || usertype == 2) {
                    String name = document.getString("Name");
                    String phone = document.getString("Phone");
                    String sinceString = document.getString("Since");
                    LocalDate sinceDate = LocalDate.parse(sinceString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String username = document.getString("Username");
                    String email = document.getString("Email");

                    Document requestDocument = requestCollection.find(eq("EmailEmployee", email)).first();
                    int status = (requestDocument != null) ? requestDocument.getInteger("status") : 1;
                    String value = (requestDocument != null) ? String.valueOf(status) : "";
                    MG.add(new Manager(name, email, formattedSince, phone, value, username));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MG;
    }

    public static List<Order> getorderCustomer() {
        List<Order> Ordercustomer = new ArrayList<>();

        try {
            Set<ObjectId> processedCustomer = new HashSet<>();
            MongoCollection<Document> collections = DBconnect.getdatabase().getCollection("Employee");
            MongoCollection<Document> Order = DBconnect.getdatabase().getCollection("Order");
            MongoCollection<Document> Customer = DBconnect.getdatabase().getCollection("Customer");
            FindIterable<Document> orders = Order.find();
            for (Document order : orders) {
                ObjectId idCustomer = order.getObjectId("id_Customer");
                ObjectId idEmployee = order.getObjectId("id_Employee");
                Document customerDocument = Customer.find(Filters.eq("_id", idCustomer)).first();
                Document EmployeeDocument = collections.find(Filters.eq("_id", idEmployee)).first();
                if (customerDocument != null && EmployeeDocument != null && !processedCustomer.contains(idCustomer)) {
                    String customerName = customerDocument.getString("Name");
                    String EmployeeName = EmployeeDocument.getString("Name");
                    String dayOrder = order.getString("Order_date");
                    LocalDate sinceDate = LocalDate.parse(dayOrder, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    int status = order.getInteger("status");
                    String Email = EmployeeDocument.getString("Email");
                    String statusString = "";
                    switch (status) {
                        case 0:
                            statusString = "Importing goods";
                            break;
                        case 1:
                            statusString = "Delivering";
                            break;
                        case 2:
                            statusString = "Cancelled";
                            break;
                        case 3:
                            statusString = "delivered";
                            break;
                        default:

                            throw new AssertionError();
                    }
                    Ordercustomer.add(new Order(customerName, formattedSince, EmployeeName, statusString, Email));
                    processedCustomer.add(idCustomer);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Ordercustomer;
    }

    public static List<Manager> getManagerAccount() {
        List<Manager> Mg = new ArrayList<>();
        MongoCollection<Document> collections = DBconnect.getdatabase().getCollection("Employee");
        MongoCollection<Document> requestCollection = DBconnect.getdatabase().getCollection("Request");
        MongoCursor<Document> cursor = collections.find().iterator();
        try {
            while (cursor.hasNext()) {
                Document document = cursor.next();
                int usertype = document.getInteger("usertype");
                if (usertype == 1 || usertype == 2) {
                    String name = document.getString("Name");
                    String phone = document.getString("Phone");
                    String sinceString = document.getString("Since");
                    LocalDate sinceDate = LocalDate.parse(sinceString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    String formattedSince = sinceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    String username = document.getString("Username");
                    String email = document.getString("Email");

                    Document requestDocument = requestCollection.find(eq("EmailEmployee", email)).first();
                    int status = (requestDocument != null) ? requestDocument.getInteger("status") : 1;
                    String value = (requestDocument != null) ? String.valueOf(status) : "";
                    Mg.add(new Manager(name, email, formattedSince, phone, value, username));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Mg;
    }
}
