package pe.gob.mimp.sisdna.fachada;

import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.DefensoriaPersona;

/**
 * Interface: DefensoriaFacadeLocal.java<br>
 * Interface de las defensorías.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Local
public interface DefensoriaPersonaFacadeLocal {

     /**
     * Registra la nueva persona
     * @param defensoriaPersona nueva persona
     */
    void create(DefensoriaPersona defensoriaPersona);

   
}
