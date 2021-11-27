module com.example.drawing_application {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.drawing_application to javafx.fxml;
    exports com.example.drawing_application;
}