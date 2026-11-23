package ni.edu.uam.rinkong.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.rinkong.model.Habitacion;
import ni.edu.uam.rinkong.service.HabitacionService;

import java.math.BigDecimal;

public class HabitacionController {

    @FXML private TextField txtNumero;
    @FXML private TextField txtTipo;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cbEstado;
    @FXML private TableView<Habitacion> tablaHabitaciones;
    @FXML private TableColumn<Habitacion, Long> colId;
    @FXML private TableColumn<Habitacion, String> colNumero;
    @FXML private TableColumn<Habitacion, String> colTipo;
    @FXML private TableColumn<Habitacion, BigDecimal> colPrecio;
    @FXML private TableColumn<Habitacion, String> colEstado;
    @FXML private Button btnGuardar;
    @FXML private Button btnActualizar;
    @FXML private Button btnEliminar;
    @FXML private Button btnLimpiar;
    @FXML private Label lblMensaje;

    private final HabitacionService servicio = new HabitacionService();
    private final ObservableList<Habitacion> habitaciones = FXCollections.observableArrayList();
    private Habitacion seleccionada;

    @FXML
    public void initialize() {
        if (colId != null) colId.setCellValueFactory(new PropertyValueFactory<>("idHabitacion"));
        if (colNumero != null) colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        if (colTipo != null) colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        if (colPrecio != null) colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioNoche"));
        if (colEstado != null) colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        if (cbEstado != null) {
            cbEstado.setItems(FXCollections.observableArrayList("Disponible", "Ocupada", "Mantenimiento"));
        }
        if (tablaHabitaciones != null) {
            tablaHabitaciones.setItems(habitaciones);
            tablaHabitaciones.getSelectionModel().selectedItemProperty()
                    .addListener((observable, anterior, actual) -> seleccionar(actual));
        }
        cargarHabitaciones();
    }

    @FXML
    public void Guardar() {
        try {
            Habitacion habitacion = leerFormulario();
            servicio.guardar(habitacion);
            limpiarFormulario();
            cargarHabitaciones();
            mostrarMensaje("Habitación guardada correctamente");
        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    @FXML
    public void Actualizar() {
        if (seleccionada == null) {
            mostrarError(new IllegalArgumentException("Seleccione una habitación"));
            return;
        }
        try {
            Habitacion habitacion = leerFormulario();
            habitacion.setIdHabitacion(seleccionada.getIdHabitacion());
            servicio.actualizar(habitacion);
            limpiarFormulario();
            cargarHabitaciones();
            mostrarMensaje("Habitación actualizada correctamente");
        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    @FXML
    public void Eliminar() {
        if (seleccionada == null) {
            mostrarError(new IllegalArgumentException("Seleccione una habitación"));
            return;
        }
        try {
            servicio.eliminar(seleccionada.getIdHabitacion());
            limpiarFormulario();
            cargarHabitaciones();
            mostrarMensaje("Habitación eliminada correctamente");
        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    @FXML
    public void Limpiar() {
        limpiarFormulario();
    }

    private void cargarHabitaciones() {
        try {
            habitaciones.setAll(servicio.listar());
        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    private Habitacion leerFormulario() {
        if (txtNumero == null || txtTipo == null || txtPrecio == null) {
            throw new IllegalArgumentException("Los controles del formulario no están disponibles");
        }

        BigDecimal precio;
        try {
            precio = new BigDecimal(txtPrecio.getText().trim().replace(',', '.'));
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("El precio por noche no es válido", e);
        }

        String estado = cbEstado != null ? cbEstado.getValue() : null;
        return new Habitacion(txtNumero.getText().trim(), txtTipo.getText().trim(), precio, estado);
    }

    private void seleccionar(Habitacion habitacion) {
        seleccionada = habitacion;
        if (habitacion == null) return;
        if (txtNumero != null) txtNumero.setText(habitacion.getNumero());
        if (txtTipo != null) txtTipo.setText(habitacion.getTipo());
        if (txtPrecio != null) txtPrecio.setText(habitacion.getPrecioNoche().toPlainString());
        if (cbEstado != null) cbEstado.setValue(habitacion.getEstado());
    }

    private void limpiarFormulario() {
        seleccionada = null;
        if (tablaHabitaciones != null) tablaHabitaciones.getSelectionModel().clearSelection();
        if (txtNumero != null) txtNumero.clear();
        if (txtTipo != null) txtTipo.clear();
        if (txtPrecio != null) txtPrecio.clear();
        if (cbEstado != null) cbEstado.getSelectionModel().clearSelection();
        if (lblMensaje != null) lblMensaje.setText("");
    }

    private void mostrarMensaje(String mensaje) {
        if (lblMensaje != null) lblMensaje.setText(mensaje);
    }

    private void mostrarError(RuntimeException excepcion) {
        String mensaje = excepcion.getMessage() != null ? excepcion.getMessage() : "Ocurrió un error inesperado";
        if (lblMensaje != null) lblMensaje.setText(mensaje);

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
