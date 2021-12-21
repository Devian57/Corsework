module com.automagia.autoShop {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    
    opens com.automagia.autoShop to javafx.fxml;
    exports com.automagia.autoShop;
}
