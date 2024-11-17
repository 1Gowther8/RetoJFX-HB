module com.example.retojfxhb {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;
    requires static lombok;

    opens modelos to org.hibernate.orm.core, javafx.base;
    opens com.example.aplicacion to javafx.fxml;
    opens controladores to javafx.fxml;

    exports com.example.aplicacion;
}






