package pe.gob.mimp.sisdna.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import pe.gob.mimp.sisdna.modelo.Municipalidad;

/**
 * Clase: MunicipalidadFacade.java<br>
 * Clase que implementa la interface de la municipalidad.<br>
 * Fecha de Creación: 20/10/2018<br>
 * 
 * @author BooleanCore
 */
@Stateless
public class MunicipalidadFacade extends AbstractFacade<Municipalidad> implements MunicipalidadFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MunicipalidadFacade() {
        super(Municipalidad.class);
    }

    @Override
    public void deleteAll() {
       Query q = em.createQuery("DELETE FROM Municipalidad");
       q.executeUpdate(); 
    }
}
