package pe.gob.mimp.sisdna.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.mimp.sisdna.modelo.DocAcreditacion;


@Stateless
public class DocAcreditacionFacade extends AbstractFacade<DocAcreditacion> implements DocAcreditacionFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DocAcreditacionFacade() {
        super(DocAcreditacion.class);
    }


}