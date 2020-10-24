package pe.gob.mimp.sisdna.fachada;


import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.mimp.sisdna.modelo.DefensoriaInfo;

/**
 * Clase: DefensoriaFacade.java<br>
 * Clase que implementa la interface de la defensoría.<br>
 * Fecha de Creación: 20/10/2018<br>
 * 
 * @author BooleanCore
 */
@Stateless
public class DefensoriaInfoFacade extends AbstractFacade<DefensoriaInfo> implements DefensoriaInfoFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DefensoriaInfoFacade() {
        super(DefensoriaInfo.class);
    }

  
}
