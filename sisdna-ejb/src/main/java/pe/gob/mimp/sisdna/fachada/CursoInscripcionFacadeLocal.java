package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.CursoInscripcion;

/**
 * Interface: CursoInscripcionFacadeLocal.java<br>
 * Interface de las solicitudes de inscripción de los cursos que dicta la DSLD.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface CursoInscripcionFacadeLocal {

    /**
     * Registra la nueva solicitud de inscripción
     * @param cursoInscripcion nueva solicitud
     */
    void create(CursoInscripcion cursoInscripcion);

     /**
     * Actualiza la solicitud de inscripción
     * @param cursoInscripcion la solicitud de inscripción
     */
    void edit(CursoInscripcion cursoInscripcion);

     /**
     * Busca la solicitud de inscripción por su id
     * @param id id de la solicitud de inscripción
     * @return la solicitud de inscripción
     */
    CursoInscripcion find(Object id);

    /**
     * Devuelve la lista de solicitudes de inscripción
     * @param parametrosRecibidos Parámetros de búsqueda
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de solicitudes de inscripción
     */
    List<CursoInscripcion> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
 }
