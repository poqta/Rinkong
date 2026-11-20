package ni.edu.uam.rinkong.service;

import ni.edu.uam.rinkong.dao.HuespedDAO;
import ni.edu.uam.rinkong.model.Huesped;

import java.util.List;

public class HuespedService {

    private final HuespedDAO dao = new HuespedDAO();

    public Huesped guardar(Huesped huesped) {
        validar(huesped);
        dao.guardar(huesped);
        return huesped;
    }

    public List<Huesped> listar() {
        return dao.listar();
    }

    public Huesped buscarPorId(Long id) {
        validarId(id);
        return dao.buscarPorId(id);
    }

    public Huesped actualizar(Huesped huesped) {
        validar(huesped);
        validarId(huesped.getIdHuesped());
        dao.actualizar(huesped);
        return huesped;
    }

    public void eliminar(Long id) {
        validarId(id);
        dao.eliminar(id);
    }

    private void validar(Huesped huesped) {
        if (huesped == null) {
            throw new IllegalArgumentException("El huésped no puede ser nulo");
        }
        if (huesped.getNombre() == null || huesped.getNombre().isBlank()) {
            throw new IllegalArgumentException("El nombre del huésped es obligatorio");
        }
        if (huesped.getApellido() == null || huesped.getApellido().isBlank()) {
            throw new IllegalArgumentException("El apellido del huésped es obligatorio");
        }
        if (huesped.getDocumento() == null || huesped.getDocumento().isBlank()) {
            throw new IllegalArgumentException("El documento del huésped es obligatorio");
        }
    }

    private void validarId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El identificador de huésped no es válido");
        }
    }
}
