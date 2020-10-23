package pe.gob.mimp.sisdna.fachada;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.CursoProgramacion;

/**
 * Interface: CursoProgramacionFacadeLocal.java<br>
 * Interface de los cursos que dicta la DSLD.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface CursoProgramacionFacadeLocal {

     /**
     * Registra la nueva programación del curso
     * @param cursoProgramacion nueva programación
     */
    void create(CursoProgramacion cursoProgramacion);

    /**
     * Actualiza la programación del curso
     * @param cursoProgramacion programación del curso
     */
    void edit(CursoProgramacion cursoProgramacion);

    /**
     * Busca la programación del curso por su id
     * @param id de la programación
     * @return la programación del curso
     */
    CursoProgramacion find(Object id);

     /**
     * Devuelve la lista de programaciones de los cursos
     * @param parametrosRecibidos Parámetros de búsqueda
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de programaciones de los cursos
     */
    List<CursoProgramacion> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);

    /**
     * Devuelve la lista de programaciones de cursos para publicar
     * @return la lista de programaciones de cursos 
     */
    List<CursoProgramacion> cursosParaPublicar();

    /**
     * Devuelve la lista de programaciones de cursos para publicar por departamento
     * @param nidDepartamento id del departamento
     * @return lista de programaciones de los cursos
     */
    List<CursoProgramacion> cursosParaPublicarDep(BigDecimal nidDepartamento);
  
    /**
     * Devuelve la lista de programaciones de cursos para publicar por provincia
     * @param nidProvincia id de la provincia
     * @return lista de programaciones de los cursos
     */
    List<CursoProgramacion> cursosParaPublicarProv(BigDecimal nidProvincia);
    
    /**
     * Devuelve la lista de programaciones de cursos para publicar por distrito
     * @param nidDistrito id del distrito
     * @return lista de programaciones de los cursos
     */
    List<CursoProgramacion> cursosParaPublicarDis(BigDecimal nidDistrito);
 
     /**
     * Devuelve la lista de programaciones de cursos para publicar por curso
     * @param nidCurso id del curso
     * @return lista de programaciones de los cursos
     */
    List<CursoProgramacion> cursosParaPublicarCurso(BigDecimal nidCurso);

     /**
     * Devuelve la lista de programaciones de cursos para publicar por departamento y curso
     * @param nidDepartamento id del departamento
     * @param nidCurso id del curso
     * @return lista de programaciones de los cursos
     */
    List<CursoProgramacion> cursosParaPublicarDepCurso(BigDecimal nidDepartamento, BigDecimal nidCurso);
  
    /**
     * Devuelve la lista de programaciones de cursos para publicar por provincia y curso
     * @param nidProvincia id de la provincia
     * @param nidCurso id del curso
     * @return lista de programaciones de los cursos
     */
    List<CursoProgramacion> cursosParaPublicarProvCurso(BigDecimal nidProvincia, BigDecimal nidCurso);
    
      /**
     * Devuelve la lista de programaciones de cursos para publicar por distrito y curso
     * @param nidDistrito id del distrito
     * @param nidCurso id del curso
     * @return lista de programaciones de los cursos
     */
    List<CursoProgramacion> cursosParaPublicarDisCurso(BigDecimal nidDistrito, BigDecimal nidCurso);

    /**
     * genera el reporte consolidado por mes y trimestre
     * @param reporte número de reporte
     * @param nidDepartamento id del departamento
     * @param nidProvincia id de la provincia
     * @param nidDistrito id del distrito
     * @param anho anho de la fecha de fin
     * @param desde desde fecha de fin
     * @param hasta hasta fecha de fin
     * @param nidCurso id del curso
     * @param nidTutor id del tutor
     * @param aprueba estado de inscripcion, aprueba 1 , desaprobado 2
     * @return lista del reporte
     */
    List<Object[]> reporteCapacitacion( Integer reporte,
                                        BigDecimal nidDepartamento, 
                                        BigDecimal nidProvincia, 
                                        BigDecimal nidDistrito,
                                        Integer anho,
                                        String desde,
                                        String hasta,
                                        BigDecimal nidCurso,
                                        BigDecimal nidTutor,
                                        Integer aprueba);
  
    /**
     * genera el reporte consolidado por mes, trimestre y sexo
     * @param reporte número de reporte
     * @param nidDepartamento id del departamento
     * @param nidProvincia id de la provincia
     * @param nidDistrito id del distrito
     * @param anho anho de la fecha de fin
     * @param desde desde fecha de fin
     * @param hasta hasta fecha de fin
     * @param nidCurso id del curso
     * @param nidTutor id del tutor
     * @param aprueba estado de inscripcion, aprueba 1 , desaprobado 2
     * @return lista del reporte
     */
    Map<String, Object>  reporteCapacitacionPorCurso(Integer reporte,
                                                    BigDecimal nidDepartamento, 
                                                    BigDecimal nidProvincia, 
                                                    BigDecimal nidDistrito,
                                                    Integer anho,
                                                    String desde,
                                                    String hasta,
                                                    BigDecimal nidCurso,
                                                    BigDecimal nidTutor,
                                                    Integer aprueba);
  
    List<Object[]>   reporteFortalecidas( BigDecimal nidDepartamento, 
                                                BigDecimal nidProvincia,
                                                BigDecimal nidDistrito, 
                                                String desde,
                                                String hasta,
                                                Integer anho);
  
}
