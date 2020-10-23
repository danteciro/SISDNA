package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.Catalogo;

/**
 * Interface: CatalogoFacadeLocal.java<br>
 * Interface del catálogo.<br>
 * Fecha de Creación: 20/11/2018<br>
 * 
 * @author BooleanCore
 */
@Local
public interface CatalogoFacadeLocal {

    /**
     * Registra el nuevo catálogo
     * @param catalogo nuevo catálogo
     */
    void create(Catalogo catalogo);

    /**
     * Actualiza el catálogo
     * @param catalogo el catálogo
     */
    void edit(Catalogo catalogo);

    /**
     * Busca el catálogo por su id
     * @param id id del catálogo
     * @return el catálogo
     */
    Catalogo find(Object id);

    /**
     * Devuelve la lista de catálogos segun atributo
     * @param field nombre del atributo
     * @param value valor del atributo
     * @return lista de catálogos
     */
    List<Catalogo> findAllByField(Object field, Object value);
    
    /**
     * Devuelve la lista de catálogos según atributo
     * @param field nombre del atributo
     * @param value valor dela atributo
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de catálogos
     */
    List<Catalogo> findAllByFieldOrder(Object field, Object value, boolean order, String orderFiled, boolean todos);
    
}
