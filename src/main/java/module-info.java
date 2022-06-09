module com.example.stockmarketide {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.example.stockmarketide to javafx.fxml;
    exports com.example.stockmarketide;
}