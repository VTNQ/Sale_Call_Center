module com.mgteam.sale_call_center_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.bson;
    requires java.mail;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    opens com.mgteam.sale_call_center_manager to javafx.fxml;
    exports com.mgteam.sale_call_center_manager;
}
