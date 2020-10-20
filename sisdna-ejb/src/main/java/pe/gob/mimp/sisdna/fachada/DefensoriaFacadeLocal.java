package pe.gob.mimp.sisdna.fachada;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.Catalogo;
import pe.gob.mimp.sisdna.modelo.Defensoria;

/**
 * Interface: DefensoriaFacadeLocal.java<br>
 * Interface de las defensorías.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface DefensoriaFacadeLocal {

     /**
     * Registra la nueva defensoría
     * @param defensoria nueva defensoría
     */
    void create(Defensoria defensoria);

    /**
     * Actualiza los datos de la defensoría
     * @param defensoria defensoría
     */
    void edit(Defensoria defensoria);

    /**
     * Busca la defensoría por su id
     * @param id de la defensoría
     * @return defensoría
     */
    Defensoria find(Object id);

    /**
     * Busca la lista de defensoria por atributo
     * @param field nombre del atributo
     * @param value valor del atributo
     * @return lista de defensorías
     */
    List<Defensoria> findAllByField(Object field, Object value);
   
    /**
     * Busca la lista de defensoria por parámetros
     * @param parametrosRecibidos parámetros
     * @return lista de defensorías
     */
    List<Defensoria> obtenerPorParametros(Object parametrosRecibidos);
    
    /**
     * Devuelve la lista de defensorías según parámetros
     * @param parametrosRecibidos parámetros
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de defensorías
     */
    List<Defensoria> obtenerPorParametrosObject(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
    
    /**
     * Devuelve la lista de defensorías según parámetros
     * @param parametrosRecibidos parámetros
     * @param estadoNuevo catálogo del estado nuevo
     * @param estadoDenegado catálogo del estado denegado
     * @return lista de defensorías
     */
    List<Defensoria> obtenerPorParametrosAndEstado(Object parametrosRecibidos, Catalogo estadoNuevo, Catalogo estadoDenegado);
 
    /**
     * Devuelve el número de la constancia de la DNA
     * @param nidDepartamento id del departamento
     * @return el número de la constancia de la DNA
     */
    int getNroConstancia(BigDecimal nidDepartamento);
    
     /**
     * Devuelve la lista de defensorías 
     * @param parametrosRecibidos Parámetros de búsqueda
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de defensorías
     */
    List<Defensoria> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
   
    /**
     * Devuelve la lista de defensorias que no han sido supervisadas según el filtro
     * @param nidDepartamento id del departamento
     * @param nidProvincia id de la provincia
     * @param nidDistrito id del distrito
     * @param fechaDesde Desde la fecha
     * @param fechaHasta Hasta la fecha
     * @param codigo código de DNA
     * @return lista de defensorias
     */
    List<Object[]> obtenerDefensoriasNoSupervisadas(BigDecimal nidDepartamento, BigDecimal nidProvincia, BigDecimal nidDistrito, 
                                                    Date fechaDesde, Date fechaHasta, String codigo);
  
}
