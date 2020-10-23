package pe.gob.mimp.sisdna.fachada;

import java.util.List;
import javax.ejb.Local;
import pe.gob.mimp.sisdna.modelo.PersonaDnaAcre;

@Local
public interface PersonaDnaAcreFacadeLocal {

    void create(PersonaDnaAcre inscripcion);

    void edit(PersonaDnaAcre inscripcion);

    void remove(PersonaDnaAcre inscripcion);

    PersonaDnaAcre find(Object id);

    List<PersonaDnaAcre> findAll();

    List<PersonaDnaAcre> findRange(int[] range);

    int count();
    
    List<PersonaDnaAcre> findAllByField(Object field, Object value);
    
    List<PersonaDnaAcre> findAllByFieldOrder(Object field, Object value, boolean order, String orderFiled, boolean todos); 
     
    List<PersonaDnaAcre> obtenerPorParametros(Object parametrosRecibidos);
    
    PersonaDnaAcre findPorParametros(Object parametrosRecibidos);
    
 }
