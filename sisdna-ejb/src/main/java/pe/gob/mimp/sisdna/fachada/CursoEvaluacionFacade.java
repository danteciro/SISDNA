package pe.gob.mimp.sisdna.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.mimp.sisdna.modelo.CursoEvaluacion;

/**
 * Clase: CursoEvaluacionFacade.java<br>
 * Clase que implementa la interface de la evaluación de los cursos.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Stateless
public class CursoEvaluacionFacade extends AbstractFacade<CursoEvaluacion> implements CursoEvaluacionFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CursoEvaluacionFacade() {
        super(CursoEvaluacion.class);
    }

   
}
