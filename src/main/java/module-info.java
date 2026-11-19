module org.example.projetojavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens org.example.projetojavafx to javafx.fxml;
    exports org.example.projetojavafx;
}