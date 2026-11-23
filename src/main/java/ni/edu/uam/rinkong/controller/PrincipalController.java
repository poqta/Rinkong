package ni.edu.uam.rinkong.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import ni.edu.uam.rinkong.PrincipalApplication;

import java.io.IOException;

public class PrincipalController {

    @FXML private Button btnInicio;
    @FXML private Button btnHabitaciones;
    @FXML private Button btnHuespedes;
    @FXML private Button btnReservas;
    @FXML private Button btnConcurrencia;
    @FXML private Label lblHabitaciones;
    @FXML private Label lblHuespedes;
    @FXML private Label lblReservas;
    @FXML private StackPane contenidoPrincipal;

    private Node vistaInicio;

    @FXML
    public void initialize() {
        lblHabitaciones.setText("0");
        lblHuespedes.setText("0");
        lblReservas.setText("0");

        btnInicio.setOnAction(event -> mostrarInicio());
        btnHabitaciones.setOnAction(event -> cargarVista("habitacion-view.fxml"));

        if (contenidoPrincipal != null && !contenidoPrincipal.getChildren().isEmpty()) {
            vistaInicio = contenidoPrincipal.getChildren().get(0);
        }
    }

    @FXML
    private void mostrarInicio() {
        if (contenidoPrincipal == null) return;
        if (vistaInicio == null && !contenidoPrincipal.getChildren().isEmpty()) {
            vistaInicio = contenidoPrincipal.getChildren().get(0);
        }
        contenidoPrincipal.getChildren().setAll(vistaInicio);
    }

    @FXML
    private void cargarVista(String fxml) {
        if (contenidoPrincipal == null) return;
        try {
            Node vista = FXMLLoader.load(PrincipalApplication.class.getResource(fxml));
            contenidoPrincipal.getChildren().setAll(vista);
        } catch (IOException | IllegalStateException e) {
            System.err.println("No se pudo cargar la vista: " + fxml);
            e.printStackTrace();
        }
    }
}
