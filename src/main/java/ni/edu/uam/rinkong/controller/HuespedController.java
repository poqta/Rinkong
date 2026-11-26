package ni.edu.uam.rinkong.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ni.edu.uam.rinkong.model.Huesped;
import ni.edu.uam.rinkong.service.HuespedService;

public class HuespedController {

    @FXML
    private TextField txtNombre;
    @FXML
    private TextField txtApellido;
    @FXML
    private TextField txtTelefono;
    @FXML
    private TextField txtCorreo;
    @FXML
    private TextField txtDocumento;

    @FXML
    private TableView<Huesped> tablaHuespedes;
    @FXML
    private TableColumn<Huesped, Long> colId;
    @FXML
    private TableColumn<Huesped, String> colNombre;
    @FXML
    private TableColumn<Huesped, String> colApellido;
    @FXML
    private TableColumn<Huesped, String> colTelefono;
    @FXML
    private TableColumn<Huesped, String> colCorreo;
    @FXML
    private TableColumn<Huesped, String> colDocumento;

    @FXML
    private Button btnGuardar;
    @FXML
    private Button btnActualizar;
    @FXML
    private Button btnEliminar;
    @FXML
    private Button btnLimpiar;
    @FXML
    private Label lblMensaje;

    private final HuespedService servicio = new HuespedService();
    private final ObservableList<Huesped> huespedes =
            FXCollections.observableArrayList();

    private Huesped seleccionado;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(
                new PropertyValueFactory<>("idHuesped"));
        colNombre.setCellValueFactory(
                new PropertyValueFactory<>("nombre"));
        colApellido.setCellValueFactory(
                new PropertyValueFactory<>("apellido"));
        colTelefono.setCellValueFactory(
                new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(
                new PropertyValueFactory<>("correo"));
        colDocumento.setCellValueFactory(
                new PropertyValueFactory<>("documento"));

        tablaHuespedes.setItems(huespedes);
        tablaHuespedes.getSelectionModel().selectedItemProperty()
                .addListener((obs, anterior, actual) -> seleccionar(actual));

        cargarHuespedes();
    }

    @FXML
    public void Guardar() {
        try {
            Huesped huesped = leerFormulario();
            servicio.guardar(huesped);
            limpiarFormulario();
            cargarHuespedes();
            mostrarMensaje("Huésped guardado correctamente");

        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    @FXML
    public void Actualizar() {
        if (seleccionado == null) {
            mostrarError(new IllegalArgumentException(
                    "Seleccione un huésped"));
            return;
        }

        try {
            Huesped huesped = leerFormulario();
            huesped.setIdHuesped(seleccionado.getIdHuesped());

            servicio.actualizar(huesped);
            limpiarFormulario();
            cargarHuespedes();
            mostrarMensaje("Huésped actualizado correctamente");

        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    @FXML
    public void Eliminar() {
        if (seleccionado == null) {
            mostrarError(new IllegalArgumentException(
                    "Seleccione un huésped"));
            return;
        }

        try {
            servicio.eliminar(seleccionado.getIdHuesped());
            limpiarFormulario();
            cargarHuespedes();
            mostrarMensaje("Huésped eliminado correctamente");

        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    @FXML
    public void Limpiar() {
        limpiarFormulario();
    }

    private void cargarHuespedes() {
        try {
            huespedes.setAll(servicio.listar());

        } catch (RuntimeException e) {
            mostrarError(e);
        }
    }

    private Huesped leerFormulario() {
        return new Huesped(
                txtNombre.getText().trim(),
                txtApellido.getText().trim(),
                txtTelefono.getText().trim(),
                txtCorreo.getText().trim(),
                txtDocumento.getText().trim()
        );
    }

    private void seleccionar(Huesped huesped) {
        seleccionado = huesped;

        if (huesped == null) return;

        txtNombre.setText(huesped.getNombre());
        txtApellido.setText(huesped.getApellido());
        txtTelefono.setText(huesped.getTelefono());
        txtCorreo.setText(huesped.getCorreo());
        txtDocumento.setText(huesped.getDocumento());
    }

    private void limpiarFormulario() {
        seleccionado = null;
        tablaHuespedes.getSelectionModel().clearSelection();

        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        txtDocumento.clear();

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
