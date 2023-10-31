module com.mgteam.sale_call_center_employee {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    requires org.mongodb.driver.sync.client;
    requires java.base;
    requires MaterialFX;
    requires VirtualizedFX;
    requires java.mail;
    requires poi.ooxml;
    opens com.mgteam.sale_call_center_employee to javafx.fxml;
    opens com.mgteam.sale_call_center_employee.model to javafx.base;
    exports com.mgteam.sale_call_center_employee;
}
