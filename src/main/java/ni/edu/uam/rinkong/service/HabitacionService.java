package ni.edu.uam.rinkong.service;

import ni.edu.uam.rinkong.dao.HabitacionDAO;
import ni.edu.uam.rinkong.model.Habitacion;

import java.math.BigDecimal;
import java.util.List;

public class HabitacionService {

    private final HabitacionDAO dao = new HabitacionDAO();

    public Habitacion guardar(Habitacion habitacion) {
        validar(habitacion);
        dao.guardar(habitacion);
        return habitacion;
    }

    public List<Habitacion> listar() {
        return dao.listar();
    }

    public Habitacion buscarPorId(Long id) {
        validarId(id);
        return dao.buscarPorId(id);
    }

    public Habitacion actualizar(Habitacion habitacion) {
        validar(habitacion);
        validarId(habitacion.getIdHabitacion());
        dao.actualizar(habitacion);
        return habitacion;
    }

    public void eliminar(Long id) {
        validarId(id);
        dao.eliminar(id);
    }

    private void validar(Habitacion habitacion) {
        if (habitacion == null) {
            throw new IllegalArgumentException("La habitación no puede ser nula");
        }
        if (habitacion.getNumero() == null || habitacion.getNumero().isBlank()) {
            throw new IllegalArgumentException("El número de habitación es obligatorio");
        }
        if (habitacion.getTipo() == null || habitacion.getTipo().isBlank()) {
            throw new IllegalArgumentException("El tipo de habitación es obligatorio");
        }
        if (habitacion.getPrecioNoche() == null || habitacion.getPrecioNoche().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio por noche debe ser mayor que cero");
        }
        if (habitacion.getEstado() == null || habitacion.getEstado().isBlank()) {
            throw new IllegalArgumentException("El estado de la habitación es obligatorio");
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El identificador de habitación no es válido");
        }
    }
}
