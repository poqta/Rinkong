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
import ni.edu.uam.rinkong.service.HabitacionService;
import ni.edu.uam.rinkong.service.HuespedService;
import ni.edu.uam.rinkong.service.ReservaService;

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

    private final HabitacionService habitacionService = new HabitacionService();
    private final HuespedService huespedService = new HuespedService();
    private final ReservaService reservaService = new ReservaService();

    @FXML
    public void initialize() {
        cargarEstadisticas();
    }

    // Carga las cantidades desde PostgreSQL
    private void cargarEstadisticas() {
        try {
            int totalHabitaciones = habitacionService.listar().size();
            int totalHuespedes = huespedService.listar().size();

            long totalReservas = reservaService.listar().stream()
                    .filter(r -> "Confirmada".equals(r.getEstado()))
                    .count();

            lblHabitaciones.setText(String.valueOf(totalHabitaciones));
            lblHuespedes.setText(String.valueOf(totalHuespedes));
            lblReservas.setText(String.valueOf(totalReservas));

        } catch (RuntimeException e) {
            mostrarError("No se pudieron cargar los datos: " + e.getMessage());
        }
    }

    @FXML
    public void Inicio() {
        cargarEstadisticas();
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

            // Actualiza los contadores al cerrar cualquier modulo
            ventana.setOnHidden(event -> cargarEstadisticas());

            ventana.show();

        } catch (IOException e) {
            mostrarError("No se pudo abrir la ventana: " + e.getMessage());

        } catch (RuntimeException e) {
            mostrarError("Error al cargar el módulo: " + e.getMessage());
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
