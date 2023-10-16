module com.mgteam.sale_call_center_manager {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mgteam.sale_call_center_manager to javafx.fxml;
    exports com.mgteam.sale_call_center_manager;
}
