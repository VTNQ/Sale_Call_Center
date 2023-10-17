module com.mgteam.sale_call_center_admin {
      requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.driver.sync.client;
requires org.mongodb.bson;
requires org.mongodb.driver.core;
    requires MaterialFX;
    requires java.mail;
    opens com.mgteam.sale_call_center_admin to javafx.fxml;
    opens com.mgteam.sale_call_center_admin.model to javafx.base;
    exports com.mgteam.sale_call_center_admin;
}
