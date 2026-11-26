package ni.edu.uam.rinkong.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.LockModeType;
import ni.edu.uam.rinkong.database.JPAUtil;
import ni.edu.uam.rinkong.model.Habitacion;
import ni.edu.uam.rinkong.model.Huesped;
import ni.edu.uam.rinkong.model.Reserva;

import java.time.LocalDate;
import java.util.List;

public class ReservaDAO {

    // Guarda una reserva verificando disponibilidad
    public void guardar(Reserva reserva) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Long idHabitacion = reserva.getHabitacion().getIdHabitacion();

            Habitacion habitacion = em.find(
                    Habitacion.class,
                    idHabitacion,
                    LockModeType.PESSIMISTIC_WRITE
            );

            if (habitacion == null) {
                throw new IllegalArgumentException("La habitación no existe");
            }

            if (!"Disponible".equals(habitacion.getEstado())) {
                throw new IllegalArgumentException("La habitación no está disponible");
            }

            if (existeConflicto(em, idHabitacion,
                    reserva.getFechaEntrada(), reserva.getFechaSalida(), null)) {
                throw new IllegalArgumentException(
                        "La habitación ya está reservada en esas fechas");
            }

            if (em.find(Huesped.class,
                    reserva.getHuesped().getIdHuesped()) == null) {
                throw new IllegalArgumentException("El huésped no existe");
            }

            reserva.setHabitacion(habitacion);
            reserva.setEstado("Confirmada");

            em.persist(reserva);
            tx.commit();

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    // Conserva el nombre que utilizamos anteriormente
    public void guardarSeguro(Reserva reserva) {
        guardar(reserva);
    }

    // Lista todas las reservas
    public List<Reserva> listar() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT r FROM Reserva r " +
                            "JOIN FETCH r.habitacion " +
                            "JOIN FETCH r.huesped " +
                            "ORDER BY r.fechaEntrada",
                    Reserva.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // Busca una reserva por su ID
    public Reserva buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                            "SELECT r FROM Reserva r " +
                                    "JOIN FETCH r.habitacion " +
                                    "JOIN FETCH r.huesped " +
                                    "WHERE r.idReserva = :id",
                            Reserva.class
                    )
                    .setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            em.close();
        }
    }

    // Actualiza una reserva existente
    public void actualizar(Reserva reserva) {
        actualizarSeguro(reserva);
    }

    // Actualiza verificando que no existan conflictos
    public void actualizarSeguro(Reserva reserva) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            // Primero consulta la habitación original
            Reserva actual = em.find(Reserva.class, reserva.getIdReserva());

            if (actual == null) {
                throw new IllegalArgumentException("La reserva no existe");
            }

            Long idHabitacion = actual.getHabitacion().getIdHabitacion();

            // Bloquea la habitación
            Habitacion habitacion = em.find(
                    Habitacion.class,
                    idHabitacion,
                    LockModeType.PESSIMISTIC_WRITE
            );

            em.refresh(actual, LockModeType.PESSIMISTIC_WRITE);

            if (!"Confirmada".equals(actual.getEstado())) {
                throw new IllegalArgumentException(
                        "Solo se pueden actualizar reservas confirmadas");
            }

            if (!idHabitacion.equals(
                    reserva.getHabitacion().getIdHabitacion())) {
                throw new IllegalArgumentException(
                        "No se permite cambiar la habitación de la reserva");
            }

            if (!"Disponible".equals(habitacion.getEstado())) {
                throw new IllegalArgumentException(
                        "La habitación no está disponible");
            }

            if (existeConflicto(em, idHabitacion,
                    reserva.getFechaEntrada(),
                    reserva.getFechaSalida(),
                    actual.getIdReserva())) {
                throw new IllegalArgumentException(
                        "La habitación ya está reservada en esas fechas");
            }

            Huesped huesped = em.find(
                    Huesped.class,
                    reserva.getHuesped().getIdHuesped()
            );

            if (huesped == null) {
                throw new IllegalArgumentException("El huésped no existe");
            }

            actual.setHuesped(huesped);
            actual.setFechaEntrada(reserva.getFechaEntrada());
            actual.setFechaSalida(reserva.getFechaSalida());

            tx.commit();

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    // Cancela una reserva
    public void cancelar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Reserva reserva = em.find(
                    Reserva.class, id, LockModeType.PESSIMISTIC_WRITE
            );

            if (reserva == null) {
                throw new IllegalArgumentException("La reserva no existe");
            }

            if (!"Confirmada".equals(reserva.getEstado())) {
                throw new IllegalArgumentException(
                        "Solo se pueden cancelar reservas confirmadas");
            }

            reserva.setEstado("Cancelada");
            tx.commit();

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    // Elimina una reserva por su ID
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Reserva reserva = em.find(Reserva.class, id);

            if (reserva != null) {
                em.remove(reserva);
            }

            tx.commit();

        } catch (RuntimeException e) {
            if (tx.isActive()) {
                tx.rollback();
            }
            throw e;

        } finally {
            em.close();
        }
    }

    // Consulta si existen fechas traslapadas
    public boolean existeConflicto(Long idHabitacion,
                                   LocalDate fechaEntrada,
                                   LocalDate fechaSalida) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            return existeConflicto(
                    em, idHabitacion, fechaEntrada, fechaSalida, null);

        } finally {
            em.close();
        }
    }

    // Consulta dentro de la misma transacción
    private boolean existeConflicto(EntityManager em,
                                    Long idHabitacion,
                                    LocalDate fechaEntrada,
                                    LocalDate fechaSalida,
                                    Long idReserva) {

        String consulta =
                "SELECT COUNT(r) FROM Reserva r " +
                        "WHERE r.habitacion.idHabitacion = :idHabitacion " +
                        "AND r.estado = 'Confirmada' " +
                        "AND r.fechaEntrada < :fechaSalida " +
                        "AND r.fechaSalida > :fechaEntrada ";

        if (idReserva != null) {
            consulta += "AND r.idReserva <> :idReserva";
        }

        var query = em.createQuery(consulta, Long.class);

        query.setParameter("idHabitacion", idHabitacion);
        query.setParameter("fechaEntrada", fechaEntrada);
        query.setParameter("fechaSalida", fechaSalida);

        if (idReserva != null) {
            query.setParameter("idReserva", idReserva);
        }

        return query.getSingleResult() > 0;
    }
}
