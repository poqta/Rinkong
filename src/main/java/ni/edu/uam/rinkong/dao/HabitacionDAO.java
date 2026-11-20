package ni.edu.uam.rinkong.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.uam.rinkong.database.JPAUtil;
import ni.edu.uam.rinkong.model.Habitacion;

import java.util.List;

public class HabitacionDAO {

    // Guarda una nueva habitacion
    public void guardar(Habitacion habitacion) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(habitacion);
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

    // Lista todas las habitaciones
    public List<Habitacion> listar() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT h FROM Habitacion h ORDER BY h.numero",
                    Habitacion.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // Busca una habitacion por su ID
    public Habitacion buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Habitacion.class, id);

        } finally {
            em.close();
        }
    }

    // Actualiza una habitacion existente
    public void actualizar(Habitacion habitacion) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(habitacion);
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

    // Elimina una habitacion por su ID
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Habitacion habitacion = em.find(Habitacion.class, id);

            if (habitacion != null) {
                em.remove(habitacion);
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
}
