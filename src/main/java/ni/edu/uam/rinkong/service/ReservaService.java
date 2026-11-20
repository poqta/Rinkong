package ni.edu.uam.rinkong.service;

import ni.edu.uam.rinkong.dao.ReservaDAO;
import ni.edu.uam.rinkong.model.Reserva;

import java.util.List;

public class ReservaService {

    private final ReservaDAO dao = new ReservaDAO();

    public Reserva guardar(Reserva reserva) {
        validar(reserva);

        if (dao.existeConflicto(
                reserva.getHabitacion().getIdHabitacion(),
                reserva.getFechaEntrada(),
                reserva.getFechaSalida())) {
            throw new IllegalStateException("La habitación ya está reservada en esas fechas");
        }

        reserva.setEstado("Confirmada");
        dao.guardar(reserva);
        return reserva;
    }

    public List<Reserva> listar() {
        return dao.listar();
    }

    public Reserva buscarPorId(Long id) {
        validarId(id);
        return dao.buscarPorId(id);
    }

    public Reserva actualizar(Reserva reserva) {
        validar(reserva);
        validarId(reserva.getIdReserva());
        dao.actualizar(reserva);
        return reserva;
    }

    public void cancelar(Long id) {
        validarId(id);
        Reserva reserva = dao.buscarPorId(id);
        if (reserva == null) {
            throw new IllegalArgumentException("No existe la reserva indicada");
        }
        reserva.setEstado("Cancelada");
        dao.actualizar(reserva);
    }

    public void eliminar(Long id) {
        validarId(id);
        dao.eliminar(id);
    }

    private void validar(Reserva reserva) {
        if (reserva == null) {
            throw new IllegalArgumentException("La reserva no puede ser nula");
        }
        if (reserva.getHabitacion() == null || reserva.getHabitacion().getIdHabitacion() == null) {
            throw new IllegalArgumentException("Debe seleccionar una habitación");
        }
        if (reserva.getHuesped() == null || reserva.getHuesped().getIdHuesped() == null) {
            throw new IllegalArgumentException("Debe seleccionar un huésped");
        }
        if (reserva.getFechaEntrada() == null || reserva.getFechaSalida() == null) {
            throw new IllegalArgumentException("Las fechas de entrada y salida son obligatorias");
        }
        if (!reserva.getFechaSalida().isAfter(reserva.getFechaEntrada())) {
            throw new IllegalArgumentException("La fecha de salida debe ser posterior a la entrada");
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El identificador de reserva no es válido");
        }
    }
}
