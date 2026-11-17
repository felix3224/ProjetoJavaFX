module org.example.projetojavafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.projetojavafx to javafx.fxml;
    exports org.example.projetojavafx;
}