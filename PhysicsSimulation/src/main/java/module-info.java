module com.example.physicssimulation {
    requires javafx.controls;
    requires javafx.fxml;
    requires json.simple;


    opens com.example.physicssimulation to javafx.fxml;
    exports com.example.physicssimulation;
}