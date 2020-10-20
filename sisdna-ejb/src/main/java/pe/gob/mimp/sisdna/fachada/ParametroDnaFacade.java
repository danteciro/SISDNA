package pe.gob.mimp.sisdna.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.mimp.sisdna.modelo.ParametroDna;

/**
 * Clase: ParametroDnaFacade.java<br>
 * Clase que implementa la interface de los parámetros del sistema.<br>
 * Fecha de Creación: 20/10/2018<br>
 * 
 * @author BooleanCore
 */
@Stateless
public class ParametroDnaFacade extends AbstractFacade<ParametroDna> implements ParametroDnaFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ParametroDnaFacade() {
        super(ParametroDna.class);
    }

}
