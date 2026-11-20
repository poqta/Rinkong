package ni.edu.uam.rinkong.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

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
}
