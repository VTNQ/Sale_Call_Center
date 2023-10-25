module com.mgteam.sale_call_center_manager {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.bson;
    requires java.mail;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires MaterialFX;
    requires java.base;
    opens com.mgteam.sale_call_center_manager to javafx.fxml;
    opens com.mgteam.sale_call_center_manager.model to javafx.base;
    exports com.mgteam.sale_call_center_manager;
}
