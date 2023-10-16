module com.mgteam.sale_call_center_employee {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mgteam.sale_call_center_employee to javafx.fxml;
    exports com.mgteam.sale_call_center_employee;
}
