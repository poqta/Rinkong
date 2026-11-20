module ni.edu.uam.rinkong {

    requires javafx.controls;
    requires javafx.fxml;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;

    opens ni.edu.uam.rinkong to javafx.fxml;
    opens ni.edu.uam.rinkong.controller to javafx.fxml;
    opens ni.edu.uam.rinkong.model to org.hibernate.orm.core, javafx.base;

    exports ni.edu.uam.rinkong;
    exports ni.edu.uam.rinkong.model;
}
