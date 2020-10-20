package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.PersonaPre;

/**
 * Interface: PresonaPreFacadeLocal.java<br>
 * Interface de la persona que se inscribe a los cursos que dicta la DSLD.<br>
 * Fecha de Creación: 20/10/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface PersonaPreFacadeLocal {

    /**
     * Registra la nueva persona
     * @param personaPre nueva persona
     */
    void create(PersonaPre personaPre);

    /**
     * Actualiza los datos de la persona
     * @param personaPre persona
     */
    void edit(PersonaPre personaPre);

    /**
     * Devuelve la lista de las personas activas
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @return lsita de personas
     */
    List<PersonaPre> obtenerActivos(boolean order, String orderFiled);
    
        
    /**
     * Devuelve la lista de personas según parámetros 
     * @param parametrosRecibidos Parámetros de búsqueda
     * @param order lista ordenada ( true )
     * @param orderFiled campo de ordenación
     * @param todos todos ( true ) sin activos (false)
     * @return lista de personas
     */
    List<PersonaPre> obtenerPorFiltro(Object parametrosRecibidos, boolean order, String orderFiled, boolean todos);
     
    
 }
