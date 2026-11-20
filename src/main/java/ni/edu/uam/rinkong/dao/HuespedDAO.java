package ni.edu.uam.rinkong.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import ni.edu.uam.rinkong.database.JPAUtil;
import ni.edu.uam.rinkong.model.Huesped;

import java.util.List;

public class HuespedDAO {

    // Guarda un nuevo huesped
    public void guardar(Huesped huesped) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(huesped);
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

    // Lista todos los huespedes
    public List<Huesped> listar() {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT h FROM Huesped h ORDER BY h.nombre",
                    Huesped.class
            ).getResultList();

        } finally {
            em.close();
        }
    }

    // Busca un huesped por su ID
    public Huesped buscarPorId(Long id) {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            return em.find(Huesped.class, id);

        } finally {
            em.close();
        }
    }

    // Actualiza un huesped existente
    public void actualizar(Huesped huesped) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.merge(huesped);
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

    // Elimina un huesped por su ID
    public void eliminar(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Huesped huesped = em.find(Huesped.class, id);

            if (huesped != null) {
                em.remove(huesped);
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
