module com.example.connect4 {
    requires javafx.controls;
    requires javafx.fxml;
            
        requires org.controlsfx.controls;
    requires java.desktop;

    opens com.example.connect4 to javafx.fxml;
    exports com.example.connect4;
}