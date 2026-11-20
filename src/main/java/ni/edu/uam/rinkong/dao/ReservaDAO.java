package ni.edu.uam.rinkong.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.uam.rinkong.database.JPAUtil;
import ni.edu.uam.rinkong.model.Reserva;

import java.time.LocalDate;
import java.util.List;

public class ReservaDAO {

    // Guarda una nueva reserva
    public void guardar(Reserva reserva) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
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
                    ).setParameter("id", id)
                    .getResultStream()
                    .findFirst()
                    .orElse(null);

        } finally {
            em.close();
        }
    }

    // Actualiza una reserva existente
    public void actualizar(Reserva reserva) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(reserva);
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

    // Verifica si una habitacion tiene reservas en las fechas indicadas
    public boolean existeConflicto(Long idHabitacion,
                                   LocalDate fechaEntrada,
                                   LocalDate fechaSalida) {

        EntityManager em = JPAUtil.getEntityManager();

        try {
            Long cantidad = em.createQuery(
                            "SELECT COUNT(r) FROM Reserva r " +
                                    "WHERE r.habitacion.idHabitacion = :idHabitacion " +
                                    "AND r.estado = 'Confirmada' " +
                                    "AND r.fechaEntrada < :fechaSalida " +
                                    "AND r.fechaSalida > :fechaEntrada",
                            Long.class
                    )
                    .setParameter("idHabitacion", idHabitacion)
                    .setParameter("fechaEntrada", fechaEntrada)
                    .setParameter("fechaSalida", fechaSalida)
                    .getSingleResult();

            return cantidad > 0;

        } finally {
            em.close();
        }
    }
}
