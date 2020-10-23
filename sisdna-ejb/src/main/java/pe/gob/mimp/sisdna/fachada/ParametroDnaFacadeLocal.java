package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.ParametroDna;

/**
 * Interface: ParametroDnaFacadeLocal.java<br>
 * Interface de los parámetros del sistema.<br>
 * Fecha de Creación: 20/11/2018<br>
 * 
 * @author BooleanCore
 */
@Local
public interface ParametroDnaFacadeLocal {

    /**
     * Actualiza los datos del parámetro
     * @param parametroDemuna parámetro
     */
    void edit(ParametroDna parametroDemuna);

    /**
     * Busca el parámetro por su id
     * @param id id del parámetro
     * @return el parámetro
     */
    ParametroDna find(Object id);

    /**
     * Devuelve la lista completa de parámetros
     * @return lista de parámetros
     */
    List<ParametroDna> findAll();
    
    /**
     * Busca la lista de parámetros según atributo
     * @param field nombre del atributo
     * @param value valor del atributo
     * @return lista de parámetros
     */
    List<ParametroDna> findAllByField(Object field, Object value);
    
     List<ParametroDna> obtenerTodos(boolean order, String orderFiled, boolean todos) ;
}
