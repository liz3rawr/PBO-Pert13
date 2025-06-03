module com.example.prak13 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.prak13 to javafx.fxml;
    exports com.example.prak13;
}