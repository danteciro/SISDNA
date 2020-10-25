package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.PersonaDna;

@Local
public interface PersonaDnaFacadeLocal {

    void create(PersonaDna inscripcion);

    void edit(PersonaDna inscripcion);

    void remove(PersonaDna inscripcion);

    PersonaDna find(Object id);

    List<PersonaDna> findAll();

    List<PersonaDna> findRange(int[] range);

    int count();
    
    List<PersonaDna> findAllByField(Object field, Object value);
    
    List<PersonaDna> findAllByFieldOrder(Object field, Object value, boolean order, String orderFiled, boolean todos); 
     
    List<PersonaDna> obtenerPorParametros(Object parametrosRecibidos);
    
    PersonaDna findPorParametros(Object parametrosRecibidos);
    
 }
