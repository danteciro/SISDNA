package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.Municipalidad;

/**
 * Interface: MunicipalidadFacadeLocal.java<br>
 * Interface de las municipalidades.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface MunicipalidadFacadeLocal {

     /**
     * Registra la nueva municipalidad
     * @param municipalidad nueva municipalidad
     */
    void create(Municipalidad municipalidad);
    
      /**
     * Elimina todos los datos de la municipalidades
     */
    void deleteAll();


    /**
     * Actualiza los datos de la municipalidad
     * @param municipalidad municipalidad
     */
    void edit(Municipalidad municipalidad);

    /**
     * Busca la municipalidad por su id
     * @param id de la municipalidad
     * @return municipalidad
     */
    Municipalidad find(Object id);

     /**
     * Devuelve la lista de municipalidads 
     * @param parametrosRecibidos Parámetros de búsqueda
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de municipalidades
     */
    List<Municipalidad> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
   
}
