module com.mgteam.sale_call_center_director {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mgteam.sale_call_center_director to javafx.fxml;
    exports com.mgteam.sale_call_center_director;
}
