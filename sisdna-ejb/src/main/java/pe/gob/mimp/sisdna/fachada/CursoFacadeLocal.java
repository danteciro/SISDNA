package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.Curso;

/**
 * Interface: CursoFacadeLocal.java<br>
 * Interface de los cursos que dicta la DSLD.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface CursoFacadeLocal {

     /**
     * Registra el nuevo curso
     * @param curso nuevo curso
     */
    void create(Curso curso);

    /**
     * Actualiza el registro del curso
     * @param curso curso
     */
    void edit(Curso curso);

    /**
     * Busca el curso por su id
     * @param id id del curso
     * @return el curso
     */
    Curso find(Object id);

    
    /**
     * Devuelve la lista de cursos activos
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @return lista de cursos
     */
    List<Curso> obtenerActivos(boolean order, String orderFiled);
    
    /**
     * Devuelve la lista de cursos 
     * @param parametrosRecibidos Parámetros de búsqueda
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de cursos
     */
    List<Curso> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
    
    /**
     * Devuelve la lista de cursos 
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de cursos
     */
    List<Curso> obtenerTodos(boolean order, String orderFiled, boolean todos);
 }
