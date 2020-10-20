package pe.gob.mimp.sisdna.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.mimp.sisdna.modelo.AcreditacionEval;

@Stateless
public class AcreditacionEvalFacade extends AbstractFacade<AcreditacionEval> implements AcreditacionEvalFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AcreditacionEvalFacade() {
        super(AcreditacionEval.class);
    }

}
