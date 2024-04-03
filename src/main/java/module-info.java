module SwimmingCompetitonJava.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.apache.logging.log4j;

    opens ro.mpp2024 to javafx.fxml;
    opens ro.mpp2024.Controller to javafx.fxml;
    exports ro.mpp2024.Controller;

    opens ro.mpp2024.Domain to javafx.base;
    exports ro.mpp2024.Domain;

}