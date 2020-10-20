package pe.gob.mimp.sisdna.fachada;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.Supervision;

/**
 * Interface: SupervisionFacadeLocal.java<br>
 * Interface de los supervisiones que realiza la DSLD.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface SupervisionFacadeLocal {

     /**
     * Registra el nuevo curso
     * @param curso nuevo curso
     */
    void create(Supervision curso);

    /**
     * Actualiza el registro del curso
     * @param curso curso
     */
    void edit(Supervision curso);

    /**
     * Busca el curso por su id
     * @param id id del curso
     * @return el curso
     */
    Supervision find(Object id);

    
    /**
     * Devuelve la lista de supervisiones activos
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @return lista de supervisiones
     */
    List<Supervision> obtenerActivos(boolean order, String orderFiled);
    
    /**
     * Devuelve la lista de supervisiones 
     * @param parametrosRecibidos Parámetros de búsqueda
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de supervisiones
     */
    List<Supervision> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
    
    /**
     * Devuelve la lista de supervisiones 
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de supervisiones
     */
    List<Supervision> obtenerTodos(boolean order, String orderFiled, boolean todos);
    
    /**
     * Devuelve la lista de supervisiones 
     * @param nidDepartamento id del departamento
     * @param nidProvincia id de la provincia
     * @param nidDistrito id del distrito
     * @param nidEstado id del estado
     * @param fechaDesde Desde la fecha
     * @param fechaHasta Hasta la fecha
     * @param codigo código de la DNA
     * @return lista de supervisiones
     */
    List<Supervision> obtenerSupervisiones(BigDecimal nidDepartamento, BigDecimal nidProvincia, BigDecimal nidDistrito, 
                                                    BigInteger nidEstado, Date fechaDesde, Date fechaHasta, String codigo, BigDecimal nidSupervisor);
  
    /**
     * Devuelve la lista de supervisiones por código de dna
     * @param codigo constancia de DNA
     * @return lista de supervisiones
     */
    List<Supervision> obtenerSupervisionesPorCodigo(String codigo);
     
    /**
     * Devuelve la lista generada según el reporte solicitado
     * @param numReporte número de reporte
     * @param nidDepartamento id del departamento
     * @param nidProvincia id de la provincia
     * @param nidDistrito id del distrito
     * @param desde fecha de inicio de búsqueda
     * @param hasta fecha de final de búsqueda
     * @param anho anho de supervisión
     * @param estadoObs estado de observación
     * @param estadoSup estado de supervisión
     * @param estadoDna estado de DNA
     * @param origen id de origen de DNA
     * @param nidSupervisor id del Supervisor
     * @return lista de supervisiones
    */
    List<Object[]> reporte( Integer numReporte, 
                            BigDecimal nidDepartamento, 
                            BigDecimal nidProvincia,
                            BigDecimal nidDistrito, 
                            String desde,  String hasta,  Integer anho,
                            Integer estadoObs, BigInteger estadoSup, BigInteger estadoDna, 
                            BigInteger origen, BigDecimal nidSupervisor);
 
 }
