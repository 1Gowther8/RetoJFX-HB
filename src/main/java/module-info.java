module com.example.retojfxhb {
    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;
    requires static lombok;
    requires net.sf.jasperreports.core;

    // Apertura amplia para JasperReports
    opens modelos to org.hibernate.orm.core, javafx.base, net.sf.jasperreports.core, java.base;

    opens controladores to javafx.fxml;

    // Otros paquetes necesarios
    opens com.example.aplicacion to javafx.base, javafx.fxml, org.hibernate.orm.core;
    opens Utilidades to javafx.base, org.hibernate.orm.core;

    exports com.example.aplicacion;
}
