package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.CursoEvaluacion;

/**
 * Interface: CursoEvaluacionFacadeLocal.java<br>
 * Interface de las evaluaciones de los cursos.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface CursoEvaluacionFacadeLocal {

    /**
     * Registra la nueva evaluación del curso
     * @param cursoEvaluacion evaluación del curso
     */
    void create(CursoEvaluacion cursoEvaluacion);

    /**
     * Actualiza el registro de la evaluación del curso
     * @param cursoEvaluacion evaluación del curso
     */
    void edit(CursoEvaluacion cursoEvaluacion);

    /**
     * Devuelve la lista de evaluaciones de los cursos
     * @param parametrosRecibidos ParamFiltro
     * @param order ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ), sin activos ( false )
     * @return lista de evaluaciones
     */
    List<CursoEvaluacion> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);


}
