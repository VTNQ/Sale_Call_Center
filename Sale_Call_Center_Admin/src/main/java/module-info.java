module com.mgteam.sale_call_center_admin {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mgteam.sale_call_center_admin to javafx.fxml;
    exports com.mgteam.sale_call_center_admin;
}
