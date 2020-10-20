package pe.gob.mimp.sisdna.fachada;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import pe.gob.mimp.sisdna.modelo.PersonaPre;

/**
 * Clase: PersonaPreFacade.java<br>
 * Clase que implementa la interface de las personas que se inscriben a los cursos.<br>
 * Fecha de Creación: 20/09/2019<br>
 * 
 * @author BooleanCore
 */
@Stateless
public class PersonaPreFacade extends AbstractFacade<PersonaPre> implements PersonaPreFacadeLocal {

    @PersistenceContext(unitName = "pe.gob.mimp_demuna-ejb_ejb_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PersonaPreFacade() {
        super(PersonaPre.class);
    }


}
