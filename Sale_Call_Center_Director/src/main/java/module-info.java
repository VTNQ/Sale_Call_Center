module com.mgteam.sale_call_center_director {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.bson;
    requires java.mail;
    requires MaterialFX;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires java.base;
    opens com.mgteam.sale_call_center_director to javafx.fxml;
     opens com.mgteam.sale_call_center_director.util.model to javafx.base;
    exports com.mgteam.sale_call_center_director;
}
