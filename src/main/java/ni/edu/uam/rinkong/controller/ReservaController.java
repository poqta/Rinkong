package ni.edu.uam.rinkong.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import ni.edu.uam.rinkong.model.Habitacion;
import ni.edu.uam.rinkong.model.Huesped;
import ni.edu.uam.rinkong.model.Reserva;

import ni.edu.uam.rinkong.service.HabitacionService;
import ni.edu.uam.rinkong.service.HuespedService;
import ni.edu.uam.rinkong.service.ReservaService;

import java.time.LocalDate;

public class ReservaController {

    @FXML
    private ComboBox<Habitacion> cmbHabitacion;
    @FXML
    private ComboBox<Huesped> cmbHuesped;
    @FXML
    private DatePicker dpFechaEntrada;
    @FXML
    private DatePicker dpFechaSalida;
    @FXML
    private ComboBox<String> cmbEstado;

    @FXML
    private TableView<Reserva> tablaReservas;
    @FXML
    private TableColumn<Reserva, Long> colId;
    @FXML
    private TableColumn<Reserva, String> colHabitacion;
    @FXML
    private TableColumn<Reserva, String> colHuesped;
    @FXML
    private TableColumn<Reserva, LocalDate> colFechaEntrada;
    @FXML
    private TableColumn<Reserva, LocalDate> colFechaSalida;
    @FXML
    private TableColumn<Reserva, String> colEstado;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnCancelar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Label lblMensaje;

    private final ReservaService servicio = new ReservaService();
    private final HabitacionService habitacionService = new HabitacionService();
    private final HuespedService huespedService = new HuespedService();

    private final ObservableList<Reserva> reservas =
            FXCollections.observableArrayList();

    private Reserva seleccionada;

    @FXML
    public void initialize() {

        colId.setCellValueFactory(
                new PropertyValueFactory<>("idReserva"));

        colHabitacion.setCellValueFactory(datos ->
                new javafx.beans.property.SimpleStringProperty(
                        datos.getValue().getHabitacion().getNumero()));

        colHuesped.setCellValueFactory(datos ->
                new javafx.beans.property.SimpleStringProperty(
                        datos.getValue().getHuesped().getNombre() + " " +
                                datos.getValue().getHuesped().getApellido()));

        colFechaEntrada.setCellValueFactory(
                new PropertyValueFactory<>("fechaEntrada"));

        colFechaSalida.setCellValueFactory(
                new PropertyValueFactory<>("fechaSalida"));

        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado"));

        cmbEstado.setItems(FXCollections.observableArrayList(
                "Confirmada", "Cancelada", "Finalizada"
        ));

        cmbEstado.setValue("Confirmada");
        cmbEstado.setDisable(true);

        configurarCombos();

        tablaReservas.setItems(reservas);

        tablaReservas.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, actual) -> seleccionar(actual));

        cargarDatos();
    }

    private void configurarCombos() {

        cmbHabitacion.setConverter(new StringConverter<>() {

            @Override
            public String toString(Habitacion habitacion) {
                if (habitacion == null) return "";

                return habitacion.getNumero() + " - " +
                        habitacion.getTipo();
            }

            @Override
            public Habitacion fromString(String texto) {
                return null;
            }
        });

        cmbHuesped.setConverter(new StringConverter<>() {

            @Override
            public String toString(Huesped huesped) {
                if (huesped == null) return "";

                return huesped.getNombre() + " " +
                        huesped.getApellido();
            }

            @Override
            public Huesped fromString(String texto) {
                return null;
            }
        });
    }

    private void cargarDatos() {
        try {
            cmbHabitacion.setItems(FXCollections.observableArrayList(
                    habitacionService.listar()
            ));

            cmbHuesped.setItems(FXCollections.observableArrayList(
                    huespedService.listar()
            ));

            reservas.setAll(servicio.listar());

        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    @FXML
    public void Guardar() {
        try {
            Reserva reserva = leerFormulario();

            if (!"Disponible".equals(
                    reserva.getHabitacion().getEstado())) {
                throw new IllegalArgumentException(
                        "La habitación no está disponible");
            }

            servicio.guardar(reserva);

            limpiarFormulario();
            cargarDatos();
            mostrarMensaje("Reserva guardada correctamente");

        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    @FXML
    public void Actualizar() {

        if (seleccionada == null) {
            mostrarError(new IllegalArgumentException(
                    "Seleccione una reserva"));
            return;
        }

        // La actualizacion requiere validar nuevamente
        // los conflictos de fechas en ReservaService.
        mostrarError(new IllegalStateException(
                "La actualización estará disponible cuando se " +
                        "complete la validación de fechas en ReservaService"));
    }

    @FXML
    public void Cancelar() {

        if (seleccionada == null) {
            mostrarError(new IllegalArgumentException(
                    "Seleccione una reserva"));
            return;
        }

        try {
            if (!"Confirmada".equals(seleccionada.getEstado())) {
                throw new IllegalArgumentException(
                        "Solo se pueden cancelar reservas confirmadas");
            }

            servicio.cancelar(seleccionada.getIdReserva());

            limpiarFormulario();
            cargarDatos();
            mostrarMensaje("Reserva cancelada correctamente");

        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    @FXML
    public void Limpiar() {
        limpiarFormulario();
    }

    private Reserva leerFormulario() {

        Habitacion habitacion = cmbHabitacion.getValue();
        Huesped huesped = cmbHuesped.getValue();
        LocalDate entrada = dpFechaEntrada.getValue();
        LocalDate salida = dpFechaSalida.getValue();

        if (habitacion == null) {
            throw new IllegalArgumentException(
                    "Seleccione una habitación");
        }

        if (huesped == null) {
            throw new IllegalArgumentException(
                    "Seleccione un huésped");
        }

        if (entrada == null || salida == null) {
            throw new IllegalArgumentException(
                    "Seleccione las fechas de entrada y salida");
        }

        if (!salida.isAfter(entrada)) {
            throw new IllegalArgumentException(
                    "La fecha de salida debe ser posterior a la entrada");
        }

        return new Reserva(habitacion, huesped, entrada, salida);
    }

    private void seleccionar(Reserva reserva) {

        seleccionada = reserva;

        if (reserva == null) return;

        cmbHabitacion.setValue(reserva.getHabitacion());
        cmbHuesped.setValue(reserva.getHuesped());

        dpFechaEntrada.setValue(reserva.getFechaEntrada());
        dpFechaSalida.setValue(reserva.getFechaSalida());

        cmbEstado.setValue(reserva.getEstado());
    }

    private void limpiarFormulario() {

        seleccionada = null;

        tablaReservas.getSelectionModel().clearSelection();

        cmbHabitacion.getSelectionModel().clearSelection();
        cmbHuesped.getSelectionModel().clearSelection();

        dpFechaEntrada.setValue(null);
        dpFechaSalida.setValue(null);

        cmbEstado.setValue("Confirmada");

        lblMensaje.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        lblMensaje.setText(mensaje);
    }

    private void mostrarError(RuntimeException e) {

        String mensaje = e.getMessage() != null
                ? e.getMessage()
                : "Ocurrió un error inesperado";

        lblMensaje.setText(mensaje);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
