module com.example.javafxlogin {
    requires javafx.controls;
    requires javafx.fxml;

    requires java.sql;



    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires jbcrypt;
    requires de.jensd.fx.glyphs.fontawesome;



    opens com.example.javafxlogin to javafx.fxml;
    exports com.example.javafxlogin;

}