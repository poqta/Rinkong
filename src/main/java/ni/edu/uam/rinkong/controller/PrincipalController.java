package ni.edu.uam.rinkong.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import ni.edu.uam.rinkong.PrincipalApplication;

import java.io.IOException;

public class PrincipalController {

    @FXML
    private Button btnInicio;
    @FXML
    private Button btnHabitaciones;
    @FXML
    private Button btnHuespedes;
    @FXML
    private Button btnReservas;
    @FXML
    private Button btnConcurrencia;

    @FXML
    private Label lblHabitaciones;
    @FXML
    private Label lblHuespedes;
    @FXML
    private Label lblReservas;

    @FXML
    public void initialize() {
        lblHabitaciones.setText("0");
        lblHuespedes.setText("0");
        lblReservas.setText("0");
    }

    @FXML
    public void Inicio() {
    }

    @FXML
    public void Habitaciones() {
        abrirVentana(
                "habitacion-view.fxml",
                "Rinkong - Gestión de Habitaciones"
        );
    }

    @FXML
    public void Huespedes() {
        abrirVentana(
                "huesped-view.fxml",
                "Rinkong - Gestión de Huéspedes"
        );
    }

    @FXML
    public void Reservas() {
        abrirVentana(
                "reserva-view.fxml",
                "Rinkong - Gestión de Reservas"
        );
    }

    @FXML
    public void Concurrencia() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rinkong");
        alert.setHeaderText(null);
        alert.setContentText(
                "El módulo de concurrencia estará disponible próximamente."
        );
        alert.showAndWait();
    }

    private void abrirVentana(String archivoFXML, String titulo) {

        try {
            FXMLLoader loader = new FXMLLoader(
                    PrincipalApplication.class.getResource(archivoFXML)
            );

            Parent root = loader.load();

            Stage ventana = new Stage();
            ventana.setTitle(titulo);
            ventana.setScene(new Scene(root));
            ventana.setResizable(false);
            ventana.show();

        } catch (IOException e) {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No se pudo abrir la ventana");
            alert.setContentText(e.getMessage());
            alert.showAndWait();

        }
    }
}
